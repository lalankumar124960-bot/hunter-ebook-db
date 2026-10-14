package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminLoginScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.student.ExamScreen
import com.example.ui.screens.student.StudentAuthScreen
import com.example.ui.screens.student.StudentPortalScreen
import com.example.ui.screens.student.SubmissionConfirmationScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.SapViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {

  private val viewModel: SapViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
        ) {
          SapApp(viewModel)
        }
      }
    }
  }
}

@Composable
fun SapApp(viewModel: SapViewModel) {
  val currentScreen by viewModel.currentScreen.collectAsState()

  when (val screen = currentScreen) {
    is Screen.Home -> {
      HomeScreen(viewModel = viewModel)
    }
    is Screen.AdminLogin -> {
      AdminLoginScreen(viewModel = viewModel)
    }
    is Screen.AdminDashboard -> {
      AdminDashboardScreen(viewModel = viewModel, initialSection = screen.section)
    }
    is Screen.StudentAuth -> {
      StudentAuthScreen(viewModel = viewModel)
    }
    is Screen.StudentPortal -> {
      StudentPortalScreen(viewModel = viewModel)
    }
    is Screen.Exam -> {
      ExamScreen(viewModel = viewModel)
    }
    is Screen.SubmissionConfirmation -> {
      SubmissionConfirmationScreen(viewModel = viewModel)
    }
  }
}
