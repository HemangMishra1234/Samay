package com.project.samay.presentation.tasks

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.samay.SamayApplication
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object NavTargetScreen

@Composable
fun TargetScreen(navController: NavController) {
    val context = LocalContext.current.applicationContext as SamayApplication
    val targetInitial by context.readTargetFromDataStore(context).collectAsState(initial = 15)
    var target by remember {
        mutableStateOf(targetInitial?.toString()?:"15")
    }
    val coroutine = rememberCoroutineScope()
    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text("Change Target")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = target, onValueChange = {
                    target = it
                })
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    if (target.toIntOrNull() == null) {
                        Toast.makeText(context, "Please enter a valid number", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }
                    if (target.toInt() < 7) {
                        Toast.makeText(
                            context,
                            "Please enter a number greater than 7",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    coroutine.launch {
                        context.saveTargetToDataStore(context, target.toInt())
                        navController.navigateUp()
                    }
                }) {
                    Text(text = "Save Target")
                }
            }

        }
    }
}