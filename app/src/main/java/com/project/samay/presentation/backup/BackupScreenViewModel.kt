package com.project.samay.presentation.backup


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.samay.domain.backup.BackupRepo
import kotlinx.coroutines.launch
import java.io.File
import java.util.zip.ZipFile

class BackupScreenViewModel(private val backupRepo: BackupRepo): ViewModel() {

    fun backUpAndShare(context: Context){
        viewModelScope.launch {
            backupRepo.backupAndShareDatabase(context)
        }
    }

    fun restoreDatabase(context: Context, zipFile: File){
        viewModelScope.launch {
            backupRepo.restoreDatabase(context, zipFile)
        }
    }

    fun handleFilePickerResult(context: Context, uri: Uri?){
        uri?.let {
            val zipFile = it.path?.let { it1 -> File(it1) }
            if (zipFile != null) {
                restoreDatabase(context, zipFile)
            }
            else{
                Log.i("Backup Screen", "Selected File Uri is null")
            }
        }
    }
}