package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.data.model.MCQQuestion
import com.example.data.model.MistakeRecord
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.example.data.model.EducationBoards

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun McqScreen(viewModel: HscViewModel) {
    val questions by viewModel.currentMcqQuestions.collectAsState()
    val isGenerating by viewModel.isGeneratingMcq.collectAsState()

    var subject by remember { mutableStateOf("Physics 1st Paper") }
    var chapter by remember { mutableStateOf("Dynamics & Motion") }
    var topic by remember { mutableStateOf("Projectile Motion") }
    var difficulty by remember { mutableStateOf("Medium") }
    var targetBoard by remember { mutableStateOf("Dhaka") }

    val selectedOptionIndices = remember { mutableStateMapOf<Int, Int>() }
    var quizSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("AI MCQ Practice", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                Text("Board Standard Multiple Choice Questions", fontSize = 12.sp, color = CyanPrimary)
            }

            Button(
                onClick = {
                    selectedOptionIndices.clear()
                    quizSubmitted = false
                    viewModel.generateMcqs(subject, chapter, topic, difficulty, 5)
                },
                modifier = Modifier.testTag("generate_mcq_button"),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.Black)
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New AI Set", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (questions.isEmpty() && !isGenerating) {
            // Setup Card
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
                    Text("Select Quiz Topic & Difficulty", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)

                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Subject") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Topic Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Target Education Board (All 11 Boards)", fontSize = 12.sp, color = TextSecondary)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(EducationBoards.ALL_BOARDS) { b ->
                            FilterChip(
                                selected = targetBoard == b,
                                onClick = { targetBoard = b },
                                label = { Text(EducationBoards.getBoardDisplayName(b), fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyanPrimary, selectedLabelColor = Color.Black)
                            )
                        }
                    }

                    Text("Difficulty", fontSize = 12.sp, color = TextSecondary)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Easy", "Medium", "Hard").forEach { d ->
                            FilterChip(
                                selected = difficulty == d,
                                onClick = { difficulty = d },
                                label = { Text(d, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyanPrimary, selectedLabelColor = Color.Black)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.generateMcqs(subject, chapter, "$topic (${targetBoard} Board Standard)", difficulty, 5)
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary, contentColor = Color.Black)
                    ) {
                        Text("Generate 5 MCQs Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else if (isGenerating) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = CyanPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("AI is crafting board-aligned MCQs & explanations...", color = TextSecondary, fontSize = 13.sp)
                }
            }
        } else {
            // Interactive Questions List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(questions) { index, q ->
                    val options = listOf(q.optionA, q.optionB, q.optionC, q.optionD)
                    val selected = selectedOptionIndices[index]

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Q${index + 1}. ${q.questionText}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextPrimary,
                                lineHeight = 21.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            options.forEachIndexed { optIdx, optionText ->
                                val isCorrect = optIdx == q.correctOptionIndex
                                val isSelected = selected == optIdx

                                val cardBg = when {
                                    quizSubmitted && isCorrect -> EmeraldSecondary.copy(alpha = 0.25f)
                                    quizSubmitted && isSelected && !isCorrect -> RoseError.copy(alpha = 0.25f)
                                    isSelected -> CyanPrimary.copy(alpha = 0.2f)
                                    else -> DarkCardSurface
                                }

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable(enabled = !quizSubmitted) {
                                            selectedOptionIndices[index] = optIdx
                                        },
                                    color = cardBg,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) CyanPrimary else GlassBorder
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${'A' + optIdx}. $optionText",
                                            fontSize = 13.sp,
                                            color = TextPrimary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }

                            if (quizSubmitted) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    color = DarkCardSurface,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "Explanation:",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = CyanPrimary
                                        )
                                        Text(
                                            text = q.explanation,
                                            fontSize = 12.sp,
                                            color = TextSecondary,
                                            lineHeight = 17.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    if (!quizSubmitted) {
                        Button(
                            onClick = {
                                quizSubmitted = true
                                var score = 0
                                questions.forEachIndexed { idx, q ->
                                    val sel = selectedOptionIndices[idx]
                                    if (sel == q.correctOptionIndex) {
                                        score++
                                    } else if (sel != null) {
                                        // Log mistake automatically
                                        viewModel.addMistake(
                                            MistakeRecord(
                                                subject = q.subject,
                                                chapter = q.chapter,
                                                topic = q.topic,
                                                questionText = q.questionText,
                                                studentAnswer = listOf(q.optionA, q.optionB, q.optionC, q.optionD).getOrElse(sel) { "" },
                                                correctAnswer = listOf(q.optionA, q.optionB, q.optionC, q.optionD).getOrElse(q.correctOptionIndex) { "" },
                                                explanation = q.explanation,
                                                mistakeType = "Concept"
                                            )
                                        )
                                    }
                                }
                                viewModel.submitQuizScore(subject, chapter, score, questions.size, 120, topic)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("submit_quiz_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary, contentColor = Color.Black)
                        ) {
                            Text("Submit Quiz & Evaluate", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
