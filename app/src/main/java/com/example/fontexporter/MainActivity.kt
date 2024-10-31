package com.example.fontexporter

import android.graphics.fonts.SystemFonts
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                FontListScreen()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun FontListScreen() {
    // Get the list of system fonts
    val fonts = remember { SystemFonts.getAvailableFonts().toList().sortedBy {
        font -> font.file?.nameWithoutExtension
    } }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(fonts) { fontInfo ->
            FontListItem(fontInfo)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun FontListItem(font: android.graphics.fonts.Font) {

    val fontName = font.file?.nameWithoutExtension

    Column {
        if (fontName != null) {
            Text(
                text = fontName,
                modifier = Modifier.padding(bottom = 8.dp)
                    .clickable(onClick = {
                        // Handle the click event here
                        copyFileToDownloads(font.file!!)
                    })
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun FontListScreenPreview() {
    MaterialTheme {
        FontListScreen()
    }
}

private fun copyFileToDownloads(sourceFile: File) {
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val destinationFile = File(downloadsDir, sourceFile.name)

    try {
        // Copy the file
        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        println("File copied to: ${destinationFile.absolutePath}")
    } catch (e: IOException) {
        e.printStackTrace()
        println("Error copying file: ${e.message}")
    }
}