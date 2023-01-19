package com.app.lsquared.utils

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {

    companion object{

        fun getYear():String{
            val cal = Calendar.getInstance()
            val year = SimpleDateFormat("yyyy")
            return year.format(cal.time)
        }

        fun getMonth():String{
            val cal = Calendar.getInstance()
            val month_date = SimpleDateFormat("MMMM")
            return month_date.format(cal.time)
        }

        fun getDate():String{
            val cal = Calendar.getInstance()
            val month_date = SimpleDateFormat("dd")
            return month_date.format(cal.time)
        }

        fun getDayName():String{
            var f = SimpleDateFormat("EEEE")
            return f.format(Date())
        }

        fun getTime():String{
            val cal = Calendar.getInstance()
            val month_date = SimpleDateFormat("HH:mm")
            return month_date.format(cal.time)
        }

        fun getHour():String{
            val cal = Calendar.getInstance()
            val month_date = SimpleDateFormat("HH")
            return month_date.format(cal.time)
        }

        fun getMinutes():String{
            val cal = Calendar.getInstance()
            val month_date = SimpleDateFormat("mm")
            return month_date.format(cal.time)
        }


        fun getTimeSeconds():String{
            val cal = Calendar.getInstance()
            val simpleformat = SimpleDateFormat("dd/MMMM/yyyy hh:mm:s")
            return simpleformat.format(cal.time)
        }

        fun secondsToMinutes(duration: Float):String{
            if(duration == null || duration.toInt()==0) return "00:00"
            var minutes = (duration/60).toString().substringBefore(".")
            var seconds = (duration%60).toString().substringBefore(".")

            if(minutes.length==1)minutes = "0$minutes"
            if(seconds.length==1)seconds = "0$seconds"

            return "$minutes:$seconds"
        }


    }


}