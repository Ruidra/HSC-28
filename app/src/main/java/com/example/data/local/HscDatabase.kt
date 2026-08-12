package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.*

@Database(
    entities = [
        StudentProfile::class,
        SubjectEntity::class,
        ChapterEntity::class,
        TopicEntity::class,
        StudyPlanItem::class,
        StudySessionRecord::class,
        AINote::class,
        MCQQuestion::class,
        QuizAttempt::class,
        CQAttempt::class,
        ExamAttempt::class,
        MistakeRecord::class,
        RevisionItem::class,
        SourceMaterial::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HscDatabase : RoomDatabase() {
    abstract fun hscDao(): HscDao

    companion object {
        @Volatile
        private var INSTANCE: HscDatabase? = null

        fun getDatabase(context: Context): HscDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HscDatabase::class.java,
                    "hsc_mentor_ai_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
