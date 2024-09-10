package com.project.samay.domain.repository

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import com.project.samay.data.source.local.CalendarDao
import com.project.samay.domain.model.CalendarEvent
import com.project.samay.domain.model.CalendarType
import com.project.samay.domain.model.NO_OF_DAYS_BEFORE
import com.project.samay.util.calculations.TimeUtils
import java.util.TimeZone
import java.util.concurrent.Flow

class CalendarRepository(private val calendarDao: CalendarDao) {
    val allSavedRoomEntries = calendarDao.getAllCalendarEvents()

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
//    CalendarContract.Events

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

    fun fetchEventsOFLastWeek(context: Context): List<CalendarEvent> {
        val timeInMillisOfDaysAgo = TimeUtils.getTimeInMillisOfDaysAgo(NO_OF_DAYS_BEFORE)
        val endTimeInMillis = System.currentTimeMillis()

        // Define the URI for the Instances table and specify the time range
        val builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, timeInMillisOfDaysAgo)
        ContentUris.appendId(builder, endTimeInMillis)
        val instancesUri = builder.build()

        val selection = "${CalendarContract.Instances.BEGIN} >= ? AND ${CalendarContract.Instances.END} <= ?"
        val selectionArgs = arrayOf(timeInMillisOfDaysAgo.toString(), endTimeInMillis.toString())

        // Query the Instances table to get both normal and recurring events
        val cursor = context.contentResolver.query(
            instancesUri,
            arrayOf(
                CalendarContract.Instances.EVENT_ID,
                CalendarContract.Instances.BEGIN,
                CalendarContract.Instances.END,
                CalendarContract.Instances.TITLE,
                CalendarContract.Instances.DESCRIPTION,
                CalendarContract.Instances.CALENDAR_COLOR,
                CalendarContract.Instances.CALENDAR_ID
            ),
            selection,
            selectionArgs,
            "${CalendarContract.Instances.END} DESC"
        )

        cursor?.use {
            val events = mutableListOf<CalendarEvent>()
            while (it.moveToNext()) {
                events.add(
                    CalendarEvent(
                        it.getLong(it.getColumnIndexOrThrow(CalendarContract.Instances.EVENT_ID)),
                        it.getString(it.getColumnIndexOrThrow(CalendarContract.Instances.CALENDAR_COLOR)) ?: "", // Handle null
                        it.getString(it.getColumnIndexOrThrow(CalendarContract.Instances.TITLE)) ?: "", // Handle null
                        it.getString(it.getColumnIndexOrThrow(CalendarContract.Instances.DESCRIPTION)) ?: "", // Handle null
                        it.getLong(it.getColumnIndexOrThrow(CalendarContract.Instances.BEGIN)),
                        it.getLong(it.getColumnIndexOrThrow(CalendarContract.Instances.END)),
                        it.getLong(it.getColumnIndexOrThrow(CalendarContract.Instances.CALENDAR_ID))
                    )
                )
            }
            Log.i("CalendarRepository", "fetchEventsOfLastWeek: $events")
            return events
        }

        return emptyList()
    }


    suspend fun addEventToDatabase(calendarEvent: CalendarEvent){
        calendarDao.upsertCalendarEvent(calendarEvent)
    }

    suspend fun deleteEventFromDatabase(calendarEvent: CalendarEvent){
        calendarDao.deleteCalendarEvent(calendarEvent)
    }

}