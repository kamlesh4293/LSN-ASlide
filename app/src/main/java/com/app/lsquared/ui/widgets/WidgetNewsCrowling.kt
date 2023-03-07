package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.util.Log.e
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.model.news_setting.NewsCrawlSettingData
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.Utility
import com.google.gson.Gson
import top.defaults.drawabletoolbox.DrawableBuilder
import java.lang.reflect.Field
import java.util.logging.Logger


class WidgetNewsCrowling {

    companion object{

        fun getWidgetNewsCrowling(ctx: Context, item: Item, data: String): View {

            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_rss, null)
            var setting_obj = Gson().fromJson(item.settings, NewsCrawlSettingData::class.java)

            var crowling_text = view.findViewById<TextView>(R.id.tv_rss_fragment_crawl)
            var llMainRss = view.findViewById<LinearLayout>(R.id.ll_main_rss)

            crowling_text.visibility = View.VISIBLE
            llMainRss.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity( setting_obj.bg!!,setting_obj.bga.toString())))
//            llMainRss.background = DrawableBuilder()
//                .solidColor(Color.parseColor(setting_obj.bg))
//                .build()
            crowling_text.textSize = setting_obj.fontSize!!.toFloat()
            crowling_text.setTextColor(Color.parseColor(setting_obj.titleText))
            crowling_text.text = data
            crowling_text.isSelected = true
            setMarqueeSpeed(crowling_text,setting_obj.speed!!.toFloat())

            return view
        }

        fun setMarqueeSpeed(tv: TextView?, speed: Float) {
            if (tv != null) {
                try {
                    var f: Field? = null
                    f = if (tv is AppCompatTextView) {
                        tv.javaClass.superclass.getDeclaredField("mMarquee")
                    } else {
                        tv.javaClass.getDeclaredField("mMarquee")
                    }
                    if (f != null) {
                        f.setAccessible(true)
                        val marquee: Any = f.get(tv)
                        if (marquee != null) {
                            var scrollSpeedFieldName = "mScrollUnit"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                scrollSpeedFieldName = "mPixelsPerSecond"
                            }
                            val mf: Field = marquee.javaClass.getDeclaredField(scrollSpeedFieldName)
                            mf.setAccessible(true)
                            mf.setFloat(marquee, speed)
                        }
                    } else {
                        Log.d("Marquee", "mMarquee object is null.")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}