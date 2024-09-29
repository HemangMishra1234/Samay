package com.project.samay.presentation.backup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.samay.domain.backup.BackupRepo
import kotlinx.coroutines.launch

class BackupScreenViewModel(private val backupRepo: BackupRepo): ViewModel() {

    fun backUpAndShare(context: Context){
        viewModelScope.launch {
            backupRepo.backupAndShareDatabase(context)
        }
    }
}