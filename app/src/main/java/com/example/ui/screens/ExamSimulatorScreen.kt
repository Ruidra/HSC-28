package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamAttempt
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.example.data.model.EducationBoards

@Composable
fun ExamSimulatorScreen(viewModel: HscViewModel) {
    val examAttempts by viewModel.allExamAttempts.collectAsState()

    var isExamActive by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf("Physics 1st Paper") }
    var selectedBoard by remember { mutableStateOf("Dhaka") }

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
                Text("HSC 2028 Model Test Simulator", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                Text("Full exam environment with timed MCQ & CQ sections", fontSize = 12.sp, color = CyanPrimary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!isExamActive) {
            // Start Exam Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Configure Model Exam", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)

                    OutlinedTextField(
                        value = selectedSubject,
                        onValueChange = { selectedSubject = it },
                        label = { Text("Exam Subject") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Target Board Standard (All 11 Boards)", fontSize = 12.sp, color = TextSecondary)
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(EducationBoards.ALL_BOARDS) { b ->
                            FilterChip(
                                selected = selectedBoard == b,
                                onClick = { selectedBoard = b },
                                label = { Text(EducationBoards.getBoardDisplayName(b), fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyanPrimary, selectedLabelColor = Color.Black)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("MCQ Section", fontSize = 12.sp, color = TextMuted)
                            Text("25 Marks (25 Mins)", fontWeight = FontWeight.Bold, color = CyanPrimary)
                        }
                        Column {
                            Text("CQ Section", fontSize = 12.sp, color = TextMuted)
                            Text("50 Marks (100 Mins)", fontWeight = FontWeight.Bold, color = EmeraldSecondary)
                        }
                    }

                    Button(
                        onClick = { isExamActive = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("start_model_exam_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black)
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start Timed Model Exam", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Previous Exam History", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))

            if (examAttempts.isEmpty()) {
                Text("No previous exam attempts logged yet.", fontSize = 13.sp, color = TextMuted)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(examAttempts) { attempt ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(attempt.examTitle, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("Score: ${attempt.userScore}/${attempt.totalScore} | Accuracy: ${attempt.accuracyPercent.toInt()}%", fontSize = 12.sp, color = CyanPrimary)
                            }
                        }
                    }
                }
            }
        } else {
            // Active Exam Runner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, RoseError, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Model Test in Progress", fontWeight = FontWeight.Bold, color = RoseError, fontSize = 16.sp)
                        Text("Remaining: 124:30", fontWeight = FontWeight.Bold, color = CyanPrimary, fontSize = 16.sp)
                    }

                    Text("Subject: $selectedSubject", fontSize = 13.sp, color = TextSecondary)

                    Surface(
                        color = DarkCardSurface,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Q1 (MCQ). What is the dimension of universal gravitational constant G?", fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("A. [M^-1 L^3 T^-2]", color = CyanPrimary)
                            Text("B. [M L T^-2]", color = TextSecondary)
                            Text("C. [M L^2 T^-1]", color = TextSecondary)
                            Text("D. [M^2 L T^-2]", color = TextSecondary)
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.submitExamAttempt("$selectedSubject Model Test", 75, 68)
                            isExamActive = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary, contentColor = Color.Black),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Submit Exam & View AI Analysis", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
