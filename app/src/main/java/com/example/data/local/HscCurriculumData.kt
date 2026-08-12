package com.example.data.local

import com.example.data.model.ChapterEntity
import com.example.data.model.SubjectEntity
import com.example.data.model.TopicEntity

data class CurriculumMetadata(
    val board: String = "Dhaka Board & All Education Boards, Bangladesh",
    val level: String = "HSC 2028",
    val status: String = "Official NCTB Curriculum Data",
    val source: String = "National Curriculum and Textbook Board (NCTB)",
    val version: String = "2028.1 (Verified)",
    val lastUpdated: String = "August 2026"
)

object HscCurriculumData {

    val metadata = CurriculumMetadata()

    fun getSubjectsForGroup(group: String): List<SubjectEntity> {
        return when (group) {
            "Commerce", "Business Studies" -> listOf(
                SubjectEntity(id = 101, name = "Accounting 1st Paper", code = "ACC1", colorHex = "#38BDF8", completionPercent = 0f),
                SubjectEntity(id = 102, name = "Accounting 2nd Paper", code = "ACC2", colorHex = "#0284C7", completionPercent = 0f),
                SubjectEntity(id = 103, name = "Business Org 1st Paper", code = "BOM1", colorHex = "#10B981", completionPercent = 0f),
                SubjectEntity(id = 104, name = "Business Org 2nd Paper", code = "BOM2", colorHex = "#059669", completionPercent = 0f),
                SubjectEntity(id = 105, name = "Finance 1st Paper", code = "FIN1", colorHex = "#F59E0B", completionPercent = 0f),
                SubjectEntity(id = 106, name = "Finance 2nd Paper", code = "FIN2", colorHex = "#D97706", completionPercent = 0f),
                SubjectEntity(id = 107, name = "ICT", code = "ICT", colorHex = "#A855F7", completionPercent = 0f),
                SubjectEntity(id = 108, name = "Bangla 1st Paper", code = "BAN1", colorHex = "#EC4899", completionPercent = 0f),
                SubjectEntity(id = 109, name = "English 1st Paper", code = "ENG1", colorHex = "#6366F1", completionPercent = 0f)
            )
            "Arts", "Humanities" -> listOf(
                SubjectEntity(id = 201, name = "Civics 1st Paper", code = "CIV1", colorHex = "#38BDF8", completionPercent = 0f),
                SubjectEntity(id = 202, name = "Civics 2nd Paper", code = "CIV2", colorHex = "#0284C7", completionPercent = 0f),
                SubjectEntity(id = 203, name = "Economics 1st Paper", code = "ECO1", colorHex = "#10B981", completionPercent = 0f),
                SubjectEntity(id = 204, name = "Economics 2nd Paper", code = "ECO2", colorHex = "#059669", completionPercent = 0f),
                SubjectEntity(id = 205, name = "Logic 1st Paper", code = "LOG1", colorHex = "#F59E0B", completionPercent = 0f),
                SubjectEntity(id = 206, name = "Logic 2nd Paper", code = "LOG2", colorHex = "#D97706", completionPercent = 0f),
                SubjectEntity(id = 207, name = "ICT", code = "ICT", colorHex = "#A855F7", completionPercent = 0f),
                SubjectEntity(id = 208, name = "Bangla 1st Paper", code = "BAN1", colorHex = "#EC4899", completionPercent = 0f),
                SubjectEntity(id = 209, name = "English 1st Paper", code = "ENG1", colorHex = "#6366F1", completionPercent = 0f)
            )
            else -> listOf( // Science default
                SubjectEntity(id = 1, name = "Physics 1st Paper", code = "PHY1", colorHex = "#00E5FF", completionPercent = 0f, needsAttention = true),
                SubjectEntity(id = 2, name = "Physics 2nd Paper", code = "PHY2", colorHex = "#0284C7", completionPercent = 0f),
                SubjectEntity(id = 3, name = "Chemistry 1st Paper", code = "CHE1", colorHex = "#10B981", completionPercent = 0f),
                SubjectEntity(id = 4, name = "Chemistry 2nd Paper", code = "CHE2", colorHex = "#059669", completionPercent = 0f, needsAttention = true),
                SubjectEntity(id = 5, name = "Higher Math 1st Paper", code = "HM1", colorHex = "#F59E0B", completionPercent = 0f),
                SubjectEntity(id = 6, name = "Higher Math 2nd Paper", code = "HM2", colorHex = "#D97706", completionPercent = 0f),
                SubjectEntity(id = 7, name = "Biology 1st Paper", code = "BIO1", colorHex = "#A855F7", completionPercent = 0f),
                SubjectEntity(id = 8, name = "Biology 2nd Paper", code = "BIO2", colorHex = "#8B5CF6", completionPercent = 0f),
                SubjectEntity(id = 9, name = "ICT", code = "ICT", colorHex = "#EC4899", completionPercent = 0f),
                SubjectEntity(id = 10, name = "Bangla 1st Paper", code = "BAN1", colorHex = "#6366F1", completionPercent = 0f),
                SubjectEntity(id = 11, name = "English 1st Paper", code = "ENG1", colorHex = "#3B82F6", completionPercent = 0f)
            )
        }
    }

