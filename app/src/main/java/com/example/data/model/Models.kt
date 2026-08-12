package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_profile")
data class StudentProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Student",
    val hscYear: String = "2028",
    val board: String = "Dhaka",
    val group: String = "Science",
    val targetResult: String = "A+",
    val currentLevel: String = "Intermediate",
    val coachingSchedule: String = "Morning 8 AM - 12 PM",
    val availableHours: Float = 5.0f,
    val preferredStudyTimes: String = "Evening & Night",
    val weeklyHolidays: String = "Friday",
    val difficultTopicsText: String = "Integration, Organic Chemistry, Vector Calculus",
    val confidentTopicsText: String = "ICT, English Grammar, Dynamics",
    val language: String = "Bangla & English",
    val streakDays: Int = 1,
    val lastStudyDate: Long = System.currentTimeMillis(),
    val xp: Int = 120,
    val isOnboarded: Boolean = false
)

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val code: String,
    val colorHex: String,
    val completionPercent: Float = 0f,
    val confidenceLevel: Int = 3,
    val lastStudiedTimestamp: Long = System.currentTimeMillis(),
    val needsAttention: Boolean = false
)

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: Int,
    val subjectName: String,
    val chapterNumber: Int,
    val title: String,
    val completionPercent: Float = 0f,
    val isDifficult: Boolean = false
)

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chapterId: Int,
    val subjectName: String,
    val chapterTitle: String,
    val title: String,
    val status: String = "Pending", // Pending, In Progress, Mastered
    val confidence: Int = 2, // 1-5
    val lastStudiedTimestamp: Long = System.currentTimeMillis(),
    val isWeak: Boolean = false,
    val mcqAccuracy: Float = 0f,
    val cqAccuracy: Float = 0f
)

@Entity(tableName = "study_plan_items")
data class StudyPlanItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // YYYY-MM-DD
    val subjectName: String,
    val topicName: String,
    val scheduledMinutes: Int = 45,
    val sessionType: String = "Concept", // Concept, Practice, Revision, Exam
    val isCompleted: Boolean = false,
    val priority: Int = 2 // 1: High, 2: Med, 3: Low
)

@Entity(tableName = "study_sessions")
data class StudySessionRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val subject: String,
    val topic: String,
    val durationMinutes: Int,
    val focusRating: Int = 4, // 1-5
    val confidence: Int = 3, // 1-5
    val notesSummary: String = "",
    val reflection: String = ""
)

@Entity(tableName = "ai_notes")
data class AINote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val chapter: String,
    val topic: String,
    val title: String,
    val contentMarkdown: String,
    val keyConcepts: String,
    val formulas: String,
    val commonMistakes: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "mcq_questions")
data class MCQQuestion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val chapter: String,
    val topic: String,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOptionIndex: Int, // 0-3
    val explanation: String,
    val difficulty: String = "Medium",
    val topicTag: String = ""
)

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val subject: String,
    val chapter: String,
    val score: Int,
    val totalQuestions: Int,
    val accuracyPercent: Float,
    val timeTakenSeconds: Int,
    val weakTopicsList: String = ""
)

@Entity(tableName = "cq_attempts")
data class CQAttempt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val subject: String,
    val chapter: String,
    val questionStem: String,
    val studentAnswer: String,
    val totalMarks: Int = 10,
    val earnedMarks: Int,
    val feedback: String,
    val knowledgeScore: Int = 2,
    val understandingScore: Int = 3,
    val applicationScore: Int = 2,
    val higherOrderScore: Int = 2,
    val modelAnswer: String
)

@Entity(tableName = "exam_attempts")
data class ExamAttempt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val examTitle: String,
    val subject: String = "Physics 1st Paper",
    val timestamp: Long = System.currentTimeMillis(),
    val totalScore: Int = 100,
    val userScore: Int = 0,
    val mcqScore: Int = 0,
    val cqScore: Int = 0,
    val durationMinutes: Int = 125,
    val timeSpentMinutes: Int = 100,
    val accuracyPercent: Float = 0f,
    val weakAreaSummary: String = "Review core numerical problems",
    val aiRecommendation: String = "Practice 5 additional practice CQs"
)

@Entity(tableName = "mistakes")
data class MistakeRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val chapter: String,
    val topic: String,
    val questionText: String,
    val studentAnswer: String,
    val correctAnswer: String,
    val explanation: String,
    val mistakeType: String, // Concept, Calculation, Memory, Careless, Misreading, Incomplete
    val dateTimestamp: Long = System.currentTimeMillis(),
    val revisionStatus: String = "Pending" // Pending, Revised, Mastered
)

@Entity(tableName = "revision_items")
data class RevisionItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val topic: String,
    val lastStudiedDate: Long,
    val dueDate: Long,
    val priorityScore: Int, // 1: Urgent, 2: High, 3: Normal
    val status: String = "Due Today" // Due Today, Upcoming, Completed
)

@Entity(tableName = "source_materials")
data class SourceMaterial(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subject: String,
    val contentText: String,
    val dateAdded: Long = System.currentTimeMillis()
)

// REAL-TIME PLATFORM DATA MODELS

data class LiveClass(
    val id: String,
    val subject: String,
    val teacherName: String,
    val topic: String,
    val dateTimeStr: String,
    val startTimeStr: String,
    val durationMins: Int,
    val description: String,
    val status: String, // UPCOMING, LIVE NOW, ENDED
    val participantCount: Int = 0,
    val isStreamConfigured: Boolean = false // Shows "Live streaming is not configured" notice if false
)

data class LiveChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val senderName: String,
    val message: String,
    val timestampStr: String,
    val isTeacher: Boolean = false
)

data class RecordedClass(
    val id: String,
    val subject: String,
    val chapter: String,
    val topic: String,
    val teacher: String,
    val durationMins: Int,
    val description: String,
    val progressPercent: Float = 0f,
    val isCompleted: Boolean = false,
    val videoUrl: String = ""
)

data class DoubtItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val subject: String,
    val topic: String,
    val questionText: String,
    val submittedTimeStr: String,
    val status: String, // NEW, AI ANALYZING, AI ANSWERED, TEACHER REVIEW, ANSWERED
    val photoUrl: String = "",
    val aiStepExplanation: String = "",
    val conceptKey: String = "",
    val formulaText: String = "",
    val exampleText: String = "",
    val commonMistakeText: String = "",
    val followUpQuestionText: String = "",
    val teacherReviewNote: String = ""
)

data class NotificationItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val timeAgoStr: String,
    val type: String, // LIVE_CLASS, EXAM, REVISION, DOUBT, AI_RECOMMENDATION
    val isRead: Boolean = false
)

data class ActivityLog(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val timeStr: String,
    val iconEmoji: String,
    val isCompleted: Boolean = true
)

data class LeaderboardEntry(
    val rank: Int,
    val studentName: String,
    val scoreXp: Int,
    val quizAccuracyPercent: Float,
    val streakDays: Int,
    val isCurrentUser: Boolean = false
)

data class AiConversation(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val subject: String = "Physics 1st Paper",
    val chapter: String = "Dynamics",
    val topic: String = "Newton's Laws",
    val lastTimestampStr: String = "Just now",
    val mode: String = "TEACH" // TEACH, PRACTICE, QUIZ, EXAM, REVISE, SOLVE, NOTES
)

data class AiMentorContext(
    val subject: String,
    val paper: String = "1st Paper",
    val chapter: String,
    val topic: String,
    val initialPrompt: String = "",
    val sourceModule: String = "Direct"
)

