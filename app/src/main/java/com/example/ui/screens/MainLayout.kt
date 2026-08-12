package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel
import java.util.Locale

data class NavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(
    viewModel: HscViewModel,
    content: @Composable (String) -> Unit
) {
    val currentRoute by viewModel.currentScreen.collectAsState()
    val studentProfile by viewModel.studentProfile.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    val isLowBandwidth by viewModel.isLowBandwidthMode.collectAsState()
    val unreadCount by viewModel.unreadNotificationsCount.collectAsState()
    val notificationsList by viewModel.notifications.collectAsState()

    var showMenuSheet by remember { mutableStateOf(false) }
    var showNotificationSheet by remember { mutableStateOf(false) }

    val navItems = listOf(
        NavItem("dashboard", "Dashboard", Icons.Default.Dashboard),
        NavItem("teacher", "AI Mentor", Icons.Default.Psychology),
        NavItem("live_class", "Live Class", Icons.Default.OndemandVideo),
        NavItem("doubt_solve", "Doubt Solve", Icons.Default.HelpOutline),
        NavItem("study_room", "Study Room", Icons.Default.MeetingRoom),
        NavItem("live_exams", "Live Exams", Icons.Default.Quiz),
        NavItem("planner", "Smart Plan", Icons.Default.CalendarToday),
        NavItem("timer", "Study Timer", Icons.Default.Timer),
        NavItem("subjects", "Subjects", Icons.Default.MenuBook),
        NavItem("notes", "Notes", Icons.Default.EditNote),
        NavItem("mcq", "MCQ Practice", Icons.Default.Quiz),
        NavItem("cq", "CQ Practice", Icons.Default.Assignment),
        NavItem("exams", "Mock Exam", Icons.Default.School),
        NavItem("mistakes", "Mistake Book", Icons.Default.ReportProblem),
        NavItem("revision", "Revision", Icons.Default.Autorenew),
        NavItem("analytics", "Analytics", Icons.Default.BarChart),
        NavItem("voice_teacher", "Voice Teacher", Icons.Default.RecordVoiceOver),
        NavItem("materials", "My Materials", Icons.Default.FolderSpecial),
        NavItem("settings", "Settings", Icons.Default.Settings)
    )

    val isMobile = BoxWithConstraintsScopeIsCompact()

    // If student not onboarded, show onboarding screen
    if (studentProfile == null || !studentProfile!!.isOnboarded) {
        OnboardingScreen(
            onComplete = { profile -> viewModel.completeOnboarding(profile) }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { showMenuSheet = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu Drawer", tint = BentoLavenderPrimary)
                    }
                },
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "HSC MENTOR AI",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                color = BentoLavenderPrimary,
                                letterSpacing = 1.sp
                            )

                            // Connection Status Pill
                            Surface(
                                color = when (connectionState) {
                                    "ONLINE" -> EmeraldSecondary.copy(alpha = 0.2f)
                                    "OFFLINE" -> RedPrimary.copy(alpha = 0.2f)
                                    else -> AmberWarning.copy(alpha = 0.2f)
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.size(6.dp).clip(CircleShape).background(
                                            when (connectionState) {
                                                "ONLINE" -> EmeraldSecondary
                                                "OFFLINE" -> RedPrimary
                                                else -> AmberWarning
                                            }
                                        )
                                    )
                                    Text(
                                        text = connectionState,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Hello, ${studentProfile?.name ?: "Sakib"}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = TextPrimary
                        )
                    }
                },
                actions = {
                    // Low Bandwidth Toggle Pill
                    IconButton(
                        onClick = { viewModel.toggleLowBandwidthMode() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Text(if (isLowBandwidth) "⚡" else "📶", fontSize = 14.sp)
                    }

                    // Notification Bell Icon with Badge
                    IconButton(
                        onClick = {
                            showNotificationSheet = true
                            viewModel.markAllNotificationsRead()
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadCount > 0) {
                                    Badge(containerColor = RedPrimary) {
                                        Text("$unreadCount", color = Color.White, fontSize = 9.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = BentoLavenderPrimary, modifier = Modifier.size(20.dp))
                        }
                    }

                    // Active timer indicator pill
                    if (timerState.isRunning) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { viewModel.navigateTo("timer") }
                                .padding(end = 4.dp),
                            color = BentoDeepPurple,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BentoLavenderPrimary)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Timer, contentDescription = null, tint = BentoLavenderPrimary, modifier = Modifier.size(14.dp))
                                val mins = timerState.remainingSeconds / 60
                                val secs = timerState.remainingSeconds % 60
                                Text(
                                    text = String.format(Locale.getDefault(), "%02d:%02d", mins, secs),
                                    fontWeight = FontWeight.Bold,
                                    color = BentoLavenderPrimary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    // Streak Pill
                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                        color = DarkSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("🔥", fontSize = 12.sp)
                            Text(
                                text = "${studentProfile?.streakDays ?: 12}",
                                fontWeight = FontWeight.Bold,
                                color = BentoFlameCoral,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Avatar Circle
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BentoLavenderPrimary)
                            .clickable { viewModel.navigateTo("settings") },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (studentProfile?.name?.take(1) ?: "S").uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = BentoDeepPurple,
                            fontSize = 15.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                contentColor = TextPrimary,
                modifier = Modifier.border(1.dp, BentoBorder)
            ) {
                val primaryBottomNav = listOf(
                    navItems[0], // Dashboard
                    navItems[4], // AI Teacher
                    navItems[3], // Timer / Focus (+)
                    navItems[5], // Notes
                    navItems[11] // Analytics / Stats
                )
                primaryBottomNav.forEachIndexed { index, item ->
                    val selected = currentRoute == item.route
                    if (index == 2) {
                        // Center Action Button
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                onClick = { viewModel.navigateTo("timer") },
                                shape = RoundedCornerShape(16.dp),
                                color = BentoLavenderPrimary,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Study Mode",
                                        tint = BentoDeepPurple,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        NavigationBarItem(
                            selected = selected,
                            onClick = { viewModel.navigateTo(item.route) },
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.title,
                                    tint = if (selected) BentoLavenderPrimary else TextSecondary
                                )
                            },
                            label = {
                                Text(
                                    item.title.uppercase(),
                                    fontSize = 9.sp,
                                    color = if (selected) BentoLavenderPrimary else TextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = BentoDeepPurple
                            ),
                            modifier = Modifier.testTag("nav_item_${item.route}")
                        )
                    }
                }
            }
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Secondary side drawer menu on wider screens or integrated scroll
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBackground)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f)) {
                        content(currentRoute)
                    }
                    // Footer Credit
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = DarkSurface
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Made by Shrihan Rudra Biswas",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = CyanPrimary.copy(alpha = 0.85f),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (showMenuSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMenuSheet = false },
            containerColor = DarkSurface,
            scrimColor = Color.Black.copy(alpha = 0.6f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("HSC MENTOR AI MODULES", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BentoLavenderPrimary, letterSpacing = 1.sp)
                    IconButton(onClick = { showMenuSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val lazyGridItems = navItems
                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                ) {
                    items(lazyGridItems.size) { idx ->
                        val item = lazyGridItems[idx]
                        val isSelected = currentRoute == item.route
                        Surface(
                            onClick = {
                                viewModel.navigateTo(item.route)
                                showMenuSheet = false
                            },
                            color = if (isSelected) BentoDeepPurple else DarkCardSurface,
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BentoLavenderPrimary else BentoBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) BentoLavenderPrimary else TextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = item.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BentoLavenderPrimary else TextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    if (showNotificationSheet) {
        ModalBottomSheet(
            onDismissRequest = { showNotificationSheet = false },
            containerColor = DarkSurface,
            scrimColor = Color.Black.copy(alpha = 0.6f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("IN-APP NOTIFICATIONS", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BentoLavenderPrimary, letterSpacing = 1.sp)
                    IconButton(onClick = { showNotificationSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                androidx.compose.foundation.lazy.LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.heightIn(max = 350.dp)
                ) {
                    items(notificationsList.size) { idx ->
                        val item = notificationsList[idx]
                        Surface(
                            color = DarkCardSurface,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(item.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                                    Text(item.timeAgoStr, fontSize = 10.sp, color = TextMuted)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(item.message, fontSize = 12.sp, color = TextSecondary, lineHeight = 17.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun BoxWithConstraintsScopeIsCompact(): Boolean {
    return true // Mobile first layout
}

