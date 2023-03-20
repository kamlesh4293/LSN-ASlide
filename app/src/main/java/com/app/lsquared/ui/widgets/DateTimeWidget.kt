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
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class DateTimeWidget {

    companion object{

        fun getDateTimeWidget(ctx: Context, item: Item): View {

            var view : View? = null

            var setting_obj = JSONObject(item.settings)
            var template = setting_obj.getString("template")

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

            // set template 1
            if(template.equals(Constant.TEMPLATE_TIME_T1))setTemplateData1(view,item)
            // set template 2
            if(template.equals(Constant.TEMPLATE_TIME_T2))setTemplateData2(view!!,item)
            // set template 3
            if(template.equals(Constant.TEMPLATE_TIME_T3))setTemplateData3(view!!,item)
            // set template 4
            if(template.equals(Constant.TEMPLATE_TIME_T4))setTemplateData4(view!!,item)
            // set template 5
            if(template.equals(Constant.TEMPLATE_TIME_T5))setTemplateData5(view!!,item)
            // set template 6
            if(template.equals(Constant.TEMPLATE_TIME_T6))setTemplateData6(view!!,item)
            // set template 7
            if(template.equals(Constant.TEMPLATE_TIME_T7))setTemplateData7(view!!,item)
            // set temlpate 8
            if(template.equals(Constant.TEMPLATE_TIME_T8))setTemplateData8(view!!,item)


            return view!!
        }

        // template 1
        private fun setTemplateData1(view: View?, item: Item) {
            var hh_tv = view?.findViewById<TextView>(R.id.tv_timetemp1_hh)
            var mm_tv = view?.findViewById<TextView>(R.id.tv_timetemp1_mm)
            var colon_tv = view?.findViewById<TextView>(R.id.tv_timetemp1_colon)
            var format_tv = view?.findViewById<TextView>(R.id.tv_timetemp1_format)
            var month_year_yv = view?.findViewById<TextView>(R.id.tv_timetemp1_d_m_y)
            var bg_ll = view?.findViewById<LinearLayout>(R.id.ll_timetemp1_bg)

            var format = "AM"
            if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"

            hh_tv?.setText("${DateTimeUtil.getHour()}")
            format_tv?.setText("$format")
            mm_tv?.setText("${DateTimeUtil.getMinutes()}")
            month_year_yv?.setText("${DateTimeUtil.getDayName()} ${DateTimeUtil.getMonth().substring(0,3)}. " +
                    "${DateTimeUtil.getDate()}, ${DateTimeUtil.getYear()}")

            hh_tv?.textSize = DimensionUtils.getLargeSize(item.frame_h)
            mm_tv?.textSize = DimensionUtils.getLargeSize(item.frame_h)
            colon_tv?.textSize = DimensionUtils.getMediumSize(item.frame_h).toFloat()
            format_tv?.textSize = DimensionUtils.getExtraSmallSize(item.frame_h).toFloat()
            month_year_yv?.textSize = DimensionUtils.getExtraSmallSize2(item.frame_h).toFloat()

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp1::class.java)

            month_year_yv?.setTextColor(UiUtils.getColor(setting_obj.dateText!!))
            hh_tv?.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            mm_tv?.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            format_tv?.setTextColor(UiUtils.getColor(setting_obj.timeText!!))


            // font
            FontUtil.setFonts(view!!.context,month_year_yv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,hh_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,mm_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,format_tv!!,setting_obj?.font?.label!!)
        }

        // template 2
        private fun setTemplateData2(view: View, item: Item) {
            var month_tv = view.findViewById<TextView>(R.id.tv_timetemp2_month)
            var time_tv = view.findViewById<TextView>(R.id.tv_timetemp2_time)

            var format = "AM"
            if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"


            month_tv.setText("${DateTimeUtil.getMonth()} ${DateTimeUtil.getDate()}")
            time_tv.setText("${DateTimeUtil.getTime()} $format")

            month_tv.textSize = DimensionUtils.getCustomSize(item.frame_h,14).toFloat()
            time_tv.textSize = DimensionUtils.getCustomSize(item.frame_h,8).toFloat()

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp2::class.java)

            month_tv.setTextColor(UiUtils.getColor(setting_obj.dateText!!))
            time_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))

            // font
            FontUtil.setFonts(view!!.context,month_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,time_tv!!,setting_obj?.font?.label!!)

        }

        // template 3
        private fun setTemplateData3(view: View, item: Item) {
            var hh_tv = view.findViewById<TextView>(R.id.tv_timetemp3_hh)
            var mm_tv = view.findViewById<TextView>(R.id.tv_timetemp3_mm)
            var colon_tv = view.findViewById<TextView>(R.id.tv_timetemp3_colon)
            var format_tv = view.findViewById<TextView>(R.id.tv_timetemp3_format)

            var format = "AM"
            if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"


            hh_tv.setText("${DateTimeUtil.getHour()}")
            mm_tv.setText("${DateTimeUtil.getMinutes()}")
            format_tv.setText("$format")

            hh_tv.textSize = DimensionUtils.getLargeSize(item.frame_h)
            mm_tv.textSize = DimensionUtils.getLargeSize(item.frame_h)
            colon_tv.textSize = DimensionUtils.getLargeSize(item.frame_h)
            format_tv.textSize = DimensionUtils.getExtraSmallSize(item.frame_h).toFloat()

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp3::class.java)

            hh_tv.setTextColor(UiUtils.getColor(setting_obj?.timeText!!))
            mm_tv.setTextColor(UiUtils.getColor(setting_obj?.timeText!!))
            format_tv.setTextColor(UiUtils.getColor(setting_obj?.timeText!!))

            // font
            FontUtil.setFonts(view!!.context,hh_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,mm_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,format_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,colon_tv!!,setting_obj?.font?.label!!)

        }

        // template 4
        private fun setTemplateData4(view: View, item: Item) {
            var time_tv = view.findViewById<TextView>(R.id.tv_timetemp4_time)

            var format = "AM"
            if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"


            time_tv.setText("${DateTimeUtil.getTime()} $format")
            time_tv.textSize = DimensionUtils.getLargeSize(item.frame_h)

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp4::class.java)

            time_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))

            // font
            FontUtil.setFonts(view!!.context,time_tv!!,setting_obj?.font?.label!!)
        }


        // template 5
        private fun setTemplateData5(view: View, item: Item) {
            var month_tv = view.findViewById<TextView>(R.id.tv_timetemp5_datetime)

            month_tv.setText("${DateTimeUtil.getDayName()} ${DateTimeUtil.getMonth().substring(0,3)}. " +
                    "${DateTimeUtil.getDate()}, ${DateTimeUtil.getYear()}")
            month_tv.textSize = DimensionUtils.getCustomSize(item.frame_h,20).toFloat()

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp5and6::class.java)

            month_tv.setTextColor(UiUtils.getColor(setting_obj.dateText!!))

            // font
            FontUtil.setFonts(view!!.context,month_tv!!,setting_obj?.font?.label!!)

        }

        // template 6
        private fun setTemplateData6(view: View, item: Item) {
            var month_tv = view.findViewById<TextView>(R.id.tv_timetemp6_month)

            month_tv.setText("${DateTimeUtil.getMonth()} ${DateTimeUtil.getDate()}")
            month_tv.textSize = DimensionUtils.getCustomSize(item.frame_h,11).toFloat()

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp5and6::class.java)

            month_tv.setTextColor(UiUtils.getColor(setting_obj.dateText!!))

            // font
            FontUtil.setFonts(view!!.context,month_tv!!,setting_obj?.font?.label!!)

        }

        // template 7
        private fun setTemplateData7(view: View, item: Item) {
            var month_tv = view.findViewById<TextView>(R.id.tv_timetemp7_month)
            var time_tv = view.findViewById<TextView>(R.id.tv_timetemp7_time)

            var format = "AM"
            if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"

            month_tv.setText("${DateTimeUtil.getMonth()} ${DateTimeUtil.getDate()}")
            time_tv.setText("${DateTimeUtil.getTime()} $format")

            month_tv.textSize = DimensionUtils.getCustomSize(item.frame_h,11).toFloat()
            time_tv.textSize = DimensionUtils.getCustomSize(item.frame_h,9).toFloat()

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp7::class.java)

            month_tv.setTextColor(UiUtils.getColor(setting_obj.dateText!!))
            time_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))

            // font
            FontUtil.setFonts(view!!.context,month_tv!!,setting_obj?.font?.label!!)
            FontUtil.setFonts(view!!.context,time_tv!!,setting_obj?.font?.label!!)

        }

        // template 8
        private fun setTemplateData8(view: View, item: Item) {
            var day_tv = view.findViewById<TextView>(R.id.tv_timetemp_day)
            var time_tv = view.findViewById<TextView>(R.id.tv_timetemp_time)
            var date_tv = view.findViewById<TextView>(R.id.tv_timetemp_date)
            var month_tv = view.findViewById<TextView>(R.id.tv_timetemp_month)

            var format = "AM"
            if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"


            date_tv.setText(DateTimeUtil.getDate())
            day_tv.setText(DateTimeUtil.getDayName())
            time_tv.setText("${DateTimeUtil.getTime()} $format")
            month_tv.setText(DateTimeUtil.getMonth())

            Log.d("TAG", "setTemplateData8: textsize - ${DimensionUtils.getSmallSize(item.frame_h).toFloat()}")
            day_tv.textSize = DimensionUtils.getSmallSize(item.frame_h).toFloat()
            time_tv.textSize = DimensionUtils.getSmallSize(item.frame_h).toFloat()
            month_tv.textSize = DimensionUtils.getSmallSize(item.frame_h).toFloat()
            date_tv.textSize = DimensionUtils.getSmallSize(item.frame_h).toFloat()

            var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp8::class.java)


            day_tv.setTextColor(UiUtils.getColor(setting_obj.dateText!!))
            time_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
            month_tv.setTextColor(UiUtils.getColor(setting_obj.monthText!!))
            date_tv.setTextColor(UiUtils.getColor(setting_obj.dateText!!))

            // font
            FontUtil.setFonts(view!!.context,date_tv!!,setting_obj?.dateFont?.label!!)
            FontUtil.setFonts(view!!.context,time_tv!!,setting_obj?.timeFont?.label!!)
            FontUtil.setFonts(view!!.context,month_tv!!,setting_obj?.monthFont?.label!!)
            FontUtil.setFonts(view!!.context,date_tv!!,setting_obj?.dateFont?.label!!)

        }


    }
}

data class DateTimeSettingTemp1 (

    @SerializedName("font"     ) var font     : Font?   = Font(),
    @SerializedName("colorOpt" ) var colorOpt : String? = null,
    @SerializedName("bg"       ) var bg       : String? = null,
    @SerializedName("bga"      ) var bga      : Int?    = null,
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
    @SerializedName("bga"      ) var bga      : Int?    = null,
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
