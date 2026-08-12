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

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.example.data.model.EducationBoards

import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.data.remote.FirebaseAuthHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: HscViewModel) {
    val profile by viewModel.studentProfile.collectAsState()
    val materials by viewModel.sourceMaterials.collectAsState()

    var nameInput by remember(profile) { mutableStateOf(profile?.name ?: "HSC Scholar") }
    var targetInput by remember(profile) { mutableStateOf(profile?.targetResult ?: "A+") }
    var selectedBoard by remember(profile) { mutableStateOf(profile?.board ?: "Dhaka") }

    var showMaterialDialog by remember { mutableStateOf(false) }
    var materialTitle by remember { mutableStateOf("") }
    var materialSubject by remember { mutableStateOf("Physics 1st Paper") }
    var materialContent by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isUserSignedIn by remember { mutableStateOf(FirebaseAuthHelper(context).isUserSignedIn()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Text("Settings & Source Materials", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
        Text("Manage profile preferences and upload class notes", fontSize = 12.sp, color = CyanPrimary)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Profile Settings", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)

                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = targetInput,
                            onValueChange = { targetInput = it },
                            label = { Text("Target HSC Grade") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text("Education Board (Select from All 11 Boards)", fontSize = 12.sp, color = TextSecondary)
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

                        Button(
                            onClick = {
                                profile?.let {
                                    viewModel.updateStudentProfile(it.copy(name = nameInput, targetResult = targetInput, board = selectedBoard))
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black),
                            modifier = Modifier.fillMaxWidth().height(44.dp)
                        ) {
                            Text("Update Profile", fontWeight = FontWeight.Bold)
                        }

                        if (!isUserSignedIn) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val success = FirebaseAuthHelper(context).signInWithGoogle()
                                        if (success) {
                                            isUserSignedIn = true
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                                modifier = Modifier.fillMaxWidth().height(44.dp)
                            ) {
                                Icon(Icons.Default.AccountCircle, contentDescription = "Google Sign In")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Sign In With Google")
                            }
                        } else {
                            Text("Signed In Securely", color = EmeraldSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Source Material Importer
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Source Materials & Notes", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                Text("Class notes & materials for AI processing", fontSize = 11.sp, color = TextSecondary)
                            }

                            Button(
                                onClick = { showMaterialDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary, contentColor = Color.Black),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Import Note", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (materials.isEmpty()) {
                            Text("No external source materials added yet.", fontSize = 12.sp, color = TextMuted)
                        } else {
                            materials.forEach { mat ->
                                Surface(
                                    color = DarkCardSurface,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(mat.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                            Text(mat.subject, fontSize = 11.sp, color = CyanPrimary)
                                        }

                                        IconButton(onClick = { viewModel.deleteSourceMaterial(mat.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RoseError)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // AI Preferences & Data Management
            item {
                var showResetConfirm by remember { mutableStateOf(false) }
                var notificationsEnabled by remember { mutableStateOf(true) }
                var offlineCacheEnabled by remember { mutableStateOf(true) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("AI & Data Preferences", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Smart Study Reminders", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = TextPrimary)
                                Text("Receive gentle notifications before memory decay", fontSize = 11.sp, color = TextSecondary)
                            }
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { notificationsEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = CyanPrimary, checkedTrackColor = PurpleAccent)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Local AI Model Caching", fontWeight = FontWeight.Medium, fontSize = 13.sp, color = TextPrimary)
                                Text("Store offline questions and notes locally in Room DB", fontSize = 11.sp, color = TextSecondary)
                            }
                            Switch(
                                checked = offlineCacheEnabled,
                                onCheckedChange = { offlineCacheEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = EmeraldSecondary, checkedTrackColor = DarkSurfaceVariant)
                            )
                        }

                        Divider(color = DarkSurfaceVariant)

                        OutlinedButton(
                            onClick = { showResetConfirm = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = RoseError),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RoseError.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Reset Study Session Cache")
                        }

                        if (showResetConfirm) {
                            AlertDialog(
                                onDismissRequest = { showResetConfirm = false },
                                containerColor = DarkSurface,
                                title = { Text("Confirm Cache Reset", fontWeight = FontWeight.Bold, color = TextPrimary) },
                                text = { Text("Are you sure you want to clear temporary local study caches? Your saved notes, mistakes, and profile will remain intact.", fontSize = 13.sp, color = TextSecondary) },
                                confirmButton = {
                                    Button(
                                        onClick = { showResetConfirm = false },
                                        colors = ButtonDefaults.buttonColors(containerColor = RoseError, contentColor = Color.White)
                                    ) {
                                        Text("Confirm Reset", fontWeight = FontWeight.Bold)
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showResetConfirm = false }) {
                                        Text("Cancel", color = TextMuted)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showMaterialDialog) {
            AlertDialog(
                onDismissRequest = { showMaterialDialog = false },
                containerColor = DarkSurface,
                title = { Text("Import Class Notes / Source Text", fontWeight = FontWeight.Bold, color = TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = materialTitle,
                            onValueChange = { materialTitle = it },
                            label = { Text("Material Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = materialSubject,
                            onValueChange = { materialSubject = it },
                            label = { Text("Subject") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = materialContent,
                            onValueChange = { materialContent = it },
                            label = { Text("Paste Notes / Text Content") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 5
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (materialTitle.isNotBlank() && materialContent.isNotBlank()) {
                                viewModel.addSourceMaterial(materialTitle, materialSubject, materialContent)
                                showMaterialDialog = false
                                materialTitle = ""
                                materialContent = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary, contentColor = Color.Black)
                    ) {
                        Text("Save Material", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}
