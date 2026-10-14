package com.example.ui.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.IconButton
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BannerEntity
import com.example.data.local.CourseClassEntity
import com.example.data.local.ExperimentPhotoEntity
import com.example.data.local.TeacherEntity
import com.example.ui.components.BiologyIllustrationPlaceholder
import com.example.ui.components.PhotoViewerDialog
import com.example.ui.components.PolicyDialog
import com.example.ui.components.SapHeaderBar
import com.example.ui.components.SapMediaDisplay
import com.example.ui.components.SapProfileDp
import com.example.ui.components.SectionHeader
import com.example.ui.components.TeacherDetailDialog
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.ui.theme.BioEmeraldSecondary
import com.example.ui.theme.BioGreenContainer
import com.example.ui.theme.BioGreenPrimary
import com.example.ui.theme.BioTealTertiary
import com.example.viewmodel.SapViewModel
import com.example.viewmodel.Screen

@Composable
fun HomeScreen(
  viewModel: SapViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val banners by viewModel.banners.collectAsState()
  val teachers by viewModel.teachers.collectAsState()
  val photos by viewModel.experimentPhotos.collectAsState()
  val courses by viewModel.courses.collectAsState()
  val coachingInfo by viewModel.coachingInfo.collectAsState()

  val selectedTeacher by viewModel.selectedTeacherDetail.collectAsState()
  val selectedPhoto by viewModel.selectedExperimentPhoto.collectAsState()
  val showPrivacy by viewModel.showPrivacyPolicyDialog.collectAsState()
  val showTerms by viewModel.showTermsDialog.collectAsState()

  Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
    // Header with Circular 'S' (Admin) and Circular 'T' (Test Portal)
    SapHeaderBar(
      onAdminClick = { viewModel.navigateTo(Screen.AdminLogin) },
      onTestPortalClick = {
        if (viewModel.currentStudent.value != null) {
          viewModel.navigateTo(Screen.StudentPortal)
        } else {
          viewModel.navigateTo(Screen.StudentAuth)
        }
      }
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 32.dp)
    ) {
      // 1. Promotional Banner Carousel Section
      item {
        PromotionsSection(
          banners = banners,
          onBannerAction = {
            if (viewModel.currentStudent.value != null) {
              viewModel.navigateTo(Screen.StudentPortal)
            } else {
              viewModel.navigateTo(Screen.StudentAuth)
            }
          }
        )
      }

      // Quick CTA Strip
      item {
        QuickCallToActionCard(
          onStartTest = {
            if (viewModel.currentStudent.value != null) {
              viewModel.navigateTo(Screen.StudentPortal)
            } else {
              viewModel.navigateTo(Screen.StudentAuth)
            }
          }
        )
      }

      // 2. Teachers Section
      item {
        SectionHeader(
          badgeText = "Faculty Excellence",
          title = "Meet Our Renowned Teachers",
          subtitle = "Experienced subject specialists dedicated to high-yield NCERT understanding and top medical entrance ranks."
        )
      }

      item {
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          items(teachers, key = { it.id }) { teacher ->
            TeacherCard(
              teacher = teacher,
              onClick = { viewModel.selectedTeacherDetail.value = teacher }
            )
          }
        }
      }

      // 3. Our Experiment Moments Section
      item {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          SectionHeader(
            badgeText = "Student & Lab Moments",
            title = "Our Experiment Moments",
            subtitle = "Swipe right to view coaching activities, experiments & student moments.",
            modifier = Modifier.weight(1f)
          )
          Surface(
            color = BioGreenContainer,
            shape = RoundedCornerShape(20.dp)
          ) {
            Text(
              text = "Swipe →",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = BioGreenPrimary,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
          }
        }
      }

      item {
        ExperimentMomentsGallery(
          photos = photos,
          onPhotoClick = { viewModel.selectedExperimentPhoto.value = it }
        )
      }

      // 4. Classes We Teach Section
      item {
        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader(
          badgeText = "Academic Programs",
          title = "Classes We Teach",
          subtitle = "Structured coaching tailored for foundation, board excellence and competitive medical entrance (NEET / Olympiads)."
        )
      }

      items(courses, key = { it.id }) { course ->
        CourseCard(
          course = course,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
      }

      // 5. Part C: Coaching Address, Founder & Co-founder
      item {
        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader(
          badgeText = "Leadership & Campus",
          title = "Mentors & Coaching Address",
          subtitle = "Visit our academic institute or connect with our founders."
        )
      }

      // Founder & Co-founder Cards
      item {
        coachingInfo?.let { info ->
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // Founder
            MentorProfileCard(
              name = info.founderName,
              role = info.founderRole,
              bio = info.founderBio,
              photoUrl = info.founderPhotoUrl,
              photoSeed = info.founderPhotoSeed,
              isFounder = true
            )

            // Co-founder (can be hidden if showCofounder is false)
            if (info.showCofounder) {
              MentorProfileCard(
                name = info.cofounderName,
                role = info.cofounderRole,
                bio = info.cofounderBio,
                photoUrl = info.cofounderPhotoUrl,
                photoSeed = info.cofounderPhotoSeed,
                isFounder = false
              )
            }
          }
        }
      }

      // Address & Map Section
      item {
        coachingInfo?.let { info ->
          AddressCard(
            info = info,
            onOpenMap = {
              try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(info.googleMapsUrl))
                context.startActivity(intent)
              } catch (e: Exception) {
                // fallback to general search query
                val searchIntent = Intent(
                  Intent.ACTION_VIEW,
                  Uri.parse("geo:0,0?q=${Uri.encode("${info.instituteName} ${info.city}")}")
                )
                context.startActivity(searchIntent)
              }
            },
            modifier = Modifier.padding(16.dp)
          )
        }
      }

      // Footer: Contact, Policies & Copyright
      item {
        coachingInfo?.let { info ->
          FooterSection(
            info = info,
            onPrivacyClick = { viewModel.showPrivacyPolicyDialog.value = true },
            onTermsClick = { viewModel.showTermsDialog.value = true }
          )
        }
      }
    }
  }

  // Dialogs
  TeacherDetailDialog(
    teacher = selectedTeacher,
    onDismiss = { viewModel.selectedTeacherDetail.value = null }
  )

  PhotoViewerDialog(
    photo = selectedPhoto,
    onDismiss = { viewModel.selectedExperimentPhoto.value = null }
  )

  if (showPrivacy && coachingInfo != null) {
    PolicyDialog(
      title = "Privacy Policy",
      content = coachingInfo!!.privacyPolicy,
      onDismiss = { viewModel.showPrivacyPolicyDialog.value = false }
    )
  }

  if (showTerms && coachingInfo != null) {
    PolicyDialog(
      title = "Terms & Conditions",
      content = coachingInfo!!.termsAndConditions,
      onDismiss = { viewModel.showTermsDialog.value = false }
    )
  }
}

