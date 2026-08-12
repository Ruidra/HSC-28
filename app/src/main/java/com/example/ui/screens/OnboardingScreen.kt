package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.data.model.StudentProfile
import com.example.ui.theme.*

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.example.data.model.EducationBoards

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onComplete: (StudentProfile) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }

    var name by remember { mutableStateOf("") }
    var board by remember { mutableStateOf("Dhaka") }
    var group by remember { mutableStateOf("Science") }
    var targetResult by remember { mutableStateOf("A+") }
    var hours by remember { mutableFloatStateOf(4.5f) }
    var preferredTimes by remember { mutableStateOf("Evening & Night") }
    var holidays by remember { mutableStateOf("Friday") }
    var difficultTopics by remember { mutableStateOf("Physics Vectors, Integration, Organic Chem") }
    var confidentTopics by remember { mutableStateOf("ICT, English Grammar, Dynamics") }
    var language by remember { mutableStateOf("Bangla & English") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(1.dp, GlassBorder, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyanPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.School, contentDescription = null, tint = Color.Black, modifier = Modifier.size(28.dp))
                    }
                    Column {
                        Text("HSC Mentor AI", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextPrimary)
                        Text("Your Personal AI Teacher for HSC 2028", fontSize = 12.sp, color = CyanPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Step indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(3) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (step > index) CyanPrimary else DarkSurfaceVariant)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                when (step) {
                    1 -> {
                        Text("Step 1 of 3: Profile & Goals", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                        Text("Let's build your personalized HSC 2028 study system.", fontSize = 13.sp, color = TextSecondary)

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Your Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CyanPrimary) },
                            modifier = Modifier.fillMaxWidth().testTag("onboarding_name"),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanPrimary, unfocusedBorderColor = DarkSurfaceVariant)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Education Board (All 11 Bangladesh Boards Supported)", fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(EducationBoards.ALL_BOARDS) { b ->
                                FilterChip(
                                    selected = board == b,
                                    onClick = { board = b },
                                    label = { Text(EducationBoards.getBoardDisplayName(b), fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyanPrimary, selectedLabelColor = Color.Black)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Academic Group", fontSize = 12.sp, color = TextSecondary)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Science", "Commerce", "Arts").forEach { g ->
                                FilterChip(
                                    selected = group == g,
                                    onClick = { group = g },
                                    label = { Text(g, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyanPrimary, selectedLabelColor = Color.Black)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Target Result", fontSize = 12.sp, color = TextSecondary)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Golden A+", "A+", "A (4.00)").forEach { t ->
                                FilterChip(
                                    selected = targetResult == t,
                                    onClick = { targetResult = t },
                                    label = { Text(t, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldSecondary, selectedLabelColor = Color.Black)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { step = 2 },
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("onboarding_next_1"),
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black)
                        ) {
                            Text("Next: Schedule & Routine", fontWeight = FontWeight.Bold)
                        }
                    }

                    2 -> {
                        Text("Step 2 of 3: Schedule & Availability", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                        Text("How many hours can you commit to self-study daily?", fontSize = 13.sp, color = TextSecondary)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Available Self-Study Hours: ${String.format("%.1f", hours)} Hours/day", fontWeight = FontWeight.Bold, color = CyanPrimary)
                        Slider(
                            value = hours,
                            onValueChange = { hours = it },
                            valueRange = 1f..10f,
                            steps = 17,
                            colors = SliderDefaults.colors(thumbColor = CyanPrimary, activeTrackColor = CyanPrimary)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = preferredTimes,
                            onValueChange = { preferredTimes = it },
                            label = { Text("Preferred Study Times") },
                            leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null, tint = CyanPrimary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanPrimary, unfocusedBorderColor = DarkSurfaceVariant)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = holidays,
                            onValueChange = { holidays = it },
                            label = { Text("Weekly Holidays / Off Days") },
                            leadingIcon = { Icon(Icons.Default.Event, contentDescription = null, tint = CyanPrimary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanPrimary, unfocusedBorderColor = DarkSurfaceVariant)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = { step = 1 },
                                modifier = Modifier.weight(1f).height(48.dp)
                            ) {
                                Text("Back")
                            }
                            Button(
                                onClick = { step = 3 },
                                modifier = Modifier.weight(1f).height(48.dp).testTag("onboarding_next_2"),
                                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black)
                            ) {
                                Text("Next: Weak Areas", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    3 -> {
                        Text("Step 3 of 3: AI Personalization", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                        Text("Tell the AI mentor what topics need the most attention.", fontSize = 13.sp, color = TextSecondary)

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = difficultTopics,
                            onValueChange = { difficultTopics = it },
                            label = { Text("Topics You Find Difficult / Weak Areas") },
                            leadingIcon = { Icon(Icons.Default.Warning, contentDescription = null, tint = RoseError) },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanPrimary, unfocusedBorderColor = DarkSurfaceVariant)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = confidentTopics,
                            onValueChange = { confidentTopics = it },
                            label = { Text("Topics You Feel Confident About") },
                            leadingIcon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 2,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyanPrimary, unfocusedBorderColor = DarkSurfaceVariant)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Preferred Teaching Language", fontSize = 12.sp, color = TextSecondary)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Bangla & English", "English", "Bangla").forEach { l ->
                                FilterChip(
                                    selected = language == l,
                                    onClick = { language = l },
                                    label = { Text(l, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PurpleAccent, selectedLabelColor = Color.White)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = { step = 2 },
                                modifier = Modifier.weight(1f).height(48.dp)
                            ) {
                                Text("Back")
                            }
                            Button(
                                onClick = {
                                    val profile = StudentProfile(
                                        name = name.ifBlank { "HSC Scholar" },
                                        board = board,
                                        group = group,
                                        targetResult = targetResult,
                                        availableHours = hours,
                                        preferredStudyTimes = preferredTimes,
                                        weeklyHolidays = holidays,
                                        difficultTopicsText = difficultTopics,
                                        confidentTopicsText = confidentTopics,
                                        language = language,
                                        isOnboarded = true
                                    )
                                    onComplete(profile)
                                },
                                modifier = Modifier.weight(1.5f).height(48.dp).testTag("onboarding_finish"),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary, contentColor = Color.Black)
                            ) {
                                Text("Launch HSC Mentor AI", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