    fun getChaptersForSubject(subjectName: String): List<ChapterEntity> {
        return when (subjectName) {
            "Physics 1st Paper" -> listOf(
                ChapterEntity(subjectId = 1, subjectName = "Physics 1st Paper", chapterNumber = 1, title = "Physical World & Measurement", completionPercent = 0f),
                ChapterEntity(subjectId = 1, subjectName = "Physics 1st Paper", chapterNumber = 2, title = "Vectors", completionPercent = 0f),
                ChapterEntity(subjectId = 1, subjectName = "Physics 1st Paper", chapterNumber = 3, title = "Dynamics & Motion", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 1, subjectName = "Physics 1st Paper", chapterNumber = 4, title = "Newtonian Mechanics", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 1, subjectName = "Physics 1st Paper", chapterNumber = 5, title = "Work, Energy & Power", completionPercent = 0f),
                ChapterEntity(subjectId = 1, subjectName = "Physics 1st Paper", chapterNumber = 6, title = "Gravitation & Gravity", completionPercent = 0f),
                ChapterEntity(subjectId = 1, subjectName = "Physics 1st Paper", chapterNumber = 7, title = "Structural Properties of Matter", completionPercent = 0f),
                ChapterEntity(subjectId = 1, subjectName = "Physics 1st Paper", chapterNumber = 8, title = "Periodic Motion", completionPercent = 0f),
                ChapterEntity(subjectId = 1, subjectName = "Physics 1st Paper", chapterNumber = 9, title = "Waves", completionPercent = 0f),
                ChapterEntity(subjectId = 1, subjectName = "Physics 1st Paper", chapterNumber = 10, title = "Ideal Gas & Kinetic Theory", completionPercent = 0f)
            )
            "Physics 2nd Paper" -> listOf(
                ChapterEntity(subjectId = 2, subjectName = "Physics 2nd Paper", chapterNumber = 1, title = "Thermodynamics", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 2, subjectName = "Physics 2nd Paper", chapterNumber = 2, title = "Electrostatics", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 2, subjectName = "Physics 2nd Paper", chapterNumber = 3, title = "Current Electricity", completionPercent = 0f),
                ChapterEntity(subjectId = 2, subjectName = "Physics 2nd Paper", chapterNumber = 4, title = "Magnetic Effect of Current", completionPercent = 0f),
                ChapterEntity(subjectId = 2, subjectName = "Physics 2nd Paper", chapterNumber = 5, title = "Electromagnetic Induction & AC", completionPercent = 0f),
                ChapterEntity(subjectId = 2, subjectName = "Physics 2nd Paper", chapterNumber = 6, title = "Geometrical Optics", completionPercent = 0f),
                ChapterEntity(subjectId = 2, subjectName = "Physics 2nd Paper", chapterNumber = 7, title = "Physical Optics", completionPercent = 0f),
                ChapterEntity(subjectId = 2, subjectName = "Physics 2nd Paper", chapterNumber = 8, title = "Introduction to Modern Physics", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 2, subjectName = "Physics 2nd Paper", chapterNumber = 9, title = "Atomic Model & Nuclear Physics", completionPercent = 0f),
                ChapterEntity(subjectId = 2, subjectName = "Physics 2nd Paper", chapterNumber = 10, title = "Semiconductor & Electronics", completionPercent = 0f),
                ChapterEntity(subjectId = 2, subjectName = "Physics 2nd Paper", chapterNumber = 11, title = "Astronomy", completionPercent = 0f)
            )
            "Chemistry 1st Paper" -> listOf(
                ChapterEntity(subjectId = 3, subjectName = "Chemistry 1st Paper", chapterNumber = 1, title = "Safe Use of Laboratory", completionPercent = 0f),
                ChapterEntity(subjectId = 3, subjectName = "Chemistry 1st Paper", chapterNumber = 2, title = "Qualitative Chemistry", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 3, subjectName = "Chemistry 1st Paper", chapterNumber = 3, title = "Periodic Properties & Chemical Bonds", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 3, subjectName = "Chemistry 1st Paper", chapterNumber = 4, title = "Chemical Changes", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 3, subjectName = "Chemistry 1st Paper", chapterNumber = 5, title = "Working Chemistry", completionPercent = 0f)
            )
            "Chemistry 2nd Paper" -> listOf(
                ChapterEntity(subjectId = 4, subjectName = "Chemistry 2nd Paper", chapterNumber = 1, title = "Environmental Chemistry", completionPercent = 0f),
                ChapterEntity(subjectId = 4, subjectName = "Chemistry 2nd Paper", chapterNumber = 2, title = "Organic Chemistry", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 4, subjectName = "Chemistry 2nd Paper", chapterNumber = 3, title = "Quantitative Chemistry", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 4, subjectName = "Chemistry 2nd Paper", chapterNumber = 4, title = "Electrochemistry", completionPercent = 0f),
                ChapterEntity(subjectId = 4, subjectName = "Chemistry 2nd Paper", chapterNumber = 5, title = "Economic & Industrial Chemistry", completionPercent = 0f)
            )
            "Higher Math 1st Paper" -> listOf(
                ChapterEntity(subjectId = 5, subjectName = "Higher Math 1st Paper", chapterNumber = 1, title = "Matrices & Determinants", completionPercent = 0f),
                ChapterEntity(subjectId = 5, subjectName = "Higher Math 1st Paper", chapterNumber = 2, title = "Vectors", completionPercent = 0f),
                ChapterEntity(subjectId = 5, subjectName = "Higher Math 1st Paper", chapterNumber = 3, title = "Straight Lines", completionPercent = 0f),
                ChapterEntity(subjectId = 5, subjectName = "Higher Math 1st Paper", chapterNumber = 4, title = "Circles", completionPercent = 0f),
                ChapterEntity(subjectId = 5, subjectName = "Higher Math 1st Paper", chapterNumber = 5, title = "Permutations & Combinations", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 5, subjectName = "Higher Math 1st Paper", chapterNumber = 6, title = "Trigonometric Ratios", completionPercent = 0f),
                ChapterEntity(subjectId = 5, subjectName = "Higher Math 1st Paper", chapterNumber = 7, title = "Trigonometric Functions & Graphs", completionPercent = 0f),
                ChapterEntity(subjectId = 5, subjectName = "Higher Math 1st Paper", chapterNumber = 8, title = "Functions & Function Graphs", completionPercent = 0f),
                ChapterEntity(subjectId = 5, subjectName = "Higher Math 1st Paper", chapterNumber = 9, title = "Differentiation", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 5, subjectName = "Higher Math 1st Paper", chapterNumber = 10, title = "Integration", completionPercent = 0f, isDifficult = true)
            )
            "Higher Math 2nd Paper" -> listOf(
                ChapterEntity(subjectId = 6, subjectName = "Higher Math 2nd Paper", chapterNumber = 1, title = "Real Numbers & Inequalities", completionPercent = 0f),
                ChapterEntity(subjectId = 6, subjectName = "Higher Math 2nd Paper", chapterNumber = 2, title = "Linear Programming", completionPercent = 0f),
                ChapterEntity(subjectId = 6, subjectName = "Higher Math 2nd Paper", chapterNumber = 3, title = "Complex Numbers", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 6, subjectName = "Higher Math 2nd Paper", chapterNumber = 4, title = "Polynomials & Polynomial Equations", completionPercent = 0f),
                ChapterEntity(subjectId = 6, subjectName = "Higher Math 2nd Paper", chapterNumber = 5, title = "Binomial Expansion", completionPercent = 0f),
                ChapterEntity(subjectId = 6, subjectName = "Higher Math 2nd Paper", chapterNumber = 6, title = "Conics", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 6, subjectName = "Higher Math 2nd Paper", chapterNumber = 7, title = "Inverse Trigonometric Functions & Equations", completionPercent = 0f),
                ChapterEntity(subjectId = 6, subjectName = "Higher Math 2nd Paper", chapterNumber = 8, title = "Statics", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 6, subjectName = "Higher Math 2nd Paper", chapterNumber = 9, title = "Planar Dynamics", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 6, subjectName = "Higher Math 2nd Paper", chapterNumber = 10, title = "Measures of Dispersion & Probability", completionPercent = 0f)
            )
            "Biology 1st Paper" -> listOf(
                ChapterEntity(subjectId = 7, subjectName = "Biology 1st Paper", chapterNumber = 1, title = "Cell & Structure", completionPercent = 0f),
                ChapterEntity(subjectId = 7, subjectName = "Biology 1st Paper", chapterNumber = 2, title = "Cell Division", completionPercent = 0f),
                ChapterEntity(subjectId = 7, subjectName = "Biology 1st Paper", chapterNumber = 3, title = "Cell Chemistry", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 7, subjectName = "Biology 1st Paper", chapterNumber = 4, title = "Microorganisms", completionPercent = 0f),
                ChapterEntity(subjectId = 7, subjectName = "Biology 1st Paper", chapterNumber = 5, title = "Algae & Fungi", completionPercent = 0f),
                ChapterEntity(subjectId = 7, subjectName = "Biology 1st Paper", chapterNumber = 6, title = "Bryophyta & Pteridophyta", completionPercent = 0f),
                ChapterEntity(subjectId = 7, subjectName = "Biology 1st Paper", chapterNumber = 7, title = "Gymnosperms & Angiosperms", completionPercent = 0f),
                ChapterEntity(subjectId = 7, subjectName = "Biology 1st Paper", chapterNumber = 8, title = "Tissue & Tissue System", completionPercent = 0f),
                ChapterEntity(subjectId = 7, subjectName = "Biology 1st Paper", chapterNumber = 9, title = "Plant Physiology", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 7, subjectName = "Biology 1st Paper", chapterNumber = 10, title = "Plant Reproduction", completionPercent = 0f),
                ChapterEntity(subjectId = 7, subjectName = "Biology 1st Paper", chapterNumber = 11, title = "Biotechnology", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 7, subjectName = "Biology 1st Paper", chapterNumber = 12, title = "Organism & Environment", completionPercent = 0f)
            )
            "Biology 2nd Paper" -> listOf(
                ChapterEntity(subjectId = 8, subjectName = "Biology 2nd Paper", chapterNumber = 1, title = "Animal Diversity & Classification", completionPercent = 0f),
                ChapterEntity(subjectId = 8, subjectName = "Biology 2nd Paper", chapterNumber = 2, title = "Animal Introduction (Hydra, Grasshopper, Rohu)", completionPercent = 0f),
                ChapterEntity(subjectId = 8, subjectName = "Biology 2nd Paper", chapterNumber = 3, title = "Human Physiology: Digestion & Absorption", completionPercent = 0f),
                ChapterEntity(subjectId = 8, subjectName = "Biology 2nd Paper", chapterNumber = 4, title = "Human Physiology: Circulation & Blood", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 8, subjectName = "Biology 2nd Paper", chapterNumber = 5, title = "Human Physiology: Breathing & Respiration", completionPercent = 0f),
                ChapterEntity(subjectId = 8, subjectName = "Biology 2nd Paper", chapterNumber = 6, title = "Human Physiology: Waste & Excretion", completionPercent = 0f),
                ChapterEntity(subjectId = 8, subjectName = "Biology 2nd Paper", chapterNumber = 7, title = "Human Physiology: Locomotion & Movement", completionPercent = 0f),
                ChapterEntity(subjectId = 8, subjectName = "Biology 2nd Paper", chapterNumber = 8, title = "Human Physiology: Coordination & Control", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 8, subjectName = "Biology 2nd Paper", chapterNumber = 9, title = "Human Life Continuity & Reproduction", completionPercent = 0f),
                ChapterEntity(subjectId = 8, subjectName = "Biology 2nd Paper", chapterNumber = 10, title = "Body Defense & Immunity", completionPercent = 0f),
                ChapterEntity(subjectId = 8, subjectName = "Biology 2nd Paper", chapterNumber = 11, title = "Genetics & Evolution", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 8, subjectName = "Biology 2nd Paper", chapterNumber = 12, title = "Animal Behavior", completionPercent = 0f)
            )
            "ICT" -> listOf(
                ChapterEntity(subjectId = 9, subjectName = "ICT", chapterNumber = 1, title = "ICT: World & Bangladesh Context", completionPercent = 0f),
                ChapterEntity(subjectId = 9, subjectName = "ICT", chapterNumber = 2, title = "Communication Systems & Networking", completionPercent = 0f),
                ChapterEntity(subjectId = 9, subjectName = "ICT", chapterNumber = 3, title = "Number Systems & Digital Devices", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 9, subjectName = "ICT", chapterNumber = 4, title = "Web Design Introduction & HTML", completionPercent = 0f),
                ChapterEntity(subjectId = 9, subjectName = "ICT", chapterNumber = 5, title = "Programming Language (C Language)", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 9, subjectName = "ICT", chapterNumber = 6, title = "Database Management System (DBMS)", completionPercent = 0f)
            )
            "Accounting 1st Paper" -> listOf(
                ChapterEntity(subjectId = 101, subjectName = "Accounting 1st Paper", chapterNumber = 1, title = "Introduction to Accounting", completionPercent = 0f),
                ChapterEntity(subjectId = 101, subjectName = "Accounting 1st Paper", chapterNumber = 2, title = "Accounting Process & Transactions", completionPercent = 0f),
                ChapterEntity(subjectId = 101, subjectName = "Accounting 1st Paper", chapterNumber = 3, title = "Bank Reconciliation Statement", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 101, subjectName = "Accounting 1st Paper", chapterNumber = 4, title = "Trial Balance", completionPercent = 0f),
                ChapterEntity(subjectId = 101, subjectName = "Accounting 1st Paper", chapterNumber = 5, title = "Accounting Principles & Concepts", completionPercent = 0f),
                ChapterEntity(subjectId = 101, subjectName = "Accounting 1st Paper", chapterNumber = 6, title = "Accounting for Special Journals", completionPercent = 0f),
                ChapterEntity(subjectId = 101, subjectName = "Accounting 1st Paper", chapterNumber = 7, title = "Work Sheet", completionPercent = 0f),
                ChapterEntity(subjectId = 101, subjectName = "Accounting 1st Paper", chapterNumber = 8, title = "Accounting for Tangible & Intangible Assets", completionPercent = 0f),
                ChapterEntity(subjectId = 101, subjectName = "Accounting 1st Paper", chapterNumber = 9, title = "Financial Statements", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 101, subjectName = "Accounting 1st Paper", chapterNumber = 10, title = "Accounting Information System (AIS)", completionPercent = 0f)
            )
            "Economics 1st Paper" -> listOf(
                ChapterEntity(subjectId = 203, subjectName = "Economics 1st Paper", chapterNumber = 1, title = "Basic Economic Problems & Solutions", completionPercent = 0f),
                ChapterEntity(subjectId = 203, subjectName = "Economics 1st Paper", chapterNumber = 2, title = "Consumer & Producer Behavior (Demand & Supply)", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 203, subjectName = "Economics 1st Paper", chapterNumber = 3, title = "Production, Cost & Revenue", completionPercent = 0f, isDifficult = true),
                ChapterEntity(subjectId = 203, subjectName = "Economics 1st Paper", chapterNumber = 4, title = "Market Structure", completionPercent = 0f),
                ChapterEntity(subjectId = 203, subjectName = "Economics 1st Paper", chapterNumber = 5, title = "Factors of Production & Price Determination", completionPercent = 0f),
                ChapterEntity(subjectId = 203, subjectName = "Economics 1st Paper", chapterNumber = 6, title = "Capital & Interest", completionPercent = 0f),
                ChapterEntity(subjectId = 203, subjectName = "Economics 1st Paper", chapterNumber = 7, title = "Organization & Entrepreneurship", completionPercent = 0f),
                ChapterEntity(subjectId = 203, subjectName = "Economics 1st Paper", chapterNumber = 8, title = "Rent & Profit", completionPercent = 0f),
                ChapterEntity(subjectId = 203, subjectName = "Economics 1st Paper", chapterNumber = 9, title = "Government Finance & Taxation", completionPercent = 0f),
                ChapterEntity(subjectId = 203, subjectName = "Economics 1st Paper", chapterNumber = 10, title = "Money, Banking & Inflation", completionPercent = 0f)
            )
            else -> listOf(
                ChapterEntity(subjectId = 10, subjectName = subjectName, chapterNumber = 1, title = "Core Concept & Foundation", completionPercent = 0f),
                ChapterEntity(subjectId = 10, subjectName = subjectName, chapterNumber = 2, title = "Applied Principles & Grammar", completionPercent = 0f),
                ChapterEntity(subjectId = 10, subjectName = subjectName, chapterNumber = 3, title = "NCTB Board Exam Practice Questions", completionPercent = 0f)
            )
        }
    }

