package com.project.samay

import BackUpRepository
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.project.samay.presentation.HomeScreen
import com.project.samay.presentation.NavHomeScreen
import com.project.samay.presentation.calender.CalendarViewModel
import com.project.samay.presentation.calender.CalenderScreen
import com.project.samay.presentation.calender.NavCalenderScreen
import com.project.samay.presentation.domains.AddDomainScreen
import com.project.samay.presentation.domains.DomainViewModel
import com.project.samay.presentation.domains.NavAddDomainScreen
import com.project.samay.presentation.domains.NavUseDomainScreen
import com.project.samay.presentation.domains.UseDomainScreen
import com.project.samay.presentation.monitor.MonitorViewModel
import com.project.samay.presentation.tasks.AddTaskScreen
import com.project.samay.presentation.tasks.NavAddTaskScreen
import com.project.samay.presentation.tasks.NavTargetScreen
import com.project.samay.presentation.tasks.NavUseTaskScreen
import com.project.samay.presentation.tasks.TargetScreen
import com.project.samay.presentation.tasks.TaskViewModel
import com.project.samay.presentation.tasks.UseTaskScreen
import com.project.samay.ui.theme.SamayTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    val backUpRepository = BackUpRepository()
    val usageViewModel by inject<MonitorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val domainViewModel by inject<DomainViewModel>()
        val taskViewModel by inject<TaskViewModel>()
        val calendarViewModel by inject<CalendarViewModel>()
        setContent {
            val navController = rememberNavController()
            calendarViewModel.fetchCalenders(this@MainActivity)
            SamayTheme {
                NavHost(navController = navController, startDestination = NavHomeScreen) {
                    composable<NavHomeScreen> {
                        HomeScreen(domainViewModel, taskViewModel, calendarViewModel,usageViewModel, navController)
                    }
                    composable<NavAddDomainScreen> {
                        val isUpdate = it.toRoute<NavAddDomainScreen>().isUpdate
                        AddDomainScreen(
                            viewModel = domainViewModel,
                            isUpdate = isUpdate,
                            navController = navController
                        )
                    }
                    composable<NavUseDomainScreen> {
                        UseDomainScreen(viewModel = domainViewModel, navController = navController)
                    }
                    composable<NavTargetScreen> {
                        TargetScreen(navController = navController)
                    }
                    composable<NavAddTaskScreen> {
                        val isUpdate = it.toRoute<NavAddTaskScreen>().isUpdate
                        AddTaskScreen(
                            taskViewModel = taskViewModel,
                            isUpdate = isUpdate,
                            navController = navController
                        )
                    }
                    composable<NavUseTaskScreen> {
                        UseTaskScreen(taskViewModel = taskViewModel, navController = navController)
                    }
                    composable<NavCalenderScreen> {
                        CalenderScreen(calendarViewModel)
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        usageViewModel.getData()
//        backUpRepository.restoreDatabase(this)
    }

    override fun onPause() {
        super.onPause()
//        backUpRepository.backupDatabase(this)
    }

}
