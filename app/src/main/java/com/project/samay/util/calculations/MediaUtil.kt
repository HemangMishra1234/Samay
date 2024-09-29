package com.project.samay.util.calculations

class MediaUtil {
    companion object{
        fun getTimeStampFromSeconds(sec: Long): String{
            val hours = sec/3600
            val minutes = (sec % 3600) / 60
            val seconds = sec % 60
            if(hours.toInt() == 0)
                return "${minutes.toString().padStart(2,'0')}:${seconds.toString().padStart(2,'0')}"
            return "${hours.toString().padStart(2,'0')}:${minutes.toString().padStart(2,'0')}:${seconds.toString().padStart(2,'0')}"
        }
    }
}