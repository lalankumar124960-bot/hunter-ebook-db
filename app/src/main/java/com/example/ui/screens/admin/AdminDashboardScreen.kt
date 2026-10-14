package com.example.ui.screens.admin

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BioGreenPrimary
import com.example.viewmodel.AdminSection
import com.example.viewmodel.SapViewModel

@Composable
fun AdminDashboardScreen(
  viewModel: SapViewModel,
  initialSection: AdminSection = AdminSection.OVERVIEW,
  modifier: Modifier = Modifier
) {
  var currentSection by remember { mutableStateOf(initialSection) }
  var showLogoutConfirmDialog by remember { mutableStateOf(false) }

  BackHandler {
    if (currentSection != AdminSection.OVERVIEW) {
      currentSection = AdminSection.OVERVIEW
    } else {
      showLogoutConfirmDialog = true
    }
  }

  val sections = AdminSection.values()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Top Bar
    Surface(
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 3.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = {
            if (currentSection != AdminSection.OVERVIEW) {
              currentSection = AdminSection.OVERVIEW
            } else {
              showLogoutConfirmDialog = true
            }
          }
        ) {
          Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = BioGreenPrimary
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Sap Biology Admin Panel",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = BioGreenPrimary
          )
          Text(
            text = currentSection.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        IconButton(
          onClick = { showLogoutConfirmDialog = true },
          modifier = Modifier.testTag("admin_logout_btn")
        ) {
          Icon(
            Icons.Default.ExitToApp,
            contentDescription = "Logout",
            tint = Color(0xFFC62828)
          )
        }
      }
    }

    // Scrollable Tab Row for all 12 sections
    ScrollableTabRow(
      selectedTabIndex = currentSection.ordinal,
      edgePadding = 16.dp,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          Modifier.tabIndicatorOffset(tabPositions[currentSection.ordinal]),
          color = BioGreenPrimary
        )
      }
    ) {
      sections.forEach { section ->
        Tab(
          selected = currentSection == section,
          onClick = { currentSection = section },
          text = {
            Text(
              text = section.title,
              fontWeight = if (currentSection == section) FontWeight.Bold else FontWeight.Normal,
              fontSize = 12.sp,
              color = if (currentSection == section) BioGreenPrimary else Color.DarkGray
            )
          }
        )
      }
    }

    // Render active section
    Box(modifier = Modifier.weight(1f)) {
      when (currentSection) {
        AdminSection.OVERVIEW -> AdminOverviewSection(viewModel, onNavigateSection = { currentSection = it })
        AdminSection.PROMOTIONS -> AdminPromotionsSection(viewModel)
        AdminSection.TEACHERS -> AdminTeachersSection(viewModel)
        AdminSection.GALLERY -> AdminGallerySection(viewModel)
        AdminSection.CLASSES -> AdminClassesSection(viewModel)
        AdminSection.FOUNDERS -> AdminFoundersSection(viewModel)
        AdminSection.ADDRESS -> AdminAddressSection(viewModel)
        AdminSection.POLICIES -> AdminPoliciesSection(viewModel)
        AdminSection.TEST_PAPERS -> AdminTestPaperSection(viewModel)
        AdminSection.STUDENTS -> AdminStudentsSection(viewModel)
        AdminSection.RESULTS -> AdminTestResultsSection(viewModel)
        AdminSection.SETTINGS -> AdminSettingsSection(viewModel)
      }
    }
  }

  // Logout confirmation
  if (showLogoutConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showLogoutConfirmDialog = false },
      title = { Text("Logout from Admin?", fontWeight = FontWeight.Bold) },
      text = { Text("Are you sure you want to exit the administrator dashboard and return to the main website?") },
      confirmButton = {
        Button(
          onClick = {
            showLogoutConfirmDialog = false
            viewModel.logoutAdmin()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
        ) {
          Text("Logout")
        }
      },
      dismissButton = {
        TextButton(onClick = { showLogoutConfirmDialog = false }) {
          Text("Stay", color = BioGreenPrimary)
        }
      }
    )
  }
}
