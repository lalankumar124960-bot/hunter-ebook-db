package com.example.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

object ImageStorageHelper {

  /**
   * Copies the chosen visual media from Android Photo Picker (Uri)
   * into the app's persistent internal files directory so it remains
   * permanently accessible across app restarts and database loads.
   */
  fun saveImageToInternalStorage(context: Context, sourceUri: Uri): String? {
    return try {
      val imagesDir = File(context.filesDir, "sap_uploaded_images").apply {
        if (!exists()) mkdirs()
      }
      val fileName = "img_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}.jpg"
      val destinationFile = File(imagesDir, fileName)

      context.contentResolver.openInputStream(sourceUri)?.use { inputStream: InputStream ->
        FileOutputStream(destinationFile).use { outputStream ->
          inputStream.copyTo(outputStream)
        }
      }
      destinationFile.absolutePath
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }
}
