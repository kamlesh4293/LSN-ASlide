package com.app.lsquared.ui.widgets

import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
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
import java.util.*
import kotlin.math.roundToInt


class WidgetDateTime {

    fun getDateTimeWidget(ctx: Context, item: Item): View {
        var view : View? = null

        var setting_obj = JSONObject(item.settings)
        var template = setting_obj.getString("template")

        if(template.equals(Constant.TEMPLATE_TIME_T1))
            view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_time_t1, null)
        if(template.equals(Constant.TEMPLATE_TIME_T2))
            view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_time_t2, null)
        if(template.equals(Constant.TEMPLATE_TIME_T3))
            view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_time_t1, null)
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
        if(template.equals(Constant.TEMPLATE_TIME_T9))
            view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_time_t9, null)

        return view!!
    }

    fun setData(view: View,item: Item){
        var setting_obj = JSONObject(item.settings)
        var template = setting_obj.getString("template")
        // set template 1
        if(template.equals(Constant.TEMPLATE_TIME_T1)) setTemplateData1(view,item,true)
        // set template 2
        if(template.equals(Constant.TEMPLATE_TIME_T2))setTemplateData2(view!!,item)
        // set template 3 - Only Time 1
        if(template.equals(Constant.TEMPLATE_TIME_T3))setTemplateData1(view!!,item,false)
        // set template 4 -
        if(template.equals(Constant.TEMPLATE_TIME_T4))setTemplateData4(view!!,item)
        // set template 5 - Only Time 2
        if(template.equals(Constant.TEMPLATE_TIME_T5))setTemplateData5(view!!,item)
        // set template 6 - Only Date 1
        if(template.equals(Constant.TEMPLATE_TIME_T6))setTemplateData6(view!!,item)
        // set template 7 - Date on Top
        if(template.equals(Constant.TEMPLATE_TIME_T7))setTemplateData7(view!!,item)
        // set temlpate 8
        if(template.equals(Constant.TEMPLATE_TIME_T8))setTemplateData8(view!!,item)
        // set temlpate 9
        if(template.equals(Constant.TEMPLATE_TIME_T9))setTemplateData9(view!!,item)

    }

    // template 1
    private fun setTemplateData1(view: View?, item: Item,m_y_d :Boolean) {

        var hh_tv = view?.findViewById<TextView>(R.id.tv_timetemp1_hh)
        var rl_mm = view?.findViewById<RelativeLayout>(R.id.rl_rounded_mm)
        var mm_tv = view?.findViewById<TextView>(R.id.tv_timetemp1_mm)
        var colon_tv = view?.findViewById<TextView>(R.id.tv_timetemp1_colon)
        var format_tv = view?.findViewById<TextView>(R.id.tv_timetemp1_format)
        var month_year_yv = view?.findViewById<TextView>(R.id.tv_timetemp1_d_m_y)

        month_year_yv?.visibility = if(m_y_d) View.VISIBLE else View.GONE

        var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp1::class.java)

        var width = getBoxWidthTemp01(item).toInt()
        var min_width = getMinuteBoxWidthTemp01(item).toInt()
        var hight = getBoxHightTemp01(item).toInt()

        hh_tv?.layoutParams = LinearLayout.LayoutParams(width,hight)
        rl_mm?.layoutParams = LinearLayout.LayoutParams(min_width,hight)


        var dmy_ll = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        var margin = (item.frame_h/100)*3
        dmy_ll.setMargins(0,margin,0,margin)
        month_year_yv?.layoutParams = dmy_ll

        var timezone = Calendar.getInstance().timeZone.id
        DateTimeUtil.setTimeZone(item.src)

        var format = "AM"
        if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"
        if(setting_obj.format.equals("24"))format = ""

        hh_tv?.setText("${DateTimeUtil.getHour(setting_obj.format!!)}")
        if(format.equals("")) format_tv?.visibility = View.GONE
        else {
            format_tv?.visibility = View.VISIBLE
            format_tv?.setText(" $format")
        }
        mm_tv?.setText("${DateTimeUtil.getMinutes()}")
        var date_time_year = "${DateTimeUtil.getDayName()} ${DateTimeUtil.getMonth().substring(0,3)}. " +
                "${DateTimeUtil.getDate()}, ${DateTimeUtil.getYear()}"

        hh_tv?.background = DataParsing.getDateTimeShape(UiUtils.getColor(UiUtils.getColorWithOpacity(setting_obj.bg!!,setting_obj?.bga!!.toString())),item.frame_h)
        rl_mm?.background = DataParsing.getDateTimeShape(UiUtils.getColor(UiUtils.getColorWithOpacity(setting_obj.bg!!,setting_obj?.bga!!.toString())),item.frame_h)

        hh_tv?.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
        mm_tv?.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
        colon_tv?.setTextColor(UiUtils.getColor(setting_obj.bg!!))
        format_tv?.setTextColor(UiUtils.getColor(setting_obj.timeText!!))

        setBlinking(colon_tv)

        // font
        FontUtil.setFonts(view!!.context,hh_tv!!,setting_obj?.font?.label!!)
        FontUtil.setFonts(view!!.context,mm_tv!!,setting_obj?.font?.label!!)
        FontUtil.setFonts(view!!.context,format_tv!!,setting_obj?.font?.label!!)
        FontUtil.setFonts(view!!.context,colon_tv!!,setting_obj?.font?.label!!)

        var time_size = (hight/4).toFloat()
        var format_size = (hight/9).toFloat()

        hh_tv?.textSize = time_size
        mm_tv?.textSize = time_size
        colon_tv?.textSize = time_size
        format_tv?.textSize = format_size

        if(m_y_d){
            month_year_yv?.setText(date_time_year)
            month_year_yv?.setTextColor(UiUtils.getColor(setting_obj.dateText!!))
            FontUtil.setFonts(view!!.context,month_year_yv!!,setting_obj?.font?.label!!)
            month_year_yv?.textSize = 100f
            setTextSize(month_year_yv,item.frame_w)
        }

        DateTimeUtil.setTimeZone(timezone)
        Handler(Looper.getMainLooper()).postDelayed(
            kotlinx.coroutines.Runnable {
                if(view!=null) setTemplateData1(view, item,m_y_d)
            },60000
        )
    }

    fun getBoxWidthTemp01(item: Item) :Float{
        var width = item.frame_w.toFloat()
        var hight = item.frame_h.toFloat()
        return  if(width>hight) (hight/100)*60  else (width/100)*40
    }

    fun getMinuteBoxWidthTemp01(item: Item) :Float{
        var width = item.frame_w.toFloat()
        var hight = item.frame_h.toFloat()
        return  if(width>hight) (hight/100)*70  else (width/100)*45
    }

    fun getBoxHightTemp01(item: Item) :Float{
        var width = item.frame_w.toFloat()
        var hight = item.frame_h.toFloat()
        return  if(width>hight) (hight/100)*60  else (width/100)*35
    }

    fun getDMDHightTemp01(item: Item) :Float{
        return (item.frame_h.toFloat()/100)*30
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

//        var text_size = (getBoxHight(item) /3).toFloat()
//        var text_size_s = (getBoxHight(item) /3.7).toFloat()

        var text_size = (TextSize().getDesizeSize("  ${month_tv.text.toString()}  ",item.frame_w,(item.frame_h.toFloat()/3.2).toInt()) /2).toFloat()
        var text_size_s = (TextSize().getDesizeSize("  ${timestart_tv.text.toString()} " +
                "${timeend_tv.text.toString()}  ",item.frame_w,(item.frame_h.toFloat()/3.2).toInt()) /2.5).toFloat()

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

        hh_tv?.textSize = (getBoxHightTemp3(item) /4 ).toFloat()
        mm_tv?.textSize = (getBoxHightTemp3(item) /4 ).toFloat()
        colon_tv?.textSize = (getBoxHightTemp3(item) /4 ).toFloat()
        format_tv?.textSize = (getBoxHightTemp3(item) /9 ).toFloat()

        hh_tv?.background = DataParsing.getDateTimeShape(UiUtils.getColor(UiUtils.getColorWithOpacity(setting_obj.bg!!,setting_obj?.bga!!.toString()))
        ,item.frame_h)
        rl_mm?.background = DataParsing.getDateTimeShape(UiUtils.getColor(UiUtils.getColorWithOpacity(setting_obj.bg!!,setting_obj?.bga!!.toString()))
        ,item.frame_h)


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

    fun getBoxSizeTemp3(item: Item): LinearLayout.LayoutParams? {
        if(item.frame_h<item.frame_w/3) return LinearLayout.LayoutParams((item.frame_h/1.2).toInt(),(item.frame_h/1.2).toInt())
        else return LinearLayout.LayoutParams(item.frame_w/3,(item.frame_w/3).toInt())
    }

    fun getBoxHightTemp3(item: Item): Int {
        if(item.frame_h<item.frame_w/3) return (item.frame_h/1.2).toInt()
        else return (item.frame_w/3).toInt()
    }

    fun getBoxSizeMmTemp3(item: Item): LinearLayout.LayoutParams? {
        if(item.frame_h<item.frame_w/3) return LinearLayout.LayoutParams((item.frame_h/1).toInt(),(item.frame_h/1.2).toInt())
        else return LinearLayout.LayoutParams((item.frame_w/2.5).toInt(),(item.frame_w/3).toInt())
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

        var start = "${DateTimeUtil.getHour(setting_obj.format!!)}"
        var end = "${DateTimeUtil.getMinutes()} $format"
        starttime_tv.setText(start)
        endtime_tv.setText(end)

//        var text_size = (getBoxHight(item) /2.5).toFloat()
        var text = "$start : $end"
        var text_size = (TextSize().getDesizeSize(text,item.frame_w,item.frame_h)/2).toFloat()

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

        var timezone = Calendar.getInstance().timeZone.id
        DateTimeUtil.setTimeZone(item.src)

        var text = "${DateTimeUtil.getDayName()} ${DateTimeUtil.getMonth().substring(0,3)}. " +
                "${DateTimeUtil.getDate()}, ${DateTimeUtil.getYear()}"
        month_tv.setText(text)

        var text_size = TextSize().getDesizeSize(text,item.frame_w,item.frame_h).toFloat()
        month_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (text_size/1.5).toFloat())

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
        var text_size1 = getTextSizeTemp6(item, text).toFloat()

        month_tv.setText(text)
        var text_size = TextSize().getDesizeSize(text,item.frame_w,item.frame_h).toFloat()
        month_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size/2)

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

