package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun AnalyticsScreen(viewModel: HscViewModel) {
    val quizAttempts by viewModel.allQuizAttempts.collectAsState()
    val sessions by viewModel.allSessions.collectAsState()
    val subjects by viewModel.allSubjects.collectAsState()

    val totalStudyHours = remember(sessions) {
        sessions.sumOf { it.durationMinutes } / 60f
    }

    val avgAccuracy = remember(quizAttempts) {
        if (quizAttempts.isEmpty()) 0f
        else quizAttempts.map { it.accuracyPercent }.average().toFloat()
    }

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
                Text("Performance Analytics", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                Text("AI Data Analysis for HSC 2028 Progress", fontSize = 12.sp, color = CyanPrimary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // High Level Metrics
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Total Study Time", fontSize = 11.sp, color = TextSecondary)
                            Text("${String.format("%.1f", totalStudyHours)} Hours", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CyanPrimary)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Quiz Accuracy", fontSize = 11.sp, color = TextSecondary)
                            Text("${avgAccuracy.toInt()}%", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = EmeraldSecondary)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Quizzes Passed", fontSize = 11.sp, color = TextSecondary)
                            Text("${quizAttempts.size}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PurpleAccent)
                        }
                    }
                }
            }

            // AI Weekly Report Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Insights, contentDescription = null, tint = CyanPrimary)
                            Text("AI Weekly Progress Report", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            "• Strength: Strong concept mastery in Physics Vectors & ICT.\n• Weak Area: Need deeper practice on Integration Substitution & Chemistry Organic Reactions.\n• Recommendation: Increase CQ written practice by 20 minutes daily for Higher Math.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Subject Mastery Gauges
            item {
                Text("Subject Mastery Gauges", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                subjects.forEach { sub ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(sub.name, fontSize = 12.sp, color = TextPrimary)
                            Text("${sub.completionPercent.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { sub.completionPercent / 100f },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                            color = CyanPrimary,
                            trackColor = DarkSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
