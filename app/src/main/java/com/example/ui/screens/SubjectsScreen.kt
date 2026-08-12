package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.data.local.HscCurriculumData
import com.example.data.model.ChapterEntity
import com.example.data.model.SubjectEntity
import com.example.data.model.TopicEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel
import kotlinx.coroutines.delay

@Composable
fun SubjectsScreen(viewModel: HscViewModel) {
    val subjects by viewModel.allSubjects.collectAsState()
    val studentProfile by viewModel.studentProfile.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // 0: My Books, 1: Subject Overview

    var selectedSubject by remember { mutableStateOf<SubjectEntity?>(null) }
    var studyTopicDialog by remember { mutableStateOf<TopicEntity?>(null) }

    val chapters by produceState<List<ChapterEntity>>(initialValue = emptyList(), key1 = selectedSubject) {
        if (selectedSubject != null) {
            viewModel.getChaptersForSubject(selectedSubject!!.name).collect { value = it }
        } else {
            value = emptyList()
        }
    }

    var isLoadingChapters by remember { mutableStateOf(false) }

    LaunchedEffect(selectedSubject) {
        selectedSubject?.let { sub ->
            isLoadingChapters = true
            viewModel.ensureChaptersForSubject(sub.name)
            delay(300)
            isLoadingChapters = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        // Header & NCTB Metadata
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("HSC 2028 Books & Syllabus", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                Text("${studentProfile?.group ?: "Science"} Group • NCTB Official Curriculum Data", fontSize = 11.sp, color = CyanPrimary)
            }
            Surface(
                color = DarkSurfaceVariant,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("🟢", fontSize = 10.sp)
                    Text("Verified v2028.1", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldSecondary)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // "CONTINUE STUDYING" Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BentoLavenderPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .clickable {
                    // Open Physics 1st Paper Ch 3 topic
                    viewModel.navigateTo("teacher")
                },
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("📖", fontSize = 12.sp)
                        Text("CONTINUE STUDYING", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = BentoLavenderPrimary)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Physics 1st Paper → Ch 3: Dynamics & Motion", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                    Text("Topic: Projectile Motion & Range Equations", fontSize = 11.sp, color = TextSecondary)
                }

                Button(
                    onClick = { viewModel.navigateTo("timer") },
                    colors = ButtonDefaults.buttonColors(containerColor = BentoLavenderPrimary, contentColor = BentoDeepPurple),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Continue", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Selector (MY BOOKS vs SUBJECT OVERVIEW)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkSurface,
            contentColor = CyanPrimary,
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("📚 MY BOOKS (${subjects.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("📊 SUBJECT OVERVIEW", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedTab == 0) {
            // MY BOOKS GRID VIEW
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(subjects) { sub ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                            .clickable { selectedSubject = sub },
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(CyanPrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(sub.code, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Black)
                                    }
                                    Column {
                                        Text(sub.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                        Text("NCTB Board Textbook • Tap to view chapters", fontSize = 11.sp, color = TextSecondary)
                                    }
                                }

                                Surface(
                                    color = DarkSurfaceVariant,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        "${sub.completionPercent.toInt()}% Complete",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = CyanPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LinearProgressIndicator(
                                progress = { (sub.completionPercent / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                                color = CyanPrimary,
                                trackColor = DarkSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("NCTB Curriculum Verified", fontSize = 10.sp, color = TextMuted)
                                TextButton(
                                    onClick = { selectedSubject = sub },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Open Book 📖", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // SUBJECT OVERVIEW LIST
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(subjects) { sub ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, GlassBorder, RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(sub.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                                Text("Confidence Level: ${sub.confidenceLevel}/5 • Needs Attention: ${if (sub.needsAttention) "Yes ⚠️" else "No ✓"}", fontSize = 11.sp, color = TextSecondary)
                            }
                            Button(
                                onClick = { selectedSubject = sub },
                                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant, contentColor = CyanPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Chapters", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // CHAPTER & TOPIC BROWSER DIALOG
        selectedSubject?.let { sub ->
            var expandedChapterId by remember { mutableStateOf<Int?>(null) }
            val topicsForSub by viewModel.getTopicsForSubject(sub.name).collectAsState(initial = emptyList())

            AlertDialog(
                onDismissRequest = { selectedSubject = null },
                containerColor = DarkSurface,
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(sub.name, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextPrimary)
                            Text("NCTB Official Syllabus • ${chapters.size} Chapters", fontSize = 11.sp, color = CyanPrimary)
                        }
                        IconButton(onClick = { selectedSubject = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 450.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (isLoadingChapters) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = CyanPrimary, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Loading NCTB chapters...", fontSize = 12.sp, color = TextSecondary)
                            }
                        } else if (chapters.isEmpty()) {
                            // NO CHAPTERS EMPTY / ERROR STATE WITH RETRY (NEVER INFINITE LOADING)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("⚠️ No local chapters found for ${sub.name}", fontSize = 12.sp, color = AmberWarning, fontWeight = FontWeight.Bold)
                                    Text("Load bundled NCTB syllabus data instantly:", fontSize = 11.sp, color = TextSecondary)

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = {
                                                viewModel.ensureChaptersForSubject(sub.name)
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Seed NCTB Syllabus Data", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        } else {
                            chapters.forEach { ch ->
                                val isExpanded = expandedChapterId == ch.id
                                val chTopics = topicsForSub.filter { it.chapterTitle.contains(ch.title, ignoreCase = true) || it.chapterId == ch.chapterNumber }

                                Surface(
                                    color = DarkCardSurface,
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isExpanded) CyanPrimary else GlassBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().clickable {
                                                expandedChapterId = if (isExpanded) null else ch.id
                                            },
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                    Text("Ch ${ch.chapterNumber}: ${ch.title}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                                    if (ch.isDifficult) {
                                                        Surface(color = RoseError.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                                            Text("Difficult ⚠️", fontSize = 9.sp, color = RoseError, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                                        }
                                                    }
                                                }
                                                Text("${chTopics.size} Topics • ${if (ch.completionPercent >= 100f) "Completed ✓" else "In Progress"}", fontSize = 10.sp, color = TextSecondary)
                                            }

                                            Icon(
                                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                                contentDescription = "Expand",
                                                tint = CyanPrimary
                                            )
                                        }

                                        if (isExpanded) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Divider(color = DarkSurfaceVariant)
                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text("Chapter Topics & Practice:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanPrimary)

                                            if (chTopics.isEmpty()) {
                                                Text("NCTB Core Concept & Board Practice Topics", fontSize = 11.sp, color = TextMuted)
                                            } else {
                                                chTopics.forEach { topic ->
                                                    Surface(
                                                        color = DarkSurface,
                                                        shape = RoundedCornerShape(8.dp),
                                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                                    ) {
                                                        Column(modifier = Modifier.padding(10.dp)) {
                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Text(topic.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary, modifier = Modifier.weight(1f))
                                                                Surface(
                                                                    color = when (topic.status) {
                                                                        "Mastered" -> EmeraldSecondary.copy(alpha = 0.2f)
                                                                        "In Progress" -> CyanPrimary.copy(alpha = 0.2f)
                                                                        else -> AmberWarning.copy(alpha = 0.2f)
                                                                    },
                                                                    shape = RoundedCornerShape(6.dp)
                                                                ) {
                                                                    Text(topic.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                                                }
                                                            }

                                                            Spacer(modifier = Modifier.height(6.dp))

                                                            Row(
                                                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Button(
                                                                    onClick = {
                                                                        studyTopicDialog = topic
                                                                    },
                                                                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black),
                                                                    shape = RoundedCornerShape(6.dp),
                                                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                                    modifier = Modifier.height(26.dp)
                                                                ) {
                                                                    Text("Study 📖", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                                }

                                                                Button(
                                                                    onClick = {
                                                                        selectedSubject = null
                                                                        viewModel.sendTeacherMessage("Explain ${topic.title} in ${sub.name} step-by-step with HSC 2028 board exam questions.")
                                                                        viewModel.navigateTo("teacher")
                                                                    },
                                                                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant, contentColor = CyanPrimary),
                                                                    shape = RoundedCornerShape(6.dp),
                                                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                                    modifier = Modifier.height(26.dp)
                                                                ) {
                                                                    Text("AI Teacher 🧠", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                                }

                                                                Button(
                                                                    onClick = {
                                                                        selectedSubject = null
                                                                        viewModel.generateMcqs(sub.name, ch.title, topic.title, "Medium", 5)
                                                                        viewModel.navigateTo("mcq")
                                                                    },
                                                                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant, contentColor = Color.White),
                                                                    shape = RoundedCornerShape(6.dp),
                                                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                                    modifier = Modifier.height(26.dp)
                                                                ) {
                                                                    Text("Quiz ❓", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedSubject = null }) {
                        Text("Close", color = CyanPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // TOPIC STUDY READER MODAL
        studyTopicDialog?.let { topic ->
            AlertDialog(
                onDismissRequest = { studyTopicDialog = null },
                containerColor = DarkSurface,
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(topic.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                            Text("${topic.subjectName} • ${topic.chapterTitle}", fontSize = 11.sp, color = CyanPrimary)
                        }
                        IconButton(onClick = { studyTopicDialog = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = DarkCardSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("📌 NCTB Core Summary", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CyanPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("This topic covers key definitions, mathematical derivations, and board exam Creative Questions (CQ) for HSC 2028.", fontSize = 11.sp, color = TextPrimary)
                            }
                        }

                        Surface(
                            color = DarkCardSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("📐 Formulas & Units", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = EmeraldSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Standard SI units, equations, and conversion factors required for board numericals.", fontSize = 11.sp, color = TextPrimary)
                            }
                        }

                        Surface(
                            color = DarkCardSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("⚠️ Common Board Exam Pitfalls", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = RoseError)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Watch out for unit mismatch (e.g. km/h vs m/s) and vector direction signs in problem solving.", fontSize = 11.sp, color = TextPrimary)
                            }
                        }

                        Text("Update Topic Status:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    viewModel.updateTopicProgress(topic.id, "Mastered", 5)
                                    studyTopicDialog = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSecondary, contentColor = Color.Black),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Mark Mastered ✓", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    viewModel.updateTopicProgress(topic.id, "In Progress", 3)
                                    studyTopicDialog = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant, contentColor = CyanPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Mark In Progress", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { studyTopicDialog = null }) {
                        Text("Done", color = CyanPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}
