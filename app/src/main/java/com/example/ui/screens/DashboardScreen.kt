package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel

@Composable
fun DashboardScreen(viewModel: HscViewModel) {
    val studentProfile by viewModel.studentProfile.collectAsState()
    val todayPlan by viewModel.todayStudyPlan.collectAsState()
    val subjects by viewModel.allSubjects.collectAsState()
    val pendingMistakes by viewModel.pendingMistakes.collectAsState()
    val activityFeed by viewModel.activityFeed.collectAsState()
    val isOffline by viewModel.isOfflineMode.collectAsState()
    val isLowBandwidth by viewModel.isLowBandwidthMode.collectAsState()
    val liveClasses by viewModel.liveClasses.collectAsState()

    val topLiveClass = liveClasses.firstOrNull { it.status == "LIVE NOW" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // OFFLINE MODE BANNER
        if (isOffline) {
            item {
                Surface(
                    color = AmberWarning.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AmberWarning)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("📶", fontSize = 16.sp)
                        Column {
                            Text("OFFLINE MODE ACTIVE", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = AmberWarning)
                            Text("Your saved study materials & cached curriculum are available. AI features require connection.", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }

        // 1. TODAY'S COMMAND CENTER HERO
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BentoLavenderPrimary, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "GOOD EVENING • HSC 2028",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoLavenderPrimary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Today's Study Command Center",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Surface(
                            color = BentoDeepPurple,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BentoLavenderPrimary)
                        ) {
                            Text("78% Today", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = BentoLavenderPrimary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { 0.78f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = BentoLavenderPrimary,
                        trackColor = DarkSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("3 of 4 planned tasks finished today", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }

        // 2. REAL-TIME AI STUDY BRAIN (NEXT BEST STUDY RECOMMENDATION)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyanPrimary, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("🧠", fontSize = 16.sp)
                            Text("NEXT BEST STUDY", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = CyanPrimary, letterSpacing = 1.sp)
                        }
                        Surface(color = RedPrimary.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) {
                            Text("HIGH PRIORITY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = RedPrimary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Physics 1st Paper • Chapter 3 — Dynamics", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                    Text("Estimated: 35 minutes", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Reason: Low recent quiz accuracy & revision overdue.", fontSize = 11.sp, color = AmberWarning)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.navigateTo("study_room") },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("START NOW ▶", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.openMentorWithContext("Physics 1st Paper", "1st Paper", "Chapter 3", "Dynamics", "Teach me Dynamics step-by-step.", "TEACH", "DashboardNextBest")
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = BentoLavenderPrimary)
                        ) {
                            Text("Ask AI 🧠", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 3. QUICK PLATFORM ACTION HUB
        item {
            Text("QUICK ACTION HUB", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = BentoLavenderPrimary, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    onClick = { viewModel.navigateTo("live_class") },
                    color = DarkSurface,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔴", fontSize = 20.sp)
                        Text("Live Class", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextPrimary)
                    }
                }

                Surface(
                    onClick = { viewModel.navigateTo("doubt_solve") },
                    color = DarkSurface,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📌", fontSize = 20.sp)
                        Text("Doubt Solve", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextPrimary)
                    }
                }

                Surface(
                    onClick = { viewModel.navigateTo("live_exams") },
                    color = DarkSurface,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏆", fontSize = 20.sp)
                        Text("Live Quiz", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextPrimary)
                    }
                }

                Surface(
                    onClick = { viewModel.navigateTo("teacher") },
                    color = DarkSurface,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🧠", fontSize = 20.sp)
                        Text("AI Mentor", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextPrimary)
                    }
                }
            }
        }

        // 4. TODAY'S STUDY PLAN CHECKLIST
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📅 Today's Smart Study Plan", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                        TextButton(onClick = { viewModel.navigateTo("planner") }) {
                            Text("View All", fontSize = 11.sp, color = CyanPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    todayPlan.take(4).forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Checkbox(
                                    checked = item.isCompleted,
                                    onCheckedChange = { viewModel.togglePlanItemCompleted(item.id, it) }
                                )
                                Column {
                                    Text(item.topicName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                    Text("${item.subjectName} • ${item.scheduledMinutes} mins", fontSize = 11.sp, color = TextSecondary)
                                }
                            }

                            IconButton(
                                onClick = {
                                    viewModel.openMentorWithContext(item.subjectName, "1st Paper", "Plan", item.topicName, "Help me complete: ${item.topicName}", "TEACH", "PlanChecklist")
                                }
                            ) {
                                Text("🧠", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }

        // 5. REAL-TIME ACTIVITY FEED
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("⚡ Today's Live Study Activity Feed", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))

                    activityFeed.take(4).forEach { act ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(act.iconEmoji, fontSize = 16.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(act.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary)
                                Text(act.description, fontSize = 11.sp, color = TextSecondary)
                            }
                            Text(act.timeStr, fontSize = 10.sp, color = TextMuted)
                        }
                    }
                }
            }
        }
    }
}
