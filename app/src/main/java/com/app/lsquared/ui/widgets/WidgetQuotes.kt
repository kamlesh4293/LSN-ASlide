package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.view.View
import android.widget.TextView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.model.quotes.Quote
import com.app.lsquared.model.quotes.QuotesData
import com.google.gson.Gson
import org.json.JSONObject

class WidgetQuotes {

    companion object{

        var pos = 0
        var size = 0

        fun getWidgetQuotes(ctx: Context, item: Item, data: String?) :View{

            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_quotes, null)

            // setting
            var settings = JSONObject(item.settings)
            var quotesText = settings.getString("quotesText")
            var rotate = settings.getInt("rotate")
            var text_size = settings.getInt("quotesSize")

            var textview = view.findViewById<TextView>(R.id.tv_quotes_quote)

            textview.textSize = text_size.toFloat()
            textview.setTextColor(Color.parseColor(quotesText));

            var quotes_data = Gson().fromJson(data, QuotesData::class.java)
            if(quotes_data !=null && quotes_data.quote!=null &&quotes_data.quote.size>0){
                size = quotes_data.quote.size
                setText(rotate,quotes_data.quote,textview)
            }
            return view
        }

        private fun setText(rotate: Int, quote: ArrayList<Quote>, textView: TextView) {
            textView.setText(quote[pos].quote)
        }

    }
}