package com.project.samay.presentation.di

import BackUpRepository
import androidx.compose.material.icons.Icons
import androidx.room.Room
import com.project.samay.data.repository.DomainRepository
import com.project.samay.data.repository.TaskRepository
import com.project.samay.data.source.local.AppDatabase
import com.project.samay.domain.notification.NotificationModule
import com.project.samay.domain.repository.CalendarRepository
import com.project.samay.domain.repository.UsageRepository
import com.project.samay.domain.service.StopwatchService
import com.project.samay.domain.usecases.DomainScreenUseCases
import com.project.samay.domain.usecases.FocusScreenUseCases
import com.project.samay.domain.usecases.MonitorAppsScreenUseCases
import com.project.samay.domain.usecases.TaskScreenUseCases
import com.project.samay.presentation.calender.CalendarViewModel
import com.project.samay.presentation.domains.DomainViewModel
import com.project.samay.presentation.focus.FocusViewModel
import com.project.samay.presentation.monitor.MonitorViewModel
import com.project.samay.presentation.tasks.TaskViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single {
        get<BackUpRepository>()
    }
    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { NotificationModule.provideNotificationManager(androidApplication()) }
    single { NotificationModule.provideNotificationBuilder(androidApplication()) }

    single { StopwatchService() }

    single { UsageRepository(androidApplication()) }
    single { CalendarRepository()}
    single { get<AppDatabase>().domainDao() }
    single { get<AppDatabase>().taskDao() }
    single { DomainRepository(get()) }
    single { TaskRepository(get())}

    single { MonitorAppsScreenUseCases(get(), get()) }
    single { DomainScreenUseCases(get()) }
    single { TaskScreenUseCases(get(), get(), get()) }
    single { FocusScreenUseCases(get()) }

    single { FocusViewModel(get()) }

    viewModel { DomainViewModel(get()) }
    viewModel { TaskViewModel(get()) }
    factory { CalendarViewModel(get()) }
    viewModel { MonitorViewModel(get())}
}