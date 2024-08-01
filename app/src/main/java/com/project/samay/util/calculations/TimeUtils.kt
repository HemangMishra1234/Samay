package com.project.samay.util.calculations

import java.util.Calendar
import java.util.TimeZone

class TimeUtils {
    companion object{
        fun getHourFromMillis(timeInMillis: Long): Int {
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            calendar.timeInMillis = timeInMillis
            return calendar.get(Calendar.HOUR_OF_DAY)
        }

        fun getMinutesFromMillis(timeInMillis: Long): Int {
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            calendar.timeInMillis = timeInMillis
            return calendar.get(Calendar.MINUTE)
        }

        fun addMinutesToMillis(currentTimeMillis: Long, timeInMinutes: Int): Long {
            // Convert timeInMinutes to milliseconds
            val timeInMillis = timeInMinutes * 60 * 1000L
            // Add the timeInMillis to currentTimeMillis
            return currentTimeMillis + timeInMillis
        }

        fun convertToMillis(hourOfDay: Int, minuteOfHour: Int): Long {
            val calendar = Calendar.getInstance(TimeZone.getDefault())

            // Set the current time in the calendar
            calendar.timeInMillis = System.currentTimeMillis()

            // Update the hour and minute while keeping the rest the same
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minuteOfHour)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            // Return the time in milliseconds
            return calendar.timeInMillis
        }

        fun convertMillisToString(timeInMillis: Long): String {
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            calendar.timeInMillis = timeInMillis
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            return String.format(null,"%02d:%02d", hour, minute)
        }
    }
}