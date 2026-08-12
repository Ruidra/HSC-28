package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.HscDatabase
import com.example.data.model.*
import com.example.data.repository.HscRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: String, // "USER" or "AI"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class TimerState(
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val subject: String = "Physics 1st Paper",
    val topic: String = "Dynamics & Motion",
    val totalSeconds: Int = 25 * 60,
    val remainingSeconds: Int = 25 * 60,
    val sessionGoal: String = "Solve 5 practice problems",
    val showReflectionDialog: Boolean = false
)

class HscViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HscRepository

    init {
        val dao = HscDatabase.getDatabase(application).hscDao()
        repository = HscRepository(dao)
    }

    // Flows from DB
    val studentProfile: StateFlow<StudentProfile?> = repository.studentProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allSubjects: StateFlow<List<SubjectEntity>> = repository.allSubjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weakTopics: StateFlow<List<TopicEntity>> = repository.weakTopics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayStudyPlan: StateFlow<List<StudyPlanItem>> = repository.getStudyPlanForToday()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSessions: StateFlow<List<StudySessionRecord>> = repository.allSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotes: StateFlow<List<AINote>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allQuizAttempts: StateFlow<List<QuizAttempt>> = repository.allQuizAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCQAttempts: StateFlow<List<CQAttempt>> = repository.allCQAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allExamAttempts: StateFlow<List<ExamAttempt>> = repository.allExamAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMistakes: StateFlow<List<MistakeRecord>> = repository.allMistakes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingMistakes: StateFlow<List<MistakeRecord>> = repository.pendingMistakes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRevisionItems: StateFlow<List<RevisionItem>> = repository.allRevisionItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sourceMaterials: StateFlow<List<SourceMaterial>> = repository.allSourceMaterials
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // AI Briefing
    private val _dailyBriefing = MutableStateFlow("Loading your daily HSC 2028 study briefing...")
    val dailyBriefing: StateFlow<String> = _dailyBriefing.asStateFlow()

    // Navigation & Platform Real-time States
    private val _currentScreen = MutableStateFlow("dashboard")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    fun navigateTo(screenRoute: String) {
        _currentScreen.value = screenRoute
    }

    // Real-Time Connection & Performance Controls
    private val _connectionState = MutableStateFlow("ONLINE") // ONLINE, CONNECTING, OFFLINE, SYNCING
    val connectionState: StateFlow<String> = _connectionState.asStateFlow()

    private val _isLowBandwidthMode = MutableStateFlow(false)
    val isLowBandwidthMode: StateFlow<Boolean> = _isLowBandwidthMode.asStateFlow()

    fun toggleLowBandwidthMode() {
        _isLowBandwidthMode.value = !_isLowBandwidthMode.value
    }

    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

    fun toggleOfflineMode() {
        val newMode = !_isOfflineMode.value
        _isOfflineMode.value = newMode
        _connectionState.value = if (newMode) "OFFLINE" else "ONLINE"
    }

    // Notification Center
    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem(
                title = "⚡ Live Class Alert",
                message = "Physics 1st Paper: Newtonian Mechanics live session is starting in 15 mins.",
                timeAgoStr = "10m ago",
                type = "LIVE_CLASS"
            ),
            NotificationItem(
                title = "🎯 AI Study Recommendation",
                message = "Based on your 65% accuracy in Chemistry Organic, 30 mins revision recommended.",
                timeAgoStr = "1h ago",
                type = "AI_RECOMMENDATION"
            ),
            NotificationItem(
                title = "📌 Doubt Solved",
                message = "AI Teacher solved your question on Vector Dot Product.",
                timeAgoStr = "3h ago",
                type = "DOUBT",
                isRead = true
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    val unreadNotificationsCount: StateFlow<Int> = _notifications.map { list ->
        list.count { !it.isRead }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)

    fun markAllNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }

    // Activity Feed (Real-Time Study Log)
    private val _activityFeed = MutableStateFlow<List<ActivityLog>>(
        listOf(
            ActivityLog(title = "Physics 1st Paper Session", description = "Completed 25 mins Dynamics revision", timeStr = "10:15 AM", iconEmoji = "⚡"),
            ActivityLog(title = "MCQ Practice Quiz", description = "Scored 4/5 (80%) on Vectors", timeStr = "09:40 AM", iconEmoji = "🎯"),
            ActivityLog(title = "Mistake Logged", description = "Added 1 question to Mistake Book", timeStr = "08:20 AM", iconEmoji = "⚠️")
        )
    )
    val activityFeed: StateFlow<List<ActivityLog>> = _activityFeed.asStateFlow()

    fun logActivity(title: String, description: String, iconEmoji: String = "✍️") {
        val timeNow = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val newLog = ActivityLog(title = title, description = description, timeStr = timeNow, iconEmoji = iconEmoji)
        _activityFeed.value = listOf(newLog) + _activityFeed.value
    }

    // Live Class & Live Classroom
    private val _liveClasses = MutableStateFlow<List<LiveClass>>(
        listOf(
            LiveClass(
                id = "live_01",
                subject = "Physics 1st Paper",
                teacherName = "Tanvir Rahman",
                topic = "Chapter 4: Newtonian Mechanics & Centripetal Force",
                dateTimeStr = "Today",
                startTimeStr = "07:00 PM",
                durationMins = 60,
                description = "Master core concepts and HSC 2028 board exam CQs on banking of roads.",
                status = "LIVE NOW",
                participantCount = 142,
                isStreamConfigured = false // Clean notice: Live streaming is not configured
            ),
            LiveClass(
                id = "live_02",
                subject = "Chemistry 1st Paper",
                teacherName = "Dr. S. Ahmed",
                topic = "Chapter 2: Qualitative Chemistry & Solubility Curves",
                dateTimeStr = "Tomorrow",
                startTimeStr = "08:30 PM",
                durationMins = 75,
                description = "Solving mathematical problems on Ksp and solubility product.",
                status = "UPCOMING",
                participantCount = 98,
                isStreamConfigured = false
            )
        )
    )
    val liveClasses: StateFlow<List<LiveClass>> = _liveClasses.asStateFlow()

    private val _liveChatMessages = MutableStateFlow<List<LiveChatMessage>>(
        listOf(
            LiveChatMessage(senderName = "Sakib", message = "Sir, can you clarify equation F = m * v^2 / r?", timestampStr = "07:05 PM"),
            LiveChatMessage(senderName = "Teacher Tanvir", message = "Yes Sakib, v^2/r is centripetal acceleration!", timestampStr = "07:06 PM", isTeacher = true),
            LiveChatMessage(senderName = "Nusrat", message = "Got it! Thanks sir.", timestampStr = "07:07 PM")
        )
    )
    val liveChatMessages: StateFlow<List<LiveChatMessage>> = _liveChatMessages.asStateFlow()

    fun sendLiveChatMessage(messageText: String) {
        if (messageText.isBlank()) return
        val timeNow = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val userMsg = LiveChatMessage(senderName = studentProfile.value?.name ?: "Student", message = messageText, timestampStr = timeNow)
        _liveChatMessages.value = _liveChatMessages.value + userMsg
    }

    // Recorded Classes
    private val _recordedClasses = MutableStateFlow<List<RecordedClass>>(
        listOf(
            RecordedClass(
                id = "rec_01",
                subject = "Physics 1st Paper",
                chapter = "Chapter 3",
                topic = "Dynamics & Projectile Motion Range Equations",
                teacher = "Tanvir Rahman",
                durationMins = 45,
                description = "Derivation of maximum height, flight time, and horizontal range.",
                progressPercent = 65f,
                isCompleted = false
            ),
            RecordedClass(
                id = "rec_02",
                subject = "Higher Math 1st Paper",
                chapter = "Chapter 9",
                topic = "Differentiation Chain Rule & Tangents",
                teacher = "Prof. K. Hoque",
                durationMins = 50,
                description = "Step by step solution of board question short tricks.",
                progressPercent = 100f,
                isCompleted = true
            )
        )
    )
    val recordedClasses: StateFlow<List<RecordedClass>> = _recordedClasses.asStateFlow()

    // Real-time Doubt Solving
    private val _doubts = MutableStateFlow<List<DoubtItem>>(
        listOf(
            DoubtItem(
                id = "doubt_01",
                subject = "Physics 1st Paper",
                topic = "Banking of Roads",
                questionText = "How do we calculate angle of banking when velocity is 60 km/h and radius is 100m?",
                submittedTimeStr = "Today 08:30 AM",
                status = "AI ANSWERED",
                aiStepExplanation = "1. Convert velocity: 60 km/h = 16.67 m/s.\n2. Use formula: tan(θ) = v^2 / (r * g).\n3. Substitute: tan(θ) = (16.67)^2 / (100 * 9.8) = 0.2835.\n4. θ = arctan(0.2835) ≈ 15.83°.",
                conceptKey = "Banking angle formula tan(θ) = v^2 / (r*g)",
                formulaText = "tan(θ) = v^2 / (r * g)",
                exampleText = "If velocity doubles, required banking angle tan(θ) quadruples.",
                commonMistakeText = "Forgetting to convert km/h to m/s by dividing by 3.6.",
                followUpQuestionText = "What happens if friction is also present?"
            )
        )
    )
    val doubts: StateFlow<List<DoubtItem>> = _doubts.asStateFlow()

    fun submitDoubt(subject: String, topic: String, questionText: String) {
        if (questionText.isBlank()) return
        val timeStr = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault()).format(Date())

        val newDoubt = DoubtItem(
            subject = subject,
            topic = topic,
            questionText = questionText,
            submittedTimeStr = timeStr,
            status = "AI ANALYZING"
        )
        _doubts.value = listOf(newDoubt) + _doubts.value

        viewModelScope.launch {
            delay(1000)
            val aiAnswer = repository.askAiTeacher("Explain step by step solution with formula: $questionText", subject)
            _doubts.value = _doubts.value.map { d ->
                if (d.id == newDoubt.id) {
                    d.copy(
                        status = "AI ANSWERED",
                        aiStepExplanation = aiAnswer,
                        conceptKey = "Core HSC 2028 Board Question Concept",
                        formulaText = "Standard formula applied",
                        commonMistakeText = "Watch out for unit mismatch and direction signs."
                    )
                } else d
            }
            logActivity("Doubt Solved by AI", "$subject: $topic", "🧠")
        }
    }

    fun requestTeacherReview(doubtId: String) {
        _doubts.value = _doubts.value.map { d ->
            if (d.id == doubtId) {
                d.copy(status = "TEACHER REVIEW", teacherReviewNote = "Flagged for human teacher verification pipeline.")
            } else d
        }
    }

    // Leaderboard
    private val _isLeaderboardOptedIn = MutableStateFlow(true)
    val isLeaderboardOptedIn: StateFlow<Boolean> = _isLeaderboardOptedIn.asStateFlow()

    fun toggleLeaderboardOptIn() {
        _isLeaderboardOptedIn.value = !_isLeaderboardOptedIn.value
    }

    val leaderboardEntries: StateFlow<List<LeaderboardEntry>> = MutableStateFlow(
        listOf(
            LeaderboardEntry(rank = 1, studentName = "Abrar K. (Dhaka Board)", scoreXp = 2450, quizAccuracyPercent = 94f, streakDays = 28),
            LeaderboardEntry(rank = 2, studentName = "Nusrat J. (Chattogram Board)", scoreXp = 2280, quizAccuracyPercent = 92f, streakDays = 24),
            LeaderboardEntry(rank = 3, studentName = "Mahir H. (Rajshahi Board)", scoreXp = 2180, quizAccuracyPercent = 90f, streakDays = 21),
            LeaderboardEntry(rank = 4, studentName = "Tanvir M. (Cumilla Board)", scoreXp = 2050, quizAccuracyPercent = 89f, streakDays = 19),
            LeaderboardEntry(rank = 5, studentName = "Sakib (You)", scoreXp = 1920, quizAccuracyPercent = 88f, streakDays = 12, isCurrentUser = true),
            LeaderboardEntry(rank = 6, studentName = "Fariha S. (Sylhet Board)", scoreXp = 1850, quizAccuracyPercent = 86f, streakDays = 15),
            LeaderboardEntry(rank = 7, studentName = "Rafi A. (Barishal Board)", scoreXp = 1790, quizAccuracyPercent = 84f, streakDays = 14),
            LeaderboardEntry(rank = 8, studentName = "Jahid K. (Jashore Board)", scoreXp = 1720, quizAccuracyPercent = 82f, streakDays = 11),
            LeaderboardEntry(rank = 9, studentName = "Sumaiya T. (Dinajpur Board)", scoreXp = 1680, quizAccuracyPercent = 81f, streakDays = 10),
            LeaderboardEntry(rank = 10, studentName = "Imran H. (Mymensingh Board)", scoreXp = 1620, quizAccuracyPercent = 80f, streakDays = 9),
            LeaderboardEntry(rank = 11, studentName = "Sabbir A. (Madrasah Board)", scoreXp = 1590, quizAccuracyPercent = 79f, streakDays = 8),
            LeaderboardEntry(rank = 12, studentName = "Nayeem R. (Technical Board)", scoreXp = 1540, quizAccuracyPercent = 78f, streakDays = 7)
        )
    ).asStateFlow()

    // DEDICATED AI MENTOR WORKSPACE STATE
    private val _conversations = MutableStateFlow<List<AiConversation>>(
        listOf(
            AiConversation(id = "conv_1", title = "Physics — Dynamics & Projectile Motion", subject = "Physics 1st Paper", chapter = "Chapter 3", topic = "Dynamics", lastTimestampStr = "Today"),
            AiConversation(id = "conv_2", title = "Chemistry — Qualitative Solubility Ksp", subject = "Chemistry 1st Paper", chapter = "Chapter 2", topic = "Qualitative Chem", lastTimestampStr = "Yesterday"),
            AiConversation(id = "conv_3", title = "Higher Math — Integration by Parts", subject = "Higher Math 1st Paper", chapter = "Chapter 10", topic = "Integration", lastTimestampStr = "3 days ago")
        )
    )
    val conversations: StateFlow<List<AiConversation>> = _conversations.asStateFlow()

    private val _currentConversationId = MutableStateFlow("conv_1")
    val currentConversationId: StateFlow<String> = _currentConversationId.asStateFlow()

    private val _selectedAiMode = MutableStateFlow("TEACH") // TEACH, PRACTICE, QUIZ, EXAM, REVISE, SOLVE, NOTES
    val selectedAiMode: StateFlow<String> = _selectedAiMode.asStateFlow()

    fun setSelectedAiMode(mode: String) {
        _selectedAiMode.value = mode
    }

    private val _isFocusMode = MutableStateFlow(false)
    val isFocusMode: StateFlow<Boolean> = _isFocusMode.asStateFlow()

    fun toggleFocusMode() {
        _isFocusMode.value = !_isFocusMode.value
    }

    private val _activeAiContext = MutableStateFlow<AiMentorContext?>(
        AiMentorContext(subject = "Physics 1st Paper", chapter = "Chapter 3", topic = "Dynamics & Motion", sourceModule = "Dashboard")
    )
    val activeAiContext: StateFlow<AiMentorContext?> = _activeAiContext.asStateFlow()

    /**
     * Context-Aware Launcher for AI Mentor from ANY screen module in the app
     */
    fun openMentorWithContext(
        subject: String,
        paper: String = "1st Paper",
        chapter: String,
        topic: String,
        initialPrompt: String = "",
        mode: String = "TEACH",
        sourceModule: String = "Direct"
    ) {
        _activeAiContext.value = AiMentorContext(
            subject = subject,
            paper = paper,
            chapter = chapter,
            topic = topic,
            initialPrompt = initialPrompt,
            sourceModule = sourceModule
        )
        _teacherContextSubject.value = subject
        _selectedAiMode.value = mode

        val titleStr = "$subject — $topic"
        val newConv = AiConversation(title = titleStr, subject = subject, chapter = chapter, topic = topic, mode = mode)
        _conversations.value = listOf(newConv) + _conversations.value
        _currentConversationId.value = newConv.id

        if (initialPrompt.isNotBlank()) {
            sendTeacherMessage(initialPrompt)
        }
        navigateTo("teacher")
    }

    fun selectConversation(convId: String) {
        _currentConversationId.value = convId
        _conversations.value.find { it.id == convId }?.let { conv ->
            _teacherContextSubject.value = conv.subject
            _activeAiContext.value = AiMentorContext(subject = conv.subject, chapter = conv.chapter, topic = conv.topic)
        }
    }

    fun createNewConversation() {
        val newConv = AiConversation(title = "New HSC Study Chat", subject = _teacherContextSubject.value)
        _conversations.value = listOf(newConv) + _conversations.value
        _currentConversationId.value = newConv.id
        _chatMessages.value = listOf(
            ChatMessage(
                sender = "AI",
                text = "Hello! I am your HSC Mentor AI teacher. I'm ready to help you with ${_teacherContextSubject.value}. What concept shall we master?"
            )
        )
    }

    fun deleteConversation(convId: String) {
        _conversations.value = _conversations.value.filter { it.id != convId }
    }

    // AI Teacher Chat
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "AI",
                text = "Hello! I am your HSC Mentor AI teacher. What topic in HSC 2028 would you like to explore or clarify today?"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _teacherContextSubject = MutableStateFlow("Physics 1st Paper")
    val teacherContextSubject: StateFlow<String> = _teacherContextSubject.asStateFlow()

    fun setTeacherContextSubject(subject: String) {
        _teacherContextSubject.value = subject
    }

    fun sendTeacherMessage(userMessageText: String) {
        if (userMessageText.isBlank()) return

        val modePromptPrefix = when (_selectedAiMode.value) {
            "PRACTICE" -> "[PRACTICE MODE: Provide hints first rather than giving away answer] "
            "QUIZ" -> "[QUIZ MODE: Ask a board-style question to test understanding] "
            "EXAM" -> "[EXAM MODE: Evaluate strictly as per HSC 2028 marking scheme] "
            "REVISE" -> "[REVISE MODE: Provide concise formulas and quick memory tricks] "
            "SOLVE" -> "[SOLVE MODE: Step by step mathematical derivation] "
            "NOTES" -> "[NOTES MODE: Create structured bullet points for revision] "
            else -> ""
        }

        val userMsg = ChatMessage(sender = "USER", text = modePromptPrefix + userMessageText)
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _isAiThinking.value = true
            val replyText = repository.askAiTeacherMultiTurn(_chatMessages.value, _teacherContextSubject.value)
            _isAiThinking.value = false
            val aiMsg = ChatMessage(sender = "AI", text = replyText)
            _chatMessages.value = _chatMessages.value + aiMsg
            logActivity("Asked AI Mentor", "${_teacherContextSubject.value}: $userMessageText", "🧠")
        }
    }


    // Timer State
    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null

    fun startTimer(subject: String, topic: String, minutes: Int, goal: String) {
        timerJob?.cancel()
        val totalSecs = minutes * 60
        _timerState.value = TimerState(
            isRunning = true,
            isPaused = false,
            subject = subject,
            topic = topic,
            totalSeconds = totalSecs,
            remainingSeconds = totalSecs,
            sessionGoal = goal
        )

        timerJob = viewModelScope.launch {
            while (_timerState.value.remainingSeconds > 0 && _timerState.value.isRunning) {
                if (!_timerState.value.isPaused) {
                    delay(1000)
                    _timerState.value = _timerState.value.copy(
                        remainingSeconds = _timerState.value.remainingSeconds - 1
                    )
                } else {
                    delay(500)
                }
            }
            if (_timerState.value.remainingSeconds <= 0 && _timerState.value.isRunning) {
                // Timer finished
                _timerState.value = _timerState.value.copy(
                    isRunning = false,
                    showReflectionDialog = true
                )
            }
        }
    }

    fun pauseTimer() {
        _timerState.value = _timerState.value.copy(isPaused = true)
    }

    fun resumeTimer() {
        _timerState.value = _timerState.value.copy(isPaused = false)
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timerState.value = _timerState.value.copy(isRunning = false, showReflectionDialog = true)
    }

    fun completeSessionWithReflection(focusRating: Int, confidence: Int, notes: String, reflection: String) {
        val current = _timerState.value
        val spentMinutes = (current.totalSeconds - current.remainingSeconds) / 60
        val duration = if (spentMinutes < 1) 1 else spentMinutes

        viewModelScope.launch {
            repository.recordStudySession(
                StudySessionRecord(
                    subject = current.subject,
                    topic = current.topic,
                    durationMinutes = duration,
                    focusRating = focusRating,
                    confidence = confidence,
                    notesSummary = notes,
                    reflection = reflection
                )
            )
            _timerState.value = TimerState()
        }
    }

    fun dismissReflectionDialog() {
        _timerState.value = _timerState.value.copy(showReflectionDialog = false)
    }

    // Onboarding
    fun completeOnboarding(profile: StudentProfile) {
        viewModelScope.launch {
            repository.saveStudentProfile(profile.copy(isOnboarded = true))
            _dailyBriefing.value = repository.getDailyBriefingText()
            _currentScreen.value = "dashboard"
        }
    }

    fun updateStudentProfile(profile: StudentProfile) {
        viewModelScope.launch {
            repository.saveStudentProfile(profile)
        }
    }

    // Plan Items
    fun togglePlanItemCompleted(itemId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            val dao = HscDatabase.getDatabase(getApplication()).hscDao()
            dao.setPlanCompleted(itemId, isCompleted)
        }
    }

    fun rebalancePlanner() {
        viewModelScope.launch {
            repository.rebalanceFutureStudyPlan()
        }
    }

    // MCQ Practice state
    private val _currentMcqQuestions = MutableStateFlow<List<MCQQuestion>>(emptyList())
    val currentMcqQuestions: StateFlow<List<MCQQuestion>> = _currentMcqQuestions.asStateFlow()

    private val _isGeneratingMcq = MutableStateFlow(false)
    val isGeneratingMcq: StateFlow<Boolean> = _isGeneratingMcq.asStateFlow()

    fun generateMcqs(subject: String, chapter: String, topic: String, difficulty: String, count: Int = 5) {
        viewModelScope.launch {
            _isGeneratingMcq.value = true
            val questions = repository.generateMcqSet(subject, chapter, topic, difficulty, count)
            _currentMcqQuestions.value = questions
            _isGeneratingMcq.value = false
        }
    }

    fun submitQuizScore(subject: String, chapter: String, score: Int, total: Int, timeTakenSecs: Int, weakTopics: String) {
        viewModelScope.launch {
            val accuracy = (score.toFloat() / total.toFloat()) * 100f
            repository.saveQuizAttempt(
                QuizAttempt(
                    subject = subject,
                    chapter = chapter,
                    score = score,
                    totalQuestions = total,
                    accuracyPercent = accuracy,
                    timeTakenSeconds = timeTakenSecs,
                    weakTopicsList = weakTopics
                )
            )
        }
    }

    fun getChaptersForSubject(subjectName: String): Flow<List<ChapterEntity>> =
        repository.getChaptersForSubject(subjectName)

    fun getTopicsForSubject(subjectName: String): Flow<List<TopicEntity>> =
        repository.getTopicsForSubject(subjectName)

    fun ensureChaptersForSubject(subjectName: String) {
        viewModelScope.launch {
            repository.ensureChaptersForSubject(subjectName)
        }
    }

    fun updateTopicProgress(topicId: Int, status: String, confidence: Int) {
        viewModelScope.launch {
            repository.updateTopicProgress(topicId, status, confidence)
        }
    }

    fun reseedCurriculumForGroup(groupName: String) {
        viewModelScope.launch {
            repository.seedInitialSubjectsIfEmpty(groupName)
        }
    }

    fun submitExamAttempt(examTitle: String, totalScore: Int, userScore: Int) {
        viewModelScope.launch {
            val accuracy = (userScore.toFloat() / totalScore.toFloat()) * 100f
            repository.saveExamAttempt(
                ExamAttempt(
                    examTitle = examTitle,
                    totalScore = totalScore,
                    userScore = userScore,
                    accuracyPercent = accuracy
                )
            )
        }
    }

    // CQ Practice
    private val _cqEvaluationResult = MutableStateFlow<CQAttempt?>(null)
    val cqEvaluationResult: StateFlow<CQAttempt?> = _cqEvaluationResult.asStateFlow()

    private val _isEvaluatingCq = MutableStateFlow(false)
    val isEvaluatingCq: StateFlow<Boolean> = _isEvaluatingCq.asStateFlow()

    fun evaluateCq(subject: String, chapter: String, stem: String, answer: String) {
        viewModelScope.launch {
            _isEvaluatingCq.value = true
            val result = repository.evaluateAndSaveCq(subject, chapter, stem, answer)
            _cqEvaluationResult.value = result
            _isEvaluatingCq.value = false
        }
    }

    fun resetCqEvaluation() {
        _cqEvaluationResult.value = null
    }

    // Mistake Book
    fun addMistake(mistake: MistakeRecord) {
        viewModelScope.launch {
            repository.logMistake(mistake)
        }
    }

    fun markMistakeStatus(id: Int, status: String) {
        viewModelScope.launch {
            repository.updateMistakeStatus(id, status)
        }
    }

    // Notes
    private val _isGeneratingNote = MutableStateFlow(false)
    val isGeneratingNote: StateFlow<Boolean> = _isGeneratingNote.asStateFlow()

    fun createNoteWithAi(subject: String, chapter: String, topic: String, rawContent: String) {
        viewModelScope.launch {
            _isGeneratingNote.value = true
            repository.generateAiNotes(subject, chapter, topic, rawContent)
            _isGeneratingNote.value = false
        }
    }

    // Source Material
    fun addSourceMaterial(title: String, subject: String, content: String) {
        viewModelScope.launch {
            repository.addSourceMaterial(SourceMaterial(title = title, subject = subject, contentText = content))
        }
    }

    fun deleteSourceMaterial(id: Int) {
        viewModelScope.launch {
            repository.deleteSourceMaterial(id)
        }
    }

    init {
        viewModelScope.launch {
            _dailyBriefing.value = repository.getDailyBriefingText()
            val profile = repository.studentProfile.firstOrNull()
            repository.ensureCurriculumSeeded(profile?.group ?: "Science")
        }
    }
}
