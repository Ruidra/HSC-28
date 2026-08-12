package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.screens.*
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.HscMentorTheme
import com.example.ui.viewmodel.HscViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: HscViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HscMentorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    MainLayout(viewModel = viewModel) { currentRoute ->
                        when (currentRoute) {
                            "dashboard" -> DashboardScreen(viewModel = viewModel)
                            "planner" -> PlannerScreen(viewModel = viewModel)
                            "subjects" -> SubjectsScreen(viewModel = viewModel)
                            "timer" -> StudyTimerScreen(viewModel = viewModel)
                            "teacher" -> AiTeacherScreen(viewModel = viewModel)
                            "live_class" -> LiveClassScreen(viewModel = viewModel)
                            "doubt_solve" -> DoubtSolveScreen(viewModel = viewModel)
                            "study_room" -> StudyRoomScreen(viewModel = viewModel)
                            "live_exams" -> LiveExamQuizScreen(viewModel = viewModel)
                            "notes" -> NotesScreen(viewModel = viewModel)
                            "mcq" -> McqScreen(viewModel = viewModel)
                            "cq" -> CqPracticeScreen(viewModel = viewModel)
                            "exams" -> ExamSimulatorScreen(viewModel = viewModel)
                            "mistakes" -> MistakeBookScreen(viewModel = viewModel)
                            "revision" -> RevisionScreen(viewModel = viewModel)
                            "analytics" -> AnalyticsScreen(viewModel = viewModel)
                            "voice_teacher" -> VoiceTeacherScreen(viewModel = viewModel)
                            "materials" -> MaterialsScreen(viewModel = viewModel)
                            "settings" -> SettingsScreen(viewModel = viewModel)
                            else -> DashboardScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
