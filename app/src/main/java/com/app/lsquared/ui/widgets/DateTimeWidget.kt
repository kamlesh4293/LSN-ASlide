package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.util.Calendar

class DateTimeWidget {

    companion object{

        fun getDateTimeWidget(ctx: Context, item: Item): View {

            var view : View? = null

            var setting_obj = JSONObject(item.settings)
            var template = setting_obj.getString("template")

//            Log.d("TAG", "setData: item - ${item.frame_h} , ${item.frame_w} , temp - $template")

            if(template.equals(Constant.TEMPLATE_TIME_T1))
                view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_time_t1, null)
            if(template.equals(Constant.TEMPLATE_TIME_T2))
                view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_time_t2, null)
            if(template.equals(Constant.TEMPLATE_TIME_T3))
                view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_time_t3, null)
            if(template.equals(Constant.TEMPLATE_TIME_T4))
                view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_time_t4, null)
            if(template.equals(Constant.TEMPLATE_TIME_T5))
                view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_time_t5, null)
            if(template.equals(Constant.TEMPLATE_TIME_T6))
                view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_time_t6, null)
            if(template.equals(Constant.TEMPLATE_TIME_T7))
                view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_time_t7, null)
            if(template.equals(Constant.TEMPLATE_TIME_T8))
                view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_time_t8, null)


            return view!!
        }

        fun setData(view: View,item: Item){
            var setting_obj = JSONObject(item.settings)
            var template = setting_obj.getString("template")

//            Log.d("TAG", "setData: item - ${item.frame_h} , ${item.frame_w} , temp - $template")

            // set template 1
            if(template.equals(Constant.TEMPLATE_TIME_T1)) setTemplateData1(view,item)
            // set template 2
            if(template.equals(Constant.TEMPLATE_TIME_T2))setTemplateData2(view!!,item)
            // set template 3 -
            if(template.equals(Constant.TEMPLATE_TIME_T3))setTemplateData3(view!!,item)
            // set template 4 - Only Time 1
            if(template.equals(Constant.TEMPLATE_TIME_T4))setTemplateData4(view!!,item)
            // set template 5 - Only Time 2
            if(template.equals(Constant.TEMPLATE_TIME_T5))setTemplateData5(view!!,item)
            // set template 6 - Only Date 1
            if(template.equals(Constant.TEMPLATE_TIME_T6))setTemplateData6(view!!,item)
            // set template 7 - Date on Top
            if(template.equals(Constant.TEMPLATE_TIME_T7))setTemplateData7(view!!,item)
            // set temlpate 8
            if(template.equals(Constant.TEMPLATE_TIME_T8))setTemplateData8(view!!,item)

        }

        // template 1
        private fun setTemplateData1(view: View?, item: Item) {
            var hh_tv = view?.findViewById<TextView>(R.id.tv_timetemp1_hh)
            var rl_mm = view?.findViewById<RelativeLayout>(R.id.rl_rounded_mm)
            var mm_tv = view?.findViewById<TextView>(R.id.tv_timetemp1_mm)
            var colon_tv = view?.findViewById<TextView>(R.id.tv_timetemp1_colon)
            var format_tv = view?.findViewById<TextView>(R.id.tv_timetemp1_format)
            var month_year_yv = view?.findViewById<TextView>(R.id.tv_timetemp1_d_m_y)

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp1::class.java)

            hh_tv?.layoutParams = getBoxSizeTemp1(item)
            rl_mm?.layoutParams = getBoxSizeMmTemp1(item)

            var timezone = Calendar.getInstance().timeZone.id
            DateTimeUtil.setTimeZone(item.src)

            var format = "AM"
            if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"
            if(setting_obj.format.equals("24"))format = ""

            hh_tv?.setText("${DateTimeUtil.getHour(setting_obj.format!!)}")
            format_tv?.setText("$format")
            mm_tv?.setText("${DateTimeUtil.getMinutes()}")
            month_year_yv?.setText("${DateTimeUtil.getDayName()} ${DateTimeUtil.getMonth().substring(0,3)}. " +
                    "${DateTimeUtil.getDate()}, ${DateTimeUtil.getYear()}")

            hh_tv?.background = DataParsing.getDateTimeShape(UiUtils.getColor(UiUtils.getColorWithOpacity(setting_obj.bg!!,setting_obj?.bga!!.toString())))
            rl_mm?.background = DataParsing.getDateTimeShape(UiUtils.getColor(UiUtils.getColorWithOpacity(setting_obj.bg!!,setting_obj?.bga!!.toString())))

            month_year_yv?.setTextColor(UiUtils.getColor(setting_obj.dateText!!))
            hh_tv?.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            mm_tv?.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            colon_tv?.setTextColor(UiUtils.getColor(setting_obj.bg!!))
            format_tv?.setTextColor(UiUtils.getColor(setting_obj.timeText!!))

            setBlinking(colon_tv)

            // font
            FontUtil.setFonts(view!!.context,month_year_yv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,hh_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,mm_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,format_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,colon_tv!!,setting_obj?.font?.label!!)

            var text_size = (getBoxHightTemp1(item)/4).toFloat()
            hh_tv?.textSize = text_size
            mm_tv?.textSize = text_size
            colon_tv?.textSize = text_size
            format_tv?.textSize = (text_size/2.5).toFloat()
            month_year_yv?.textSize = (text_size/2).toFloat()

            DateTimeUtil.setTimeZone(timezone)

            Handler(Looper.getMainLooper()).postDelayed(
                kotlinx.coroutines.Runnable {
                    if(view!=null) setTemplateData1(view, item)
                },60000
            )
        }

        // template 2
        private fun setTemplateData2(view: View, item: Item) {
            var month_tv = view.findViewById<TextView>(R.id.tv_timetemp2_month)
            var timestart_tv = view.findViewById<TextView>(R.id.tv_timetemp2_time_start)
            var colon_tv = view.findViewById<TextView>(R.id.tv_timetemp2_time_colon)
            var timeend_tv = view.findViewById<TextView>(R.id.tv_timetemp2_time_end)

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp2::class.java)

            var timezone = Calendar.getInstance().timeZone.id
            DateTimeUtil.setTimeZone(item.src)

            var format = "AM"
            if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"
            if(setting_obj.format.equals("24"))format = ""

            month_tv.setText("${DateTimeUtil.getMonth()} ${DateTimeUtil.getDate()}")
            timestart_tv.setText("${DateTimeUtil.getHour(setting_obj.format!!)}")
            timeend_tv.setText("${DateTimeUtil.getMinutes()} $format")

            var text_size = (getBoxHight(item)/3).toFloat()
            var text_size_s = (getBoxHight(item)/3.7).toFloat()
            month_tv.textSize = text_size_s
            timestart_tv.textSize = text_size
            colon_tv.textSize = text_size
            timeend_tv.textSize = text_size

            month_tv.setTextColor(UiUtils.getColor(setting_obj.dateText!!))
            timestart_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            timeend_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            colon_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))

            setBlinking(colon_tv)

            // font
            FontUtil.setFonts(view!!.context,month_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,timestart_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,timeend_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,colon_tv!!,setting_obj?.font?.label!!)

            DateTimeUtil.setTimeZone(timezone)

            Handler(Looper.getMainLooper()).postDelayed(
                kotlinx.coroutines.Runnable {
                    if(view!=null) setTemplateData2(view, item)
                },60000
            )

        }

        // template 3
        private fun setTemplateData3(view: View, item: Item) {
            var hh_tv = view.findViewById<TextView>(R.id.tv_timetemp3_hh)
            var rl_mm = view.findViewById<RelativeLayout>(R.id.rl_temp3_mm)
            var mm_tv = view.findViewById<TextView>(R.id.tv_timetemp3_mm)
            var colon_tv = view.findViewById<TextView>(R.id.tv_timetemp3_colon)
            var format_tv = view.findViewById<TextView>(R.id.tv_timetemp3_format)

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp3::class.java)

            var timezone = Calendar.getInstance().timeZone.id
            DateTimeUtil.setTimeZone(item.src)

            var format = "AM"
            if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"
            if(setting_obj.format.equals("24"))format = ""

            hh_tv?.layoutParams = getBoxSizeTemp3(item)
            rl_mm?.layoutParams = getBoxSizeMmTemp3(item)

            hh_tv.setText("${DateTimeUtil.getHour(setting_obj.format!!)}")
            mm_tv.setText("${DateTimeUtil.getMinutes()}")
            format_tv.setText("  $format")

            hh_tv?.textSize = (getBoxHightTemp3(item)/4 ).toFloat()
            mm_tv?.textSize = (getBoxHightTemp3(item)/4 ).toFloat()
            colon_tv?.textSize = (getBoxHightTemp3(item)/4 ).toFloat()
            format_tv?.textSize = (getBoxHightTemp3(item)/9 ).toFloat()

            hh_tv?.background = DataParsing.getDateTimeShape(UiUtils.getColor(UiUtils.getColorWithOpacity(setting_obj.bg!!,setting_obj?.bga!!.toString())))
            rl_mm?.background = DataParsing.getDateTimeShape(UiUtils.getColor(UiUtils.getColorWithOpacity(setting_obj.bg!!,setting_obj?.bga!!.toString())))


            hh_tv.setTextColor(UiUtils.getColor(setting_obj?.timeText!!))
            mm_tv.setTextColor(UiUtils.getColor(setting_obj?.timeText!!))
            format_tv.setTextColor(UiUtils.getColor(setting_obj?.timeText!!))
            colon_tv.setTextColor(UiUtils.getColor(setting_obj.bg!!))

            setBlinking(colon_tv)

            // font
            FontUtil.setFonts(view!!.context,hh_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,mm_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,format_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,colon_tv!!,setting_obj?.font?.label!!)

            DateTimeUtil.setTimeZone(timezone)

            Handler(Looper.getMainLooper()).postDelayed(
                kotlinx.coroutines.Runnable {
                    if(view!=null) setTemplateData3(view, item)
                },60000
            )

        }

        // template 4
        private fun setTemplateData4(view: View, item: Item) {
            var starttime_tv = view.findViewById<TextView>(R.id.tv_timetemp4_time_start)
            var colon_tv = view.findViewById<TextView>(R.id.tv_timetemp4_time_colon)
            var endtime_tv = view.findViewById<TextView>(R.id.tv_timetemp4_time_end)

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp4::class.java)

            var timezone = Calendar.getInstance().timeZone.id
            DateTimeUtil.setTimeZone(item.src)

            var format = "AM"
            if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"
            if(setting_obj.format.equals("24"))format = ""

            starttime_tv.setText("${DateTimeUtil.getHour(setting_obj.format!!)}")
            endtime_tv.setText("${DateTimeUtil.getMinutes()} $format")

            var text_size = (getBoxHight(item)/2.5).toFloat()
            starttime_tv.textSize = text_size
            endtime_tv.textSize = text_size
            colon_tv.textSize = text_size

            starttime_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            colon_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            endtime_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))

            setBlinking(colon_tv)

            // font
            FontUtil.setFonts(view!!.context,starttime_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,endtime_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,colon_tv!!,setting_obj?.font?.label!!)

            DateTimeUtil.setTimeZone(timezone)

            Handler(Looper.getMainLooper()).postDelayed(
                kotlinx.coroutines.Runnable {
                    if(view!=null) setTemplateData4(view, item)
                },60000
            )

        }

        // template 5
        private fun setTemplateData5(view: View, item: Item) {
            var month_tv = view.findViewById<TextView>(R.id.tv_timetemp5_datetime)

            var text_size = (getBoxHight(item)/7).toFloat()
            if(item.frame_h*3<item.frame_w) text_size = (getBoxHight(item)/3.5).toFloat()

            var timezone = Calendar.getInstance().timeZone.id
            DateTimeUtil.setTimeZone(item.src)

            month_tv.setText("${DateTimeUtil.getDayName()} ${DateTimeUtil.getMonth().substring(0,3)}. " +
                    "${DateTimeUtil.getDate()}, ${DateTimeUtil.getYear()}")
            month_tv.textSize = text_size

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp5and6::class.java)
            month_tv.setTextColor(UiUtils.getColor(setting_obj.dateText!!))

            // font
            FontUtil.setFonts(view!!.context,month_tv!!,setting_obj?.font?.label!!)

            DateTimeUtil.setTimeZone(timezone)

        }

        // template 6
        private fun setTemplateData6(view: View, item: Item) {
            var month_tv = view.findViewById<TextView>(R.id.tv_timetemp6_month)

            var timezone = Calendar.getInstance().timeZone.id
            DateTimeUtil.setTimeZone(item.src)

            var text = "${DateTimeUtil.getMonth()} ${DateTimeUtil.getDate()}"
            var text_size = getTextSizeTemp6(item,text).toFloat()
            month_tv.setText(text)
            month_tv.textSize = text_size

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp5and6::class.java)
            month_tv.setTextColor(UiUtils.getColor(setting_obj.dateText!!))
            // font
            FontUtil.setFonts(view!!.context,month_tv!!,setting_obj?.font?.label!!)
            DateTimeUtil.setTimeZone(timezone)
        }

        // template 7
        private fun setTemplateData7(view: View, item: Item) {
            var month_tv = view.findViewById<TextView>(R.id.tv_timetemp7_month)
            var timestart_tv = view.findViewById<TextView>(R.id.tv_timetemp7_time_start)
            var colon_tv = view.findViewById<TextView>(R.id.tv_timetemp7_time_colon)
            var timeend_tv = view.findViewById<TextView>(R.id.tv_timetemp7_time_end)

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp7::class.java)
            var timezone = Calendar.getInstance().timeZone.id
            DateTimeUtil.setTimeZone(item.src)

            var format = "AM"
            if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"
            if(setting_obj.format.equals("24"))format = ""

            setBlinking(colon_tv)

            // font
            FontUtil.setFonts(view!!.context,month_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,timestart_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,timeend_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,colon_tv!!,setting_obj?.font?.label!!)

            var text = "${DateTimeUtil.getMonth()} ${DateTimeUtil.getDate()}"
            month_tv.setText(text)
            timestart_tv.setText("${DateTimeUtil.getHour(setting_obj.format!!)}")
            timeend_tv.setText("${DateTimeUtil.getMinutes()} $format")

            var text_size = (getBoxHight(item)/3).toFloat()
            var text_size_s = (getBoxHight(item)/3.7).toFloat()

            month_tv.textSize = text_size_s
            timestart_tv.textSize = text_size
            colon_tv.textSize = text_size
            timeend_tv.textSize = text_size

            month_tv.setTextColor(UiUtils.getColor(setting_obj.dateText!!))
            timestart_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            timeend_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            colon_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))

            DateTimeUtil.setTimeZone(timezone)
            Handler(Looper.getMainLooper()).postDelayed(
                kotlinx.coroutines.Runnable {
                    if(view!=null) setTemplateData7(view, item)
                },60000
            )

        }

        // template 8
        private fun setTemplateData8(view: View, item: Item) {
            var day_tv = view.findViewById<TextView>(R.id.tv_timetemp_day)
            var starttime_tv = view.findViewById<TextView>(R.id.tv_timetemp_time_start)
            var colon_tv = view.findViewById<TextView>(R.id.tv_timetemp_time_colon)
            var endtime_tv = view.findViewById<TextView>(R.id.tv_timetemp_time_end)
            var date_tv = view.findViewById<TextView>(R.id.tv_timetemp_date)
            var month_tv = view.findViewById<TextView>(R.id.tv_timetemp_month)

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp8::class.java)

            var timezone = Calendar.getInstance().timeZone.id
            DateTimeUtil.setTimeZone(item.src)

            var format = "AM"
            if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"
            if(setting_obj.format.equals("24"))format = ""

            date_tv.setText(DateTimeUtil.getDateTemp8())
            day_tv.setText(DateTimeUtil.getDayName())
            starttime_tv.setText("${DateTimeUtil.getHour(setting_obj.format!!)}")
            endtime_tv.setText("${DateTimeUtil.getMinutes()} $format")
            month_tv.setText(DateTimeUtil.getMonth())

            var text_size = getTextSizeTemp8(item).toFloat()

            day_tv.textSize = text_size
            starttime_tv.textSize = text_size
            colon_tv.textSize = text_size
            endtime_tv.textSize = text_size
            month_tv.textSize = text_size
            date_tv.textSize = text_size

            day_tv.setTextColor(UiUtils.getColor(setting_obj.dayText!!))
            starttime_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            colon_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            endtime_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            month_tv.setTextColor(UiUtils.getColor(setting_obj.monthText!!))
            date_tv.setTextColor(UiUtils.getColor(setting_obj.dateText!!))


            setBlinking(colon_tv)

            // font
            FontUtil.setFonts(view!!.context,day_tv!!,setting_obj?.dayFont?.label!!)
            FontUtil.setFonts(view!!.context,starttime_tv!!,setting_obj?.timeFont?.label!!)
            FontUtil.setFonts(view!!.context,colon_tv!!,setting_obj?.timeFont?.label!!)
            FontUtil.setFonts(view!!.context,endtime_tv!!,setting_obj?.timeFont?.label!!)
            FontUtil.setFonts(view!!.context,month_tv!!,setting_obj?.monthFont?.label!!)
            FontUtil.setFonts(view!!.context,date_tv!!,setting_obj?.dateFont?.label!!)

            DateTimeUtil.setTimeZone(timezone)

            Handler(Looper.getMainLooper()).postDelayed(
                kotlinx.coroutines.Runnable {
                    if(view!=null) setTemplateData8(view, item)
                },60000
            )
        }

        private fun setBlinking(colon_tv: TextView?) {
            val anim: Animation = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 500 //You can manage the blinking time with this parameter
            anim.startOffset = 20
            anim.repeatMode = Animation.REVERSE
            anim.repeatCount = Animation.INFINITE
            colon_tv?.startAnimation(anim)
        }

        fun getBoxSizeTemp1(item: Item): ViewGroup.LayoutParams? {
            if(item.frame_h<item.frame_w/3) return LinearLayout.LayoutParams((item.frame_h/1.6).toInt(),(item.frame_h/1.5).toInt())
            else return LinearLayout.LayoutParams((item.frame_w/3.5).toInt(),(item.frame_w/3.5).toInt())
        }

        fun getBoxHightTemp1(item: Item): Int {
            if(item.frame_h<item.frame_w/3) return (item.frame_h/1.2).toInt()
            else return (item.frame_w/3).toInt()
        }

        fun getBoxHight(item: Item): Int {
            if(item.frame_h<item.frame_w/3) return (item.frame_h/1.2).toInt()
            else return (item.frame_w/3).toInt()
        }

        fun getTextSizeTemp6(item: Item,text:String): Int {
            var length = text.length+2
            return item.frame_w/length
        }

        fun getTextSizeTemp8(item: Item): Int {
            var size = item.frame_w/10
            return if(item.frame_h>size*8) size else item.frame_h/8
        }

        fun getBoxHightForTemp8(item: Item): Int {
            if(item.frame_h<item.frame_w/2) return (item.frame_h/2.5).toInt()
            else return (item.frame_w/3).toInt()
        }

        fun getBoxSizeMmTemp1(item: Item): ViewGroup.LayoutParams? {
            if(item.frame_h<item.frame_w/3) return LinearLayout.LayoutParams((item.frame_h/1.2).toInt(),(item.frame_h/1.5).toInt())
            else return LinearLayout.LayoutParams((item.frame_w/2.8).toInt(),(item.frame_w/3.5).toInt())
        }

        fun getDateBoxSizeTemp1(item: Item): ViewGroup.LayoutParams? {
            return LinearLayout.LayoutParams(item.frame_w,item.frame_h/8)
        }

        fun getBoxSizeTemp3(item: Item): ViewGroup.LayoutParams? {
            if(item.frame_h<item.frame_w/3) return LinearLayout.LayoutParams((item.frame_h/1.2).toInt(),(item.frame_h/1.2).toInt())
            else return LinearLayout.LayoutParams(item.frame_w/3,(item.frame_w/3).toInt())
        }

        fun getBoxHightTemp3(item: Item): Int {
            if(item.frame_h<item.frame_w/3) return (item.frame_h/1.2).toInt()
            else return (item.frame_w/3).toInt()
        }

        fun getBoxSizeMmTemp3(item: Item): ViewGroup.LayoutParams? {
            if(item.frame_h<item.frame_w/3) return LinearLayout.LayoutParams((item.frame_h/1).toInt(),(item.frame_h/1.2).toInt())
            else return LinearLayout.LayoutParams((item.frame_w/2.5).toInt(),(item.frame_w/3).toInt())
        }




    }
}

