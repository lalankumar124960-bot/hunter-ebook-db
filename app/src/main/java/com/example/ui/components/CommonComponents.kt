package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.io.File
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.data.local.ExperimentPhotoEntity
import com.example.data.local.TeacherEntity
import com.example.ui.theme.BioEmeraldSecondary
import com.example.ui.theme.BioGreenContainer
import com.example.ui.theme.BioGreenPrimary

/**
 * Top Header requested by specification:
 * Left: Circular 'S' logo/icon for secure Admin Login.
 * Centre: Sap Biology brand name.
 * Right: Circular 'T'/test icon to open online test section.
 */
@Composable
fun SapHeaderBar(
  onAdminClick: () -> Unit,
  onTestPortalClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 3.dp,
    shadowElevation = 2.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Circular 'S' Admin Icon Button
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(
              listOf(BioGreenPrimary, BioEmeraldSecondary)
            )
          )
          .clickable(onClick = onAdminClick)
          .testTag("admin_login_icon"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "S",
          color = Color.White,
          fontSize = 22.sp,
          fontWeight = FontWeight.Black
        )
      }

      // Brand Center
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.weight(1f)
      ) {
        Icon(
          imageVector = Icons.Default.Eco,
          contentDescription = "Sap Biology Leaf Logo",
          tint = BioGreenPrimary,
          modifier = Modifier
            .size(26.dp)
            .padding(end = 6.dp)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "SAP BIOLOGY",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.2.sp
            ),
            color = BioGreenPrimary
          )
          Text(
            text = "PRE-MEDICAL & LIFE SCIENCES",
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = BioEmeraldSecondary,
            letterSpacing = 0.8.sp
          )
        }
      }

      // Circular 'T' Test Icon Button
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(
              listOf(Color(0xFF00796B), Color(0xFF004D40))
            )
          )
          .clickable(onClick = onTestPortalClick)
          .testTag("student_test_icon"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "T",
          color = Color.White,
          fontSize = 22.sp,
          fontWeight = FontWeight.Black
        )
      }
    }
  }
}

