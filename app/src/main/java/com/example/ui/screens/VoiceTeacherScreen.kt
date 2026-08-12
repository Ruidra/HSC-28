package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel

enum class VoiceState { IDLE, LISTENING, THINKING, SPEAKING }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceTeacherScreen(viewModel: HscViewModel) {
    var voiceState by remember { mutableStateOf(VoiceState.IDLE) }
    var selectedLanguage by remember { mutableStateOf("Bangla & English") }
    var currentSpeechTranscript by remember { mutableStateOf("Tap the microphone button and ask any question orally...") }
    var aiVoiceResponse by remember { mutableStateOf("Ready to tutor you via voice! Ask about Physics, Chemistry, Higher Math, or ICT.") }

    val infiniteTransition = rememberInfiniteTransition(label = "VoicePulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (voiceState == VoiceState.LISTENING || voiceState == VoiceState.SPEAKING) 1.25f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Pulse"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
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
                            .background(BentoMediumPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = BentoLavenderPrimary, modifier = Modifier.size(24.dp))
                    }
                    Column {
                        Text("AI Voice Teacher", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                        Text("Interactive Speech Tutor for HSC 2028", fontSize = 11.sp, color = BentoLavenderPrimary)
                    }
                }

                // Language Toggle Pill
                Surface(
                    onClick = {
                        selectedLanguage = if (selectedLanguage.contains("Bangla")) "English Only" else "Bangla & English"
                    },
                    shape = RoundedCornerShape(20.dp),
                    color = DarkSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BentoBorder)
                ) {
                    Text(
                        text = selectedLanguage,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BentoLavenderPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Center Animated Voice Orb
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(
                            when (voiceState) {
                                VoiceState.IDLE -> BentoMediumPurple.copy(alpha = 0.3f)
                                VoiceState.LISTENING -> BentoFlameCoral.copy(alpha = 0.4f)
                                VoiceState.THINKING -> BentoLavenderPrimary.copy(alpha = 0.5f)
                                VoiceState.SPEAKING -> EmeraldSecondary.copy(alpha = 0.4f)
                            }
                        )
                        .border(
                            2.dp,
                            when (voiceState) {
                                VoiceState.IDLE -> BentoLavenderPrimary
                                VoiceState.LISTENING -> BentoFlameCoral
                                VoiceState.THINKING -> BentoLavenderPrimary
                                VoiceState.SPEAKING -> EmeraldSecondary
                            },
                            CircleShape
                        )
                        .clickable {
                            when (voiceState) {
                                VoiceState.IDLE -> {
                                    voiceState = VoiceState.LISTENING
                                    currentSpeechTranscript = "Listening... 'Sir, explain projectile motion max height formula'"
                                }
                                VoiceState.LISTENING -> {
                                    voiceState = VoiceState.THINKING
                                    currentSpeechTranscript = "Processing your question..."
                                    aiVoiceResponse = "For projectile motion, maximum height H is given by (u^2 * sin^2 θ) / (2g). At max height, vertical velocity becomes zero."
                                    voiceState = VoiceState.SPEAKING
                                }
                                else -> {
                                    voiceState = VoiceState.IDLE
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (voiceState) {
                            VoiceState.IDLE -> Icons.Default.Mic
                            VoiceState.LISTENING -> Icons.Default.GraphicEq
                            VoiceState.THINKING -> Icons.Default.Psychology
                            VoiceState.SPEAKING -> Icons.Default.VolumeUp
                        },
                        contentDescription = "Voice Assistant",
                        tint = TextPrimary,
                        modifier = Modifier.size(56.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = when (voiceState) {
                        VoiceState.IDLE -> "Tap orb to speak"
                        VoiceState.LISTENING -> "Listening to your voice..."
                        VoiceState.THINKING -> "AI Thinking..."
                        VoiceState.SPEAKING -> "AI Teacher Speaking 🔊"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = BentoLavenderPrimary
                )
            }
        }

        // Live Transcript & AI Voice Response Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BentoBorder, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("STUDENT VOICE TRANSCRIPT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 0.5.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(currentSpeechTranscript, fontSize = 13.sp, color = TextPrimary)

                Divider(modifier = Modifier.padding(vertical = 12.dp), color = BentoBorder)

                Text("AI TEACHER AUDIO EXPLANATION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BentoLavenderPrimary, letterSpacing = 0.5.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(aiVoiceResponse, fontSize = 13.sp, color = TextPrimary, lineHeight = 19.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Quick Oral Question Buttons
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Quick Voice Prompts:", fontSize = 11.sp, color = TextMuted)
            Spacer(modifier = Modifier.height(6.dp))

            listOf(
                "Explain Gauss's Law orally in Bangla" to "Physics 1st Paper",
                "Quiz me orally on Organic Chemistry reactions" to "Chemistry 2nd Paper",
                "What is integration by parts formula?" to "Higher Math"
            ).forEach { (prompt, sub) ->
                Surface(
                    onClick = {
                        currentSpeechTranscript = prompt
                        voiceState = VoiceState.THINKING
                        aiVoiceResponse = "Here is the oral step-by-step breakdown for $sub: $prompt."
                        voiceState = VoiceState.SPEAKING
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .border(1.dp, BentoBorder, RoundedCornerShape(14.dp)),
                    color = DarkSurfaceVariant,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = BentoLavenderPrimary, modifier = Modifier.size(16.dp))
                            Text(prompt, fontSize = 12.sp, color = TextPrimary)
                        }
                        Text(sub, fontSize = 10.sp, color = BentoMutedText)
                    }
                }
            }
        }
    }
}
