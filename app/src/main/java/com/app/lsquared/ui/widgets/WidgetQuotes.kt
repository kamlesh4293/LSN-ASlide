package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.model.quotes.Quote
import com.app.lsquared.model.quotes.QuotesData
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.FontUtil
import com.app.lsquared.utils.ImageUtil
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


class WidgetQuotes {

    companion object{

        var pos = 0
        var size = 0
        var rotate = 5
        var quote: ArrayList<Quote>? = null
        var textView: TextView? = null
        var auther_tv: TextView? = null

        val handler: Handler = Handler(Looper.getMainLooper())


        fun getWidgetQuotes(ctx: Context, item: Item, data: String?) :View{

            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_quotes, null)

            // setting
            var setting_obj = Gson().fromJson(item.settings,SettingQuotes::class.java)

            textView = view.findViewById<TextView>(R.id.tv_quotes_quote)
            var ll_bg = view.findViewById<RelativeLayout>(R.id.ll_quotes_bg)
            auther_tv = view.findViewById<TextView>(R.id.tv_quotes_auther)
            var image = view.findViewById<ImageView>(R.id.iv_quote_bg)

            item.content

            // bg
            ll_bg.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(setting_obj.bg!!,setting_obj.bga!!)))

            // quote text
            textView!!.textSize = setting_obj.quotesSize!!.toFloat()
            textView!!.setTextColor(Color.parseColor(setting_obj.quotesText))
            FontUtil.setFonts(ctx,textView!!,setting_obj.quotesFont?.label!!)

            // auther text
            auther_tv!!.textSize = setting_obj.authorSize!!.toFloat()
            auther_tv!!.setTextColor(Color.parseColor(setting_obj.authorText))
            FontUtil.setFonts(ctx,auther_tv!!,setting_obj.authorFont?.label!!)


            var quotes_data = Gson().fromJson(data, QuotesData::class.java)
            quote = quotes_data.quote

            if(item.content!=null &&item.content.size>0){
                var filename = item.content[0].fileName
                ImageUtil.loadLocalImage(filename,image)
            }

            if(quotes_data.quote != null && quotes_data.quote.size>0){
                rotate = setting_obj?.rotate!!
                size = quotes_data.quote.size
                setText()
            }
            return view
        }

        fun setText() {
            if(pos >= quote!!.size) pos = 0
            textView!!.setText(quote!![pos].quote)
            auther_tv!!.setText(quote!![pos].author)
        }

        fun getRotation(item: Item): Int {
            // setting
            var setting_obj = Gson().fromJson(item.settings,SettingQuotes::class.java)
            return setting_obj.rotate ?: 5
        }

    }
}

data class SettingQuotes (

    @SerializedName("bg"          ) var bg          : String?     = null,
    @SerializedName("bga"         ) var bga         : String?     = null,
    @SerializedName("quotesText"  ) var quotesText  : String?     = null,
    @SerializedName("authorText"  ) var authorText  : String?     = null,
    @SerializedName("rotationOpt" ) var rotationOpt : String?     = null,
    @SerializedName("rotate"      ) var rotate      : Int?        = null,
    @SerializedName("quotesSize"  ) var quotesSize  : Int?        = null,
    @SerializedName("authorSize"  ) var authorSize  : Int?        = null,
    @SerializedName("quotesFont"  ) var quotesFont  : QuotesFont? = QuotesFont(),
    @SerializedName("authorFont"  ) var authorFont  : AuthorFont? = AuthorFont()

)

data class QuotesFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class AuthorFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)