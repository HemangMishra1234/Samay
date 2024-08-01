package com.project.samay.presentation.calender

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.project.samay.SamayApplication
import com.project.samay.domain.model.CalendarType
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object NavCalenderScreen

@Composable
fun CalenderScreen(calendarViewModel: CalendarViewModel) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            val calenders by calendarViewModel.calendarType
            val context = LocalContext.current.applicationContext as SamayApplication
            val selectedCalendarIndex by context.readGoalCalendarFromDataStore(context)
                .collectAsState(initial = null)
            var isSelectDialogueVisible by remember {
                mutableStateOf(false)
            }
            val scope = rememberCoroutineScope()

            Column {
                Text(text = "Select Calender")
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Current calender is: ${
                        calendarViewModel.getCalenderAtIndex(
                            selectedCalendarIndex
                        )?.displayName ?: "None"
                    }"
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedCard(onClick = {
                    isSelectDialogueVisible = true
                },
                    modifier = Modifier) {
                    Text(text = "Select Calender", modifier = Modifier.padding(16.dp))
                }
            }

            AnimatedVisibility(visible = isSelectDialogueVisible) {
                CalenderDialogue(list = calenders) {
                    Toast.makeText(context,it?.displayName , Toast.LENGTH_SHORT).show()
                    if (it != null) {
                        scope.launch {
                            context.saveGoalCalendarToDataStore(context, it.id.toInt())
                        }
                    }
                    isSelectDialogueVisible = false
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalenderDialogue(list: List<CalendarType>, onSelect: (CalendarType?) -> Unit) {
//    val context = LocalContext.current
    BasicAlertDialog(onDismissRequest = { onSelect(null) }) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .clip(RoundedCornerShape(32.dp))
                .padding(16.dp)
        ) {
            Column {
                list.forEach {
                    Text(
                        text = it.displayName,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                onSelect(it)
                            },
                        style = MaterialTheme.typography.bodyMedium,

                    )
                }
            }
        }
    }
}