package com.project.samay.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.project.samay.presentation.calender.CalendarViewModel
import com.project.samay.presentation.calender.CalenderScreen
import com.project.samay.presentation.domains.DomainScreen
import com.project.samay.presentation.domains.DomainViewModel
import com.project.samay.presentation.tasks.TaskViewModel
import com.project.samay.presentation.tasks.TasksScreen
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

enum class NavItem(val label: String, val notSelectedIcon: ImageVector, val icon: ImageVector) {

    TASKS(
        "Tasks",
        Icons.Outlined.Task,
        Icons.Default.Task,
    ),
    DOMAINS("Domains", Icons.Outlined.Category, Icons.Default.Category),
    CALENDAR("Calendar", Icons.Outlined.CalendarToday, Icons.Default.CalendarToday)
}

@Serializable
object NavHomeScreen

@Composable
fun HomeScreen(
    domainViewModel: DomainViewModel,
    taskViewModel: TaskViewModel,
    calendarViewModel: CalendarViewModel,
    navController: NavHostController
) {
    val pagerState = rememberPagerState(pageCount = { NavItem.entries.size })
    val scope = rememberCoroutineScope()

    Scaffold(bottomBar = {
        NavigationBar {
            NavItem.entries.forEachIndexed { index, navItem ->
                NavigationBarItem(icon = {
                    Icon(
                        imageVector = if (pagerState.currentPage == index) navItem.icon else navItem.notSelectedIcon,
                        contentDescription = navItem.label
                    )
                },
                    label = { Text(navItem.label) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(index)
                        }
                    })
            }
        }
    }) { innerPadding ->
        HorizontalPager(
            state = pagerState, modifier = Modifier.padding(innerPadding)
        ) { page ->
            when (NavItem.entries[page]) {
                NavItem.DOMAINS -> DomainScreen(domainViewModel, navController = navController)
                NavItem.TASKS -> TasksScreen(taskViewModel, navController)
                NavItem.CALENDAR-> CalenderScreen(calendarViewModel = calendarViewModel)
            }
        }
    }
}

