package com.example.data.model

object EducationBoards {
    val ALL_BOARDS = listOf(
        "Dhaka",
        "Rajshahi",
        "Chattogram",
        "Cumilla",
        "Sylhet",
        "Barishal",
        "Jashore",
        "Dinajpur",
        "Mymensingh",
        "Madrasah",
        "Technical"
    )

    fun getBoardDisplayName(boardKey: String): String {
        return when (boardKey.lowercase().trim()) {
            "dhaka" -> "Dhaka Board"
            "rajshahi" -> "Rajshahi Board"
            "chattogram", "chittagong" -> "Chattogram Board"
            "cumilla", "comilla" -> "Cumilla Board"
            "sylhet" -> "Sylhet Board"
            "barishal", "barisal" -> "Barishal Board"
            "jashore", "jessore" -> "Jashore Board"
            "dinajpur" -> "Dinajpur Board"
            "mymensingh" -> "Mymensingh Board"
            "madrasah" -> "Madrasah Board"
            "technical" -> "Technical Board"
            else -> "$boardKey Board"
        }
    }
}