@Composable
fun PromotionsSection(
  banners: List<BannerEntity>,
  onBannerAction: () -> Unit
) {
  if (banners.isEmpty()) return

  LazyRow(
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    items(banners, key = { it.id }) { banner ->
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
          .width(310.dp)
          .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(16.dp))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(
                listOf(BioGreenContainer.copy(alpha = 0.6f), Color.White)
              )
            )
            .padding(16.dp)
        ) {
          if (banner.imageUrl.isNotBlank()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(10.dp))
            ) {
              AsyncImage(
                model = banner.imageUrl,
                contentDescription = banner.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
          }

          Surface(
            color = BioGreenPrimary,
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = banner.tag,
              color = Color.White,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = banner.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = banner.subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = onBannerAction,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            modifier = Modifier.testTag("banner_action_btn_${banner.id}")
          ) {
            Text(
              text = banner.actionText,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun QuickCallToActionCard(
  onStartTest: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = BioGreenPrimary),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 10.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "Online Objective Test Portal",
          color = Color.White,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
          text = "Tap the circular T button or enroll to evaluate your NCERT preparation.",
          color = Color.White.copy(alpha = 0.85f),
          fontSize = 11.sp
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Button(
        onClick = onStartTest,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.testTag("portal_cta_btn")
      ) {
        Text("Take Test", color = BioGreenPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
      }
    }
  }
}

@Composable
fun TeacherCard(
  teacher: TeacherEntity,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .width(220.dp)
      .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .testTag("teacher_card_${teacher.id}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Profile Picture DP
      SapProfileDp(
        imageUrl = teacher.avatarUrl,
        fallbackIcon = Icons.Default.School,
        size = 72.dp
      )

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = teacher.name,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Text(
        text = teacher.subject,
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
        color = BioGreenPrimary,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = teacher.experience,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = BioEmeraldSecondary
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = teacher.shortIntro,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        lineHeight = 14.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text("View Profile", fontSize = 11.sp, color = BioGreenPrimary, fontWeight = FontWeight.Bold)
      }
    }
  }
}

/**
 * Displays 2 photos side by side per slide with horizontal swipe navigation (HorizontalPager)
 * Allows browsing unlimited photos with teachers & students smoothly without page clutter.
 */
