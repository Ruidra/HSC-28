package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * High-quality Academic Content Viewer for HSC 2028 topics, formulas, notes,
 * and AI Mentor responses. Properly formats Bangla + English text, formulas,
 * headers, lists, and callout blocks without line wrapping issues or clipping.
 */
@Composable
fun AcademicContentViewer(
    content: String,
    modifier: Modifier = Modifier,
    maxWidth: Dp = 680.dp,
    containerColor: Color = Color.Transparent
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val lines = content.split("\n")
            var inFormulaBlock = false
            var formulaBuffer = mutableListOf<String>()

            lines.forEach { rawLine ->
                val line = rawLine.trim()

                when {
                    line.startsWith("# ") -> {
                        Text(
                            text = parseStyledText(line.removePrefix("# ")),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = BentoLavenderPrimary,
                            lineHeight = 24.sp
                        )
                    }
                    line.startsWith("## ") -> {
                        Text(
                            text = parseStyledText(line.removePrefix("## ")),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = CyanPrimary,
                            lineHeight = 22.sp
                        )
                    }
                    line.startsWith("### ") -> {
                        Text(
                            text = parseStyledText(line.removePrefix("### ")),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                    }
                    line.startsWith("Formula:") || line.startsWith("📐 Formula") || line.contains("F = ") || line.contains("E = ") || line.contains("y = ") -> {
                        // Formula callout card
                        Surface(
                            color = DarkCardSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSecondary.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "📐 FORMULA / RELATION",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = EmeraldSecondary,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = parseStyledText(line),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = TextPrimary,
                                    fontFamily = FontFamily.Monospace,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                    line.startsWith("⚠️") || line.contains("Common Mistake") || line.contains("Pitfall") -> {
                        // Warning/Pitfall callout
                        Surface(
                            color = DarkCardSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RoseError.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("⚠️", fontSize = 14.sp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = parseStyledText(line),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp,
                                        color = TextPrimary,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                    line.startsWith("* ") || line.startsWith("- ") || line.startsWith("• ") -> {
                        // Bullet point
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("•", fontWeight = FontWeight.Bold, color = CyanPrimary, fontSize = 14.sp)
                            Text(
                                text = parseStyledText(line.removePrefix("* ").removePrefix("- ").removePrefix("• ")),
                                fontSize = 13.sp,
                                color = TextPrimary,
                                lineHeight = 19.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    line.matches(Regex("^\\d+\\.\\s+.*")) -> {
                        // Numbered list item
                        val parts = line.split(". ", limit = 2)
                        val num = parts.getOrNull(0) ?: "1"
                        val text = parts.getOrNull(1) ?: ""

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                color = DarkSurfaceVariant,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "$num.",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoLavenderPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = parseStyledText(text),
                                fontSize = 13.sp,
                                color = TextPrimary,
                                lineHeight = 19.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    line.isBlank() -> {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    else -> {
                        // Standard paragraph
                        Text(
                            text = parseStyledText(line),
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 19.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

/**
 * Parses bold **text** and italic *text* syntax into AnnotatedString.
 */
private fun parseStyledText(input: String) = buildAnnotatedString {
    var cursor = 0
    val boldRegex = Regex("\\*\\*(.*?)\\*\\*")
    val matches = boldRegex.findAll(input)

    matches.forEach { match ->
        if (match.range.first > cursor) {
            append(input.substring(cursor, match.range.first))
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = TextPrimary)) {
            append(match.groupValues[1])
        }
        cursor = match.range.last + 1
    }

    if (cursor < input.length) {
        append(input.substring(cursor))
    }
}