data class DateTimeSettingTemp1 (

    @SerializedName("font"     ) var font     : Font?   = Font(),
    @SerializedName("colorOpt" ) var colorOpt : String? = null,
    @SerializedName("bg"       ) var bg       : String? = null,
    @SerializedName("bga"      ) var bga      : String?  = null,
    @SerializedName("dateText" ) var dateText : String? = null,
    @SerializedName("timeText" ) var timeText : String? = null,
    @SerializedName("template" ) var template : String? = null,
    @SerializedName("lang"     ) var lang     : String? = null,
    @SerializedName("format"   ) var format   : String? = null

)

data class DateTimeSettingTemp2 (

    @SerializedName("font"     ) var font     : Font?   = Font(),
    @SerializedName("colorOpt" ) var colorOpt : String? = null,
    @SerializedName("dateText" ) var dateText : String? = null,
    @SerializedName("timeText" ) var timeText : String? = null,
    @SerializedName("template" ) var template : String? = null,
    @SerializedName("lang"     ) var lang     : String? = null,
    @SerializedName("format"   ) var format   : String? = null

)

data class DateTimeSettingTemp3 (

    @SerializedName("font"     ) var font     : Font?   = Font(),
    @SerializedName("colorOpt" ) var colorOpt : String? = null,
    @SerializedName("bg"       ) var bg       : String? = null,
    @SerializedName("bga"      ) var bga      : String?    = null,
    @SerializedName("timeText" ) var timeText : String? = null,
    @SerializedName("template" ) var template : String? = null,
    @SerializedName("lang"     ) var lang     : String? = null,
    @SerializedName("format"   ) var format   : String? = null

)