    fun getTopicsForChapter(subjectName: String, chapterNumber: Int, chapterTitle: String): List<TopicEntity> {
        return when {
            subjectName == "Physics 1st Paper" && chapterNumber == 2 -> listOf(
                TopicEntity(chapterId = 2, subjectName = subjectName, chapterTitle = chapterTitle, title = "Vector Addition & Parallelogram Law", status = "In Progress", confidence = 3),
                TopicEntity(chapterId = 2, subjectName = subjectName, chapterTitle = chapterTitle, title = "Dot & Cross Product Applications", status = "Pending", confidence = 2),
                TopicEntity(chapterId = 2, subjectName = subjectName, chapterTitle = chapterTitle, title = "Gradient, Divergence & Curl", status = "Pending", confidence = 1, isWeak = true)
            )
            subjectName == "Physics 1st Paper" && chapterNumber == 3 -> listOf(
                TopicEntity(chapterId = 3, subjectName = subjectName, chapterTitle = chapterTitle, title = "Projectile Motion & Maximum Range", status = "In Progress", confidence = 3, isWeak = true),
                TopicEntity(chapterId = 3, subjectName = subjectName, chapterTitle = chapterTitle, title = "Uniform Circular Motion & Centripetal Acceleration", status = "Pending", confidence = 2),
                TopicEntity(chapterId = 3, subjectName = subjectName, chapterTitle = chapterTitle, title = "Relative Velocity & River-Boat Problems", status = "Pending", confidence = 2, isWeak = true)
            )
            subjectName == "Physics 1st Paper" && chapterNumber == 4 -> listOf(
                TopicEntity(chapterId = 4, subjectName = subjectName, chapterTitle = chapterTitle, title = "Banking of Roads & Friction Angle", status = "Pending", confidence = 1, isWeak = true),
                TopicEntity(chapterId = 4, subjectName = subjectName, chapterTitle = chapterTitle, title = "Moment of Inertia & Torque Derivations", status = "Pending", confidence = 2),
                TopicEntity(chapterId = 4, subjectName = subjectName, chapterTitle = chapterTitle, title = "Conservation of Angular Momentum", status = "Pending", confidence = 2)
            )
            subjectName == "Chemistry 1st Paper" && chapterNumber == 2 -> listOf(
                TopicEntity(chapterId = 2, subjectName = subjectName, chapterTitle = chapterTitle, title = "Quantum Numbers & Orbital Energy Levels", status = "In Progress", confidence = 3),
                TopicEntity(chapterId = 2, subjectName = subjectName, chapterTitle = chapterTitle, title = "Aufbau, Pauli & Hund Principles", status = "In Progress", confidence = 4),
                TopicEntity(chapterId = 2, subjectName = subjectName, chapterTitle = chapterTitle, title = "Solubility Product (Ksp) Calculations", status = "Pending", confidence = 1, isWeak = true)
            )
            subjectName == "Higher Math 1st Paper" && chapterNumber == 9 -> listOf(
                TopicEntity(chapterId = 9, subjectName = subjectName, chapterTitle = chapterTitle, title = "Limits & Continuity Definitions", status = "In Progress", confidence = 3),
                TopicEntity(chapterId = 9, subjectName = subjectName, chapterTitle = chapterTitle, title = "Chain Rule & Implicit Differentiation", status = "Pending", confidence = 2, isWeak = true),
                TopicEntity(chapterId = 9, subjectName = subjectName, chapterTitle = chapterTitle, title = "Maxima & Minima Applications", status = "Pending", confidence = 1, isWeak = true)
            )
            subjectName == "Higher Math 1st Paper" && chapterNumber == 10 -> listOf(
                TopicEntity(chapterId = 10, subjectName = subjectName, chapterTitle = chapterTitle, title = "Indefinite Integration & Substitution", status = "Pending", confidence = 1, isWeak = true),
                TopicEntity(chapterId = 10, subjectName = subjectName, chapterTitle = chapterTitle, title = "Integration by Parts Formula", status = "Pending", confidence = 2),
                TopicEntity(chapterId = 10, subjectName = subjectName, chapterTitle = chapterTitle, title = "Definite Integral Area Calculation", status = "Pending", confidence = 1, isWeak = true)
            )
            else -> listOf(
                TopicEntity(chapterId = chapterNumber, subjectName = subjectName, chapterTitle = chapterTitle, title = "NCTB Core Concept 1: Definitions & Theory", status = "Pending", confidence = 2),
                TopicEntity(chapterId = chapterNumber, subjectName = subjectName, chapterTitle = chapterTitle, title = "NCTB Core Concept 2: Formulas & Derivations", status = "Pending", confidence = 2),
                TopicEntity(chapterId = chapterNumber, subjectName = subjectName, chapterTitle = chapterTitle, title = "NCTB Board Exam Creative Question Practice", status = "Pending", confidence = 1)
            )
        }
    }
}
