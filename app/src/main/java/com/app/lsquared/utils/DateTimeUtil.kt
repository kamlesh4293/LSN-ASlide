package com.app.lsquared.utils

import android.util.Log
import com.app.lsquared.model.Frame
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
            var date = month_date.format(cal.time)
            return if(date.toInt()<10) date.replace("0","") else date
        }

        fun getDateTemp8():String{
            val cal = Calendar.getInstance()
            val month_date = SimpleDateFormat("dd")
            return month_date.format(cal.time)
        }

        fun getDateTemp9():String{
            val cal = Calendar.getInstance()
            val month_date = SimpleDateFormat("MMMM dd, YYYY")
            return month_date.format(cal.time)
        }


        fun getDayName():String{
            var f = SimpleDateFormat("EEEE")
            return f.format(Date())
        }

        fun getShortDayName():String{
            var f = SimpleDateFormat("EEE")
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

        fun getHour(format:String):String{
            val cal = Calendar.getInstance()
            val month_date = SimpleDateFormat("HH")
            var hours = month_date.format(cal.time).toInt()
            if(format.equals("12")) {
                hours = if(hours>12) hours-12 else hours
            }
            return if(hours<0) "${hours*-1}" else "$hours"
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

        // custom time

        fun createDateForCustomTime(): Date {
            val df = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US)
            val time = df.format(Date())
            val date = df.parse(time)
            Log.d("TAG", "craeteDateForCustomTime: $time")
            return date
        }

        fun createDateForCustomTimeForDeviceOnOff(): Date {
            val df = SimpleDateFormat("hh:mm:ss a", Locale.US)
            val time = df.format(Date())
            val date = df.parse(time)
            Log.d("TAG", "craeteDateForCustomTime: $time")
            return date
        }

        fun createDateForCustomTimeForDeviceOnOffString(): String {
            val df = SimpleDateFormat("HH:mm:ss", Locale.US)
            val time = df.format(Date())
            Log.d("TAG", "createDateForCustomTimeForDeviceOnOffString: $time")
            return time.substring(0,5)
        }

        fun createTimeForContentConfirmation(): String {
            val df = SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.US)
            val time = df.format(Date())
            return time
        }

        fun createTimeForContentConfirmationUTC(): String {
//            val df = SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.US)
//            df.setTimeZone(TimeZone.getTimeZone("UTC"))
            val df = SimpleDateFormat("yyyy/MM/dd kk:mm:ss")
            val time = df.format(Date())
            return time
        }

        fun isValidWithTime(frame: Frame): Boolean {
            val df = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
            val start_time = df.parse(frame.st)
            val end_time = df.parse(frame.et)
            val current_time = createDateForCustomTime()
            Log.d("TAG", "isValidWithTime current ${df.format(current_time)}")
            var check = if(current_time.compareTo(start_time) > 0 && current_time.compareTo(end_time) < 0) true else false
            return check
        }

        fun isValidTimeForMeeting(start: String,end: String,time: String): Boolean {

            Log.d("TAG", "isValidTimeForMeeting - $start, $end, $time")
            val df = SimpleDateFormat("hh:mm a", Locale.US)
            val start_time = df.parse(start)
            val end_time = df.parse(end)
            val current_time = df.parse(time)
            Log.d("TAG", "isValidTimeForMeeting current ${df.format(current_time)}")
            var check = if(current_time.compareTo(start_time) == 0 || current_time.compareTo(start_time) > 0 && current_time.compareTo(end_time) < 0) true else false
            return check
        }

        fun isValidDownloadingTime(start: String,end: String,): Boolean {
            val df = SimpleDateFormat("HH:mm:ss", Locale.US)
            val start_time = df.parse("$start:00")
            val end_time = df.parse("$end:00")
            val current_time = df.parse(df.format(Date()))
            var check = if(current_time.compareTo(start_time) > 0 && current_time.compareTo(end_time) < 0) true else false
            return check
        }

        fun isValidWithTimeForDeviceOnOff(st: String,et:String): Boolean {
            val df = SimpleDateFormat("hh:mm:ss", Locale.US)
            val start_time = df.parse(st)
            val end_time = df.parse(et)
            val current_time = createDateForCustomTimeForDeviceOnOff()
            Log.d("TAG", "isValidWithTime current ${df.format(current_time)}")
            var check = if(current_time.compareTo(start_time) > 0 && current_time.compareTo(end_time) < 0) true else false
            return check
        }

        fun isValidDeviceOnTime(st: String,et:String): Boolean {
            val df = SimpleDateFormat("hh:mm:ss", Locale.US)
            val start_time = df.parse(st)
            val current_time = createDateForCustomTimeForDeviceOnOff()
            return true
        }

        fun setTimeZone(timeZone: String) {
            val tz = TimeZone.getTimeZone(timeZone)
            TimeZone.setDefault(tz)
        }

    }


}