package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SapRepository
import com.example.data.local.AppDatabase
import com.example.data.local.BannerEntity
import com.example.data.local.CoachingInfoEntity
import com.example.data.local.CourseClassEntity
import com.example.data.local.ExperimentPhotoEntity
import com.example.data.local.QuestionEntity
import com.example.data.local.StudentEntity
import com.example.data.local.TestPaperEntity
import com.example.data.local.TestSubmissionEntity
import com.example.data.local.TeacherEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
  object Home : Screen()
  object AdminLogin : Screen()
  data class AdminDashboard(val section: AdminSection = AdminSection.OVERVIEW) : Screen()
  object StudentAuth : Screen()
  object StudentPortal : Screen()
  data class Exam(val paperId: Long) : Screen()
  object SubmissionConfirmation : Screen()
}

enum class AdminSection(val title: String, val iconName: String) {
  OVERVIEW("Overview", "dashboard"),
  PROMOTIONS("Promotions & Banners", "campaign"),
  TEACHERS("Teacher Management", "group"),
  GALLERY("Experiment Moments", "photo_library"),
  CLASSES("Classes We Teach", "school"),
  FOUNDERS("Founder & Co-founder", "person"),
  ADDRESS("Coaching Address", "location_on"),
  POLICIES("Policies & Contact", "policy"),
  TEST_PAPERS("Test Papers", "quiz"),
  STUDENTS("Student Accounts", "badge"),
  RESULTS("Test Results", "assessment"),
  SETTINGS("Security & Settings", "security")
}

class SapViewModel(application: Application) : AndroidViewModel(application) {

  val repository: SapRepository

  init {
    val db = AppDatabase.getDatabase(application, viewModelScope)
    repository = SapRepository(db.sapDao())
  }

