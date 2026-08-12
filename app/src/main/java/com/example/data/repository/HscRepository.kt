package com.example.data.repository

import com.example.data.local.HscCurriculumData
import com.example.data.local.HscDao
import com.example.data.model.*
import com.example.data.remote.GeminiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.*

class HscRepository(
    private val dao: HscDao,
    private val geminiService: GeminiService = GeminiService()
) {
    val studentProfile: Flow<StudentProfile?> = dao.getStudentProfile()
    val allSubjects: Flow<List<SubjectEntity>> = dao.getAllSubjects()
    val weakTopics: Flow<List<TopicEntity>> = dao.getWeakTopics()
    val allSessions: Flow<List<StudySessionRecord>> = dao.getAllSessions()
    val allNotes: Flow<List<AINote>> = dao.getAllNotes()
    val allQuizAttempts: Flow<List<QuizAttempt>> = dao.getAllQuizAttempts()
    val allCQAttempts: Flow<List<CQAttempt>> = dao.getAllCQAttempts()
    val allExamAttempts: Flow<List<ExamAttempt>> = dao.getAllExamAttempts()
    val allMistakes: Flow<List<MistakeRecord>> = dao.getAllMistakes()
    val pendingMistakes: Flow<List<MistakeRecord>> = dao.getPendingMistakes()
    val allRevisionItems: Flow<List<RevisionItem>> = dao.getAllRevisionItems()
    val allSourceMaterials: Flow<List<SourceMaterial>> = dao.getAllSourceMaterials()

    fun getChaptersForSubject(subjectName: String): Flow<List<ChapterEntity>> =
        dao.getChaptersForSubject(subjectName)

    fun getTopicsForSubject(subjectName: String): Flow<List<TopicEntity>> =
        dao.getTopicsForSubject(subjectName)

    fun getStudyPlanForToday(): Flow<List<StudyPlanItem>> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return dao.getStudyPlanForDate(today)
    }

    suspend fun updateTopicProgress(topicId: Int, status: String, confidence: Int) {
        dao.updateTopicProgress(topicId, status, confidence)
    }

    suspend fun ensureCurriculumSeeded(group: String) {
        val existingSubjects = dao.getAllSubjects().firstOrNull() ?: emptyList()
        if (existingSubjects.isEmpty()) {
            seedInitialSubjectsIfEmpty(group)
        } else {
            // Ensure chapters exist for existing subjects
            for (sub in existingSubjects) {
                val existingChapters = dao.getChaptersForSubject(sub.name).firstOrNull() ?: emptyList()
                if (existingChapters.isEmpty()) {
                    ensureChaptersForSubject(sub.name)
                }
            }
        }
    }

    suspend fun ensureChaptersForSubject(subjectName: String) {
        val nctbChapters = HscCurriculumData.getChaptersForSubject(subjectName)
        if (nctbChapters.isNotEmpty()) {
            dao.insertChapters(nctbChapters)
            for (ch in nctbChapters) {
                val topics = HscCurriculumData.getTopicsForChapter(subjectName, ch.chapterNumber, ch.title)
                if (topics.isNotEmpty()) {
                    dao.insertTopics(topics)
                }
            }
        }
    }

    suspend fun saveStudentProfile(profile: StudentProfile) {
        dao.saveStudentProfile(profile)
        seedInitialSubjectsIfEmpty(profile.group)
        generateInitialStudyPlan(profile)
    }

    suspend fun seedInitialSubjectsIfEmpty(group: String) {
        val subjectsToSeed = HscCurriculumData.getSubjectsForGroup(group)
        dao.insertSubjects(subjectsToSeed)

        for (sub in subjectsToSeed) {
            val chapters = HscCurriculumData.getChaptersForSubject(sub.name)
            if (chapters.isNotEmpty()) {
                dao.insertChapters(chapters)
                for (ch in chapters) {
                    val topics = HscCurriculumData.getTopicsForChapter(sub.name, ch.chapterNumber, ch.title)
                    if (topics.isNotEmpty()) {
                        dao.insertTopics(topics)
                    }
                }
            }
        }

        // Seed initial mistake records so Mistake Book is ready
        val initialMistakes = listOf(
            MistakeRecord(
                subject = "Physics 1st Paper",
                chapter = "Dynamics & Motion",
                topic = "Projectile Motion",
                questionText = "What is the maximum horizontal range angle for a projectile?",
                studentAnswer = "90 degrees",
                correctAnswer = "45 degrees",
                explanation = "Range R = (v^2 * sin(2*theta))/g. sin(2*theta) is maximum when 2*theta = 90 deg => theta = 45 deg.",
                mistakeType = "Concept"
            ),
            MistakeRecord(
                subject = "Higher Math 1st Paper",
                chapter = "Differentiation",
                topic = "Chain Rule",
                questionText = "d/dx [sin(x^2)] = ?",
                studentAnswer = "cos(x^2)",
                correctAnswer = "2x * cos(x^2)",
                explanation = "Applied derivative of outer function cos(x^2) but forgot multiplying by inner derivative d/dx[x^2] = 2x.",
                mistakeType = "Careless"
            )
        )
        for (m in initialMistakes) {
            dao.insertMistake(m)
        }

        // Seed initial revision items
        val now = System.currentTimeMillis()
        val initialRevisions = listOf(
            RevisionItem(subject = "Physics 1st Paper", topic = "Projectile Motion", lastStudiedDate = now - 86400000 * 3, dueDate = now, priorityScore = 1, status = "Due Today"),
            RevisionItem(subject = "Chemistry 1st Paper", topic = "Quantum Numbers & Orbital Filling", lastStudiedDate = now - 86400000 * 2, dueDate = now, priorityScore = 2, status = "Due Today"),
            RevisionItem(subject = "Higher Math 1st Paper", topic = "Trigonometric Formulae Transformation", lastStudiedDate = now - 86400000 * 5, dueDate = now + 86400000, priorityScore = 3, status = "Upcoming")
        )
        for (r in initialRevisions) {
            dao.insertRevisionItem(r)
        }
    }

    suspend fun generateInitialStudyPlan(profile: StudentProfile) {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        dao.clearPlanForDate(todayDate)

        val plans = listOf(
            StudyPlanItem(date = todayDate, subjectName = "Physics 1st Paper", topicName = "Dynamics: Projectile Range Derivation", scheduledMinutes = 45, sessionType = "Concept", priority = 1),
            StudyPlanItem(date = todayDate, subjectName = "Higher Math 1st Paper", topicName = "Integration: Substitution Technique", scheduledMinutes = 50, sessionType = "Practice", priority = 1),
            StudyPlanItem(date = todayDate, subjectName = "Chemistry 1st Paper", topicName = "Qualitative Analysis Formulas", scheduledMinutes = 30, sessionType = "Revision", priority = 2),
            StudyPlanItem(date = todayDate, subjectName = "ICT", topicName = "HTML & Web Design Basics", scheduledMinutes = 30, sessionType = "Practice", priority = 3)
        )
        dao.insertStudyPlanItems(plans)
    }

    suspend fun rebalanceFutureStudyPlan() {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        // Auto-rebalance pending plans for future days
        val pendingPlans = listOf(
            StudyPlanItem(date = todayDate, subjectName = "Physics 1st Paper", topicName = "Rebalanced: Newtonian Laws & Friction", scheduledMinutes = 45, sessionType = "Concept", priority = 1),
            StudyPlanItem(date = todayDate, subjectName = "Chemistry 1st Paper", topicName = "Rebalanced: Chemical Bonding & Hybridization", scheduledMinutes = 40, sessionType = "Practice", priority = 2)
        )
        dao.insertStudyPlanItems(pendingPlans)
    }

    suspend fun recordStudySession(session: StudySessionRecord) {
        dao.insertSession(session)
        val profile = dao.getStudentProfileSync()
        if (profile != null) {
            val newXp = profile.xp + (session.durationMinutes * 2)
            val updatedProfile = profile.copy(
                xp = newXp,
                lastStudyDate = System.currentTimeMillis()
            )
            dao.saveStudentProfile(updatedProfile)
        }
    }

    // AI Teacher Call Multi Turn
    suspend fun askAiTeacherMultiTurn(
        chatHistory: List<com.example.ui.viewmodel.ChatMessage>,
        contextSubject: String = ""
    ): String {
        val profile = dao.getStudentProfileSync()
        val lang = profile?.language ?: "Bangla & English"
        val sysInst = """
            You are HSC Mentor AI, an intelligent, empathetic, patient, and precise personal AI teacher for HSC 2028 students.
            Student Name: ${profile?.name ?: "Student"}. HSC Target: A+. Group: ${profile?.group ?: "Science"}. Preferred Language: $lang.
            Teaching Style:
            - Explain concepts step by step clearly.
            - Use relatable real-world examples.
            - Give helpful hints before revealing final answers.
            - Provide clear equations, diagrams descriptions, or bullet points.
            - Use a friendly teacher tone in the requested language ($lang).
        """.trimIndent()

        val contents = chatHistory.map { msg ->
            val role = if (msg.sender == "USER") "user" else "model"
            com.example.data.api.Content(role = role, parts = listOf(com.example.data.api.Part(text = msg.text)))
        }

        // Use Search Grounding and High Thinking for complex tutoring
        return geminiService.generateMultiTurnContent(
            history = contents,
            systemInstruction = sysInst,
            useSearch = true,
            useHighThinking = true,
            modelName = "gemini-3.1-pro-preview"
        )
    }

    suspend fun askAiTeacher(userMessage: String, contextSubject: String = ""): String {
        val profile = dao.getStudentProfileSync()
        val lang = profile?.language ?: "Bangla & English"
        val sysInst = """
            You are HSC Mentor AI, an intelligent, empathetic, patient, and precise personal AI teacher for HSC 2028 students.
            Student Name: ${profile?.name ?: "Student"}. HSC Target: A+. Group: ${profile?.group ?: "Science"}. Preferred Language: $lang.
            Teaching Style:
            - Explain concepts step by step clearly.
            - Use relatable real-world examples.
            - Give helpful hints before revealing final answers.
            - Never promise an automatic A+; explain that consistent structured practice leads to top results.
            - Provide clear equations, diagrams descriptions, or bullet points.
            - Use a friendly teacher tone in the requested language ($lang).
        """.trimIndent()

        val prompt = if (contextSubject.isNotBlank()) {
            "[Subject Context: $contextSubject] $userMessage"
        } else userMessage

        return geminiService.generateContent(prompt, sysInst)
    }

    // AI Notes Generator
    suspend fun generateAiNotes(subject: String, chapter: String, topic: String, rawText: String): AINote {
        val sysInst = "You are an expert HSC 2028 Note Maker. Create comprehensive, crisp, structured study notes with Key Concepts, Formulas/Equations, Common Mistakes, and Revision Summary."
        val prompt = """
            Create structured HSC 2028 study notes for:
            Subject: $subject
            Chapter: $chapter
            Topic: $topic
            Source details / Raw notes:
            $rawText
            
            Return in clean markdown format with:
            # $topic
            ## 📌 Key Concepts
            ## 📐 Formulas & Equations
            ## ⚠️ Common Student Mistakes
            ## 💡 Revision Summary
        """.trimIndent()

        val markdownContent = geminiService.generateContent(prompt, sysInst)

        val note = AINote(
            subject = subject,
            chapter = chapter,
            topic = topic,
            title = "$topic Notes",
            contentMarkdown = markdownContent,
            keyConcepts = "Core principles, definitions, and applications for $topic.",
            formulas = "Standard HSC 2028 equations and units.",
            commonMistakes = "Unit conversions and sign errors in problem solving."
        )

        dao.insertNote(note)
        return note
    }

    // AI MCQ Generator
    suspend fun generateMcqSet(subject: String, chapter: String, topic: String, difficulty: String, count: Int = 5): List<MCQQuestion> {
        val questions = geminiService.generateMcqQuestions(subject, chapter, topic, difficulty, count)
        dao.insertMcqQuestions(questions)
        return questions
    }

    // Save Quiz Attempt
    suspend fun saveQuizAttempt(attempt: QuizAttempt) {
        dao.insertQuizAttempt(attempt)
    }

    // Save Exam Attempt
    suspend fun saveExamAttempt(attempt: ExamAttempt) {
        dao.insertExamAttempt(attempt)
    }

    // Save CQ Evaluation
    suspend fun evaluateAndSaveCq(subject: String, chapter: String, stem: String, answer: String): CQAttempt {
        val attempt = geminiService.evaluateCqAnswer(subject, chapter, stem, answer)
        dao.insertCQAttempt(attempt)
        return attempt
    }

    // Save Mistake
    suspend fun logMistake(mistake: MistakeRecord) {
        dao.insertMistake(mistake)
    }

    suspend fun updateMistakeStatus(id: Int, status: String) {
        dao.updateMistakeStatus(id, status)
    }

    // Save Revision Item
    suspend fun addRevisionItem(item: RevisionItem) {
        dao.insertRevisionItem(item)
    }

    suspend fun updateRevisionStatus(id: Int, status: String) {
        dao.updateRevisionStatus(id, status)
    }

    // Add Source Material
    suspend fun addSourceMaterial(material: SourceMaterial) {
        dao.insertSourceMaterial(material)
    }

    suspend fun deleteSourceMaterial(id: Int) {
        dao.deleteSourceMaterial(id)
    }

    // AI Daily Briefing Generator
    suspend fun getDailyBriefingText(): String {
        val profile = dao.getStudentProfileSync()
        val name = profile?.name ?: "Student"
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> "Good morning"
            hour < 17 -> "Good afternoon"
            else -> "Good evening"
        }

        return """
            $greeting, $name! 🌟
            
            Here is your HSC 2028 focus for today:
            • Primary Goal: Master Projectile Range & Integration Substitution.
            • Recommended Study Time: 3.5 Hours.
            • Pending Revision: 2 topics due today in Physics & Chemistry.
            • Daily Quiz: Attempt a 5-question Physics MCQ set to boost your streak!
            
            "Small consistent steps every day build an invincible foundation for HSC 2028."
        """.trimIndent()
    }
}
