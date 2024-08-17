package com.project.samay.util

fun formatTimeToStopwatchType(seconds: String, hours: String, minutes: String): String{
    return "$hours:$minutes:$seconds"
}

fun Int.pad(): String{
    return this.toString().padStart(2,'0')
}