package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "banners")
data class BannerEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val subtitle: String,
  val tag: String,
  val actionText: String = "Explore Batch",
  val imageUrl: String = "",
  val orderIndex: Int = 0,
  val isActive: Boolean = true
)

@Entity(tableName = "teachers")
data class TeacherEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String,
  val subject: String,
  val experience: String,
  val shortIntro: String,
  val bio: String,
  val avatarUrl: String = "",
  val avatarSeed: String = "bio_mentor",
  val orderIndex: Int = 0
)

@Entity(tableName = "experiment_photos")
data class ExperimentPhotoEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val caption: String,
  val tag: String,
  val photoSeed: String, // e.g. "mitosis", "chromatography", "dna_spooling", "plasmolysis", "stomata", "blood_cells"
  val customUrl: String = "", // Holds picked photo URI or external URL
  val orderIndex: Int = 0,
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "classes_courses")
data class CourseClassEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val grade: String,
  val targetAudience: String,
  val description: String,
  val featuresList: String, // Semicolon separated
  val timings: String,
  val orderIndex: Int = 0
)

@Entity(tableName = "coaching_info")
data class CoachingInfoEntity(
  @PrimaryKey val id: Int = 1,
  val instituteName: String = "Sap Biology",
  val tagline: String = "Excellence in Medical & Botanical Sciences",
  val locality: String = "Vidya Nagar, Opposite Science College Gate",
  val city: String = "Patna",
  val district: String = "Patna",
  val state: String = "Bihar",
  val pinCode: String = "800001",
  val phone: String = "+91 98765 43210",
  val email: String = "admissions@sapbiology.com",
  val googleMapsUrl: String = "https://maps.google.com/?q=Patna+Science+College",
  val founderName: String = "Dr. S. K. Anand",
  val founderRole: String = "Founder & Senior Biology Mentor",
  val founderBio: String = "M.Sc., Ph.D. in Botanical Sciences with 18+ years of dedicated mentoring for NEET aspirants. Renowned for simplifying complex physiological pathways.",
  val founderPhotoUrl: String = "",
  val founderPhotoSeed: String = "founder_male",
  val cofounderName: String = "Prof. Pooja Verma",
  val cofounderRole: String = "Co-Founder & Academic Coordinator",
  val cofounderBio: String = "M.Sc. Zoology, specialized in Animal Physiology and Genetics. Passionate educator designing outcome-based medical entrance preparatory courses.",
  val cofounderPhotoUrl: String = "",
  val cofounderPhotoSeed: String = "cofounder_female",
  val showCofounder: Boolean = true,
  val privacyPolicy: String = "Sap Biology values your privacy. Student records, contact numbers, and test responses are securely handled strictly for academic tracking and coaching communications. We never sell student data.",
  val termsAndConditions: String = "All questions, mock tests, and educational materials provided on this platform are proprietary to Sap Biology. Students must adhere to ethical conduct during objective examinations.",
  val copyrightText: String = "© 2026 Sap Biology Institute. All rights reserved."
)

@Entity(
  tableName = "students",
  indices = [Index(value = ["mobileNumber"], unique = true)]
)
data class StudentEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val fullName: String,
  val mobileNumber: String,
  val passwordHash: String,
  val registeredAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "test_papers")
data class TestPaperEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val subject: String,
  val instructions: String = "Each question carries 1 mark. There is no negative marking. Choose the most appropriate option A, B, C or D.",
  val durationMinutes: Int = 20, // 0 means untimed
  val totalQuestions: Int = 0,
  val isPublished: Boolean = true,
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "questions")
data class QuestionEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val paperId: Long,
  val questionNumber: Int,
  val questionText: String,
  val optionA: String,
  val optionB: String,
  val optionC: String,
  val optionD: String,
  val correctAnswer: String, // "A", "B", "C", "D"
  val explanation: String = ""
)

@Entity(tableName = "test_submissions")
data class TestSubmissionEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val paperId: Long,
  val studentId: Long,
  val studentName: String,
  val studentMobile: String,
  val subject: String,
  val paperTitle: String,
  val totalQuestions: Int,
  val attemptedCount: Int,
  val unansweredCount: Int,
  val correctCount: Int,
  val incorrectCount: Int,
  val score: Int,
  val percentage: Float,
  val answersJson: String, // "{\"qId\":\"A\",...}"
  val submittedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "admin_users")
data class AdminUserEntity(
  @PrimaryKey val id: Long = 1,
  val username: String = "admin",
  val email: String = "admin@sapbiology.com",
  val passwordHash: String
)
