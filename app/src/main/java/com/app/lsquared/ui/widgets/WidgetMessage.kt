package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.utils.FontUtil
import com.app.lsquared.utils.ImageUtil
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class WidgetMessage (){

    companion object{

        fun getMessageWidget(ctx: Context, item: Item,image_name:String):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.widget_message,null)

            var title_tv = view.findViewById<TextView>(R.id.tv_message_title)
            var msg_tv = view.findViewById<TextView>(R.id.tv_message_message)
            var image = view.findViewById<ImageView>(R.id.iv_message_image_bg)
            var image_top = view.findViewById<ImageView>(R.id.iv_message_image_top)
            var image_bottom = view.findViewById<ImageView>(R.id.iv_message_image_bottom)
            var image_left = view.findViewById<ImageView>(R.id.iv_message_image_left)
            var image_right = view.findViewById<ImageView>(R.id.iv_message_image_right)

            // setting
            var settings = Gson().fromJson(item.settings,SettingMessageWidget::class.java)
            // data
            var data = getData(item.data)

            // title
            title_tv.text = data.title
            title_tv.textSize = settings.titleSize!!.toFloat()
            title_tv.setTextColor(Color.parseColor(settings.titleText))
            FontUtil.setFonts(ctx,title_tv,settings.titleFont?.label!!)

            // message
            msg_tv.text = data.desc
            msg_tv.textSize = settings.descSize!!.toFloat()
            msg_tv.setTextColor(Color.parseColor(settings.descText))
            FontUtil.setFonts(ctx,msg_tv,settings.descFont?.label!!)

            // image
            if(!image.equals("") && settings.imgPosition.equals("bg")){
                ImageUtil.loadLocalImage(image_name,image)
            }else if(!image.equals("") && settings.imgPosition.equals("t")){
                ImageUtil.loadLocalImage(image_name,image_top)
            }else if(!image.equals("") && settings.imgPosition.equals("b")){
                ImageUtil.loadLocalImage(image_name,image_bottom)
            }else if(!image.equals("") && settings.imgPosition.equals("l")){
                ImageUtil.loadLocalImage(image_name,image_left)
            }else if(!image.equals("") && settings.imgPosition.equals("r")){
                ImageUtil.loadLocalImage(image_name,image_right)
            }


            return view
        }

        fun getData(data:String): DataMessageWidget {
            return Gson().fromJson(data,DataMessageWidget::class.java)
        }

    }

}

data class DataMessageWidget (

    @SerializedName("widgetVersion"  ) var widgetVersion  : Int?    = null,
    @SerializedName("timeZoneOffset" ) var timeZoneOffset : String? = null,
    @SerializedName("title"          ) var title          : String? = null,
    @SerializedName("desc"           ) var desc           : String? = null,
    @SerializedName("contentid"      ) var contentid      : Int?    = null,
    @SerializedName("ctype"          ) var ctype          : Ctype?  = Ctype()

)

data class Ctype (

    @SerializedName("titleText" ) var titleText : String? = null,
    @SerializedName("descText"  ) var descText  : String? = null

)

data class SettingMessageWidget (

    @SerializedName("imgPosition" ) var imgPosition : String?    = null,
    @SerializedName("titleText"   ) var titleText   : String?    = null,
    @SerializedName("titleSize"   ) var titleSize   : Int?       = null,
    @SerializedName("titleFont"   ) var titleFont   : TitleFont? = TitleFont(),
    @SerializedName("descText"    ) var descText    : String?    = null,
    @SerializedName("descSize"    ) var descSize    : Int?       = null,
    @SerializedName("descFont"    ) var descFont    : DescFont?  = DescFont()

)

data class TitleFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class DescFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)