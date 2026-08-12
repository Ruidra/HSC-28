package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MistakeBookScreen(viewModel: HscViewModel) {
    val mistakes by viewModel.allMistakes.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }

    val filteredMistakes = remember(mistakes, selectedFilter) {
        if (selectedFilter == "All") mistakes
        else mistakes.filter { it.mistakeType == selectedFilter || it.revisionStatus == selectedFilter }
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
                Text("Smart Mistake Book", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                Text("Turn errors into A+ strengths", fontSize = 12.sp, color = AmberWarning)
            }

            Button(
                onClick = {
                    viewModel.generateMcqs("Physics 1st Paper", "Dynamics", "Mistake Revision", "Medium", 5)
                    viewModel.navigateTo("mcq")
                },
                modifier = Modifier.testTag("quiz_from_mistakes_button"),
                colors = ButtonDefaults.buttonColors(containerColor = AmberWarning, contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Autorenew, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("QUIZ ME FROM MY MISTAKES", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filter Chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "Pending", "Concept", "Careless").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = AmberWarning, selectedLabelColor = Color.Black)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredMistakes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No mistake records logged for this filter! Excellent job.", color = TextMuted, fontSize = 13.sp)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredMistakes) { m ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = RoseError.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${m.subject} • ${m.mistakeType} Error",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoseError,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        val newStatus = if (m.revisionStatus == "Pending") "Mastered" else "Pending"
                                        viewModel.markMistakeStatus(m.id, newStatus)
                                    }
                                ) {
                                    Text(
                                        text = if (m.revisionStatus == "Mastered") "Mastered ✓" else "Mark Mastered",
                                        color = if (m.revisionStatus == "Mastered") EmeraldSecondary else CyanPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Q: ${m.questionText}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("Your Answer: ${m.studentAnswer}", fontSize = 12.sp, color = RoseError)
                            Text("Correct Answer: ${m.correctAnswer}", fontSize = 12.sp, color = EmeraldSecondary, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Explanation: ${m.explanation}", fontSize = 11.sp, color = TextSecondary, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
