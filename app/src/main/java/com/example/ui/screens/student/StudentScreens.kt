package com.example.ui.screens.student

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuestionEntity
import com.example.data.local.TestPaperEntity
import com.example.ui.theme.BioEmeraldSecondary
import com.example.ui.theme.BioGreenContainer
import com.example.ui.theme.BioGreenPrimary
import com.example.viewmodel.SapViewModel
import com.example.viewmodel.Screen
import kotlinx.coroutines.launch

/**
 * 7. Student Registration and Login
 */
@Composable
fun StudentAuthScreen(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateToHome() }

  var selectedTab by remember { mutableIntStateOf(0) } // 0 = Login, 1 = Register

  // Registration fields
  var regName by remember { mutableStateOf("") }
  var regMobile by remember { mutableStateOf("") }
  var regPass by remember { mutableStateOf("") }
  var regConfirmPass by remember { mutableStateOf("") }

  // Login fields
  var loginMobile by remember { mutableStateOf("") }
  var loginPass by remember { mutableStateOf("") }

  val authError by viewModel.studentAuthError.collectAsState()
  val authSuccess by viewModel.studentAuthSuccess.collectAsState()

  Scaffold(
    topBar = {
      Surface(tonalElevation = 2.dp) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = { viewModel.navigateToHome() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Home")
          }
          Text(
            text = "Online Test Portal",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = BioGreenPrimary
          )
        }
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      item {
        Box(
          modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(BioGreenContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Quiz,
            contentDescription = null,
            tint = BioGreenPrimary,
            modifier = Modifier.size(40.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "Sap Biology Examination System",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground
        )

        Text(
          text = "Login or create your student account to attempt objective papers.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        TabRow(
          selectedTabIndex = selectedTab,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = BioGreenPrimary
            )
          }
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = { Text("Student Login", fontWeight = FontWeight.Bold) }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = { Text("New Registration", fontWeight = FontWeight.Bold) }
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error message banner
        if (authError != null) {
          Surface(
            color = Color(0xFFFFEBEE),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          ) {
            Text(
              text = authError!!,
              color = Color(0xFFC62828),
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(12.dp)
            )
          }
        }

        if (authSuccess != null) {
          Surface(
            color = Color(0xFFE8F5E9),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 12.dp)
          ) {
            Text(
              text = authSuccess!!,
              color = Color(0xFF2E7D32),
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(12.dp)
            )
          }
        }
      }

      if (selectedTab == 0) {
        // Login Form
        item {
          OutlinedTextField(
            value = loginMobile,
            onValueChange = { loginMobile = it },
            label = { Text("Mobile Number") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_mobile_input")
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = loginPass,
            onValueChange = { loginPass = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_password_input")
          )

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = { viewModel.loginStudent(loginMobile, loginPass) },
            colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("login_submit_btn")
          ) {
            Text("Login & Access Tests", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          }

          Spacer(modifier = Modifier.height(12.dp))

          TextButton(onClick = { selectedTab = 1 }) {
            Text("Don't have an account? Register here", color = BioGreenPrimary)
          }
        }
      } else {
        // Registration Form
        item {
          OutlinedTextField(
            value = regName,
            onValueChange = { regName = it },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("register_name_input")
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = regMobile,
            onValueChange = { regMobile = it },
            label = { Text("Mobile Number (Unique 10-Digits)") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("register_mobile_input")
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = regPass,
            onValueChange = { regPass = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("register_password_input")
          )

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = regConfirmPass,
            onValueChange = { regConfirmPass = it },
            label = { Text("Confirm Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("register_confirm_password_input")
          )

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = {
              viewModel.registerStudent(regName, regMobile, regPass, regConfirmPass)
            },
            colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("register_submit_btn")
          ) {
            Text("Register Account", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          }

          Spacer(modifier = Modifier.height(12.dp))

          TextButton(onClick = { selectedTab = 0 }) {
            Text("Already registered? Log in here", color = BioGreenPrimary)
          }
        }
      }
    }
  }
}

/**
 * 7. Student Portal Screen with Subject Filter and Test Papers
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentPortalScreen(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateToHome() }

  val student by viewModel.currentStudent.collectAsState()
  val papers by viewModel.publishedPapers.collectAsState()

  // Extract unique subjects
  val subjects = remember(papers) {
    listOf("All Subjects") + papers.map { it.subject }.distinct()
  }
  var selectedSubject by remember { mutableStateOf("All Subjects") }

  val filteredPapers = remember(papers, selectedSubject) {
    if (selectedSubject == "All Subjects") papers
    else papers.filter { it.subject.equals(selectedSubject, ignoreCase = true) }
  }

  Scaffold(
    topBar = {
      Surface(tonalElevation = 3.dp) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { viewModel.navigateToHome() }) {
              Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Home")
            }
            Column {
              Text(
                text = "Sap Biology Portal",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = BioGreenPrimary
              )
              Text(
                text = "Student: ${student?.fullName ?: "Learner"}",
                fontSize = 11.sp,
                color = BioEmeraldSecondary,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          OutlinedButton(
            onClick = { viewModel.logoutStudent() },
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Logout", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Logout", fontSize = 11.sp)
          }
        }
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(padding),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = BioGreenContainer),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Welcome, ${student?.fullName}!",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = BioGreenPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Select a subject and an active test paper to begin your objective examination.",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // Subject Filter Chips
      item {
        Text(
          text = "Select Subject",
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(subjects) { subj ->
            FilterChip(
              selected = selectedSubject == subj,
              onClick = { selectedSubject = subj },
              label = { Text(subj, fontWeight = FontWeight.SemiBold) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = BioGreenPrimary,
                selectedLabelColor = Color.White
              )
            )
          }
        }
      }

      item {
        Text(
          text = "Available Test Papers (${filteredPapers.size})",
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.titleSmall
        )
      }

      if (filteredPapers.isEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 24.dp)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(Icons.Default.Quiz, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(40.dp))
              Spacer(modifier = Modifier.height(8.dp))
              Text("No test papers available in this category yet.", color = Color.Gray, fontSize = 13.sp)
            }
          }
        }
      } else {
        items(filteredPapers, key = { it.id }) { paper ->
          TestPaperCard(
            paper = paper,
            onStartExam = { viewModel.startExam(paper.id) }
          )
        }
      }
    }
  }
}

@Composable
fun TestPaperCard(
  paper: TestPaperEntity,
  onStartExam: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(16.dp))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          color = BioGreenContainer,
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = paper.subject,
            color = BioGreenPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (paper.durationMinutes > 0) "${paper.durationMinutes} Mins" else "Untimed",
            fontSize = 11.sp,
            color = Color.Gray,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = paper.title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = paper.instructions,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 16.sp
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Marks: ${paper.totalQuestions} • Questions: ${paper.totalQuestions}",
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold,
          color = BioEmeraldSecondary
        )

        Button(
          onClick = onStartExam,
          colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
          shape = RoundedCornerShape(10.dp),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
          modifier = Modifier.testTag("start_test_btn_${paper.id}")
        ) {
          Text("Start Test", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      }
    }
  }
}

/**
 * 8. Objective Examination Screen
 * Single question at a time, 4 options A, B, C, D, Back, Next, Exit, Submit.
 * Question palette, preserved answers, auto-submit countdown timer.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val paper by viewModel.activePaper.collectAsState()
  val questions by viewModel.examQuestions.collectAsState()
  val currentIndex by viewModel.currentQuestionIndex.collectAsState()
  val answers by viewModel.studentAnswers.collectAsState()
  val remainingSecs by viewModel.remainingSeconds.collectAsState()
  val isSubmitting by viewModel.isSubmitting.collectAsState()

  var showExitConfirmDialog by remember { mutableStateOf(false) }
  var showSubmitConfirmDialog by remember { mutableStateOf(false) }
  var showQuestionPaletteSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState()
  val scope = rememberCoroutineScope()

  // Prevent back button without warning
  BackHandler {
    showExitConfirmDialog = true
  }

  if (paper == null || questions.isEmpty()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      CircularProgressIndicator(color = BioGreenPrimary)
    }
    return
  }

  val currentQ = questions.getOrNull(currentIndex) ?: return
  val selectedAnswer = answers[currentQ.id]

  Scaffold(
    topBar = {
      Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Branding & Paper info
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Sap Biology Examination",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = BioGreenPrimary
              )
              Text(
                text = paper!!.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
              )
            }

            // Countdown Timer (if timed)
            if (paper!!.durationMinutes > 0) {
              val mins = remainingSecs / 60
              val secs = remainingSecs % 60
              Surface(
                color = if (remainingSecs < 120) Color(0xFFFFEBEE) else BioGreenContainer,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Icon(
                    Icons.Default.AccessTime,
                    contentDescription = "Timer",
                    tint = if (remainingSecs < 120) Color(0xFFC62828) else BioGreenPrimary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = String.format("%02d:%02d", mins, secs),
                    color = if (remainingSecs < 120) Color(0xFFC62828) else BioGreenPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  )
                }
              }
            }

            // Question palette toggle button
            IconButton(onClick = { showQuestionPaletteSheet = true }) {
              Icon(Icons.Default.GridView, contentDescription = "Question Palette", tint = BioGreenPrimary)
            }
          }

          Spacer(modifier = Modifier.height(4.dp))

          // Question progress indicator
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Question ${currentIndex + 1} of ${questions.size}",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = BioGreenPrimary
            )
            Text(
              text = "Answered: ${answers.size}/${questions.size}",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = BioEmeraldSecondary
            )
          }
        }
      }
    },
    bottomBar = {
      // Bottom Navigation Bar with Back, Next, Exit, Submit
      Surface(
        tonalElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Exit button
          OutlinedButton(
            onClick = { showExitConfirmDialog = true },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC62828)),
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(Color(0xFFEF9A9A), Color(0xFFEF9A9A)))),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
            modifier = Modifier.testTag("exam_exit_btn")
          ) {
            Text("Exit", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Back Button
            OutlinedButton(
              onClick = { viewModel.goToPreviousQuestion() },
              enabled = currentIndex > 0,
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
              modifier = Modifier.testTag("exam_back_btn")
            ) {
              Text("Back", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            // Next Button
            if (currentIndex < questions.size - 1) {
              Button(
                onClick = { viewModel.goToNextQuestion() },
                colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.testTag("exam_next_btn")
              ) {
                Text("Next", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            } else {
              // Final Question Submit Button
              Button(
                onClick = { showSubmitConfirmDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = BioEmeraldSecondary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                modifier = Modifier.testTag("exam_submit_btn")
              ) {
                Text("Submit Test", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Question Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(16.dp))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Surface(
              color = BioGreenContainer,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "QUESTION ${currentIndex + 1}",
                color = BioGreenPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = currentQ.questionText,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onSurface,
              lineHeight = 24.sp
            )
          }
        }
      }

      // 4 Options: A, B, C, D
      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          ExamOptionCard(
            optionKey = "A",
            optionText = currentQ.optionA,
            isSelected = selectedAnswer == "A",
            onSelect = { viewModel.selectAnswer(currentQ.id, "A") }
          )
          ExamOptionCard(
            optionKey = "B",
            optionText = currentQ.optionB,
            isSelected = selectedAnswer == "B",
            onSelect = { viewModel.selectAnswer(currentQ.id, "B") }
          )
          ExamOptionCard(
            optionKey = "C",
            optionText = currentQ.optionC,
            isSelected = selectedAnswer == "C",
            onSelect = { viewModel.selectAnswer(currentQ.id, "C") }
          )
          ExamOptionCard(
            optionKey = "D",
            optionText = currentQ.optionD,
            isSelected = selectedAnswer == "D",
            onSelect = { viewModel.selectAnswer(currentQ.id, "D") }
          )
        }
      }

      // Clear answer button
      if (selectedAnswer != null) {
        item {
          Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            TextButton(
              onClick = { viewModel.clearAnswer(currentQ.id) }
            ) {
              Text("Clear Selection", color = Color.Gray, fontSize = 12.sp)
            }
          }
        }
      }
    }
  }

  // Question Palette Bottom Sheet
  if (showQuestionPaletteSheet) {
    ModalBottomSheet(
      onDismissRequest = { showQuestionPaletteSheet = false },
      sheetState = sheetState
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Text(
          text = "Question Palette",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = BioGreenPrimary
        )
        Text(
          text = "Tap any question number to jump directly.",
          fontSize = 12.sp,
          color = Color.Gray
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Legend
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(12.dp).background(BioGreenPrimary, CircleShape))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Answered", fontSize = 11.sp)
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(12.dp).background(Color(0xFFEEEEEE), CircleShape))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Unanswered", fontSize = 11.sp)
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(12.dp)
                .border(2.dp, BioGreenPrimary, CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Current", fontSize = 11.sp)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
          columns = GridCells.Fixed(5),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          itemsIndexed(questions) { idx, q ->
            val isAnswered = answers.containsKey(q.id)
            val isCurrent = idx == currentIndex
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                  when {
                    isAnswered -> BioGreenPrimary
                    else -> Color(0xFFF1F5F0)
                  }
                )
                .then(
                  if (isCurrent) Modifier.border(2.dp, Color(0xFF004D40), RoundedCornerShape(8.dp))
                  else Modifier
                )
                .clickable {
                  viewModel.jumpToQuestion(idx)
                  scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) showQuestionPaletteSheet = false
                  }
                },
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "${idx + 1}",
                color = if (isAnswered) Color.White else Color(0xFF191C19),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
          onClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
              if (!sheetState.isVisible) showQuestionPaletteSheet = false
            }
            showSubmitConfirmDialog = true
          },
          colors = ButtonDefaults.buttonColors(containerColor = BioEmeraldSecondary),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Finish & Submit Test", fontWeight = FontWeight.Bold)
        }
      }
    }
  }

  // Exit Confirmation Dialog
  if (showExitConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showExitConfirmDialog = false },
      title = { Text("Exit Examination?", fontWeight = FontWeight.Bold) },
      text = { Text("Are you sure you want to exit? Your answers for this session will not be submitted.") },
      confirmButton = {
        Button(
          onClick = {
            showExitConfirmDialog = false
            viewModel.navigateTo(Screen.StudentPortal)
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
        ) {
          Text("Yes, Exit")
        }
      },
      dismissButton = {
        TextButton(onClick = { showExitConfirmDialog = false }) {
          Text("Continue Test", color = BioGreenPrimary)
        }
      }
    )
  }

  // Submit Confirmation Dialog
  if (showSubmitConfirmDialog) {
    val answeredCount = answers.size
    val totalCount = questions.size
    AlertDialog(
      onDismissRequest = { showSubmitConfirmDialog = false },
      title = { Text("Submit Examination?", fontWeight = FontWeight.Bold, color = BioGreenPrimary) },
      text = {
        Column {
          Text("You have answered $answeredCount out of $totalCount questions.")
          if (answeredCount < totalCount) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              "${totalCount - answeredCount} question(s) remain unanswered.",
              color = Color(0xFFB71C1C),
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text("Are you sure you want to finalize your submission?")
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showSubmitConfirmDialog = false
            viewModel.submitExam()
          },
          colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary)
        ) {
          if (isSubmitting) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp))
          } else {
            Text("Yes, Submit")
          }
        }
      },
      dismissButton = {
        TextButton(onClick = { showSubmitConfirmDialog = false }) {
          Text("Review Answers", color = Color.Gray)
        }
      }
    )
  }
}

@Composable
fun ExamOptionCard(
  optionKey: String,
  optionText: String,
  isSelected: Boolean,
  onSelect: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) BioGreenContainer else MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
    modifier = Modifier
      .fillMaxWidth()
      .border(
        width = if (isSelected) 2.dp else 1.dp,
        color = if (isSelected) BioGreenPrimary else Color(0xFFE0E5DF),
        shape = RoundedCornerShape(12.dp)
      )
      .clickable(onClick = onSelect)
      .testTag("option_${optionKey}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(if (isSelected) BioGreenPrimary else Color(0xFFF1F5F0)),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = optionKey,
          color = if (isSelected) Color.White else Color(0xFF191C19),
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Text(
        text = optionText,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        ),
        color = if (isSelected) BioGreenPrimary else MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.weight(1f)
      )

      RadioButton(
        selected = isSelected,
        onClick = onSelect,
        colors = RadioButtonDefaults.colors(selectedColor = BioGreenPrimary)
      )
    }
  }
}

/**
 * 10. AFTER SUBMISSION — STUDENT VIEW
 * Exact specification:
 * Shows:
 * Thank You!
 * Your test has been submitted successfully.
 *
 * Strictly NO marks, NO correct/incorrect count, NO percentage, NO answer key!
 */
@Composable
fun SubmissionConfirmationScreen(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler { viewModel.navigateTo(Screen.StudentPortal) }

  Scaffold { padding ->
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .padding(24.dp),
      contentAlignment = Alignment.Center
    ) {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(20.dp))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(28.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(80.dp)
              .clip(CircleShape)
              .background(BioGreenContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Success",
              tint = BioGreenPrimary,
              modifier = Modifier.size(52.dp)
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          Text(
            text = "Thank You!",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = BioGreenPrimary,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Your test has been submitted successfully.",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
          )

          Spacer(modifier = Modifier.height(28.dp))

          Button(
            onClick = { viewModel.navigateTo(Screen.StudentPortal) },
            colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("return_to_portal_btn")
          ) {
            Text("Back to Test Portal", fontWeight = FontWeight.Bold)
          }

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedButton(
            onClick = { viewModel.navigateToHome() },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
          ) {
            Text("Return to Sap Biology Home", color = BioGreenPrimary, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
