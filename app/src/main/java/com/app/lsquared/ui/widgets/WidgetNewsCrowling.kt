package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.model.news_setting.NewsCrawlSettingData
import com.google.gson.Gson
import top.defaults.drawabletoolbox.DrawableBuilder

class WidgetNewsCrowling {

    companion object{

        fun getWidgetNewsCrowling(ctx: Context, item: Item, data: String): View {

            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_rss, null)
            var setting_obj = Gson().fromJson(item.settings, NewsCrawlSettingData::class.java)

            var crowling_text = view.findViewById<TextView>(R.id.tv_rss_fragment_crawl)
            var llMainRss = view.findViewById<LinearLayout>(R.id.ll_main_rss)

            crowling_text.visibility = View.VISIBLE
            llMainRss.background = DrawableBuilder()
                .solidColor(Color.parseColor(setting_obj.bg))
                .build()
            crowling_text.textSize = setting_obj.fontSize!!.toFloat()
            crowling_text.setTextColor(Color.parseColor(setting_obj.titleText))
            crowling_text.text = data
            crowling_text.isSelected = true

            return view
        }
    }
}