data class DateTimeSettingTemp4 (

    @SerializedName("font"     ) var font     : Font?   = Font(),
    @SerializedName("colorOpt" ) var colorOpt : String? = null,
    @SerializedName("timeText" ) var timeText : String? = null,
    @SerializedName("template" ) var template : String? = null,
    @SerializedName("lang"     ) var lang     : String? = null,
    @SerializedName("format"   ) var format   : String? = null

)

data class DateTimeSettingTemp5and6 (

    @SerializedName("font"     ) var font     : Font?   = Font(),
    @SerializedName("colorOpt" ) var colorOpt : String? = null,
    @SerializedName("dateText" ) var dateText : String? = null,
    @SerializedName("template" ) var template : String? = null,
    @SerializedName("lang"     ) var lang     : String? = null,
    @SerializedName("format"   ) var format   : String? = null

)


data class DateTimeSettingTemp7 (

    @SerializedName("font"     ) var font     : Font?   = Font(),
    @SerializedName("colorOpt" ) var colorOpt : String? = null,
    @SerializedName("dateText" ) var dateText : String? = null,
    @SerializedName("timeText" ) var timeText : String? = null,
    @SerializedName("template" ) var template : String? = null,
    @SerializedName("lang"     ) var lang     : String? = null,
    @SerializedName("format"   ) var format   : String? = null

)

data class DateTimeSettingTemp8 (

    @SerializedName("dateFont"  ) var dateFont  : DateFont?  = DateFont(),
    @SerializedName("colorOpt"  ) var colorOpt  : String?    = null,
    @SerializedName("dateText"  ) var dateText  : String?    = null,
    @SerializedName("timeText"  ) var timeText  : String?    = null,
    @SerializedName("timeFont"  ) var timeFont  : TimeFont?  = TimeFont(),
    @SerializedName("dayText"   ) var dayText   : String?    = null,
    @SerializedName("dayFont"   ) var dayFont   : DayFont?   = DayFont(),
    @SerializedName("monthText" ) var monthText : String?    = null,
    @SerializedName("monthFont" ) var monthFont : MonthFont? = MonthFont(),
    @SerializedName("template"  ) var template  : String?    = null,
    @SerializedName("lang"      ) var lang      : String?    = null,
    @SerializedName("format"    ) var format    : String?    = null

)

data class DateFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class TimeFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class DayFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class MonthFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)
