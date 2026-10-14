package com.example.ui.screens.admin

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BannerEntity
import com.example.data.local.CoachingInfoEntity
import com.example.data.local.CourseClassEntity
import com.example.data.local.ExperimentPhotoEntity
import com.example.data.local.QuestionEntity
import com.example.data.local.StudentEntity
import com.example.data.local.TeacherEntity
import com.example.data.local.TestPaperEntity
import com.example.data.local.TestSubmissionEntity
import com.example.ui.components.BiologyIllustrationPlaceholder
import com.example.ui.components.PhotoUploadPickerField
import com.example.ui.components.SapMediaDisplay
import com.example.ui.components.SapProfileDp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.ui.theme.BioEmeraldSecondary
import com.example.ui.theme.BioGreenContainer
import com.example.ui.theme.BioGreenPrimary
import com.example.ui.theme.BioTealTertiary
import com.example.viewmodel.AdminSection
import com.example.viewmodel.SapViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- SECTION 1: OVERVIEW & QUICK STATS ---
@Composable
fun AdminOverviewSection(
  viewModel: SapViewModel,
  onNavigateSection: (AdminSection) -> Unit,
  modifier: Modifier = Modifier
) {
  val banners by viewModel.allBanners.collectAsState()
  val teachers by viewModel.teachers.collectAsState()
  val photos by viewModel.experimentPhotos.collectAsState()
  val courses by viewModel.courses.collectAsState()
  val papers by viewModel.allPapers.collectAsState()
  val students by viewModel.allStudents.collectAsState()
  val submissions by viewModel.allSubmissions.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BioGreenPrimary),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "Welcome to Sap Biology Admin Portal",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Real-time control over coaching content, faculty roster, practical laboratory gallery, objective examination questions, and student test records.",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 12.sp,
            lineHeight = 16.sp
          )
        }
      }
    }

    item {
      Text(
        text = "Key Metrics",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleMedium
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        MetricCard("Teachers", "${teachers.size}", Icons.Default.Group, Modifier.weight(1f)) {
          onNavigateSection(AdminSection.TEACHERS)
        }
        MetricCard("Photos", "${photos.size}", Icons.Default.PhotoLibrary, Modifier.weight(1f)) {
          onNavigateSection(AdminSection.GALLERY)
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        MetricCard("Test Papers", "${papers.size}", Icons.Default.Quiz, Modifier.weight(1f)) {
          onNavigateSection(AdminSection.TEST_PAPERS)
        }
        MetricCard("Students", "${students.size}", Icons.Default.School, Modifier.weight(1f)) {
          onNavigateSection(AdminSection.STUDENTS)
        }
      }
    }

    item {
      MetricCard(
        title = "Total Test Submissions",
        count = "${submissions.size}",
        icon = Icons.Default.Assessment,
        modifier = Modifier.fillMaxWidth()
      ) {
        onNavigateSection(AdminSection.RESULTS)
      }
    }

    item {
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Quick Administrative Actions",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleMedium
      )
    }

    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        QuickActionItem("Create / Edit Test Papers", Icons.Default.Quiz) {
          onNavigateSection(AdminSection.TEST_PAPERS)
        }
        QuickActionItem("Inspect Student Test Results", Icons.Default.Assessment) {
          onNavigateSection(AdminSection.RESULTS)
        }
        QuickActionItem("Manage Faculty & Teachers", Icons.Default.Group) {
          onNavigateSection(AdminSection.TEACHERS)
        }
        QuickActionItem("Upload Experiment Lab Photos", Icons.Default.PhotoLibrary) {
          onNavigateSection(AdminSection.GALLERY)
        }
        QuickActionItem("Update Coaching Address & Map", Icons.Default.LocationOn) {
          onNavigateSection(AdminSection.ADDRESS)
        }
      }
    }
  }
}

@Composable
fun MetricCard(
  title: String,
  count: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(14.dp))
      .clickable(onClick = onClick)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(BioGreenContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = title, tint = BioGreenPrimary, modifier = Modifier.size(24.dp))
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(text = count, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BioGreenPrimary)
        Text(text = title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
  }
}

@Composable
fun QuickActionItem(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(10.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(10.dp))
      .clickable(onClick = onClick)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = BioGreenPrimary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
      }
      Icon(
        Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = null,
        tint = Color.Gray,
        modifier = Modifier.size(16.dp)
      )
    }
  }
}