  // --- Navigation Stack ---
  private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))
  val currentScreen: StateFlow<Screen> = MutableStateFlow<Screen>(Screen.Home).apply {
    viewModelScope.launch {
      _screenStack.collect { stack ->
        value = stack.lastOrNull() ?: Screen.Home
      }
    }
  }

  fun navigateTo(screen: Screen) {
    val current = _screenStack.value
    if (current.lastOrNull() != screen) {
      _screenStack.value = current + screen
    }
  }

  fun popBack(): Boolean {
    val current = _screenStack.value
    return if (current.size > 1) {
      _screenStack.value = current.dropLast(1)
      true
    } else {
      false
    }
  }

  fun navigateToHome() {
    _screenStack.value = listOf(Screen.Home)
  }

  // --- Public Flows ---
  val banners: StateFlow<List<BannerEntity>> = repository.activeBannersFlow.stateIn(
    viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
  )
  val allBanners: StateFlow<List<BannerEntity>> = repository.bannersFlow.stateIn(
    viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
  )
  val teachers: StateFlow<List<TeacherEntity>> = repository.teachersFlow.stateIn(
    viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
  )
  val experimentPhotos: StateFlow<List<ExperimentPhotoEntity>> = repository.experimentPhotosFlow.stateIn(
    viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
  )
  val courses: StateFlow<List<CourseClassEntity>> = repository.coursesFlow.stateIn(
    viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
  )
  val coachingInfo: StateFlow<CoachingInfoEntity?> = repository.coachingInfoFlow.stateIn(
    viewModelScope, SharingStarted.WhileSubscribed(5000), null
  )

  // --- Admin State ---
  private val _isAdminLoggedIn = MutableStateFlow(false)
  val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

  private val _adminLoginError = MutableStateFlow<String?>(null)
  val adminLoginError: StateFlow<String?> = _adminLoginError.asStateFlow()

  val allPapers: StateFlow<List<TestPaperEntity>> = repository.allPapersFlow.stateIn(
    viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
  )
  val allStudents: StateFlow<List<StudentEntity>> = repository.allStudentsFlow.stateIn(
    viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
  )
  val allSubmissions: StateFlow<List<TestSubmissionEntity>> = repository.allSubmissionsFlow.stateIn(
    viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
  )

  fun loginAdmin(usernameOrEmail: String, passwordAttempt: String) {
    viewModelScope.launch {
      _adminLoginError.value = null
      val verified = repository.verifyAdmin(usernameOrEmail.trim(), passwordAttempt)
      if (verified) {
        _isAdminLoggedIn.value = true
        navigateTo(Screen.AdminDashboard(AdminSection.OVERVIEW))
      } else {
        _adminLoginError.value = "Invalid administrator credentials. Please check your username/email and password."
      }
    }
  }

  fun logoutAdmin() {
    _isAdminLoggedIn.value = false
    navigateToHome()
  }

  // --- Student State ---
  private val _currentStudent = MutableStateFlow<StudentEntity?>(null)
  val currentStudent: StateFlow<StudentEntity?> = _currentStudent.asStateFlow()

  private val _studentAuthError = MutableStateFlow<String?>(null)
  val studentAuthError: StateFlow<String?> = _studentAuthError.asStateFlow()

  private val _studentAuthSuccess = MutableStateFlow<String?>(null)
  val studentAuthSuccess: StateFlow<String?> = _studentAuthSuccess.asStateFlow()

  val publishedPapers: StateFlow<List<TestPaperEntity>> = repository.publishedPapersFlow.stateIn(
    viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
  )

  fun registerStudent(name: String, mobile: String, pass: String, confirmPass: String) {
    viewModelScope.launch {
      _studentAuthError.value = null
      _studentAuthSuccess.value = null
      if (name.isBlank()) {
        _studentAuthError.value = "Please enter student's full name."
        return@launch
      }
      if (pass != confirmPass) {
        _studentAuthError.value = "Passwords do not match."
        return@launch
      }
      when (val result = repository.registerStudent(name, mobile, pass)) {
        is SapRepository.StudentAuthResult.Success -> {
          _currentStudent.value = result.student
          _studentAuthSuccess.value = "Registration successful! Welcome, ${result.student.fullName}"
          navigateTo(Screen.StudentPortal)
        }
        is SapRepository.StudentAuthResult.Error -> {
          _studentAuthError.value = result.message
        }
      }
    }
  }

  fun loginStudent(mobile: String, pass: String) {
    viewModelScope.launch {
      _studentAuthError.value = null
      _studentAuthSuccess.value = null
      when (val result = repository.loginStudent(mobile, pass)) {
        is SapRepository.StudentAuthResult.Success -> {
          _currentStudent.value = result.student
          navigateTo(Screen.StudentPortal)
        }
        is SapRepository.StudentAuthResult.Error -> {
          _studentAuthError.value = result.message
        }
      }
    }
  }

  fun logoutStudent() {
    _currentStudent.value = null
    navigateToHome()
  }

  // --- Active Objective Examination State ---
  private val _activePaper = MutableStateFlow<TestPaperEntity?>(null)
  val activePaper: StateFlow<TestPaperEntity?> = _activePaper.asStateFlow()

  private val _examQuestions = MutableStateFlow<List<QuestionEntity>>(emptyList())
  val examQuestions: StateFlow<List<QuestionEntity>> = _examQuestions.asStateFlow()

  private val _currentQuestionIndex = MutableStateFlow(0)
  val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

  // Map of questionId -> student selected option ("A", "B", "C", "D")
  private val _studentAnswers = MutableStateFlow<Map<Long, String>>(emptyMap())
  val studentAnswers: StateFlow<Map<Long, String>> = _studentAnswers.asStateFlow()

  private val _remainingSeconds = MutableStateFlow(0)
  val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

  private var timerJob: Job? = null

  private val _isSubmitting = MutableStateFlow(false)
  val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

  fun startExam(paperId: Long) {
    viewModelScope.launch {
      val paper = repository.getPaperById(paperId) ?: return@launch
      val questions = repository.getQuestionsDirect(paperId)
      if (questions.isEmpty()) return@launch

      _activePaper.value = paper
      _examQuestions.value = questions
      _currentQuestionIndex.value = 0
      _studentAnswers.value = emptyMap()
      _isSubmitting.value = false

      if (paper.durationMinutes > 0) {
        _remainingSeconds.value = paper.durationMinutes * 60
        startTimer()
      } else {
        _remainingSeconds.value = 0
        timerJob?.cancel()
      }

      navigateTo(Screen.Exam(paperId))
    }
  }

  private fun startTimer() {
    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      while (_remainingSeconds.value > 0) {
        delay(1000L)
        _remainingSeconds.value -= 1
      }
      // Auto-submit when timer expires
      if (!_isSubmitting.value && _activePaper.value != null) {
        submitExam()
      }
    }
  }

  fun selectAnswer(questionId: Long, option: String) {
    val currentMap = _studentAnswers.value.toMutableMap()
    currentMap[questionId] = option
    _studentAnswers.value = currentMap
  }

  fun clearAnswer(questionId: Long) {
    val currentMap = _studentAnswers.value.toMutableMap()
    currentMap.remove(questionId)
    _studentAnswers.value = currentMap
  }

  fun goToNextQuestion() {
    val nextIndex = _currentQuestionIndex.value + 1
    if (nextIndex < _examQuestions.value.size) {
      _currentQuestionIndex.value = nextIndex
    }
  }

  fun goToPreviousQuestion() {
    val prevIndex = _currentQuestionIndex.value - 1
    if (prevIndex >= 0) {
      _currentQuestionIndex.value = prevIndex
    }
  }

  fun jumpToQuestion(index: Int) {
    if (index in 0 until _examQuestions.value.size) {
      _currentQuestionIndex.value = index
    }
  }

  fun submitExam() {
    if (_isSubmitting.value) return
    _isSubmitting.value = true
    timerJob?.cancel()

    val student = _currentStudent.value
    val paper = _activePaper.value

    if (student != null && paper != null) {
      viewModelScope.launch {
        repository.submitObjectiveExam(
          paperId = paper.id,
          studentId = student.id,
          studentName = student.fullName,
          studentMobile = student.mobileNumber,
          studentAnswers = _studentAnswers.value
        )
        _isSubmitting.value = false
        // Navigate strictly to the Student Post-Submission Thank You screen
        // (NO marks, NO answer key, NO score shown to student!)
        _screenStack.value = listOf(Screen.Home, Screen.StudentPortal, Screen.SubmissionConfirmation)
      }
    } else {
      _isSubmitting.value = false
    }
  }

  // --- Dialog & Inspection States ---
  var selectedTeacherDetail = MutableStateFlow<TeacherEntity?>(null)
  var selectedExperimentPhoto = MutableStateFlow<ExperimentPhotoEntity?>(null)
  var showPrivacyPolicyDialog = MutableStateFlow(false)
  var showTermsDialog = MutableStateFlow(false)

  // --- Admin Inspection of a result ---
  var selectedSubmissionForDetail = MutableStateFlow<TestSubmissionEntity?>(null)
}
