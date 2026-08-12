package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveExamQuizScreen(viewModel: HscViewModel) {
    val leaderboardEntries by viewModel.leaderboardEntries.collectAsState()
    val isLeaderboardOptedIn by viewModel.isLeaderboardOptedIn.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Live Quiz, 1: Scheduled Exams, 2: Leaderboard
    var quizCodeInput by remember { mutableStateOf("") }
    var joinStatusMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Real-Time Live Quiz & Exams", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                Text("Server-authoritative timer & competitive board exams", fontSize = 11.sp, color = CyanPrimary)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Navigation Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkSurface,
            contentColor = CyanPrimary,
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("⚡ LIVE QUIZ", fontWeight = FontWeight.Bold, fontSize = 11.sp) })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("🎓 SCHEDULED EXAMS", fontWeight = FontWeight.Bold, fontSize = 11.sp) })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("🏆 LEADERBOARD", fontWeight = FontWeight.Bold, fontSize = 11.sp) })
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedTab) {
            0 -> {
                // JOIN LIVE QUIZ BY CODE
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BentoLavenderPrimary)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("🔑 Join Live Classroom Quiz", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                        Text("Enter 6-digit Quiz Code provided by your teacher:", fontSize = 12.sp, color = TextSecondary)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = quizCodeInput,
                                onValueChange = { quizCodeInput = it.take(6) },
                                placeholder = { Text("e.g. 849201", fontSize = 13.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Button(
                                onClick = {
                                    if (quizCodeInput.length == 6) {
                                        joinStatusMessage = "Connecting to Live Quiz #$quizCodeInput..."
                                        viewModel.generateMcqs("Physics 1st Paper", "Chapter 3", "Live Quiz", "Medium", 5)
                                        viewModel.navigateTo("mcq")
                                    } else {
                                        joinStatusMessage = "Please enter a valid 6-digit quiz code."
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Join Quiz 🚀", fontWeight = FontWeight.Bold)
                            }
                        }

                        if (joinStatusMessage.isNotBlank()) {
                            Text(joinStatusMessage, fontSize = 11.sp, color = CyanPrimary)
                        }
                    }
                }
            }

            1 -> {
                // SCHEDULED EXAMS
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().border(1.dp, RedPrimary, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(color = RedPrimary, shape = RoundedCornerShape(8.dp)) {
                                        Text("LIVE NOW", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                    }
                                    Text("Ends in: 45:20", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmberWarning)
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text("HSC 2028 Model Exam 04: Physics & Chemistry", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                Text("Full Length Board Standard MCQ + CQ Paper", fontSize = 12.sp, color = CyanPrimary)

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = { viewModel.navigateTo("exams") },
                                    colors = ButtonDefaults.buttonColors(containerColor = RedPrimary, contentColor = Color.White),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Enter Live Exam Room 🔴", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // LEADERBOARD WITH OPT-IN / ANONYMOUS SWITCH
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🏆 HSC 2028 Weekly Leaderboard", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Opt In:", fontSize = 11.sp, color = TextMuted)
                                Switch(
                                    checked = isLeaderboardOptedIn,
                                    onCheckedChange = { viewModel.toggleLeaderboardOptIn() }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (!isLeaderboardOptedIn) {
                            Text("Leaderboard participation is currently disabled in your Settings.", fontSize = 12.sp, color = AmberWarning)
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.heightIn(max = 300.dp)
                            ) {
                                items(leaderboardEntries) { entry ->
                                    Surface(
                                        color = if (entry.isCurrentUser) BentoDeepPurple else DarkCardSurface,
                                        shape = RoundedCornerShape(10.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, if (entry.isCurrentUser) BentoLavenderPrimary else GlassBorder),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                                Text("#${entry.rank}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BentoLavenderPrimary)
                                                Column {
                                                    Text(entry.studentName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                                    Text("Accuracy: ${entry.quizAccuracyPercent.toInt()}% • Streak: ${entry.streakDays}d 🔥", fontSize = 10.sp, color = TextSecondary)
                                                }
                                            }

                                            Text("${entry.scoreXp} XP", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = EmeraldSecondary)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
