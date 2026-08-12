package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyTimerScreen(viewModel: HscViewModel) {
    val timerState by viewModel.timerState.collectAsState()
    val subjects by viewModel.allSubjects.collectAsState()

    var selectedSubject by remember { mutableStateOf("Physics 1st Paper") }
    var topicName by remember { mutableStateOf("Dynamics & Motion") }
    var selectedMinutes by remember { mutableIntStateOf(25) }
    var sessionGoal by remember { mutableStateOf("Master Projectile Equations & 3 Numerical Problems") }

    // Reflection Dialog state
    var focusRating by remember { mutableIntStateOf(4) }
    var confidence by remember { mutableIntStateOf(4) }
    var notesSummary by remember { mutableStateOf("") }
    var reflectionText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Header
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Distraction-Free Focus Mode", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
            Text("Pomodoro & Deep Work Session", fontSize = 12.sp, color = CyanPrimary)
        }

        if (!timerState.isRunning && !timerState.showReflectionDialog) {
            // Configurator Before Start
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
                    Text("Configure Study Session", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)

                    // Subject Dropdown / Selection
                    Text("Subject", fontSize = 12.sp, color = TextSecondary)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedSubject,
                            onValueChange = { selectedSubject = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanPrimary, unfocusedBorderColor = DarkSurfaceVariant)
                        )
                    }

                    OutlinedTextField(
                        value = topicName,
                        onValueChange = { topicName = it },
                        label = { Text("Topic Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanPrimary, unfocusedBorderColor = DarkSurfaceVariant)
                    )

                    OutlinedTextField(
                        value = sessionGoal,
                        onValueChange = { sessionGoal = it },
                        label = { Text("Session Goal") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanPrimary, unfocusedBorderColor = DarkSurfaceVariant)
                    )

                    Text("Duration: $selectedMinutes Minutes", fontWeight = FontWeight.Bold, color = CyanPrimary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(15, 25, 45, 60).forEach { mins ->
                            FilterChip(
                                selected = selectedMinutes == mins,
                                onClick = { selectedMinutes = mins },
                                label = { Text("${mins}m", fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyanPrimary, selectedLabelColor = Color.Black)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            viewModel.startTimer(selectedSubject, topicName, selectedMinutes, sessionGoal)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("start_timer_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Start Deep Study Session", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else if (timerState.isRunning) {
            // Active Timer Circular Display
            val mins = timerState.remainingSeconds / 60
            val secs = timerState.remainingSeconds % 60
            val progress = timerState.remainingSeconds.toFloat() / timerState.totalSeconds.toFloat()

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(240.dp)
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = CyanPrimary,
                    trackColor = DarkSurfaceVariant,
                    strokeWidth = 12.dp
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = String.format(Locale.getDefault(), "%02d:%02d", mins, secs),
                        fontWeight = FontWeight.Bold,
                        fontSize = 42.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = timerState.subject,
                        fontSize = 13.sp,
                        color = CyanPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = timerState.topic,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            // Controls (Pause, Resume, Complete)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (timerState.isPaused) {
                    Button(
                        onClick = { viewModel.resumeTimer() },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary, contentColor = Color.Black),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Resume")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Resume", fontWeight = FontWeight.Bold)
                    }
                } else {
                    OutlinedButton(
                        onClick = { viewModel.pauseTimer() },
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(Icons.Default.Pause, contentDescription = "Pause")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pause")
                    }
                }

                Button(
                    onClick = { viewModel.stopTimer() },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseError, contentColor = Color.White),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(Icons.Default.Stop, contentDescription = "Stop")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Complete", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Post-Session Reflection Dialog
        if (timerState.showReflectionDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissReflectionDialog() },
                containerColor = DarkSurface,
                title = { Text("Session Complete! 🎉", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Reflect on your study session:", fontSize = 12.sp, color = TextSecondary)

                        Text("Focus Rating (1-5): $focusRating / 5", fontSize = 12.sp, color = CyanPrimary)
                        Slider(
                            value = focusRating.toFloat(),
                            onValueChange = { focusRating = it.toInt() },
                            valueRange = 1f..5f,
                            steps = 3,
                            colors = SliderDefaults.colors(thumbColor = CyanPrimary)
                        )

                        Text("Confidence Level: $confidence / 5", fontSize = 12.sp, color = EmeraldSecondary)
                        Slider(
                            value = confidence.toFloat(),
                            onValueChange = { confidence = it.toInt() },
                            valueRange = 1f..5f,
                            steps = 3,
                            colors = SliderDefaults.colors(thumbColor = EmeraldSecondary)
                        )

                        OutlinedTextField(
                            value = notesSummary,
                            onValueChange = { notesSummary = it },
                            label = { Text("Key formulas or points studied") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = reflectionText,
                            onValueChange = { reflectionText = it },
                            label = { Text("What was difficult? Need revision?") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.completeSessionWithReflection(focusRating, confidence, notesSummary, reflectionText)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary, contentColor = Color.Black)
                    ) {
                        Text("Save & Log XP (+50 XP)", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
