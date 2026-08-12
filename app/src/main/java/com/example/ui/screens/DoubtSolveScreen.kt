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
import com.example.ui.components.AcademicContentViewer
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoubtSolveScreen(viewModel: HscViewModel) {
    val doubts by viewModel.doubts.collectAsState()
    val subjects by viewModel.allSubjects.collectAsState()

    var showSubmitDialog by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf("Physics 1st Paper") }
    var topicText by remember { mutableStateOf("") }
    var questionInputText by remember { mutableStateOf("") }

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
                Text("HSC Doubt Solving Pipeline", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                Text("Instant AI step-by-step breakdown & teacher review pipeline", fontSize = 11.sp, color = CyanPrimary)
            }

            Button(
                onClick = { showSubmitDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Ask Doubt", modifier = Modifier.size(18.dp))
                    Text("Ask Doubt", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Doubts List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(doubts) { doubt ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = when (doubt.status) {
                                    "AI ANSWERED" -> EmeraldSecondary
                                    "TEACHER REVIEW" -> AmberWarning
                                    "AI ANALYZING" -> CyanPrimary
                                    else -> DarkSurfaceVariant
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    doubt.status,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Text("Submitted: ${doubt.submittedTimeStr}", fontSize = 10.sp, color = TextMuted)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${doubt.subject} • ${doubt.topic}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CyanPrimary)
                        Text(doubt.questionText, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)

                        if (doubt.aiStepExplanation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                color = DarkCardSurface,
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BentoLavenderPrimary.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("🧠 AI STEP-BY-STEP EXPLANATION", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = BentoLavenderPrimary)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    AcademicContentViewer(content = doubt.aiStepExplanation)
                                }
                            }
                        }

                        if (doubt.teacherReviewNote.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(color = DarkSurfaceVariant, shape = RoundedCornerShape(8.dp)) {
                                Text("👩‍🏫 ${doubt.teacherReviewNote}", fontSize = 11.sp, color = AmberWarning, modifier = Modifier.padding(8.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { viewModel.requestTeacherReview(doubt.id) }
                            ) {
                                Text("Request Teacher Review 👩‍🏫", fontSize = 11.sp, color = TextSecondary)
                            }

                            Button(
                                onClick = {
                                    viewModel.openMentorWithContext(doubt.subject, "1st Paper", "Doubt", doubt.topic, "Explain my doubt further: ${doubt.questionText}", "SOLVE", "DoubtSolve")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Open in AI Mentor 🧠", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // SUBMIT DOUBT DIALOG
        if (showSubmitDialog) {
            AlertDialog(
                onDismissRequest = { showSubmitDialog = false },
                containerColor = DarkSurface,
                title = { Text("Ask an Academic Doubt", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Subject:", fontSize = 11.sp, color = TextMuted)
                        OutlinedTextField(
                            value = selectedSubject,
                            onValueChange = { selectedSubject = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Text("Chapter/Topic:", fontSize = 11.sp, color = TextMuted)
                        OutlinedTextField(
                            value = topicText,
                            onValueChange = { topicText = it },
                            placeholder = { Text("e.g. Vectors & Dot Product", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Text("Your Question / Problem:", fontSize = 11.sp, color = TextMuted)
                        OutlinedTextField(
                            value = questionInputText,
                            onValueChange = { questionInputText = it },
                            placeholder = { Text("Type full academic question here...", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (questionInputText.isNotBlank()) {
                                viewModel.submitDoubt(selectedSubject, if (topicText.isBlank()) "General" else topicText, questionInputText)
                                questionInputText = ""
                                topicText = ""
                                showSubmitDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple)
                    ) {
                        Text("Submit Doubt", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSubmitDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }
    }
}
