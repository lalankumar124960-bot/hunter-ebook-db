package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.BioEmeraldSecondary
import com.example.ui.theme.BioGreenContainer
import com.example.ui.theme.BioGreenPrimary
import com.example.util.ImageStorageHelper

/**
 * Reusable Photo / DP Upload Component for Admin Panel.
 * Uses Android Photo Picker (zero-permission, Play Store compliant)
 * and persists the selected file to internal storage.
 */
@Composable
fun PhotoUploadPickerField(
  currentImageUrl: String,
  onImageSelected: (String) -> Unit,
  label: String = "Upload Photo / DP",
  isCircle: Boolean = false,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var showUrlInput by remember { mutableStateOf(false) }
  var showPresetsDialog by remember { mutableStateOf(false) }
  var manualUrl by remember { mutableStateOf("") }

  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia(),
    onResult = { uri: Uri? ->
      if (uri != null) {
        val savedPath = ImageStorageHelper.saveImageToInternalStorage(context, uri)
        if (savedPath != null) {
          onImageSelected(savedPath)
        }
      }
    }
  )

  Column(modifier = modifier.fillMaxWidth()) {
    Text(
      text = label,
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(6.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Preview thumbnail
      if (isCircle) {
        Box(
          modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(BioGreenContainer)
            .border(1.5.dp, BioGreenPrimary, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          if (currentImageUrl.isNotBlank()) {
            AsyncImage(
              model = currentImageUrl,
              contentDescription = "Preview DP",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          } else {
            Icon(
              imageVector = Icons.Default.AddPhotoAlternate,
              contentDescription = null,
              tint = BioGreenPrimary,
              modifier = Modifier.size(32.dp)
            )
          }
        }
      } else {
        Box(
          modifier = Modifier
            .size(width = 90.dp, height = 65.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(BioGreenContainer)
            .border(1.5.dp, BioGreenPrimary, RoundedCornerShape(8.dp)),
          contentAlignment = Alignment.Center
        ) {
          if (currentImageUrl.isNotBlank()) {
            AsyncImage(
              model = currentImageUrl,
              contentDescription = "Preview Image",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          } else {
            Icon(
              imageVector = Icons.Default.AddPhotoAlternate,
              contentDescription = null,
              tint = BioGreenPrimary,
              modifier = Modifier.size(32.dp)
            )
          }
        }
      }

      // Action buttons
      Column(modifier = Modifier.weight(1f)) {
        OutlinedButton(
          onClick = {
            photoPickerLauncher.launch(
              PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
          },
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = BioGreenPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("choose_photo_btn")
        ) {
          Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (currentImageUrl.isNotBlank()) "Replace from Gallery" else "Upload from Gallery",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TextButton(
              onClick = { showPresetsDialog = true }
            ) {
              Text("Presets", fontSize = 10.sp, color = BioGreenPrimary, fontWeight = FontWeight.Bold)
            }
            TextButton(
              onClick = { showUrlInput = !showUrlInput }
            ) {
              Text(
                text = if (showUrlInput) "Hide URL" else "Paste URL",
                fontSize = 10.sp,
                color = BioEmeraldSecondary
              )
            }
          }

          if (currentImageUrl.isNotBlank()) {
            TextButton(
              onClick = { onImageSelected("") }
            ) {
              Text("Remove", fontSize = 10.sp, color = Color(0xFFC62828))
            }
          }
        }
      }
    }

    if (showUrlInput) {
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = manualUrl,
          onValueChange = { manualUrl = it },
          placeholder = { Text("https://example.com/photo.jpg", fontSize = 11.sp) },
          singleLine = true,
          modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(6.dp))
        OutlinedButton(
          onClick = {
            if (manualUrl.isNotBlank()) {
              onImageSelected(manualUrl.trim())
              manualUrl = ""
              showUrlInput = false
            }
          },
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Apply", fontSize = 11.sp)
        }
      }
    }
  }

  if (showPresetsDialog) {
    val samplePresets = if (isCircle) {
      listOf(
        "Senior Biology Mentor DP" to "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500&auto=format&fit=crop&q=80",
        "Professor / Academic Head DP" to "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500&auto=format&fit=crop&q=80",
        "Female Faculty DP" to "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=500&auto=format&fit=crop&q=80",
        "Botany Specialist DP" to "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=500&auto=format&fit=crop&q=80"
      )
    } else {
      listOf(
        "Classroom Batch & Discussion" to "https://images.unsplash.com/photo-1523240795612-9a054b0db644?w=700&auto=format&fit=crop&q=80",
        "Microscope Lab Experiment" to "https://images.unsplash.com/photo-1532094349884-543bc11b234d?w=700&auto=format&fit=crop&q=80",
        "Students Study Group" to "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=700&auto=format&fit=crop&q=80",
        "Biology Science Laboratory" to "https://images.unsplash.com/photo-1576086213369-97a306d36557?w=700&auto=format&fit=crop&q=80",
        "DNA & Pipette Research" to "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=700&auto=format&fit=crop&q=80"
      )
    }

    AlertDialog(
      onDismissRequest = { showPresetsDialog = false },
      title = { Text("Choose Photo Preset", fontWeight = FontWeight.Bold, color = BioGreenPrimary) },
      text = {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          items(samplePresets) { (name, url) ->
            Card(
              shape = RoundedCornerShape(10.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE0E5DF), RoundedCornerShape(10.dp))
                .clickable {
                  onImageSelected(url)
                  showPresetsDialog = false
                }
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(if (isCircle) 44.dp else 54.dp, 44.dp)
                    .clip(if (isCircle) CircleShape else RoundedCornerShape(6.dp))
                ) {
                  AsyncImage(
                    model = url,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = name, fontWeight = FontWeight.Medium, fontSize = 12.sp, modifier = Modifier.weight(1f))
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showPresetsDialog = false }) {
          Text("Cancel", color = BioGreenPrimary)
        }
      }
    )
  }
}