//        var text_size = (getBoxHight(item) /3).toFloat()
//        var text_size_s = (getBoxHight(item) /3.7).toFloat()
        var text_size = (TextSize().getDesizeSize("  $text  ",item.frame_w,(item.frame_h.toFloat()/3.2).toInt()) /2).toFloat()
        var text_size_s = (TextSize().getDesizeSize("  ${timestart_tv.text.toString()} " +
                "${timeend_tv.text.toString()}  ",item.frame_w,(item.frame_h.toFloat()/3.2).toInt()) /2.5).toFloat()

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

        var day = DateTimeUtil.getDayName()
        date_tv.setText(DateTimeUtil.getDateTemp8())
        day_tv.setText(day)
        starttime_tv.setText("${DateTimeUtil.getHour(setting_obj.format!!)}")
        endtime_tv.setText("${DateTimeUtil.getMinutes()} $format")
        month_tv.setText(DateTimeUtil.getMonth())

        var text_size = (TextSize().getDesizeSize("  $day  ",item.frame_w,item.frame_h/5,)/2.3).toFloat()

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

    // template 9
    private fun setTemplateData9(view: View, item: Item) {

        var day_tv = view.findViewById<TextView>(R.id.tv_timetemp9_day)
        var starttime_tv = view.findViewById<TextView>(R.id.tv_timetemp9_time_start)
        var colon_tv = view.findViewById<TextView>(R.id.tv_timetemp9_time_colon)
        var endtime_tv = view.findViewById<TextView>(R.id.tv_timetemp9_time_end)
        var date_tv = view.findViewById<TextView>(R.id.tv_timetemp9_date)

        var setting_obj = Gson().fromJson(item.settings,DateTimeSettingTemp8::class.java)

        var timezone = Calendar.getInstance().timeZone.id
        DateTimeUtil.setTimeZone(item.src)

        var format = "AM"
        if(DateTimeUtil.getHour().toInt() >= 12)format = "PM"
        if(setting_obj.format.equals("24"))format = ""

        var day = DateTimeUtil.getDayName()
        date_tv.setText(DateTimeUtil.getDateTemp9())
        day_tv.setText(day)
        starttime_tv.setText("${DateTimeUtil.getHour(setting_obj.format!!)}")
        endtime_tv.setText("${DateTimeUtil.getMinutes()} $format")

        var text_size = (TextSize().getDesizeSize("  ${DateTimeUtil.getDateTemp9()}  ",item.frame_w,item.frame_h/4,)/2.3).toFloat()

        day_tv.textSize = text_size
        starttime_tv.textSize = text_size
        colon_tv.textSize = text_size
        endtime_tv.textSize = text_size
        date_tv.textSize = text_size

        day_tv.setTextColor(UiUtils.getColor(setting_obj.dayText!!))
        starttime_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
        colon_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
        endtime_tv.setTextColor(UiUtils.getColor(setting_obj.timeText!!))
        date_tv.setTextColor(UiUtils.getColor(setting_obj.dateText!!))

        setBlinking(colon_tv)
        // font
        FontUtil.setFonts(view!!.context,day_tv!!,setting_obj?.dayFont?.label!!)
        FontUtil.setFonts(view!!.context,starttime_tv!!,setting_obj?.timeFont?.label!!)
        FontUtil.setFonts(view!!.context,colon_tv!!,setting_obj?.timeFont?.label!!)
        FontUtil.setFonts(view!!.context,endtime_tv!!,setting_obj?.timeFont?.label!!)
        FontUtil.setFonts(view!!.context,date_tv!!,setting_obj?.dateFont?.label!!)

        DateTimeUtil.setTimeZone(timezone)

        Handler(Looper.getMainLooper()).postDelayed(
            kotlinx.coroutines.Runnable {
                if(view!=null) setTemplateData9(view, item)
            },60000
        )
    }

    fun setBlinking(colon_tv: TextView?) {
        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500 //You can manage the blinking time with this parameter
        anim.startOffset = 20
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
        colon_tv?.startAnimation(anim)
    }

    fun getTextSizeTemp6(item: Item,text:String): Int {
        var length = text.length+2
        return item.frame_w/length
    }

    fun setTextSize(textView: TextView, desiredWidth: Int) {
        val paint = Paint()
        val bounds = Rect()
        paint.typeface = textView.typeface
        var textSize = textView.textSize
        paint.textSize = textSize
        val text = textView.text.toString()
        paint.getTextBounds(text, 0, text.length, bounds)
        while (bounds.width() > desiredWidth) {
            textSize--
            paint.textSize = textSize
            paint.getTextBounds(text, 0, text.length, bounds)
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize/1.2).toFloat())
    }

}

class TextSize{

    fun getDesizeSize(text:String,text_width:Int,text_height:Int): Int {
        var paint = Paint()
        var bounds = Rect()

        var incr_text_size = 300
        var found_desired_size = true

        while (found_desired_size){
            paint.setTextSize(incr_text_size.toFloat())
            paint.getTextBounds(text, 0, text.length, bounds)
            incr_text_size--
            if (text_height > bounds.height() && text_width > bounds.width()){
                found_desired_size = false
            }
        }
        return incr_text_size
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
