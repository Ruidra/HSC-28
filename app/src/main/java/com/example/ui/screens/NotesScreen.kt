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
import com.example.data.model.AINote
import com.example.ui.components.AcademicContentViewer
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: HscViewModel) {
    val notes by viewModel.allNotes.collectAsState()
    val isGeneratingNote by viewModel.isGeneratingNote.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<AINote?>(null) }

    var subjectInput by remember { mutableStateOf("Physics 1st Paper") }
    var chapterInput by remember { mutableStateOf("Vectors") }
    var topicInput by remember { mutableStateOf("Dot & Cross Product Applications") }
    var rawNotesInput by remember { mutableStateOf("Dot product scalar A.B = |A||B|cos(theta). Work W = F.s. Cross product vector A x B = |A||B|sin(theta) n_hat. Torque tau = r x F.") }

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
                Text("AI Note Maker", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                Text("Auto-structured notes, formulas & revision cards", fontSize = 12.sp, color = CyanPrimary)
            }

            Button(
                onClick = { showCreateDialog = true },
                modifier = Modifier.testTag("create_note_button"),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Generate AI Note", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (notes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.EditNote, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No AI notes created yet. Click 'Generate AI Note' above!", fontSize = 13.sp, color = TextMuted)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notes) { note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                            .clickable { selectedNote = note },
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
                                    color = PurpleAccent.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${note.subject} • ${note.chapter}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PurpleAccent,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                Text("Tap to view full note ↗", fontSize = 10.sp, color = CyanPrimary)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = note.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            AcademicContentViewer(content = note.contentMarkdown)

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.openMentorWithContext(note.subject, "1st Paper", note.chapter, note.title, "Teach me concepts from my note: ${note.title}", "TEACH", "NoteDetail")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Ask AI Mentor 🧠", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        selectedNote?.let { n ->
            AlertDialog(
                onDismissRequest = { selectedNote = null },
                containerColor = DarkSurface,
                title = { Text(n.title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp) },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("${n.subject} • ${n.chapter}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CyanPrimary)
                        Divider(color = DarkSurfaceVariant)
                        Text(n.contentMarkdown, fontSize = 13.sp, color = TextPrimary, lineHeight = 20.sp)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { selectedNote = null },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black)
                    ) {
                        Text("Close", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                containerColor = DarkSurface,
                title = { Text("Generate AI Note", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = subjectInput,
                            onValueChange = { subjectInput = it },
                            label = { Text("Subject") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = chapterInput,
                            onValueChange = { chapterInput = it },
                            label = { Text("Chapter") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = topicInput,
                            onValueChange = { topicInput = it },
                            label = { Text("Topic Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = rawNotesInput,
                            onValueChange = { rawNotesInput = it },
                            label = { Text("Class Notes / Raw Text / Key Concepts") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 4
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.createNoteWithAi(subjectInput, chapterInput, topicInput, rawNotesInput)
                            showCreateDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black)
                    ) {
                        if (isGeneratingNote) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.Black)
                        } else {
                            Text("Generate & Save Note", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        }
    }
}
