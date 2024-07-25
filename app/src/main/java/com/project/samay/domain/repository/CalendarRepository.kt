package com.project.samay.domain.repository

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import com.project.samay.domain.model.CalendarType
import java.util.TimeZone

class CalendarRepository {
    fun addEvent(context: Context,calenderId: Long, title: String, description: String, startTime: Long, endTime: Long){
        val values = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, calenderId)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.DTSTART, startTime)
            put(CalendarContract.Events.DTEND, endTime)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }
        context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
    }

    fun fetchCalendars(context: Context): List<CalendarType>{
        val cursor = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            arrayOf(
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE
            ),
            null,
            null,
            null
        )
        cursor?.use {
            val calenders = mutableListOf<CalendarType>()
            while (it.moveToNext()) {
                calenders.add(
                    CalendarType(
                        it.getLong(it.getColumnIndexOrThrow(CalendarContract.Calendars._ID)),
                        it.getString(it.getColumnIndexOrThrow(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)),
                        it.getString(it.getColumnIndexOrThrow(CalendarContract.Calendars.ACCOUNT_NAME)),
                        it.getString(it.getColumnIndexOrThrow(CalendarContract.Calendars.ACCOUNT_TYPE))
                    )
                )

            }
            return calenders
        }
        return emptyList()
    }
}