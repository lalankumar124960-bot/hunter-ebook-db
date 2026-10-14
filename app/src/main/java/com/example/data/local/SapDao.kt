package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SapDao {

  // --- Banners ---
  @Query("SELECT * FROM banners ORDER BY orderIndex ASC, id DESC")
  fun getBannersFlow(): Flow<List<BannerEntity>>

  @Query("SELECT * FROM banners WHERE isActive = 1 ORDER BY orderIndex ASC, id DESC")
  fun getActiveBannersFlow(): Flow<List<BannerEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBanner(banner: BannerEntity): Long

  @Update
  suspend fun updateBanner(banner: BannerEntity)

  @Query("DELETE FROM banners WHERE id = :id")
  suspend fun deleteBannerById(id: Long)

  // --- Teachers ---
  @Query("SELECT * FROM teachers ORDER BY orderIndex ASC, id ASC")
  fun getTeachersFlow(): Flow<List<TeacherEntity>>

  @Query("SELECT * FROM teachers WHERE id = :id")
  suspend fun getTeacherById(id: Long): TeacherEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTeacher(teacher: TeacherEntity): Long

  @Update
  suspend fun updateTeacher(teacher: TeacherEntity)

  @Query("DELETE FROM teachers WHERE id = :id")
  suspend fun deleteTeacherById(id: Long)

  // --- Experiment Photos ---
  @Query("SELECT * FROM experiment_photos ORDER BY orderIndex ASC, id DESC")
  fun getExperimentPhotosFlow(): Flow<List<ExperimentPhotoEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertExperimentPhoto(photo: ExperimentPhotoEntity): Long

  @Update
  suspend fun updateExperimentPhoto(photo: ExperimentPhotoEntity)

  @Query("DELETE FROM experiment_photos WHERE id = :id")
  suspend fun deleteExperimentPhotoById(id: Long)

  // --- Courses & Classes ---
  @Query("SELECT * FROM classes_courses ORDER BY orderIndex ASC, id ASC")
  fun getCoursesFlow(): Flow<List<CourseClassEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCourse(course: CourseClassEntity): Long

  @Update
  suspend fun updateCourse(course: CourseClassEntity)

  @Query("DELETE FROM classes_courses WHERE id = :id")
  suspend fun deleteCourseById(id: Long)

  // --- Coaching Info ---
  @Query("SELECT * FROM coaching_info WHERE id = 1")
  fun getCoachingInfoFlow(): Flow<CoachingInfoEntity?>

  @Query("SELECT * FROM coaching_info WHERE id = 1")
  suspend fun getCoachingInfoDirect(): CoachingInfoEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateCoachingInfo(info: CoachingInfoEntity)

  // --- Students ---
  @Query("SELECT * FROM students WHERE mobileNumber = :mobile LIMIT 1")
  suspend fun getStudentByMobile(mobile: String): StudentEntity?

  @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
  suspend fun getStudentById(id: Long): StudentEntity?

  @Insert(onConflict = OnConflictStrategy.ABORT)
  suspend fun insertStudent(student: StudentEntity): Long

  @Query("SELECT * FROM students ORDER BY registeredAt DESC")
  fun getAllStudentsFlow(): Flow<List<StudentEntity>>

  @Query("SELECT COUNT(*) FROM students")
  fun getStudentCountFlow(): Flow<Int>

  // --- Test Papers ---
  @Query("SELECT * FROM test_papers WHERE isPublished = 1 ORDER BY id DESC")
  fun getPublishedPapersFlow(): Flow<List<TestPaperEntity>>

  @Query("SELECT * FROM test_papers ORDER BY id DESC")
  fun getAllPapersFlow(): Flow<List<TestPaperEntity>>

  @Query("SELECT * FROM test_papers WHERE id = :paperId")
  suspend fun getPaperById(paperId: Long): TestPaperEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPaper(paper: TestPaperEntity): Long

  @Update
  suspend fun updatePaper(paper: TestPaperEntity)

  @Query("DELETE FROM test_papers WHERE id = :paperId")
  suspend fun deletePaperById(paperId: Long)

  // --- Questions ---
  @Query("SELECT * FROM questions WHERE paperId = :paperId ORDER BY questionNumber ASC")
  fun getQuestionsForPaperFlow(paperId: Long): Flow<List<QuestionEntity>>

  @Query("SELECT * FROM questions WHERE paperId = :paperId ORDER BY questionNumber ASC")
  suspend fun getQuestionsForPaperDirect(paperId: Long): List<QuestionEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuestion(question: QuestionEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuestions(questions: List<QuestionEntity>)

  @Update
  suspend fun updateQuestion(question: QuestionEntity)

  @Query("DELETE FROM questions WHERE id = :questionId")
  suspend fun deleteQuestionById(questionId: Long)

  @Query("DELETE FROM questions WHERE paperId = :paperId")
  suspend fun deleteQuestionsForPaper(paperId: Long)

  // --- Submissions & Results ---
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSubmission(submission: TestSubmissionEntity): Long

  @Query("SELECT * FROM test_submissions ORDER BY submittedAt DESC")
  fun getAllSubmissionsFlow(): Flow<List<TestSubmissionEntity>>

  @Query("SELECT * FROM test_submissions WHERE studentId = :studentId ORDER BY submittedAt DESC")
  fun getSubmissionsByStudentFlow(studentId: Long): Flow<List<TestSubmissionEntity>>

  @Query("SELECT * FROM test_submissions WHERE id = :id LIMIT 1")
  suspend fun getSubmissionById(id: Long): TestSubmissionEntity?

  @Query("SELECT COUNT(*) FROM test_submissions")
  fun getSubmissionCountFlow(): Flow<Int>

  // --- Admin ---
  @Query("SELECT * FROM admin_users WHERE id = 1 LIMIT 1")
  suspend fun getAdminUser(): AdminUserEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAdminUser(admin: AdminUserEntity)

  @Update
  suspend fun updateAdminUser(admin: AdminUserEntity)
}
