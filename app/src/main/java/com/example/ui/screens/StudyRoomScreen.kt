package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AcademicContentViewer
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyRoomScreen(viewModel: HscViewModel) {
    val timerState by viewModel.timerState.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()

    var studySubject by remember { mutableStateOf("Physics 1st Paper") }
    var studyTopic by remember { mutableStateOf("Chapter 3: Dynamics & Friction") }
    var durationMinutes by remember { mutableStateOf(25) }
    var sessionGoalText by remember { mutableStateOf("Solve 5 CQ problems & master banking angle formula.") }

    var selectedSection by remember { mutableStateOf(0) } // 0: Timer & Focus, 1: AI Teacher Panel, 2: Practice MCQs, 3: Notes
    var aiQueryInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        // Room Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BentoLavenderPrimary, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("⚡ LIVE STUDY ROOM", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BentoLavenderPrimary)
                        if (timerState.isRunning) {
                            Surface(color = EmeraldSecondary, shape = RoundedCornerShape(8.dp)) {
                                Text("ACTIVE SESSION", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }

                    Text("HSC 2028 Study Workspace", fontSize = 11.sp, color = TextSecondary)
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(studyTopic, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                Text("Subject: $studySubject • Goal: $sessionGoalText", fontSize = 12.sp, color = CyanPrimary)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Section Tabs
        TabRow(
            selectedTabIndex = selectedSection,
            containerColor = DarkSurface,
            contentColor = CyanPrimary,
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
            Tab(selected = selectedSection == 0, onClick = { selectedSection = 0 }, text = { Text("⏱️ TIMER", fontWeight = FontWeight.Bold, fontSize = 11.sp) })
            Tab(selected = selectedSection == 1, onClick = { selectedSection = 1 }, text = { Text("🧠 AI MENTOR", fontWeight = FontWeight.Bold, fontSize = 11.sp) })
            Tab(selected = selectedSection == 2, onClick = { selectedSection = 2 }, text = { Text("🎯 PRACTICE", fontWeight = FontWeight.Bold, fontSize = 11.sp) })
            Tab(selected = selectedSection == 3, onClick = { selectedSection = 3 }, text = { Text("📝 NOTES", fontWeight = FontWeight.Bold, fontSize = 11.sp) })
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedSection) {
            0 -> {
                // TIMER & FOCUS CONTROLS
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        val mins = timerState.remainingSeconds / 60
                        val secs = timerState.remainingSeconds % 60
                        val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", mins, secs)

                        Text("SESSION COUNTDOWN", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = BentoLavenderPrimary, letterSpacing = 1.sp)

                        Text(
                            text = formattedTime,
                            fontWeight = FontWeight.Bold,
                            fontSize = 54.sp,
                            color = TextPrimary
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            if (!timerState.isRunning) {
                                Button(
                                    onClick = { viewModel.startTimer(studySubject, studyTopic, durationMinutes, sessionGoalText) },
                                    colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.height(44.dp)
                                ) {
                                    Text("Start Session ▶", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            } else {
                                if (timerState.isPaused) {
                                    Button(
                                        onClick = { viewModel.resumeTimer() },
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary, contentColor = Color.Black),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Resume ▶", fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Button(
                                        onClick = { viewModel.pauseTimer() },
                                        colors = ButtonDefaults.buttonColors(containerColor = AmberWarning, contentColor = Color.Black),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Pause ⏸", fontWeight = FontWeight.Bold)
                                    }
                                }

                                Button(
                                    onClick = { viewModel.stopTimer() },
                                    colors = ButtonDefaults.buttonColors(containerColor = RedPrimary, contentColor = Color.White),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Mark Complete ✓", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // AI MENTOR SIDE PANEL INSIDE STUDY ROOM
                Column(modifier = Modifier.fillMaxSize()) {
                    Text("Ask AI Mentor about $studyTopic:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(chatMessages.takeLast(4)) { msg ->
                            Surface(
                                color = if (msg.sender == "USER") DarkSurfaceVariant else DarkCardSurface,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(if (msg.sender == "USER") "You" else "AI Teacher", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = CyanPrimary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    AcademicContentViewer(content = msg.text)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedTextField(
                            value = aiQueryInput,
                            onValueChange = { aiQueryInput = it },
                            placeholder = { Text("Ask study room teacher...", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Button(
                            onClick = {
                                if (aiQueryInput.isNotBlank()) {
                                    viewModel.sendTeacherMessage(aiQueryInput)
                                    aiQueryInput = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Ask", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            2 -> {
                // QUICK PRACTICE
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("🎯 Study Room Quick Practice", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                        Text("Generate 5 practice questions for this session:", fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                viewModel.generateMcqs(studySubject, "Chapter 3", studyTopic, "Medium", 5)
                                viewModel.navigateTo("mcq")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Generate Session MCQs 🚀", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            3 -> {
                // SESSION NOTES
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("📝 Session Smart Notes", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                        Text("Save key takeaways to your Notes module:", fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { viewModel.navigateTo("notes") },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Open Notes Module 📓", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
