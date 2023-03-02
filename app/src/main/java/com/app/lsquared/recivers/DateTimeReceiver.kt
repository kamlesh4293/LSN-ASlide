package com.app.lsquared.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter




class DateTimeReceiver : BroadcastReceiver(){

    companion object{

        fun getTimeIntent(): IntentFilter {
            var intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_TIME_CHANGED)
            intentFilter.addAction(Intent.ACTION_TIME_TICK)
            intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
            return intentFilter
        }
    }


    override fun onReceive(p0: Context?, p1: Intent?) {
    }

}