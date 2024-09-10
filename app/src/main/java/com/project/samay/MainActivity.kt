package com.project.samay

import BackUpRepository
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.project.samay.domain.service.AudioService
import com.project.samay.domain.service.StopwatchService
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
    val domainViewModel by inject<DomainViewModel>()
    val taskViewModel by inject<TaskViewModel>()
    val calendarViewModel by inject<CalendarViewModel>()


    private var isBound by mutableStateOf(false)
    private lateinit var stopwatchService: StopwatchService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as StopwatchService.StopwatchBinder
            stopwatchService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(
            this,
            StopwatchService::class.java,
        ).also {intent->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }

        //Audio service setup:
        val sessionToken = SessionToken(this, ComponentName(this, AudioService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
//        controllerFuture.addListener(

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            if (isBound) {
                val navController = rememberNavController()
                calendarViewModel.fetchCalenders(this@MainActivity)
                SamayTheme(darkTheme = true) {
                    NavHost(navController = navController, startDestination = NavHomeScreen) {
                        composable<NavHomeScreen> {
                            HomeScreen(
                                domainViewModel,
                                taskViewModel,
                                calendarViewModel,
                                usageViewModel,
                                navController,
                                stopwatchService
                            )
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
                            UseDomainScreen(
                                viewModel = domainViewModel,
                                navController = navController
                            )
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
                            UseTaskScreen(
                                taskViewModel = taskViewModel,
                                navController = navController
                            )
                        }
                        composable<NavCalenderScreen> {
                            CalenderScreen(calendarViewModel)
                        }
                    }
                }
            }else{

            }
        }
    }

    override fun onStop() {
        //Unbinding the created connection
        super.onStop()
        unbindService(connection)
        isBound = false
    }

    override fun onResume() {
        super.onResume()
        usageViewModel.getData()
        calendarViewModel.refresh(this)
//        backUpRepository.restoreDatabase(this)
    }

    override fun onPause() {
        super.onPause()
//        backUpRepository.backupDatabase(this)
    }

    @Composable
    fun Test(){
        if(isSystemInDarkTheme()){
            Log.i("MainActivity","Dark Theme")
        }else
            Log.i("MainActivity","Light Theme")
    }

}
