package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.FontUtil
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class WidgetText {

    companion object{

        fun getWidgetTextStatic(ctx: Context, item: Item, text: String?):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_text,null)

            var textview = view.findViewById<TextView>(R.id.tv_text_static)
            var layout = view.findViewById<LinearLayout>(R.id.ll_text_main)
            textview.visibility = View.VISIBLE

            // setting
            var settings = Gson().fromJson(item.settings,TextStaticWidgetSetting::class.java)
            layout.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(settings.bg?:"#000000",settings.bga.toString())))
            textview.textSize = settings.size!!.toFloat()
            textview.setTextColor(Color.parseColor(settings.titleText))
            FontUtil.setFonts(ctx,textview,settings.font?.label!!)

            var hori_align = if(settings.align.equals("l")) Gravity.LEFT else if(settings.align.equals("c")) Gravity.CENTER else Gravity.RIGHT
            var vert_align = if(settings.vAlign.equals("t")) Gravity.TOP else if(settings.vAlign.equals("m")) Gravity.CENTER else Gravity.BOTTOM

            textview.gravity = hori_align or vert_align

            textview.text = text
            return view
        }

        fun getWidgetTextCrowling(ctx: Context, item: Item, data: String?):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_text_crawling,null)

            var textview = view.findViewById<TextView>(R.id.tv_text_crawling)
            var layout = view.findViewById<LinearLayout>(R.id.ll_text_main)
            textview.visibility = View.VISIBLE

            // setting
            var setting = Gson().fromJson(item.settings,TextWidgetSetting::class.java)
            layout.setBackgroundColor(Color.parseColor(setting.bg))
            textview.textSize = setting.fontSize!!.toFloat()
            textview.setTextColor(Color.parseColor(setting.titleText))
            FontUtil.setFonts(ctx,textview,setting.font?.label!!)
            textview.isSelected = true


            var list = mutableListOf<String>()
            var json_data = JSONObject(data)
            var channel_array = json_data.getJSONArray("channel")
            for (i in 0..10){
                for (i in 0 until channel_array.length()){
                    var title_obj = channel_array.getJSONObject(i).getString("title")
                    Log.d("TAG", "setData: listitem $i - $title_obj")
                    list.add(title_obj)
                }
            }
            var builer = StringBuilder()
            for (i in list){
                builer.append("$i       ")
            }
            textview.text = builer.toString()
            return view
        }

        fun getBlankView(ctx: Context):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_text,null)

            var textview = view.findViewById<TextView>(R.id.tv_text_static)
            var layout = view.findViewById<LinearLayout>(R.id.ll_text_main)
            textview.visibility = View.GONE
            layout.setBackgroundColor(ctx.resources.getColor(R.color.black))
            return view
        }

    }

}

data class TextWidgetSetting (

    @SerializedName("speed"       ) var speed       : Int?    = null,
    @SerializedName("bg"          ) var bg          : String? = null,
    @SerializedName("bga"         ) var bga         : Int?    = null,
    @SerializedName("bullet"      ) var bullet      : String? = null,
    @SerializedName("titleText"   ) var titleText   : String? = null,
    @SerializedName("font"        ) var font        : Font?   = Font(),
    @SerializedName("dir"         ) var dir         : String? = null,
    @SerializedName("align"       ) var align       : String? = null,
    @SerializedName("fontSize"    ) var fontSize    : Int?    = null,
    @SerializedName("fontSizeOpt" ) var fontSizeOpt : String? = null

)

data class Font (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class TextStaticWidgetSetting (

    @SerializedName("rotationOpt" ) var rotationOpt : String? = null,
    @SerializedName("size"        ) var size        : Int?    = null,
    @SerializedName("rotate"      ) var rotate      : Int?    = null,
    @SerializedName("bg"          ) var bg          : String? = null,
    @SerializedName("bga"         ) var bga         : Int?    = null,
    @SerializedName("titleText"   ) var titleText   : String? = null,
    @SerializedName("font"        ) var font        : Font?   = Font(),
    @SerializedName("align"       ) var align       : String? = null,
    @SerializedName("vAlign"      ) var vAlign      : String? = null

)