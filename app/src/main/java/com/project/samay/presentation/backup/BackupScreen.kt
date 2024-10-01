package com.project.samay.presentation.backup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun BackupScreen(backupScreenViewModel: BackupScreenViewModel) {
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.i("Backup Screen", "Result code: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK) {
            selectedFileUri = result.data?.data
            backupScreenViewModel.handleFilePickerResult(context, selectedFileUri)
        }
    }
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column {
                Button(onClick = {
                    backupScreenViewModel.backUpAndShare(context)
                }) {
                    Text("Create backup")
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "application/zip"
                    }
                    filePickerLauncher.launch(intent)
                }) {
                    Text(text = "Import files")
                }
            }
        }
    }
}

@Composable
private fun TopBarBackup() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .padding(8.dp)
    ) {
        Text(
            text = "BackUpYourData", modifier = Modifier.align(Alignment.BottomStart),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}