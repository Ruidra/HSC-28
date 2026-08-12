package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.SourceMaterial
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialsScreen(viewModel: HscViewModel) {
    val materials by viewModel.sourceMaterials.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedMaterial by remember { mutableStateOf<SourceMaterial?>(null) }

    var titleInput by remember { mutableStateOf("") }
    var subjectInput by remember { mutableStateOf("Physics 1st Paper") }
    var contentInput by remember { mutableStateOf("") }

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
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BentoDeepPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FolderSpecial, contentDescription = null, tint = BentoLavenderPrimary, modifier = Modifier.size(24.dp))
                }
                Column {
                    Text("My Study Materials", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                    Text("Class notes, guides & imported text sources", fontSize = 11.sp, color = BentoLavenderPrimary)
                }
            }

            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = BentoLavenderPrimary,
                contentColor = BentoDeepPurple,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("add_material_fab"),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Material")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (materials.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📂", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No study materials imported yet", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                    Text("Import lecture notes or text guides to generate AI notes & quizzes", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { showAddDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple)
                    ) {
                        Text("Import First Document", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(materials) { mat ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BentoBorder, RoundedCornerShape(20.dp))
                            .clickable { selectedMaterial = mat },
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("📄", fontSize = 18.sp)
                                    Column {
                                        Text(mat.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                        Text(mat.subject, fontSize = 11.sp, color = BentoLavenderPrimary)
                                    }
                                }

                                IconButton(onClick = { viewModel.deleteSourceMaterial(mat.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = BentoFlameCoral)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = mat.contentText,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                maxLines = 3,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.createNoteWithAi(mat.subject, "Imported Note", mat.title, mat.contentText)
                                        viewModel.navigateTo("notes")
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = BentoDeepPurple, contentColor = BentoLavenderPrimary),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Generate AI Note", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        viewModel.generateMcqs(mat.subject, mat.title, mat.contentText, "Medium", 5)
                                        viewModel.navigateTo("mcq")
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = BentoMediumPurple, contentColor = Color.White),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Generate AI Quiz", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Material Detail Modal
        selectedMaterial?.let { mat ->
            AlertDialog(
                onDismissRequest = { selectedMaterial = null },
                containerColor = DarkSurface,
                title = { Text(mat.title, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 18.sp) },
                text = {
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Subject: ${mat.subject}", fontSize = 12.sp, color = BentoLavenderPrimary, fontWeight = FontWeight.Bold)
                        Divider(color = BentoBorder)
                        Text(mat.contentText, fontSize = 13.sp, color = TextPrimary, lineHeight = 20.sp)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { selectedMaterial = null },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple)
                    ) {
                        Text("Close", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Add Material Dialog
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                containerColor = DarkSurface,
                title = { Text("Import Material / Class Note", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = titleInput,
                            onValueChange = { titleInput = it },
                            label = { Text("Document Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = subjectInput,
                            onValueChange = { subjectInput = it },
                            label = { Text("Subject") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = contentInput,
                            onValueChange = { contentInput = it },
                            label = { Text("Paste Text / Lecture Notes") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (titleInput.isNotBlank() && contentInput.isNotBlank()) {
                                viewModel.addSourceMaterial(titleInput, subjectInput, contentInput)
                                titleInput = ""
                                contentInput = ""
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple)
                    ) {
                        Text("Import Material", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }
    }
}
