package com.example.data

import com.example.data.local.AdminUserEntity
import com.example.data.local.BannerEntity
import com.example.data.local.CoachingInfoEntity
import com.example.data.local.CourseClassEntity
import com.example.data.local.ExperimentPhotoEntity
import com.example.data.local.QuestionEntity
import com.example.data.local.SapDao
import com.example.data.local.StudentEntity
import com.example.data.local.TeacherEntity
import com.example.data.local.TestPaperEntity
import com.example.data.local.TestSubmissionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SapRepository(private val dao: SapDao) {

  // --- Public / Home Flows ---
  val bannersFlow: Flow<List<BannerEntity>> = dao.getBannersFlow()
  val activeBannersFlow: Flow<List<BannerEntity>> = dao.getActiveBannersFlow()
  val teachersFlow: Flow<List<TeacherEntity>> = dao.getTeachersFlow()
  val experimentPhotosFlow: Flow<List<ExperimentPhotoEntity>> = dao.getExperimentPhotosFlow()
  val coursesFlow: Flow<List<CourseClassEntity>> = dao.getCoursesFlow()
  val coachingInfoFlow: Flow<CoachingInfoEntity?> = dao.getCoachingInfoFlow()

  // --- Test Papers Flows ---
  val publishedPapersFlow: Flow<List<TestPaperEntity>> = dao.getPublishedPapersFlow()
  val allPapersFlow: Flow<List<TestPaperEntity>> = dao.getAllPapersFlow()
  val allStudentsFlow: Flow<List<StudentEntity>> = dao.getAllStudentsFlow()
  val allSubmissionsFlow: Flow<List<TestSubmissionEntity>> = dao.getAllSubmissionsFlow()

  // --- Coaching Info ---
  suspend fun getCoachingInfo(): CoachingInfoEntity {
    return withContext(Dispatchers.IO) {
      dao.getCoachingInfoDirect() ?: CoachingInfoEntity(id = 1)
    }
  }

  suspend fun updateCoachingInfo(info: CoachingInfoEntity) {
    withContext(Dispatchers.IO) {
      dao.insertOrUpdateCoachingInfo(info)
    }
  }

  // --- Banners CRUD ---
  suspend fun saveBanner(banner: BannerEntity) {
    withContext(Dispatchers.IO) {
      if (banner.id == 0L) {
        dao.insertBanner(banner)
      } else {
        dao.updateBanner(banner)
      }
    }
  }

  suspend fun deleteBanner(id: Long) {
    withContext(Dispatchers.IO) {
      dao.deleteBannerById(id)
    }
  }

  // --- Teachers CRUD ---
  suspend fun saveTeacher(teacher: TeacherEntity) {
    withContext(Dispatchers.IO) {
      if (teacher.id == 0L) {
        dao.insertTeacher(teacher)
      } else {
        dao.updateTeacher(teacher)
      }
    }
  }

  suspend fun deleteTeacher(id: Long) {
    withContext(Dispatchers.IO) {
      dao.deleteTeacherById(id)
    }
  }

  // --- Experiment Photos CRUD ---
  suspend fun saveExperimentPhoto(photo: ExperimentPhotoEntity) {
    withContext(Dispatchers.IO) {
      if (photo.id == 0L) {
        dao.insertExperimentPhoto(photo)
      } else {
        dao.updateExperimentPhoto(photo)
      }
    }
  }

  suspend fun deleteExperimentPhoto(id: Long) {
    withContext(Dispatchers.IO) {
      dao.deleteExperimentPhotoById(id)
    }
  }

  // --- Courses CRUD ---
  suspend fun saveCourse(course: CourseClassEntity) {
    withContext(Dispatchers.IO) {
      if (course.id == 0L) {
        dao.insertCourse(course)
      } else {
        dao.updateCourse(course)
      }
    }
  }

  suspend fun deleteCourse(id: Long) {
    withContext(Dispatchers.IO) {
      dao.deleteCourseById(id)
    }
  }

  // --- Admin Auth ---
  suspend fun verifyAdmin(identifier: String, passwordAttempt: String): Boolean {
    return withContext(Dispatchers.IO) {
      val admin = dao.getAdminUser()
      if (admin == null) {
        // Fallback default admin credentials
        (identifier == "admin" || identifier == "admin@sapbiology.com") && passwordAttempt == "admin123"
      } else {
        (identifier.equals(admin.username, ignoreCase = true) ||
          identifier.equals(admin.email, ignoreCase = true)) &&
          passwordAttempt == admin.passwordHash
      }
    }
  }

  suspend fun updateAdminPassword(newPassword: String, newEmail: String?): Boolean {
    return withContext(Dispatchers.IO) {
      val current = dao.getAdminUser() ?: AdminUserEntity(id = 1, passwordHash = "admin123")
      val updated = current.copy(
        passwordHash = newPassword,
        email = newEmail ?: current.email
      )
      dao.insertAdminUser(updated)
      true
    }
  }

  // --- Student Auth ---
  sealed class StudentAuthResult {
    data class Success(val student: StudentEntity) : StudentAuthResult()
    data class Error(val message: String) : StudentAuthResult()
  }

  suspend fun registerStudent(fullName: String, mobileNumber: String, password: String): StudentAuthResult {
    return withContext(Dispatchers.IO) {
      val trimmedMobile = mobileNumber.trim().replace(" ", "").replace("+91", "")
      if (trimmedMobile.length < 10) {
        return@withContext StudentAuthResult.Error("Please enter a valid 10-digit mobile number.")
      }
      if (password.length < 4) {
        return@withContext StudentAuthResult.Error("Password must be at least 4 characters.")
      }

      val existing = dao.getStudentByMobile(trimmedMobile)
      if (existing != null) {
        return@withContext StudentAuthResult.Error("An account with this mobile number already exists. Please log in.")
      }

      val newStudent = StudentEntity(
        fullName = fullName.trim(),
        mobileNumber = trimmedMobile,
        passwordHash = password
      )
      try {
        val id = dao.insertStudent(newStudent)
        StudentAuthResult.Success(newStudent.copy(id = id))
      } catch (e: Exception) {
        StudentAuthResult.Error("Registration failed: ${e.localizedMessage}")
      }
    }
  }

  suspend fun loginStudent(mobileNumber: String, password: String): StudentAuthResult {
    return withContext(Dispatchers.IO) {
      val trimmedMobile = mobileNumber.trim().replace(" ", "").replace("+91", "")
      val student = dao.getStudentByMobile(trimmedMobile)
      if (student == null) {
        return@withContext StudentAuthResult.Error("No account found with this mobile number. Please register first.")
      }
      if (student.passwordHash != password) {
        return@withContext StudentAuthResult.Error("Incorrect password. Please try again.")
      }
      StudentAuthResult.Success(student)
    }
  }

  // --- Test Paper Management ---
  suspend fun saveTestPaper(paper: TestPaperEntity): Long {
    return withContext(Dispatchers.IO) {
      if (paper.id == 0L) {
        dao.insertPaper(paper)
      } else {
        dao.updatePaper(paper)
        paper.id
      }
    }
  }

  suspend fun deleteTestPaper(paperId: Long) {
    withContext(Dispatchers.IO) {
      dao.deleteQuestionsForPaper(paperId)
      dao.deletePaperById(paperId)
    }
  }

  suspend fun getPaperById(paperId: Long): TestPaperEntity? {
    return withContext(Dispatchers.IO) {
      dao.getPaperById(paperId)
    }
  }

  fun getQuestionsFlow(paperId: Long): Flow<List<QuestionEntity>> {
    return dao.getQuestionsForPaperFlow(paperId)
  }

  suspend fun getQuestionsDirect(paperId: Long): List<QuestionEntity> {
    return withContext(Dispatchers.IO) {
      dao.getQuestionsForPaperDirect(paperId)
    }
  }

  suspend fun saveQuestion(question: QuestionEntity): Long {
    return withContext(Dispatchers.IO) {
      val qId = if (question.id == 0L) {
        dao.insertQuestion(question)
      } else {
        dao.updateQuestion(question)
        question.id
      }
      // Update total questions in paper
      val count = dao.getQuestionsForPaperDirect(question.paperId).size
      val paper = dao.getPaperById(question.paperId)
      if (paper != null) {
        dao.updatePaper(paper.copy(totalQuestions = count))
      }
      qId
    }
  }

  suspend fun deleteQuestion(questionId: Long, paperId: Long) {
    withContext(Dispatchers.IO) {
      dao.deleteQuestionById(questionId)
      val count = dao.getQuestionsForPaperDirect(paperId).size
      val paper = dao.getPaperById(paperId)
      if (paper != null) {
        dao.updatePaper(paper.copy(totalQuestions = count))
      }
    }
  }

  // --- Objective Test Submission & Marking Evaluation ---
  // Strictly evaluates score on backend: 1 mark per correct answer, 0 for incorrect/unanswered.
  // No negative marking.
  suspend fun submitObjectiveExam(
    paperId: Long,
    studentId: Long,
    studentName: String,
    studentMobile: String,
    studentAnswers: Map<Long, String> // QuestionId -> selected "A", "B", "C", "D"
  ): TestSubmissionEntity {
    return withContext(Dispatchers.IO) {
      val paper = dao.getPaperById(paperId) ?: throw IllegalArgumentException("Paper not found")
      val questions = dao.getQuestionsForPaperDirect(paperId)

      var correctCount = 0
      var incorrectCount = 0
      var unansweredCount = 0

      val answersJsonObj = JSONObject()

      for (q in questions) {
        val selected = studentAnswers[q.id]?.uppercase()?.trim() ?: ""
        answersJsonObj.put(q.id.toString(), selected)

        if (selected.isEmpty()) {
          unansweredCount++
        } else if (selected == q.correctAnswer.uppercase().trim()) {
          correctCount++
        } else {
          incorrectCount++
        }
      }

      val totalQuestions = questions.size
      val attemptedCount = correctCount + incorrectCount
      val score = correctCount // 1 mark each
      val percentage = if (totalQuestions > 0) (correctCount.toFloat() / totalQuestions) * 100f else 0f

      val submission = TestSubmissionEntity(
        paperId = paperId,
        studentId = studentId,
        studentName = studentName,
        studentMobile = studentMobile,
        subject = paper.subject,
        paperTitle = paper.title,
        totalQuestions = totalQuestions,
        attemptedCount = attemptedCount,
        unansweredCount = unansweredCount,
        correctCount = correctCount,
        incorrectCount = incorrectCount,
        score = score,
        percentage = percentage,
        answersJson = answersJsonObj.toString(),
        submittedAt = System.currentTimeMillis()
      )

      val subId = dao.insertSubmission(submission)
      submission.copy(id = subId)
    }
  }

  // CSV Generator for Admin Results Export
  fun generateResultsCsv(submissions: List<TestSubmissionEntity>): String {
    val sb = StringBuilder()
    sb.append("Submission ID,Student Name,Mobile Number,Subject,Paper Title,Date,Time,Total Questions,Attempted,Unanswered,Correct,Incorrect,Score,Max Marks,Percentage\n")
    val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd,HH:mm:ss", java.util.Locale.getDefault())

    for (s in submissions) {
      val dateFormatted = dateFormat.format(java.util.Date(s.submittedAt))
      val safeName = "\"${s.studentName.replace("\"", "\"\"")}\""
      val safeTitle = "\"${s.paperTitle.replace("\"", "\"\"")}\""
      val safeSubject = "\"${s.subject.replace("\"", "\"\"")}\""
      sb.append("${s.id},$safeName,${s.studentMobile},$safeSubject,$safeTitle,$dateFormatted,${s.totalQuestions},${s.attemptedCount},${s.unansweredCount},${s.correctCount},${s.incorrectCount},${s.score},${s.totalQuestions},${String.format(java.util.Locale.US, "%.1f", s.percentage)}%\n")
    }
    return sb.toString()
  }
}
