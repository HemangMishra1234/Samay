package com.project.samay.presentation.calender

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.samay.domain.model.CalendarType
import com.project.samay.domain.repository.CalendarRepository
import kotlinx.coroutines.launch

class CalendarViewModel(private val calendarRepository: CalendarRepository) : ViewModel() {
    private var _calendarsList = mutableStateOf(emptyList<CalendarType>())
    val calendarType: State<List<CalendarType>> = _calendarsList

    fun getCalenderAtIndex(index: Int?): CalendarType? {
        if (index == null)
            return null
        if (index < _calendarsList.value.size) {
            return _calendarsList.value[index]
        }
        return null
    }

    fun fetchCalenders(context: Context) {
        viewModelScope.launch {
            _calendarsList.value = calendarRepository.fetchCalendars(context)
        }
    }
}

