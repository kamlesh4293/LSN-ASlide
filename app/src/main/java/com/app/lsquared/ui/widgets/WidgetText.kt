package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import org.json.JSONObject

class WidgetText {

    companion object{

        fun getWidgetTextStatic(ctx: Context, item: Item, text: String?):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_text,null)

            var textview = view.findViewById<TextView>(R.id.tv_text_static)
            var layout = view.findViewById<LinearLayout>(R.id.ll_text_main)
            textview.visibility = View.VISIBLE

            // setting
            var settings = JSONObject(item.settings)
            var bg_color = settings.getString("bg")
            layout.setBackgroundColor(Color.parseColor(bg_color));


            var text_size = settings.getInt("size")
            var text_color = settings.getString("titleText")

            textview.textSize = text_size.toFloat()
            textview.setTextColor(Color.parseColor(text_color))

            textview.text = text
            return view
        }

        fun getWidgetTextCrowling(ctx: Context, item: Item, data: String?):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_text_crawling,null)

            var textview = view.findViewById<TextView>(R.id.tv_text_crawling)
            var layout = view.findViewById<LinearLayout>(R.id.ll_text_main)
            textview.visibility = View.VISIBLE

            // setting
            var settings = JSONObject(item.settings)
            var bg_color = settings.getString("bg")
            layout.setBackgroundColor(Color.parseColor(bg_color));

            var text_size = settings.getInt("fontSize")
            var text_color = settings.getString("titleText")

            textview.textSize = text_size.toFloat()
            textview.setTextColor(Color.parseColor(text_color))
            textview.isSelected = true


            var list = mutableListOf<String>()
            var json_data = JSONObject(data)
            var channel_array = json_data.getJSONArray("channel")
            for (i in 0 until channel_array.length()){
                var title_obj = channel_array.getJSONObject(i).getString("title")
                Log.d("TAG", "setData: listitem $i - $title_obj")
                list.add(title_obj)
            }

            var builer = StringBuilder()
            for (i in list){
                builer.append("$i       ")
            }
            textview.text = builer.toString()
            return view
        }

    }

}