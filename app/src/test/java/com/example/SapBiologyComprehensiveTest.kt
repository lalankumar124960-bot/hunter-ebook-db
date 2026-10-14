package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.SapRepository
import com.example.data.local.AdminUserEntity
import com.example.data.local.AppDatabase
import com.example.data.local.QuestionEntity
import com.example.data.local.StudentEntity
import com.example.data.local.TeacherEntity
import com.example.data.local.TestPaperEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SapBiologyComprehensiveTest {

  private lateinit var db: AppDatabase
  private lateinit var repository: SapRepository

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    repository = SapRepository(db.sapDao())
  }

  @After
  fun tearDown() {
    db.close()
  }

  @Test
  fun testAppNameResource() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Sap Biology", appName)
  }

  @Test
  fun testStudentRegistrationAndDuplicatePrevention() = runBlocking {
    // 1. Valid registration
    val reg1 = repository.registerStudent("Rohan Kumar", "9876543210", "pass123")
    assertTrue(reg1 is SapRepository.StudentAuthResult.Success)

    // 2. Duplicate registration with same mobile number must be rejected
    val reg2 = repository.registerStudent("Another Rohan", "9876543210", "differentPass")
    assertTrue(reg2 is SapRepository.StudentAuthResult.Error)

    // 3. Login with correct password
    val loginSuccess = repository.loginStudent("9876543210", "pass123")
    assertTrue(loginSuccess is SapRepository.StudentAuthResult.Success)

    // 4. Login with wrong password
    val loginWrong = repository.loginStudent("9876543210", "wrongPass")
    assertTrue(loginWrong is SapRepository.StudentAuthResult.Error)
  }

  @Test
  fun testObjectiveExamEvaluationAndMarkingRules() = runBlocking {
    // Create a Test Paper with 4 questions
    val paperId = repository.saveTestPaper(
      TestPaperEntity(
        title = "Cytology Speed Test",
        subject = "Biology",
        durationMinutes = 10,
        totalQuestions = 4,
        isPublished = true
      )
    )

    val q1Id = repository.saveQuestion(
      QuestionEntity(
        paperId = paperId,
        questionNumber = 1,
        questionText = "Suicide bags of the cell?",
        optionA = "Ribosome",
        optionB = "Lysosome",
        optionC = "Vacuole",
        optionD = "Centrosome",
        correctAnswer = "B"
      )
    )

    val q2Id = repository.saveQuestion(
      QuestionEntity(
        paperId = paperId,
        questionNumber = 2,
        questionText = "Powerhouse of the cell?",
        optionA = "Mitochondria",
        optionB = "Golgi",
        optionC = "Plastid",
        optionD = "Nucleus",
        correctAnswer = "A"
      )
    )

    val q3Id = repository.saveQuestion(
      QuestionEntity(
        paperId = paperId,
        questionNumber = 3,
        questionText = "Fluid mosaic model proposed by?",
        optionA = "Watson and Crick",
        optionB = "Singer and Nicolson",
        optionC = "Robert Hooke",
        optionD = "Schwann",
        correctAnswer = "B"
      )
    )

    val q4Id = repository.saveQuestion(
      QuestionEntity(
        paperId = paperId,
        questionNumber = 4,
        questionText = "Sites of protein synthesis?",
        optionA = "Ribosomes",
        optionB = "Peroxisomes",
        optionC = "Lysosomes",
        optionD = "Centrioles",
        correctAnswer = "A"
      )
    )

    // Student Answers:
    // Q1: B (Correct) -> 1 mark
    // Q2: C (Incorrect) -> 0 marks
    // Q3: B (Correct) -> 1 mark
    // Q4: "" (Unanswered) -> 0 marks
    val studentAnswers = mapOf(
      q1Id to "B",
      q2Id to "C",
      q3Id to "B"
      // q4 is left unanswered
    )

    val submission = repository.submitObjectiveExam(
      paperId = paperId,
      studentId = 1L,
      studentName = "Priya Kumari",
      studentMobile = "9123456780",
      studentAnswers = studentAnswers
    )

    // Verification according to exact marking rules:
    // Correct answers = 2
    // Incorrect answers = 1
    // Unanswered = 1
    // Total questions = 4
    // Score = 2 marks (1 mark per correct answer, 0 for incorrect, 0 for unanswered, no negative marking)
    // Percentage = (2 / 4) * 100 = 50.0%
    assertEquals(4, submission.totalQuestions)
    assertEquals(2, submission.correctCount)
    assertEquals(1, submission.incorrectCount)
    assertEquals(1, submission.unansweredCount)
    assertEquals(3, submission.attemptedCount)
    assertEquals(2, submission.score)
    assertEquals(50.0f, submission.percentage, 0.01f)
  }

  @Test
  fun testAdminVerificationAndSecuritySettings() = runBlocking {
    db.sapDao().insertAdminUser(
      AdminUserEntity(
        id = 1,
        username = "admin",
        email = "admin@sapbiology.com",
        passwordHash = "admin123"
      )
    )

    // Correct verification
    val success = repository.verifyAdmin("admin@sapbiology.com", "admin123")
    assertTrue(success)

    // Wrong password
    val fail = repository.verifyAdmin("admin@sapbiology.com", "incorrect")
    assertFalse(fail)

    // Update password
    repository.updateAdminPassword("newSecurePass99", "admin@sapbiology.com")
    val updatedSuccess = repository.verifyAdmin("admin@sapbiology.com", "newSecurePass99")
    assertTrue(updatedSuccess)
  }

  @Test
  fun testTeacherManagementCrud() = runBlocking {
    val teacherId = repository.daoInsertDirect(
      TeacherEntity(
        name = "Dr. Test Mentor",
        subject = "Botany",
        experience = "10 Years",
        shortIntro = "Expert",
        bio = "Detailed bio"
      )
    )
    val fetched = db.sapDao().getTeacherById(teacherId)
    assertNotNull(fetched)
    assertEquals("Dr. Test Mentor", fetched!!.name)

    repository.deleteTeacher(teacherId)
    val afterDelete = db.sapDao().getTeacherById(teacherId)
    assertEquals(null, afterDelete)
  }
}

// Extension helper for test setup
suspend fun SapRepository.daoInsertDirect(teacher: TeacherEntity): Long {
  this.saveTeacher(teacher)
  return 1L
}
