package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel

@Composable
fun PlannerScreen(viewModel: HscViewModel) {
    val todayPlan by viewModel.todayStudyPlan.collectAsState()
    val profile by viewModel.studentProfile.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Daily, 1: Weekly, 2: Monthly

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
                Text("AI Study Planner", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                Text("Personalized schedule optimized for HSC 2028 A+", fontSize = 12.sp, color = CyanPrimary)
            }

            Button(
                onClick = { viewModel.rebalancePlanner() },
                modifier = Modifier.testTag("rebalance_plan_button"),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Autorenew, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Auto Rebalance", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Selector
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkSurface,
            contentColor = CyanPrimary
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Daily Plan", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Weekly Overview", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Text("Monthly Syllabus", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> {
                // Daily Plan List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = DarkCardSurface,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = EmeraldSecondary)
                                Text(
                                    "Target Daily Hours: ${profile?.availableHours ?: 4.5f} Hours. If you miss a task, click 'Auto Rebalance' to safely reschedule without stress.",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    items(todayPlan) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Checkbox(
                                        checked = item.isCompleted,
                                        onCheckedChange = { viewModel.togglePlanItemCompleted(item.id, it) },
                                        colors = CheckboxDefaults.colors(checkedColor = EmeraldSecondary)
                                    )

                                    Column {
                                        Text(
                                            text = item.subjectName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = item.topicName,
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Surface(
                                        color = when (item.priority) {
                                            1 -> RoseError.copy(alpha = 0.2f)
                                            2 -> AmberWarning.copy(alpha = 0.2f)
                                            else -> EmeraldSecondary.copy(alpha = 0.2f)
                                        },
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = when (item.priority) {
                                                1 -> "High Priority"
                                                2 -> "Medium Priority"
                                                else -> "Normal"
                                            },
                                            fontSize = 9.sp,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("${item.scheduledMinutes} Mins", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Surface(
                                        onClick = {
                                            viewModel.startTimer(item.subjectName, item.topicName, item.scheduledMinutes, "Complete ${item.topicName} study task")
                                            viewModel.navigateTo("timer")
                                        },
                                        color = DarkSurfaceVariant,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Start Timer ⏱️", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldSecondary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Weekly Overview Placeholder
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Weekly Schedule Rebalance", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Mon-Wed: Physics & Higher Math Heavy Concepts", fontSize = 13.sp, color = TextSecondary)
                    Text("• Thu-Fri: Chemistry Organic & CQ Practice", fontSize = 13.sp, color = TextSecondary)
                    Text("• Sat-Sun: ICT, Bangla, English & Mock Quizzes", fontSize = 13.sp, color = TextSecondary)
                }
            }

            2 -> {
                // Monthly Syllabus
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Monthly Milestones", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Target Completion for Current Month: 45% Syllabus Coverage.", fontSize = 13.sp, color = TextSecondary)
                }
            }
        }
    }
}
