package com.project.samay.util.calculations

import com.project.samay.presentation.domains.toOneDecimalPlace
import com.project.samay.util.ProfileColors

class Logic {
    companion object{
        fun rateCalculator(presentPercent : Float, expectedPercent: Float): Float{
            val rate: Float
            var lowerBound = 20.0F
            if(expectedPercent < lowerBound)
                lowerBound = expectedPercent
            //  Log.e("Rate calculator","Lower bound = $lowerBound, expectedPercent= $expectedPercent, presentPercent = $presentPercent")
            rate = if(presentPercent > (expectedPercent)  && presentPercent < (expectedPercent + 20)){
                350.0F - (presentPercent - expectedPercent)*(350.0F - 50.0F)/20.0F
            }else if(presentPercent < expectedPercent && presentPercent > (expectedPercent-lowerBound)) {
                350.0F + (750.0F - 350.0F)*(expectedPercent - presentPercent)/lowerBound
            }else if(presentPercent >= (expectedPercent + 20))
                50.0F
            else if(presentPercent <= (expectedPercent - lowerBound))
                750.0F
            else
                350.0F
            return rate
        }

        fun getColor(size: Int): Long{
            val noOfColors = ProfileColors.entries.size
            val color = ProfileColors.entries.elementAt(size % noOfColors)
            return color.hex
        }

        fun formatInHrsAndMins(timeInMin : Long): String{
            val hours = timeInMin/60
            val minutes = timeInMin%60
            return "${hours}h ${minutes}m"
        }

        fun calculateTargetTime(target: Int, expectedPercent: Float): Long{
            val targetTime = (target*expectedPercent*60)/100
            return targetTime.toLong()
        }
    }
}