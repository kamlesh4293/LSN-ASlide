package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.google.gson.Gson

class CrawlingView(var ctx:Context) {

    val mHandler: Handler = Handler(Looper.getMainLooper())
    var pixelsToMove = 50
    lateinit var recyclerView: RecyclerView

    val SCROLLING_RUNNABLE: Runnable = object : Runnable {
        override fun run() {
            recyclerView.smoothScrollBy(pixelsToMove, 0)
            mHandler.postDelayed(this, 100)
        }
    }


    fun getCrawlingView(item:Item,list: MutableList<String>): View? {

        var view = (ctx as Activity).layoutInflater.inflate(R.layout.item_recyclerview, null)
        var setting_obj = Gson().fromJson(item.settings, NewsCrawlSettingData::class.java)

        recyclerView = view.findViewById(R.id.rv_recyclerview)


        return view
    }

}