// --- SECTION 2: PROMOTIONS & BANNERS ---
@Composable
fun AdminPromotionsSection(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val banners by viewModel.allBanners.collectAsState()
  val scope = rememberCoroutineScope()
  var showEditDialog by remember { mutableStateOf<BannerEntity?>(null) }
  var isNewBanner by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Promotional Banners (${banners.size})",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
      Button(
        onClick = {
          isNewBanner = true
          showEditDialog = BannerEntity(
            title = "",
            subtitle = "",
            tag = "NEW BATCH",
            actionText = "Join Now"
          )
        },
        colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Add Banner")
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      items(banners, key = { it.id }) { banner ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(12.dp))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(color = BioGreenContainer, shape = RoundedCornerShape(6.dp)) {
                Text(
                  text = banner.tag,
                  color = BioGreenPrimary,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
              Row {
                IconButton(onClick = {
                  isNewBanner = false
                  showEditDialog = banner
                }) {
                  Icon(Icons.Default.Edit, contentDescription = "Edit", tint = BioGreenPrimary)
                }
                IconButton(onClick = {
                  scope.launch { viewModel.repository.deleteBanner(banner.id) }
                }) {
                  Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828))
                }
              }
            }

            if (banner.imageUrl.isNotBlank()) {
              Spacer(modifier = Modifier.height(8.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(110.dp)
                  .clip(RoundedCornerShape(8.dp))
              ) {
                AsyncImage(
                  model = banner.imageUrl,
                  contentDescription = banner.title,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
            }

            Text(text = banner.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = banner.subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }
  }

  // Edit / Add Banner Dialog
  showEditDialog?.let { b ->
    var title by remember { mutableStateOf(b.title) }
    var subtitle by remember { mutableStateOf(b.subtitle) }
    var tag by remember { mutableStateOf(b.tag) }
    var actionText by remember { mutableStateOf(b.actionText) }
    var imageUrl by remember { mutableStateOf(b.imageUrl) }

    AlertDialog(
      onDismissRequest = { showEditDialog = null },
      title = { Text(if (isNewBanner) "Add Promotional Banner" else "Edit Banner") },
      text = {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          item {
            PhotoUploadPickerField(
              currentImageUrl = imageUrl,
              onImageSelected = { imageUrl = it },
              label = "Banner Thumbnail / Image",
              isCircle = false
            )
          }
          item {
            OutlinedTextField(
              value = title,
              onValueChange = { title = it },
              label = { Text("Banner Headline") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = subtitle,
              onValueChange = { subtitle = it },
              label = { Text("Subtitle / Details") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = tag,
              onValueChange = { tag = it },
              label = { Text("Badge Tag (e.g. ADMISSIONS OPEN)") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = actionText,
              onValueChange = { actionText = it },
              label = { Text("Button Text") },
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            scope.launch {
              viewModel.repository.saveBanner(
                b.copy(
                  title = title.trim(),
                  subtitle = subtitle.trim(),
                  tag = tag.trim(),
                  actionText = actionText.trim(),
                  imageUrl = imageUrl.trim()
                )
              )
              showEditDialog = null
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary)
        ) {
          Text("Save Banner")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditDialog = null }) {
          Text("Cancel")
        }
      }
    )
  }
}

// --- SECTION 3: TEACHER MANAGEMENT ---
@Composable
fun AdminTeachersSection(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val teachers by viewModel.teachers.collectAsState()
  val scope = rememberCoroutineScope()
  var showEditTeacherDialog by remember { mutableStateOf<TeacherEntity?>(null) }
  var isNewTeacher by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Faculty Roster (${teachers.size})",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
      Button(
        onClick = {
          isNewTeacher = true
          showEditTeacherDialog = TeacherEntity(
            name = "",
            subject = "",
            experience = "5+ Years",
            shortIntro = "",
            bio = ""
          )
        },
        colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Add Teacher")
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      items(teachers, key = { it.id }) { teacher ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(12.dp))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            SapProfileDp(
              imageUrl = teacher.avatarUrl,
              fallbackIcon = Icons.Default.School,
              size = 48.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(text = teacher.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Text(text = teacher.subject, fontSize = 12.sp, color = BioGreenPrimary)
              Text(text = teacher.experience, fontSize = 11.sp, color = Color.Gray)
            }
            Row {
              IconButton(onClick = {
                isNewTeacher = false
                showEditTeacherDialog = teacher
              }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = BioGreenPrimary)
              }
              IconButton(onClick = {
                scope.launch { viewModel.repository.deleteTeacher(teacher.id) }
              }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828))
              }
            }
          }
        }
      }
    }
  }

  // Edit / Add Teacher Dialog
  showEditTeacherDialog?.let { t ->
    var name by remember { mutableStateOf(t.name) }
    var subject by remember { mutableStateOf(t.subject) }
    var experience by remember { mutableStateOf(t.experience) }
    var shortIntro by remember { mutableStateOf(t.shortIntro) }
    var bio by remember { mutableStateOf(t.bio) }
    var avatarUrl by remember { mutableStateOf(t.avatarUrl) }

    AlertDialog(
      onDismissRequest = { showEditTeacherDialog = null },
      title = { Text(if (isNewTeacher) "Add Faculty Teacher" else "Edit Faculty Member") },
      text = {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          item {
            PhotoUploadPickerField(
              currentImageUrl = avatarUrl,
              onImageSelected = { avatarUrl = it },
              label = "Teacher Profile Photo / DP",
              isCircle = true
            )
          }
          item {
            OutlinedTextField(
              value = name,
              onValueChange = { name = it },
              label = { Text("Teacher Full Name") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = subject,
              onValueChange = { subject = it },
              label = { Text("Subject / Specialization") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = experience,
              onValueChange = { experience = it },
              label = { Text("Experience (e.g. 15+ Years)") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = shortIntro,
              onValueChange = { shortIntro = it },
              label = { Text("Short Intro (Cards)") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = bio,
              onValueChange = { bio = it },
              label = { Text("Full Biography / Achievements") },
              modifier = Modifier.fillMaxWidth(),
              maxLines = 4
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            scope.launch {
              viewModel.repository.saveTeacher(
                t.copy(
                  name = name.trim(),
                  subject = subject.trim(),
                  experience = experience.trim(),
                  shortIntro = shortIntro.trim(),
                  bio = bio.trim(),
                  avatarUrl = avatarUrl.trim()
                )
              )
              showEditTeacherDialog = null
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary)
        ) {
          Text("Save Teacher")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditTeacherDialog = null }) {
          Text("Cancel")
        }
      }
    )
  }
}

// --- SECTION 4: EXPERIMENT MOMENTS GALLERY ---
@Composable
fun AdminGallerySection(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val photos by viewModel.experimentPhotos.collectAsState()
  val scope = rememberCoroutineScope()
  var showAddPhotoDialog by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Experiment Moments (${photos.size})",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
      Button(
        onClick = { showAddPhotoDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Add Moment")
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      items(photos, key = { it.id }) { photo ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(12.dp))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            SapMediaDisplay(
              imageUrl = photo.customUrl,
              seed = photo.photoSeed,
              title = photo.title,
              badge = photo.tag,
              modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(text = photo.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text(
                text = photo.caption,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
              )
            }
            IconButton(onClick = {
              scope.launch { viewModel.repository.deleteExperimentPhoto(photo.id) }
            }) {
              Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828))
            }
          }
        }
      }
    }
  }

  if (showAddPhotoDialog) {
    var title by remember { mutableStateOf("") }
    var caption by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("Laboratory Session") }
    var customUrl by remember { mutableStateOf("") }

    AlertDialog(
      onDismissRequest = { showAddPhotoDialog = false },
      title = { Text("Add Experiment Lab Moment") },
      text = {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          item {
            PhotoUploadPickerField(
              currentImageUrl = customUrl,
              onImageSelected = { customUrl = it },
              label = "Upload Experiment / Student Lab Photo",
              isCircle = false
            )
          }
          item {
            OutlinedTextField(
              value = title,
              onValueChange = { title = it },
              label = { Text("Experiment Title") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = caption,
              onValueChange = { caption = it },
              label = { Text("Caption / Details") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = tag,
              onValueChange = { tag = it },
              label = { Text("Category Tag (e.g. Cytology, DNA, Student Batch)") },
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank() || customUrl.isNotBlank()) {
              val finalTitle = title.ifBlank { "Coaching Moment" }
              scope.launch {
                viewModel.repository.saveExperimentPhoto(
                  ExperimentPhotoEntity(
                    title = finalTitle.trim(),
                    caption = caption.trim(),
                    tag = tag.trim(),
                    photoSeed = if (tag.contains("DNA", ignoreCase = true)) "dna_spooling" else "mitosis",
                    customUrl = customUrl.trim()
                  )
                )
                showAddPhotoDialog = false
              }
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary)
        ) {
          Text("Add to Gallery")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddPhotoDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

// --- SECTION 5: CLASSES & COURSES ---
@Composable
fun AdminClassesSection(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val courses by viewModel.courses.collectAsState()
  val scope = rememberCoroutineScope()
  var showEditCourseDialog by remember { mutableStateOf<CourseClassEntity?>(null) }
  var isNewCourse by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Classes & Courses (${courses.size})",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
      Button(
        onClick = {
          isNewCourse = true
          showEditCourseDialog = CourseClassEntity(
            title = "",
            grade = "Class 11",
            targetAudience = "",
            description = "",
            featuresList = "Daily Lectures;DPPs;Weekly Tests",
            timings = "4:30 PM - 6:30 PM"
          )
        },
        colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Add Class")
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      items(courses, key = { it.id }) { course ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(12.dp))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = course.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Row {
                IconButton(onClick = {
                  isNewCourse = false
                  showEditCourseDialog = course
                }) {
                  Icon(Icons.Default.Edit, contentDescription = "Edit", tint = BioGreenPrimary)
                }
                IconButton(onClick = {
                  scope.launch { viewModel.repository.deleteCourse(course.id) }
                }) {
                  Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828))
                }
              }
            }
            Text(text = "Grade: ${course.grade}", fontSize = 12.sp, color = BioGreenPrimary)
            Text(text = course.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }
  }

  showEditCourseDialog?.let { c ->
    var title by remember { mutableStateOf(c.title) }
    var grade by remember { mutableStateOf(c.grade) }
    var targetAudience by remember { mutableStateOf(c.targetAudience) }
    var description by remember { mutableStateOf(c.description) }
    var featuresList by remember { mutableStateOf(c.featuresList) }
    var timings by remember { mutableStateOf(c.timings) }

    AlertDialog(
      onDismissRequest = { showEditCourseDialog = null },
      title = { Text(if (isNewCourse) "Add Course / Class" else "Edit Course") },
      text = {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          item {
            OutlinedTextField(
              value = title,
              onValueChange = { title = it },
              label = { Text("Course Title") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = grade,
              onValueChange = { grade = it },
              label = { Text("Target Class / Grade (e.g. Class 11)") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = targetAudience,
              onValueChange = { targetAudience = it },
              label = { Text("Audience (e.g. NEET Medical Aspirants)") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = description,
              onValueChange = { description = it },
              label = { Text("Course Overview") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = featuresList,
              onValueChange = { featuresList = it },
              label = { Text("Features (separated by semicolons ';')") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = timings,
              onValueChange = { timings = it },
              label = { Text("Schedule / Timings") },
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            scope.launch {
              viewModel.repository.saveCourse(
                c.copy(
                  title = title.trim(),
                  grade = grade.trim(),
                  targetAudience = targetAudience.trim(),
                  description = description.trim(),
                  featuresList = featuresList.trim(),
                  timings = timings.trim()
                )
              )
              showEditCourseDialog = null
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary)
        ) {
          Text("Save Course")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditCourseDialog = null }) {
          Text("Cancel")
        }
      }
    )
  }
}

// --- SECTION 6: FOUNDER & CO-FOUNDER ---
@Composable
fun AdminFoundersSection(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val info by viewModel.coachingInfo.collectAsState()
  val scope = rememberCoroutineScope()
  val context = LocalContext.current

  if (info == null) return

  var founderName by remember(info) { mutableStateOf(info!!.founderName) }
  var founderRole by remember(info) { mutableStateOf(info!!.founderRole) }
  var founderBio by remember(info) { mutableStateOf(info!!.founderBio) }
  var founderPhotoUrl by remember(info) { mutableStateOf(info!!.founderPhotoUrl) }

  var cofounderName by remember(info) { mutableStateOf(info!!.cofounderName) }
  var cofounderRole by remember(info) { mutableStateOf(info!!.cofounderRole) }
  var cofounderBio by remember(info) { mutableStateOf(info!!.cofounderBio) }
  var cofounderPhotoUrl by remember(info) { mutableStateOf(info!!.cofounderPhotoUrl) }
  var showCofounder by remember(info) { mutableStateOf(info!!.showCofounder) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Text(
        text = "Founder Profile",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = BioGreenPrimary
      )
    }

    item {
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(12.dp))
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          PhotoUploadPickerField(
            currentImageUrl = founderPhotoUrl,
            onImageSelected = { founderPhotoUrl = it },
            label = "Founder Profile DP",
            isCircle = true
          )
          OutlinedTextField(
            value = founderName,
            onValueChange = { founderName = it },
            label = { Text("Founder Name") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = founderRole,
            onValueChange = { founderRole = it },
            label = { Text("Founder Role") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = founderBio,
            onValueChange = { founderBio = it },
            label = { Text("Founder Biography & Qualifications") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
          )
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Co-founder Profile",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = BioEmeraldSecondary
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text("Show on Website", fontSize = 12.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Switch(
            checked = showCofounder,
            onCheckedChange = { showCofounder = it },
            colors = SwitchDefaults.colors(checkedThumbColor = BioGreenPrimary)
          )
        }
      }
    }

    if (showCofounder) {
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(12.dp))
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            PhotoUploadPickerField(
              currentImageUrl = cofounderPhotoUrl,
              onImageSelected = { cofounderPhotoUrl = it },
              label = "Co-founder Profile DP",
              isCircle = true
            )
            OutlinedTextField(
              value = cofounderName,
              onValueChange = { cofounderName = it },
              label = { Text("Co-founder Name") },
              modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
              value = cofounderRole,
              onValueChange = { cofounderRole = it },
              label = { Text("Co-founder Role") },
              modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
              value = cofounderBio,
              onValueChange = { cofounderBio = it },
              label = { Text("Co-founder Biography") },
              modifier = Modifier.fillMaxWidth(),
              maxLines = 4
            )
          }
        }
      }
    }

    item {
      Button(
        onClick = {
          scope.launch {
            viewModel.repository.updateCoachingInfo(
              info!!.copy(
                founderName = founderName.trim(),
                founderRole = founderRole.trim(),
                founderBio = founderBio.trim(),
                founderPhotoUrl = founderPhotoUrl.trim(),
                cofounderName = cofounderName.trim(),
                cofounderRole = cofounderRole.trim(),
                cofounderBio = cofounderBio.trim(),
                cofounderPhotoUrl = cofounderPhotoUrl.trim(),
                showCofounder = showCofounder
              )
            )
            Toast.makeText(context, "Founder details saved!", Toast.LENGTH_SHORT).show()
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
      ) {
        Text("Save Leadership Profiles", fontWeight = FontWeight.Bold)
      }
    }
  }
}

// --- SECTION 7: COACHING ADDRESS ---
@Composable
fun AdminAddressSection(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val info by viewModel.coachingInfo.collectAsState()
  val scope = rememberCoroutineScope()
  val context = LocalContext.current

  if (info == null) return

  var instituteName by remember(info) { mutableStateOf(info!!.instituteName) }
  var tagline by remember(info) { mutableStateOf(info!!.tagline) }
  var locality by remember(info) { mutableStateOf(info!!.locality) }
  var city by remember(info) { mutableStateOf(info!!.city) }
  var district by remember(info) { mutableStateOf(info!!.district) }
  var state by remember(info) { mutableStateOf(info!!.state) }
  var pinCode by remember(info) { mutableStateOf(info!!.pinCode) }
  var phone by remember(info) { mutableStateOf(info!!.phone) }
  var email by remember(info) { mutableStateOf(info!!.email) }
  var mapUrl by remember(info) { mutableStateOf(info!!.googleMapsUrl) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text(
        text = "Coaching Institute Address & Coordinates",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = BioGreenPrimary
      )
    }

    item {
      OutlinedTextField(
        value = instituteName,
        onValueChange = { instituteName = it },
        label = { Text("Coaching Name") },
        modifier = Modifier.fillMaxWidth()
      )
    }

    item {
      OutlinedTextField(
        value = tagline,
        onValueChange = { tagline = it },
        label = { Text("Tagline") },
        modifier = Modifier.fillMaxWidth()
      )
    }

    item {
      OutlinedTextField(
        value = locality,
        onValueChange = { locality = it },
        label = { Text("Locality / Street") },
        modifier = Modifier.fillMaxWidth()
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = city,
          onValueChange = { city = it },
          label = { Text("City") },
          modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
          value = district,
          onValueChange = { district = it },
          label = { Text("District") },
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = state,
          onValueChange = { state = it },
          label = { Text("State") },
          modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
          value = pinCode,
          onValueChange = { pinCode = it },
          label = { Text("PIN Code") },
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        label = { Text("Phone Number") },
        modifier = Modifier.fillMaxWidth()
      )
    }

    item {
      OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Contact Email") },
        modifier = Modifier.fillMaxWidth()
      )
    }

    item {
      OutlinedTextField(
        value = mapUrl,
        onValueChange = { mapUrl = it },
        label = { Text("Google Maps Direction URL") },
        modifier = Modifier.fillMaxWidth()
      )
    }

    item {
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        onClick = {
          scope.launch {
            viewModel.repository.updateCoachingInfo(
              info!!.copy(
                instituteName = instituteName.trim(),
                tagline = tagline.trim(),
                locality = locality.trim(),
                city = city.trim(),
                district = district.trim(),
                state = state.trim(),
                pinCode = pinCode.trim(),
                phone = phone.trim(),
                email = email.trim(),
                googleMapsUrl = mapUrl.trim()
              )
            )
            Toast.makeText(context, "Address updated successfully!", Toast.LENGTH_SHORT).show()
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
      ) {
        Text("Save Coaching Address", fontWeight = FontWeight.Bold)
      }
    }
  }
}

// --- SECTION 8: POLICIES & CONTACT ---
@Composable
fun AdminPoliciesSection(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val info by viewModel.coachingInfo.collectAsState()
  val scope = rememberCoroutineScope()
  val context = LocalContext.current

  if (info == null) return

  var privacyPolicy by remember(info) { mutableStateOf(info!!.privacyPolicy) }
  var terms by remember(info) { mutableStateOf(info!!.termsAndConditions) }
  var copyrightText by remember(info) { mutableStateOf(info!!.copyrightText) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text(
        text = "Website Policies & Disclaimers",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = BioGreenPrimary
      )
    }

    item {
      OutlinedTextField(
        value = privacyPolicy,
        onValueChange = { privacyPolicy = it },
        label = { Text("Privacy Policy Content") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 6
      )
    }

    item {
      OutlinedTextField(
        value = terms,
        onValueChange = { terms = it },
        label = { Text("Terms and Conditions") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 6
      )
    }

    item {
      OutlinedTextField(
        value = copyrightText,
        onValueChange = { copyrightText = it },
        label = { Text("Footer Copyright Notice") },
        modifier = Modifier.fillMaxWidth()
      )
    }

    item {
      Button(
        onClick = {
          scope.launch {
            viewModel.repository.updateCoachingInfo(
              info!!.copy(
                privacyPolicy = privacyPolicy.trim(),
                termsAndConditions = terms.trim(),
                copyrightText = copyrightText.trim()
              )
            )
            Toast.makeText(context, "Policies updated!", Toast.LENGTH_SHORT).show()
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
      ) {
        Text("Save Website Policies", fontWeight = FontWeight.Bold)
      }
    }
  }
}

// --- SECTION 9: TEST PAPER MANAGEMENT ---
@Composable
fun AdminTestPaperSection(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val papers by viewModel.allPapers.collectAsState()
  val scope = rememberCoroutineScope()

  var selectedPaperForQuestions by remember { mutableStateOf<TestPaperEntity?>(null) }
  var showCreatePaperDialog by remember { mutableStateOf(false) }

  if (selectedPaperForQuestions != null) {
    // Question Bank Manager Subview for the paper
    PaperQuestionManagerView(
      paper = selectedPaperForQuestions!!,
      viewModel = viewModel,
      onBack = { selectedPaperForQuestions = null }
    )
    return
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Test Papers (${papers.size})",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
      Button(
        onClick = { showCreatePaperDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("New Paper")
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      items(papers, key = { it.id }) { paper ->
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(14.dp))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                color = if (paper.isPublished) BioGreenContainer else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = if (paper.isPublished) "PUBLISHED" else "UNPUBLISHED",
                  color = if (paper.isPublished) BioGreenPrimary else Color(0xFFC62828),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Publish", fontSize = 11.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Switch(
                  checked = paper.isPublished,
                  onCheckedChange = { checked ->
                    scope.launch {
                      viewModel.repository.saveTestPaper(paper.copy(isPublished = checked))
                    }
                  },
                  colors = SwitchDefaults.colors(checkedThumbColor = BioGreenPrimary)
                )
                IconButton(onClick = {
                  scope.launch { viewModel.repository.deleteTestPaper(paper.id) }
                }) {
                  Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828))
                }
              }
            }

            Text(text = paper.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(
              text = "Subject: ${paper.subject} • Duration: ${if (paper.durationMinutes > 0) "${paper.durationMinutes} mins" else "Untimed"}",
              fontSize = 12.sp,
              color = BioEmeraldSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
              onClick = { selectedPaperForQuestions = paper },
              colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Manage Questions & Answer Key", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }
        }
      }
    }
  }

  if (showCreatePaperDialog) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Biology") }
    var duration by remember { mutableStateOf("20") }
    var instructions by remember {
      mutableStateOf("Each question carries 1 mark. No negative marking. Select one option among A, B, C or D.")
    }

    AlertDialog(
      onDismissRequest = { showCreatePaperDialog = false },
      title = { Text("Create New Test Paper") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Paper Title") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Subject (Biology, Botany, Zoology...)") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Time Limit (Minutes, 0 for untimed)") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = instructions,
            onValueChange = { instructions = it },
            label = { Text("Instructions") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank()) {
              scope.launch {
                val pId = viewModel.repository.saveTestPaper(
                  TestPaperEntity(
                    title = title.trim(),
                    subject = subject.trim(),
                    instructions = instructions.trim(),
                    durationMinutes = duration.toIntOrNull() ?: 20,
                    isPublished = true
                  )
                )
                showCreatePaperDialog = false
              }
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary)
        ) {
          Text("Create Paper")
        }
      },
      dismissButton = {
        TextButton(onClick = { showCreatePaperDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

/**
 * Question Bank Manager: Add/Edit/Delete MCQs, 4 options A/B/C/D, Correct Key
 */
@Composable
fun PaperQuestionManagerView(
  paper: TestPaperEntity,
  viewModel: SapViewModel,
  onBack: () -> Unit
) {
  val questionsFlow = remember(paper.id) { viewModel.repository.getQuestionsFlow(paper.id) }
  val questions by questionsFlow.collectAsState(initial = emptyList())
  val scope = rememberCoroutineScope()
  var showAddQuestionDialog by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) {
          Icon(Icons.Default.Close, contentDescription = "Back")
        }
        Column {
          Text(text = paper.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Text(text = "Total Questions: ${questions.size}", fontSize = 11.sp, color = BioEmeraldSecondary)
        }
      }

      Button(
        onClick = { showAddQuestionDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Add MCQ")
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      items(questions, key = { it.id }) { q ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(12.dp))
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(color = BioGreenContainer, shape = RoundedCornerShape(4.dp)) {
                Text(
                  text = "Q${q.questionNumber}",
                  color = BioGreenPrimary,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
              IconButton(onClick = {
                scope.launch { viewModel.repository.deleteQuestion(q.id, paper.id) }
              }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828))
              }
            }

            Text(text = q.questionText, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text("A) ${q.optionA}", fontSize = 11.sp, color = if (q.correctAnswer == "A") BioGreenPrimary else Color.DarkGray, fontWeight = if (q.correctAnswer == "A") FontWeight.Bold else FontWeight.Normal)
              Text("B) ${q.optionB}", fontSize = 11.sp, color = if (q.correctAnswer == "B") BioGreenPrimary else Color.DarkGray, fontWeight = if (q.correctAnswer == "B") FontWeight.Bold else FontWeight.Normal)
              Text("C) ${q.optionC}", fontSize = 11.sp, color = if (q.correctAnswer == "C") BioGreenPrimary else Color.DarkGray, fontWeight = if (q.correctAnswer == "C") FontWeight.Bold else FontWeight.Normal)
              Text("D) ${q.optionD}", fontSize = 11.sp, color = if (q.correctAnswer == "D") BioGreenPrimary else Color.DarkGray, fontWeight = if (q.correctAnswer == "D") FontWeight.Bold else FontWeight.Normal)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(6.dp)) {
              Text(
                text = "Correct Answer: Option ${q.correctAnswer} (1 Mark)",
                color = BioGreenPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }
      }
    }
  }

  if (showAddQuestionDialog) {
    var qText by remember { mutableStateOf("") }
    var optA by remember { mutableStateOf("") }
    var optB by remember { mutableStateOf("") }
    var optC by remember { mutableStateOf("") }
    var optD by remember { mutableStateOf("") }
    var correctOpt by remember { mutableStateOf("A") }

    AlertDialog(
      onDismissRequest = { showAddQuestionDialog = false },
      title = { Text("Add Objective MCQ") },
      text = {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          item {
            OutlinedTextField(
              value = qText,
              onValueChange = { qText = it },
              label = { Text("Question Statement") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = optA,
              onValueChange = { optA = it },
              label = { Text("Option A") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = optB,
              onValueChange = { optB = it },
              label = { Text("Option B") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = optC,
              onValueChange = { optC = it },
              label = { Text("Option C") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            OutlinedTextField(
              value = optD,
              onValueChange = { optD = it },
              label = { Text("Option D") },
              modifier = Modifier.fillMaxWidth()
            )
          }
          item {
            Text("Select Correct Option:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
              listOf("A", "B", "C", "D").forEach { opt ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                  RadioButton(
                    selected = correctOpt == opt,
                    onClick = { correctOpt = opt }
                  )
                  Text(opt, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (qText.isNotBlank() && optA.isNotBlank() && optB.isNotBlank()) {
              scope.launch {
                viewModel.repository.saveQuestion(
                  QuestionEntity(
                    paperId = paper.id,
                    questionNumber = questions.size + 1,
                    questionText = qText.trim(),
                    optionA = optA.trim(),
                    optionB = optB.trim(),
                    optionC = optC.trim(),
                    optionD = optD.trim(),
                    correctAnswer = correctOpt
                  )
                )
                showAddQuestionDialog = false
              }
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary)
        ) {
          Text("Save Question")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddQuestionDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

// --- SECTION 10: STUDENT ACCOUNTS ---
@Composable
fun AdminStudentsSection(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val students by viewModel.allStudents.collectAsState()
  val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Text(
      text = "Registered Students (${students.size})",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
    Text(
      text = "All student accounts registered through the Online Objective Test Portal.",
      fontSize = 12.sp,
      color = Color.Gray
    )

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
      items(students, key = { it.id }) { s ->
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(12.dp))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(BioGreenContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Person, contentDescription = null, tint = BioGreenPrimary)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(text = s.fullName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Text(text = "Mobile: ${s.mobileNumber}", fontSize = 12.sp, color = BioEmeraldSecondary, fontWeight = FontWeight.SemiBold)
              Text(
                text = "Registered: ${dateFormat.format(Date(s.registeredAt))}",
                fontSize = 11.sp,
                color = Color.Gray
              )
            }
          }
        }
      }
    }
  }
}

// --- SECTION 11: TEST RESULTS DASHBOARD & CSV EXPORT ---
@Composable
fun AdminTestResultsSection(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val submissions by viewModel.allSubmissions.collectAsState()
  var searchQuery by remember { mutableStateOf("") }
  var selectedSubjectFilter by remember { mutableStateOf("All") }
  val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

  val subjects = remember(submissions) {
    listOf("All") + submissions.map { it.subject }.distinct()
  }

  val filteredSubmissions = remember(submissions, searchQuery, selectedSubjectFilter) {
    submissions.filter { s ->
      val matchesSubject = selectedSubjectFilter == "All" || s.subject.equals(selectedSubjectFilter, ignoreCase = true)
      val matchesSearch = searchQuery.isBlank() ||
        s.studentName.contains(searchQuery, ignoreCase = true) ||
        s.studentMobile.contains(searchQuery, ignoreCase = true) ||
        s.paperTitle.contains(searchQuery, ignoreCase = true)
      matchesSubject && matchesSearch
    }
  }

  val selectedDetail by viewModel.selectedSubmissionForDetail.collectAsState()

  // Detailed Result Inspector Dialog
  if (selectedDetail != null) {
    SubmissionDetailDialog(
      submission = selectedDetail!!,
      viewModel = viewModel,
      onDismiss = { viewModel.selectedSubmissionForDetail.value = null }
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Test Results Dashboard",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
          text = "Total Submissions: ${submissions.size}",
          fontSize = 11.sp,
          color = BioEmeraldSecondary,
          fontWeight = FontWeight.SemiBold
        )
      }

      // Export CSV Button
      Button(
        onClick = {
          val csv = viewModel.repository.generateResultsCsv(submissions)
          val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, csv)
            type = "text/csv"
          }
          val shareIntent = Intent.createChooser(sendIntent, "Export Sap Biology Test Results (CSV)")
          context.startActivity(shareIntent)
        },
        colors = ButtonDefaults.buttonColors(containerColor = BioEmeraldSecondary),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
      ) {
        Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Export CSV", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Search bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      placeholder = { Text("Search by student, mobile or paper...") },
      singleLine = true,
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Subject Filter Chips
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(subjects) { subj ->
        FilterChip(
          selected = selectedSubjectFilter == subj,
          onClick = { selectedSubjectFilter = subj },
          label = { Text(subj, fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = BioGreenPrimary,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    if (filteredSubmissions.isEmpty()) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No test submissions found.", color = Color.Gray, fontSize = 13.sp)
      }
    } else {
      LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(filteredSubmissions, key = { it.id }) { s ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
              .fillMaxWidth()
              .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(12.dp))
              .clickable { viewModel.selectedSubmissionForDetail.value = s }
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = s.studentName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Surface(
                  color = BioGreenContainer,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = "${s.score}/${s.totalQuestions} (${String.format(Locale.US, "%.1f", s.percentage)}%)",
                    color = BioGreenPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = "${s.paperTitle} • ${s.subject}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Text(
                text = "Mobile: ${s.studentMobile} • Date: ${dateFormat.format(Date(s.submittedAt))}",
                fontSize = 11.sp,
                color = Color.Gray
              )

              Spacer(modifier = Modifier.height(6.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Correct: ${s.correctCount}", fontSize = 11.sp, color = BioGreenPrimary, fontWeight = FontWeight.Bold)
                Text("Incorrect: ${s.incorrectCount}", fontSize = 11.sp, color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                Text("Unanswered: ${s.unansweredCount}", fontSize = 11.sp, color = Color.Gray)
              }
            }
          }
        }
      }
    }
  }
}

/**
 * Detailed Submission Inspector: Question, student choice, correct key, correctness badge
 */
@Composable
fun SubmissionDetailDialog(
  submission: TestSubmissionEntity,
  viewModel: SapViewModel,
  onDismiss: () -> Unit
) {
  val questionsFlow = remember(submission.paperId) {
    viewModel.repository.getQuestionsFlow(submission.paperId)
  }
  val questions by questionsFlow.collectAsState(initial = emptyList())

  val studentAnswersMap = remember(submission.answersJson) {
    val map = mutableMapOf<Long, String>()
    try {
      val json = JSONObject(submission.answersJson)
      val keys = json.keys()
      while (keys.hasNext()) {
        val k = keys.next()
        map[k.toLong()] = json.getString(k)
      }
    } catch (e: Exception) {
      // ignore
    }
    map
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Column {
        Text("Result Details: ${submission.studentName}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(
          "${submission.paperTitle} • Score: ${submission.score}/${submission.totalQuestions} (${String.format(Locale.US, "%.1f", submission.percentage)}%)",
          fontSize = 12.sp,
          color = BioGreenPrimary,
          fontWeight = FontWeight.SemiBold
        )
      }
    },
    text = {
      LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(questions) { q ->
          val studentChoice = studentAnswersMap[q.id]?.uppercase() ?: ""
          val isCorrect = studentChoice.isNotBlank() && studentChoice == q.correctAnswer.uppercase()
          val isUnanswered = studentChoice.isBlank()

          Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
              containerColor = when {
                isCorrect -> Color(0xFFE8F5E9)
                isUnanswered -> Color(0xFFF5F5F5)
                else -> Color(0xFFFFEBEE)
              }
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = "Q${q.questionNumber}: ${q.questionText}",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text("A) ${q.optionA}", fontSize = 10.sp)
              Text("B) ${q.optionB}", fontSize = 10.sp)
              Text("C) ${q.optionC}", fontSize = 10.sp)
              Text("D) ${q.optionD}", fontSize = 10.sp)

              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Student Answer: ${if (isUnanswered) "Not Attempted" else studentChoice}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = when {
                  isCorrect -> BioGreenPrimary
                  isUnanswered -> Color.Gray
                  else -> Color(0xFFC62828)
                }
              )
              Text(
                text = "Correct Answer Key: Option ${q.correctAnswer}",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = BioGreenPrimary
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary)
      ) {
        Text("Close")
      }
    }
  )
}

// --- SECTION 12: SECURITY & SETTINGS ---
@Composable
fun AdminSettingsSection(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val scope = rememberCoroutineScope()
  val context = LocalContext.current

  var newPassword by remember { mutableStateOf("") }
  var confirmPassword by remember { mutableStateOf("") }
  var adminEmail by remember { mutableStateOf("admin@sapbiology.com") }
  var statusMessage by remember { mutableStateOf<String?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(
      text = "Account & Security Settings",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = BioGreenPrimary
    )

    Text(
      text = "Update the master administrator login password and notification email for Sap Biology.",
      fontSize = 12.sp,
      color = Color.Gray
    )

    if (statusMessage != null) {
      Surface(
        color = Color(0xFFE8F5E9),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = statusMessage!!,
          color = BioGreenPrimary,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(12.dp)
        )
      }
    }

    OutlinedTextField(
      value = adminEmail,
      onValueChange = { adminEmail = it },
      label = { Text("Admin Email Address") },
      modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
      value = newPassword,
      onValueChange = { newPassword = it },
      label = { Text("New Admin Password") },
      visualTransformation = PasswordVisualTransformation(),
      modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
      value = confirmPassword,
      onValueChange = { confirmPassword = it },
      label = { Text("Confirm New Password") },
      visualTransformation = PasswordVisualTransformation(),
      modifier = Modifier.fillMaxWidth()
    )

    Button(
      onClick = {
        if (newPassword.isBlank()) {
          Toast.makeText(context, "Password cannot be blank", Toast.LENGTH_SHORT).show()
          return@Button
        }
        if (newPassword != confirmPassword) {
          Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
          return@Button
        }
        scope.launch {
          viewModel.repository.updateAdminPassword(newPassword.trim(), adminEmail.trim())
          statusMessage = "Admin password updated successfully! Keep your credentials safe."
        }
      },
      colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
      shape = RoundedCornerShape(10.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
    ) {
      Text("Update Admin Security", fontWeight = FontWeight.Bold)
    }
  }
}
