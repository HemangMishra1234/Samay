package com.project.samay.presentation.permissions

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.project.samay.domain.model.PermissionsRequired

class PermissionViewModel: ViewModel() {
    val visiblePermissionDialogQueue = mutableStateListOf<PermissionsRequired>()

    fun dismissPermissionDialog(){
        visiblePermissionDialogQueue.removeLast()
    }

    fun onPermissionDialogue(permission: PermissionsRequired,
                             isGranted: Boolean){
        if(!isGranted){
            visiblePermissionDialogQueue.add(0, permission)
        }
    }
}