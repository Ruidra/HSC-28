package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HscDao {
    // Student Profile
    @Query("SELECT * FROM student_profile WHERE id = 1")
    fun getStudentProfile(): Flow<StudentProfile?>

    @Query("SELECT * FROM student_profile WHERE id = 1")
    suspend fun getStudentProfileSync(): StudentProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStudentProfile(profile: StudentProfile)

    // Subjects
    @Query("SELECT * FROM subjects ORDER BY name ASC")
    fun getAllSubjects(): Flow<List<SubjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: SubjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>)

    @Query("DELETE FROM subjects WHERE id = :subjectId")
    suspend fun deleteSubject(subjectId: Int)

    // Chapters
    @Query("SELECT * FROM chapters WHERE subjectName = :subjectName ORDER BY chapterNumber ASC")
    fun getChaptersForSubject(subjectName: String): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters")
    fun getAllChapters(): Flow<List<ChapterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: ChapterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    // Topics
    @Query("SELECT * FROM topics WHERE subjectName = :subjectName ORDER BY id ASC")
    fun getTopicsForSubject(subjectName: String): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics WHERE isWeak = 1")
    fun getWeakTopics(): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics")
    fun getAllTopics(): Flow<List<TopicEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<TopicEntity>)

    @Query("UPDATE topics SET status = :status, confidence = :confidence WHERE id = :id")
    suspend fun updateTopicProgress(id: Int, status: String, confidence: Int)

    // Study Plan Items
    @Query("SELECT * FROM study_plan_items WHERE date = :date ORDER BY priority ASC")
    fun getStudyPlanForDate(date: String): Flow<List<StudyPlanItem>>

    @Query("SELECT * FROM study_plan_items WHERE isCompleted = 0")
    fun getPendingStudyPlans(): Flow<List<StudyPlanItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyPlanItem(item: StudyPlanItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyPlanItems(items: List<StudyPlanItem>)

    @Query("UPDATE study_plan_items SET isCompleted = :completed WHERE id = :itemId")
    suspend fun setPlanCompleted(itemId: Int, completed: Boolean)

    @Query("DELETE FROM study_plan_items WHERE date = :date")
    suspend fun clearPlanForDate(date: String)

    // Study Sessions
    @Query("SELECT * FROM study_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<StudySessionRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionRecord)

    // AI Notes
    @Query("SELECT * FROM ai_notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<AINote>>

    @Query("SELECT * FROM ai_notes WHERE subject = :subject ORDER BY timestamp DESC")
    fun getNotesForSubject(subject: String): Flow<List<AINote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: AINote)

    @Query("DELETE FROM ai_notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: Int)

    // MCQ Questions & Quizzes
    @Query("SELECT * FROM mcq_questions WHERE subject = :subject")
    suspend fun getMcqQuestionsForSubject(subject: String): List<MCQQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMcqQuestion(question: MCQQuestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMcqQuestions(questions: List<MCQQuestion>)

    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC")
    fun getAllQuizAttempts(): Flow<List<QuizAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizAttempt(attempt: QuizAttempt)

    // CQ Attempts
    @Query("SELECT * FROM cq_attempts ORDER BY timestamp DESC")
    fun getAllCQAttempts(): Flow<List<CQAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCQAttempt(attempt: CQAttempt)

    // Exam Attempts
    @Query("SELECT * FROM exam_attempts ORDER BY timestamp DESC")
    fun getAllExamAttempts(): Flow<List<ExamAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamAttempt(attempt: ExamAttempt)

    // Mistake Book
    @Query("SELECT * FROM mistakes ORDER BY dateTimestamp DESC")
    fun getAllMistakes(): Flow<List<MistakeRecord>>

    @Query("SELECT * FROM mistakes WHERE revisionStatus = 'Pending'")
    fun getPendingMistakes(): Flow<List<MistakeRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistake(mistake: MistakeRecord)

    @Query("UPDATE mistakes SET revisionStatus = :status WHERE id = :id")
    suspend fun updateMistakeStatus(id: Int, status: String)

    @Query("DELETE FROM mistakes WHERE id = :id")
    suspend fun deleteMistake(id: Int)

    // Revision Items
    @Query("SELECT * FROM revision_items ORDER BY dueDate ASC")
    fun getAllRevisionItems(): Flow<List<RevisionItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRevisionItem(item: RevisionItem)

    @Query("UPDATE revision_items SET status = :status WHERE id = :id")
    suspend fun updateRevisionStatus(id: Int, status: String)

    // Source Materials
    @Query("SELECT * FROM source_materials ORDER BY dateAdded DESC")
    fun getAllSourceMaterials(): Flow<List<SourceMaterial>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSourceMaterial(material: SourceMaterial)

    @Query("DELETE FROM source_materials WHERE id = :id")
    suspend fun deleteSourceMaterial(id: Int)
}
