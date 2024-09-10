package com.project.samay.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CalendarEvent(
    @PrimaryKey
    val id: Long,
    val color: String,
    val title: String,
    val description: String,
    val dtstart: Long,  // Changed to Long for Unix timestamp
    val dtend: Long,    // Changed to Long for Unix timestamp
    val calendarId: Long,
    val status: CalendarEventStatus = CalendarEventStatus.NOT_VERIFIED

)

enum class CalendarEventStatus{
    NOT_VERIFIED,
    ADDED,
    REJECTED
}

const val NO_OF_DAYS_BEFORE = 7