@Composable
fun SectionHeader(
  title: String,
  subtitle: String? = null,
  badgeText: String? = null,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    if (badgeText != null) {
      Surface(
        color = BioGreenContainer,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(bottom = 6.dp)
      ) {
        Text(
          text = badgeText.uppercase(),
          color = BioGreenPrimary,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
      }
    }
    Text(
      text = title,
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onBackground
    )
    if (subtitle != null) {
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

/**
 * Visual graphic placeholder for Biology themes: microscope, cells, DNA, anatomy
 */
@Composable
fun BiologyIllustrationPlaceholder(
  seed: String,
  title: String,
  modifier: Modifier = Modifier,
  badge: String? = null
) {
  val gradientColors = when (seed.lowercase()) {
    "mitosis" -> listOf(Color(0xFF1B5E20), Color(0xFF43A047))
    "chromatography" -> listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
    "dna_spooling" -> listOf(Color(0xFF00796B), Color(0xFF26A69A))
    "plasmolysis" -> listOf(Color(0xFF004D40), Color(0xFF00897B))
    "blood_cells" -> listOf(Color(0xFFB71C1C), Color(0xFFE53935))
    "stomata" -> listOf(Color(0xFF33691E), Color(0xFF689F38))
    "mentor_1", "founder_male" -> listOf(Color(0xFF1B5E20), Color(0xFF388E3C))
    "mentor_2", "cofounder_female" -> listOf(Color(0xFF00695C), Color(0xFF00897B))
    else -> listOf(Color(0xFF2E7D32), Color(0xFF81C784))
  }

  Box(
    modifier = modifier
      .background(Brush.radialGradient(gradientColors)),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.padding(8.dp)
    ) {
      Icon(
        imageVector = when {
          seed.contains("dna") || seed.contains("mitosis") -> Icons.Default.Science
          seed.contains("teacher") || seed.contains("mentor") || seed.contains("founder") -> Icons.Default.Person
          seed.contains("quiz") -> Icons.Default.Quiz
          else -> Icons.Default.Eco
        },
        contentDescription = title,
        tint = Color.White.copy(alpha = 0.9f),
        modifier = Modifier.size(36.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = title,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        maxLines = 1
      )
    }

    if (badge != null) {
      Surface(
        color = Color.Black.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(6.dp)
      ) {
        Text(
          text = badge,
          color = Color.White,
          fontSize = 9.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }
    }
  }
}

/**
 * Loads real uploaded/custom image (file path, content uri, or http url) via Coil
 * with seamless fallback to BiologyIllustrationPlaceholder
 */
@Composable
fun SapMediaDisplay(
  imageUrl: String,
  seed: String,
  title: String,
  modifier: Modifier = Modifier,
  badge: String? = null
) {
  if (imageUrl.isNotBlank()) {
    Box(modifier = modifier) {
      AsyncImage(
        model = imageUrl,
        contentDescription = title,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
      if (badge != null) {
        Surface(
          color = Color.Black.copy(alpha = 0.55f),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(6.dp)
        ) {
          Text(
            text = badge,
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }
    }
  } else {
    BiologyIllustrationPlaceholder(
      seed = seed,
      title = title,
      modifier = modifier,
      badge = badge
    )
  }
}

/**
 * Circular Avatar / DP loader for teachers, founder, co-founder with real image support
 */
@Composable
fun SapProfileDp(
  imageUrl: String,
  fallbackIcon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Default.Person,
  modifier: Modifier = Modifier,
  size: androidx.compose.ui.unit.Dp = 72.dp
) {
  Box(
    modifier = modifier
      .size(size)
      .clip(CircleShape)
      .background(BioGreenContainer),
    contentAlignment = Alignment.Center
  ) {
    if (imageUrl.isNotBlank()) {
      AsyncImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    } else {
      Icon(
        imageVector = fallbackIcon,
        contentDescription = null,
        tint = BioGreenPrimary,
        modifier = Modifier.size(size * 0.55f)
      )
    }
  }
}

/**
 * Lightbox / Dialog for viewing experiment photos with caption
 */
@Composable
fun PhotoViewerDialog(
  photo: ExperimentPhotoEntity?,
  onDismiss: () -> Unit
) {
  if (photo == null) return

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
        ) {
          SapMediaDisplay(
            imageUrl = photo.customUrl,
            seed = photo.photoSeed,
            title = photo.title,
            badge = photo.tag,
            modifier = Modifier.fillMaxSize()
          )
          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(8.dp)
              .background(Color.Black.copy(alpha = 0.6f), CircleShape)
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
          }
        }
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = photo.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = BioGreenPrimary
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = photo.caption,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = onDismiss,
            colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
            modifier = Modifier.align(Alignment.End)
          ) {
            Text("Close")
          }
        }
      }
    }
  }
}

/**
 * Teacher Profile Detail Dialog
 */
@Composable
fun TeacherDetailDialog(
  teacher: TeacherEntity?,
  onDismiss: () -> Unit
) {
  if (teacher == null) return

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Teacher Avatar DP
        SapProfileDp(
          imageUrl = teacher.avatarUrl,
          fallbackIcon = Icons.Default.School,
          size = 80.dp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = teacher.name,
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center
        )

        Surface(
          color = BioGreenContainer,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        ) {
          Text(
            text = teacher.subject,
            color = BioGreenPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
          )
        }

        Text(
          text = "Experience: ${teacher.experience}",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
          color = BioEmeraldSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = teacher.bio.ifBlank { teacher.shortIntro },
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
          lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = BioGreenPrimary),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Close Profile")
        }
      }
    }
  }
}

/**
 * Policy Dialog (Privacy Policy / Terms and Conditions)
 */
@Composable
fun PolicyDialog(
  title: String,
  content: String,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        color = BioGreenPrimary
      )
    },
    text = {
      Text(
        text = content,
        style = MaterialTheme.typography.bodyMedium,
        lineHeight = 22.sp
      )
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Understood", color = BioGreenPrimary, fontWeight = FontWeight.Bold)
      }
    }
  )
}