@Composable
fun ExperimentMomentsGallery(
  photos: List<ExperimentPhotoEntity>,
  onPhotoClick: (ExperimentPhotoEntity) -> Unit
) {
  if (photos.isEmpty()) return

  // Group photos in pairs of 2 so exactly 2 photos appear per section view
  val pairedPhotos = photos.chunked(2)
  val pagerState = rememberPagerState { pairedPhotos.size }
  val scope = rememberCoroutineScope()

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    // Top bar for gallery: page counter and quick previous/next buttons
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Surface(
        color = BioGreenContainer,
        shape = RoundedCornerShape(12.dp)
      ) {
        Text(
          text = "2 Photos / View • Slide ${pagerState.currentPage + 1} of ${pairedPhotos.size}",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = BioGreenPrimary,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
      }

      Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = {
            if (pagerState.currentPage > 0) {
              scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
            }
          },
          enabled = pagerState.currentPage > 0,
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Previous Photos",
            tint = if (pagerState.currentPage > 0) BioGreenPrimary else Color.LightGray,
            modifier = Modifier.size(18.dp)
          )
        }

        IconButton(
          onClick = {
            if (pagerState.currentPage < pairedPhotos.size - 1) {
              scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
            }
          },
          enabled = pagerState.currentPage < pairedPhotos.size - 1,
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Next Photos",
            tint = if (pagerState.currentPage < pairedPhotos.size - 1) BioGreenPrimary else Color.LightGray,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }

    // Horizontal Swipeable Pager: exactly 2 photos per page side-by-side
    HorizontalPager(
      state = pagerState,
      modifier = Modifier.fillMaxWidth()
    ) { pageIndex ->
      val pair = pairedPhotos[pageIndex]
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        pair.forEach { photo ->
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
              .weight(1f)
              .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(14.dp))
              .clickable { onPhotoClick(photo) }
              .testTag("experiment_photo_${photo.id}")
          ) {
            Column(modifier = Modifier.fillMaxWidth()) {
              SapMediaDisplay(
                imageUrl = photo.customUrl,
                seed = photo.photoSeed,
                title = photo.title,
                badge = photo.tag,
                modifier = Modifier
                  .fillMaxWidth()
                  .height(130.dp)
              )
              Column(modifier = Modifier.padding(10.dp)) {
                Text(
                  text = photo.title,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = photo.caption,
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 2,
                  overflow = TextOverflow.Ellipsis,
                  lineHeight = 13.sp
                )
              }
            }
          }
        }
        if (pair.size == 1) {
          Spacer(modifier = Modifier.weight(1f))
        }
      }
    }

    // Pagination Dots Indicator
    if (pairedPhotos.size > 1) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 2.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        repeat(pairedPhotos.size) { index ->
          val isSelected = pagerState.currentPage == index
          Box(
            modifier = Modifier
              .padding(horizontal = 3.dp)
              .size(if (isSelected) 18.dp else 6.dp, 6.dp)
              .clip(CircleShape)
              .background(if (isSelected) BioGreenPrimary else Color(0xFFC8D6C9))
          )
        }
      }
    }
  }
}

@Composable
fun CourseCard(
  course: CourseClassEntity,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
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
        Text(
          text = course.title,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.weight(1f)
        )
        Surface(
          color = BioGreenContainer,
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = course.grade,
            color = BioGreenPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = course.targetAudience,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = BioEmeraldSecondary
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = course.description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Features list
      val features = course.featuresList.split(";").filter { it.isNotBlank() }
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        features.forEach { feature ->
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = BioGreenPrimary,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = feature.trim(),
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Schedule,
          contentDescription = null,
          tint = BioTealTertiary,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = course.timings,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
          color = BioTealTertiary
        )
      }
    }
  }
}

@Composable
fun MentorProfileCard(
  name: String,
  role: String,
  bio: String,
  photoUrl: String = "",
  photoSeed: String = "",
  isFounder: Boolean
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(16.dp))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.Top
    ) {
      SapProfileDp(
        imageUrl = photoUrl,
        fallbackIcon = if (isFounder) Icons.Default.Person else Icons.Default.School,
        size = 64.dp
      )

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = name,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Text(
          text = role,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (isFounder) BioGreenPrimary else BioTealTertiary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = bio,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 16.sp
        )
      }
    }
  }
}

@Composable
fun AddressCard(
  info: com.example.data.local.CoachingInfoEntity,
  onOpenMap: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(16.dp))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = BioGreenPrimary,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = info.instituteName,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = BioGreenPrimary
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "${info.locality}, ${info.city}, ${info.district}, ${info.state} - ${info.pinCode}",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = 20.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Call,
          contentDescription = null,
          tint = BioEmeraldSecondary,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = info.phone,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Email,
          contentDescription = null,
          tint = BioEmeraldSecondary,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = info.email,
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Button(
        onClick = onOpenMap,
        colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("directions_button")
      ) {
        Icon(
          imageVector = Icons.Default.Map,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Get Directions on Google Maps", fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
fun FooterSection(
  info: com.example.data.local.CoachingInfoEntity,
  onPrivacyClick: () -> Unit,
  onTermsClick: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0xFFF1F5F0))
      .padding(vertical = 24.dp, horizontal = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = info.instituteName,
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = BioGreenPrimary
    )
    Text(
      text = info.tagline,
      fontSize = 11.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(14.dp))

    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      TextButton(onClick = onPrivacyClick) {
        Text("Privacy Policy", fontSize = 12.sp, color = BioGreenPrimary)
      }
      Text("•", color = Color.Gray, modifier = Modifier.padding(horizontal = 4.dp))
      TextButton(onClick = onTermsClick) {
        Text("Terms & Conditions", fontSize = 12.sp, color = BioGreenPrimary)
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = info.copyrightText,
      fontSize = 11.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
      textAlign = TextAlign.Center
    )
  }
}
