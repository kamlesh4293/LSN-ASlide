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
import com.app.lsquared.model.quotes.Quote
import com.app.lsquared.model.quotes.QuotesData
import com.app.lsquared.model.settings.SettingQuotes
import com.google.gson.Gson

class WidgetQuotes {

    companion object{

        var pos = 0
        var size = 0

        fun getWidgetQuotes(ctx: Context, item: Item, data: String?) :View{

            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_quotes, null)

            // setting
            var setting_obj = Gson().fromJson(item.settings,SettingQuotes::class.java)


//            var settings = JSONObject(item.settings)
//            var quotesText = settings.getString("quotesText")
//            var rotate = settings.getInt("rotate")
//            var text_size = settings.getInt("quotesSize")

            var textview = view.findViewById<TextView>(R.id.tv_quotes_quote)
            var ll_bg = view.findViewById<LinearLayout>(R.id.ll_quotes_bg)
            var auther_tv = view.findViewById<TextView>(R.id.tv_quotes_auther)

            // bg
            ll_bg.setBackgroundColor(Color.parseColor(setting_obj.bg))
            // quote
            textview.textSize = setting_obj.quotesSize!!.toFloat()
            textview.setTextColor(Color.parseColor(setting_obj.quotesText))
            // auther
            auther_tv.textSize = setting_obj.authorSize!!.toFloat()
            auther_tv.setTextColor(Color.parseColor(setting_obj.authorText))

            Log.d("TAG", "response data: $data")

            var quotes_data = Gson().fromJson(data, QuotesData::class.java)
            if(quotes_data.quote.size>0){
                Log.d("TAG", "setText: if")
                size = quotes_data.quote.size
                setText(setting_obj?.rotate!!,quotes_data.quote,textview,auther_tv)
            }else{
                Log.d("TAG", "setText: else")
            }
            return view
        }

        private fun setText(
            rotate: Int,
            quote: ArrayList<Quote>,
            textView: TextView,
            auther_tv: TextView
        ) {
            Log.d("TAG", "setText: ${quote[pos].quote}")
            textView.setText(quote[pos].quote)
            auther_tv.setText(quote[pos].author)
        }

    }
}