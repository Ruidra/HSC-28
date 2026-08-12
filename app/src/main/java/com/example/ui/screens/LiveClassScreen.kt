package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LiveClass
import com.example.data.model.RecordedClass
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveClassScreen(viewModel: HscViewModel) {
    val liveClasses by viewModel.liveClasses.collectAsState()
    val liveChatMessages by viewModel.liveChatMessages.collectAsState()
    val recordedClasses by viewModel.recordedClasses.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Live Schedule, 1: Live Classroom, 2: Recorded Library
    var activeClassroom by remember { mutableStateOf<LiveClass?>(liveClasses.find { it.status == "LIVE NOW" }) }
    var chatInputText by remember { mutableStateOf("") }
    var isHandRaised by remember { mutableStateOf(false) }

    // Playback state for recorded classes
    var selectedRecordedLesson by remember { mutableStateOf<RecordedClass?>(null) }
    var playbackSpeed by remember { mutableStateOf("1.0x") }
    var isPlaying by remember { mutableStateOf(false) }

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
                Text("HSC Live Classes & Lessons", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                Text("Interactive live classroom & recorded lecture library", fontSize = 11.sp, color = CyanPrimary)
            }

            Surface(
                color = RedPrimary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, RedPrimary)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(RedPrimary))
                    Text("1 LIVE NOW", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
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
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("🔴 LIVE CLASSES", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("📺 CLASSROOM", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("📹 RECORDED (${recordedClasses.size})", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> {
                // LIVE & UPCOMING CLASSES
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(liveClasses) { liveItem ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = if (liveItem.status == "LIVE NOW") RedPrimary else GlassBorder,
                                    shape = RoundedCornerShape(16.dp)
                                ),
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
                                        color = if (liveItem.status == "LIVE NOW") RedPrimary else DarkSurfaceVariant,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            liveItem.status,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    Text("${liveItem.participantCount} Students Joined", fontSize = 11.sp, color = TextSecondary)
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(liveItem.topic, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                Text("${liveItem.subject} • Teacher: ${liveItem.teacherName}", fontSize = 12.sp, color = CyanPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(liveItem.description, fontSize = 11.sp, color = TextSecondary)

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Time: ${liveItem.dateTimeStr} @ ${liveItem.startTimeStr}", fontSize = 11.sp, color = TextMuted)

                                    Button(
                                        onClick = {
                                            activeClassroom = liveItem
                                            selectedTab = 1
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (liveItem.status == "LIVE NOW") RedPrimary else BentoLavenderPrimary,
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(if (liveItem.status == "LIVE NOW") "Enter Classroom 🔴" else "View Class Details", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // LIVE CLASSROOM VIEW
                activeClassroom?.let { cls ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Video Player Area with Clean Unconfigured Notice
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = DarkCardSurface),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Icon(Icons.Default.VideocamOff, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                                    Text("Live streaming is not configured.", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                                    Text("When live streaming provider is attached, video broadcast will display here.", fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }

                        // Class Details & Real-Time Actions
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(cls.topic, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                                        Text("Teacher: ${cls.teacherName} • ${cls.participantCount} active students", fontSize = 11.sp, color = CyanPrimary)
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        IconButton(
                                            onClick = { isHandRaised = !isHandRaised },
                                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(if (isHandRaised) RedPrimary else DarkSurfaceVariant)
                                        ) {
                                            Text("✋", fontSize = 16.sp)
                                        }

                                        Button(
                                            onClick = {
                                                viewModel.openMentorWithContext(cls.subject, "1st Paper", "Chapter", cls.topic, "Explain ${cls.topic} during live class.", "TEACH", "LiveClass")
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text("Ask AI Teacher 🧠", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        // REAL-TIME CLASS CHAT
                        Card(
                            modifier = Modifier.fillMaxWidth().height(260.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("💬 Real-Time Live Class Q&A Chat", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CyanPrimary)
                                Spacer(modifier = Modifier.height(6.dp))

                                LazyColumn(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    items(liveChatMessages) { msg ->
                                        Surface(
                                            color = if (msg.isTeacher) BentoDeepPurple else DarkSurfaceVariant,
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(8.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                        Text(msg.senderName, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (msg.isTeacher) BentoLavenderPrimary else CyanPrimary)
                                                        Text(msg.timestampStr, fontSize = 9.sp, color = TextMuted)
                                                    }
                                                    Text(msg.message, fontSize = 12.sp, color = TextPrimary)
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = chatInputText,
                                        onValueChange = { chatInputText = it },
                                        placeholder = { Text("Ask teacher or class...", fontSize = 11.sp, color = TextMuted) },
                                        modifier = Modifier.weight(1f).height(48.dp),
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    Button(
                                        onClick = {
                                            if (chatInputText.isNotBlank()) {
                                                viewModel.sendLiveChatMessage(chatInputText)
                                                chatInputText = ""
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Send", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // RECORDED LESSONS LIBRARY
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(recordedClasses) { rec ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                                .clickable { selectedRecordedLesson = rec },
                            colors = CardDefaults.cardColors(containerColor = DarkSurface),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${rec.subject} • ${rec.chapter}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = CyanPrimary)
                                    Surface(color = DarkSurfaceVariant, shape = RoundedCornerShape(6.dp)) {
                                        Text("${rec.durationMins} mins", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(rec.topic, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                                Text("Teacher: ${rec.teacher}", fontSize = 11.sp, color = TextSecondary)

                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = { rec.progressPercent / 100f },
                                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                                    color = EmeraldSecondary,
                                    trackColor = DarkSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(if (rec.isCompleted) "Completed ✓" else "Progress: ${rec.progressPercent.toInt()}%", fontSize = 10.sp, color = TextMuted)
                                    Button(
                                        onClick = { selectedRecordedLesson = rec },
                                        colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant, contentColor = CyanPrimary),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Play Lesson ▶", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // RECORDED LESSON PLAYER MODAL
        selectedRecordedLesson?.let { rec ->
            AlertDialog(
                onDismissRequest = { selectedRecordedLesson = null },
                containerColor = DarkSurface,
                title = { Text(rec.topic, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("${rec.subject} • ${rec.teacher}", fontSize = 12.sp, color = CyanPrimary)

                        Card(
                            modifier = Modifier.fillMaxWidth().height(160.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkCardSurface)
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                IconButton(onClick = { isPlaying = !isPlaying }) {
                                    Icon(
                                        if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                        contentDescription = "Play/Pause",
                                        tint = BentoLavenderPrimary,
                                        modifier = Modifier.size(54.dp)
                                    )
                                }
                            }
                        }

                        Text("Playback Speed:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("0.75x", "1.0x", "1.25x", "1.5x", "2.0x").forEach { spd ->
                                FilterChip(
                                    selected = playbackSpeed == spd,
                                    onClick = { playbackSpeed = spd },
                                    label = { Text(spd, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = CyanPrimary,
                                        selectedLabelColor = Color.Black
                                    )
                                )
                            }
                        }

                        Button(
                            onClick = {
                                selectedRecordedLesson = null
                                viewModel.openMentorWithContext(rec.subject, "1st Paper", rec.chapter, rec.topic, "Explain concepts from recorded lesson: ${rec.topic}", "TEACH", "RecordedLesson")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Ask AI Mentor About Lesson 🧠", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedRecordedLesson = null }) {
                        Text("Close", color = CyanPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}
