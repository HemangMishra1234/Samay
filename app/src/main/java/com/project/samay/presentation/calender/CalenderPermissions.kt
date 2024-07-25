package com.project.samay.presentation.calender

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
object NavCalenderPermissionScreen

@Composable
fun CheckCalenderPermissions(navController: NavController) {
//    val calendarPermissionState = permission(
//        permission = Manifest.permission.READ_CALENDAR
//    )
    val coroutine = rememberCoroutineScope()
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Grant Below Permissions to use calender")
                }
            }
        }
    }
}