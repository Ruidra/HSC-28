package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.ui.components.AcademicContentViewer
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiTeacherScreen(viewModel: HscViewModel) {
    val messages by viewModel.chatMessages.collectAsState()
    val isThinking by viewModel.isAiThinking.collectAsState()
    val contextSubject by viewModel.teacherContextSubject.collectAsState()
    val subjects by viewModel.allSubjects.collectAsState()
    val conversations by viewModel.conversations.collectAsState()
    val currentConvId by viewModel.currentConversationId.collectAsState()
    val activeContext by viewModel.activeAiContext.collectAsState()
    val selectedMode by viewModel.selectedAiMode.collectAsState()
    val isFocusMode by viewModel.isFocusMode.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var showHistoryDrawer by remember { mutableStateOf(false) }
    var showVoiceModal by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // DESKTOP / TABLET CONVERSATION HISTORY SIDEBAR (Hidden if Focus Mode)
        if (!isFocusMode) {
            Surface(
                modifier = Modifier
                    .width(260.dp)
                    .fillMaxHeight()
                    .border(1.dp, BentoBorder),
                color = DarkSurface
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // New Chat Button
                    Button(
                        onClick = { viewModel.createNewConversation() },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Add, contentDescription = "New Chat", modifier = Modifier.size(18.dp))
                            Text("New Study Chat", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("STUDY CHATS HISTORY", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = BentoLavenderPrimary, letterSpacing = 1.sp)

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(conversations) { conv ->
                            val isSelected = conv.id == currentConvId
                            Surface(
                                onClick = { viewModel.selectConversation(conv.id) },
                                color = if (isSelected) BentoDeepPurple else DarkCardSurface,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BentoLavenderPrimary else GlassBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(conv.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary, maxLines = 1)
                                        Text("${conv.subject} • ${conv.lastTimestampStr}", fontSize = 10.sp, color = TextSecondary)
                                    }
                                    IconButton(
                                        onClick = { viewModel.deleteConversation(conv.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }

                    // Context indicator
                    Surface(
                        color = DarkSurfaceVariant,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("ACTIVE CONTEXT", fontWeight = FontWeight.Bold, fontSize = 9.sp, color = CyanPrimary)
                            Text("${activeContext?.subject ?: contextSubject} • ${activeContext?.topic ?: "General Topic"}", fontSize = 11.sp, color = TextPrimary)
                        }
                    }
                }
            }
        }

        // MAIN WORKSPACE CONTENT AREA
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(14.dp)
        ) {
            // Workspace Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(BentoLavenderPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = BentoDeepPurple, modifier = Modifier.size(22.dp))
                    }
                    Column {
                        Text("HSC AI Mentor", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                        Text(
                            text = "Studying: ${activeContext?.subject ?: contextSubject} • ${activeContext?.topic ?: "Dynamics & Vectors"}",
                            fontSize = 11.sp,
                            color = CyanPrimary
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Voice Teacher button
                    IconButton(
                        onClick = { viewModel.navigateTo("voice_teacher") },
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(DarkSurfaceVariant)
                    ) {
                        Icon(Icons.Default.RecordVoiceOver, contentDescription = "Voice Mode", tint = BentoLavenderPrimary, modifier = Modifier.size(20.dp))
                    }

                    // Focus Mode toggle button
                    IconButton(
                        onClick = { viewModel.toggleFocusMode() },
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(if (isFocusMode) BentoDeepPurple else DarkSurfaceVariant)
                    ) {
                        Icon(if (isFocusMode) Icons.Default.FullscreenExit else Icons.Default.Fullscreen, contentDescription = "Focus Mode", tint = BentoLavenderPrimary, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mode Selector Chips
            val modes = listOf(
                "TEACH" to "📖 Teach",
                "PRACTICE" to "✍️ Practice",
                "QUIZ" to "❓ Quiz Me",
                "EXAM" to "🎓 Exam",
                "REVISE" to "⚡ Revise",
                "SOLVE" to "📐 Solve",
                "NOTES" to "📝 Notes"
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(modes) { (mKey, mLabel) ->
                    FilterChip(
                        selected = selectedMode == mKey,
                        onClick = { viewModel.setSelectedAiMode(mKey) },
                        label = { Text(mLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BentoLavenderPrimary,
                            selectedLabelColor = BentoDeepPurple
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subject Filter Bar
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(subjects) { sub ->
                    FilterChip(
                        selected = contextSubject == sub.name,
                        onClick = { viewModel.setTeacherContextSubject(sub.name) },
                        label = { Text(sub.name, fontSize = 10.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkSurfaceVariant,
                            selectedLabelColor = CyanPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // LANDING QUICK ACTION PROMPTS (When message count is 1 / clean state)
            if (messages.size <= 1) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("What do you want to learn today?", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                        Text("Tap a quick action or type your HSC question below:", fontSize = 11.sp, color = TextSecondary)

                        Spacer(modifier = Modifier.height(12.dp))

                        val quickActions = listOf(
                            "Explain a topic" to "Explain ${activeContext?.topic ?: "Dynamics"} step-by-step with formulas.",
                            "Quiz me" to "Give me 3 board questions on ${activeContext?.topic ?: "Physics"}.",
                            "Help with homework" to "How do I solve mathematical CQ problems on ${activeContext?.topic ?: "Vectors"}?",
                            "Make notes" to "Create concise bullet point revision notes for ${activeContext?.topic ?: "Newton's Laws"}.",
                            "Solve a problem" to "Solve a hard numerical problem on ${activeContext?.topic ?: "Physics 1st Paper"}.",
                            "Prepare for exam" to "What are the most frequent CQ/MCQ topics in HSC 2028?",
                            "Revise my mistakes" to "Help me review my weak areas and mistake book entries."
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.heightIn(max = 200.dp)
                        ) {
                            items(quickActions) { (label, promptText) ->
                                Surface(
                                    onClick = { viewModel.sendTeacherMessage(promptText) },
                                    color = DarkCardSurface,
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text("💡", fontSize = 12.sp)
                                        Text(label, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = CyanPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // CHAT MESSAGES FEED
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    val isUser = msg.sender == "USER"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Column(
                            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
                            modifier = Modifier.widthIn(max = 680.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = if (isUser) CyanPrimary.copy(alpha = 0.5f) else BentoLavenderPrimary.copy(alpha = 0.4f),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isUser) DarkSurfaceVariant else DarkCardSurface
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (isUser) "You" else "HSC Mentor AI",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isUser) CyanPrimary else BentoLavenderPrimary
                                        )
                                        Surface(color = DarkSurface, shape = RoundedCornerShape(6.dp)) {
                                            Text(selectedMode, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    if (isUser) {
                                        Text(
                                            text = msg.text,
                                            fontSize = 13.sp,
                                            color = TextPrimary,
                                            lineHeight = 19.sp
                                        )
                                    } else {
                                        // Use AcademicContentViewer for readable formatting
                                        AcademicContentViewer(content = msg.text)
                                    }
                                }
                            }

                            // RESPONSE ACTION PILLS BELOW AI MESSAGES
                            if (!isUser) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.padding(start = 4.dp)
                                ) {
                                    val actionPills = listOf(
                                        "Explain Simpler" to "Can you explain this in even simpler terms with a real-life analogy?",
                                        "Give Example" to "Can you give a numerical HSC board question example?",
                                        "Quiz Me" to "Test me on this with 1 practice question.",
                                        "Make Notes" to "Convert this explanation into key revision notes."
                                    )
                                    actionPills.forEach { (pillLabel, pillPrompt) ->
                                        Surface(
                                            onClick = { viewModel.sendTeacherMessage(pillPrompt) },
                                            color = DarkSurface,
                                            shape = RoundedCornerShape(12.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                                        ) {
                                            Text(pillLabel, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyanPrimary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (isThinking) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Surface(
                                color = DarkCardSurface,
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = BentoLavenderPrimary, strokeWidth = 2.dp)
                                    Text("HSC Mentor AI is formulating a step-by-step response...", fontSize = 12.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // INPUT BAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { /* Handle Image Upload */ }) {
                    Icon(Icons.Default.Image, contentDescription = "Upload Image", tint = TextMuted)
                }
                IconButton(onClick = { /* Handle Video Upload */ }) {
                    Icon(Icons.Default.VideoFile, contentDescription = "Upload Video", tint = TextMuted)
                }
                IconButton(onClick = { /* Handle Audio Upload */ }) {
                    Icon(Icons.Default.Mic, contentDescription = "Record Audio", tint = TextMuted)
                }

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask your HSC Mentor teacher...", fontSize = 13.sp, color = TextMuted) },
                    modifier = Modifier.weight(1f).testTag("ai_teacher_input"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface,
                        focusedBorderColor = BentoLavenderPrimary,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Button(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendTeacherMessage(inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier.height(52.dp).testTag("ai_teacher_send_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
