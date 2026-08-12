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
import com.example.ui.theme.*
import com.example.ui.viewmodel.HscViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CqPracticeScreen(viewModel: HscViewModel) {
    val cqResult by viewModel.cqEvaluationResult.collectAsState()
    val isEvaluating by viewModel.isEvaluatingCq.collectAsState()

    var subject by remember { mutableStateOf("Physics 1st Paper") }
    var chapter by remember { mutableStateOf("Dynamics & Motion") }
    var stemText by remember { mutableStateOf("A cricketer throws a ball with an initial velocity of 30 m/s at an angle of 30 degrees to the horizontal from a height of 2 meters.\n(a) Define projectile motion. [1]\n(b) Why does horizontal velocity remain constant in ideal projectile motion? [2]\n(c) Calculate the maximum height reached by the ball above the ground. [3]\n(d) Will the ball clear a 5m tall wall placed 40m away? Justify mathematically. [4]") }
    var studentAnswerText by remember { mutableStateOf("(a) Motion of a body projected in air under gravity is projectile motion.\n(b) Because there is no horizontal force acting on the body (ignoring air resistance), acceleration ax = 0.\n(c) H_max = (v0^2 * sin^2(theta))/(2g) + 2m = (900 * 0.25)/(19.6) + 2 = 11.48m + 2m = 13.48m.\n(d) x = v0*cos(theta)*t => t = 40 / (30 * cos(30)) = 1.54s. Height y = 2 + v0*sin(30)*t - 0.5*g*t^2 = 2 + 15(1.54) - 4.9(2.37) = 2 + 23.1 - 11.6 = 13.5m > 5m. Yes, it will clear.") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Column {
            Text("CQ Written Practice & Examiner", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
            Text("Board standard Creative Question evaluator (10 Marks)", fontSize = 12.sp, color = CyanPrimary)
        }

        if (cqResult == null) {
            // CQ Editor
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
                    Text("1. Question Stem & Sub-questions", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)

                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Subject") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = stemText,
                        onValueChange = { stemText = it },
                        label = { Text("CQ Question Stem (10 Marks)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 6
                    )

                    Text("2. Your Written Response", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)

                    OutlinedTextField(
                        value = studentAnswerText,
                        onValueChange = { studentAnswerText = it },
                        label = { Text("Type your full answer here...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 8
                    )

                    Button(
                        onClick = {
                            viewModel.evaluateCq(subject, chapter, stemText, studentAnswerText)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("evaluate_cq_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black)
                    ) {
                        if (isEvaluating) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.Black)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Examiner Evaluating...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Grading, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Evaluate Answer with AI Examiner", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // CQ Evaluation Result Card
            val res = cqResult!!
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, EmeraldSecondary, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("AI Examiner Evaluation", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                            Text("* Estimated practice marks (AI guidance)", fontSize = 10.sp, color = AmberWarning)
                        }
                        Surface(
                            color = EmeraldSecondary.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "${res.earnedMarks} / ${res.totalMarks} Marks",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = EmeraldSecondary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Divider(color = DarkSurfaceVariant)

                    // Breakdown Scores
                    Text("Mark Breakdown:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextSecondary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(modifier = Modifier.weight(1f), color = DarkCardSurface, shape = RoundedCornerShape(10.dp)) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Knowledge (ক)", fontSize = 10.sp, color = TextMuted)
                                Text("${res.knowledgeScore}/2", fontWeight = FontWeight.Bold, color = CyanPrimary)
                            }
                        }
                        Surface(modifier = Modifier.weight(1f), color = DarkCardSurface, shape = RoundedCornerShape(10.dp)) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Comprehension (খ)", fontSize = 10.sp, color = TextMuted)
                                Text("${res.understandingScore}/3", fontWeight = FontWeight.Bold, color = CyanPrimary)
                            }
                        }
                        Surface(modifier = Modifier.weight(1f), color = DarkCardSurface, shape = RoundedCornerShape(10.dp)) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Application (গ)", fontSize = 10.sp, color = TextMuted)
                                Text("${res.applicationScore}/3", fontWeight = FontWeight.Bold, color = CyanPrimary)
                            }
                        }
                        Surface(modifier = Modifier.weight(1f), color = DarkCardSurface, shape = RoundedCornerShape(10.dp)) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Higher Order (ঘ)", fontSize = 10.sp, color = TextMuted)
                                Text("${res.higherOrderScore}/2", fontWeight = FontWeight.Bold, color = CyanPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Constructive Feedback:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CyanPrimary)
                    Text(res.feedback, fontSize = 13.sp, color = TextPrimary, lineHeight = 19.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Model Answer Structure:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = EmeraldSecondary)
                    Text(res.modelAnswer, fontSize = 12.sp, color = TextSecondary, lineHeight = 18.sp)

                    Button(
                        onClick = {
                            viewModel.resetCqEvaluation()
                            studentAnswerText = ""
                        },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color.Black)
                    ) {
                        Text("Attempt Another CQ", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
