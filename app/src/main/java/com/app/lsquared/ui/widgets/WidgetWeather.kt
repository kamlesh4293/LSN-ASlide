package com.app.lsquared.ui.widgets

import WeatherFive
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar.LayoutParams
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.pasring.DataParsingSetting
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.FontUtil
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable
import com.sdsmdg.harjot.vectormaster.VectorMasterView
import com.sdsmdg.harjot.vectormaster.models.PathModel


class WidgetWeather {

    companion object{

        // current date template 1
        fun getWidgetWeatherCurremtTemp1(ctx: Context, item: Item, weather_data: WeatherFive):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_weather_t1,null)

            var iv = view.findViewById<ImageView>(R.id.iv_weather_temp1_image)
            var temp_tv = view.findViewById<TextView>(R.id.tv_weather_temp1_temp)
            var temp_desc_tv = view.findViewById<TextView>(R.id.tv_weather_temp1_desc)
            var temp_city_tv = view.findViewById<TextView>(R.id.tv_weather_temp1_city)
//            var vector_iv = view.findViewById<VectorMasterView>(R.id.heart_vector)

            var text_size = (getBoxHight(item)/3).toFloat()
            iv.layoutParams = getLayoutParam(item)
            temp_tv.textSize = (text_size/1.2).toFloat()
            temp_desc_tv.textSize = (text_size/3).toFloat()
            temp_city_tv.textSize = (text_size/3.5).toFloat()

            var color = DataParsingSetting.getTintColor(item.settings)
            temp_tv.setTextColor(color)
            temp_desc_tv.setTextColor(color)
            temp_city_tv.setTextColor(color)

            var unit = DataParsingSetting.getUnit(item.settings)

            temp_tv.text = "${weather_data.current?.temp}°$unit"
            temp_desc_tv.text = "${weather_data.current?.desc}"
            temp_city_tv.text = "${weather_data.current?.city},${weather_data.current?.country}"
            UiUtils.setWeatherVecotImage(iv,weather_data.current?.icon,color,ctx)

            var font_label = DataParsingSetting.getFontLabel(item.settings)
            FontUtil.setFonts(ctx,temp_tv,font_label)
            FontUtil.setFonts(ctx,temp_desc_tv,font_label)
            FontUtil.setFonts(ctx,temp_city_tv,font_label)

//            val stroke: PathModel = iv.getPathModelByName("stroke")
//            val fill: PathModel = iv.getPathModelByName("fill")
//            stroke.setStrokeColor(color)
//            fill.setFillColor(color)
//            outline.setFillColor(Color.parseColor("#ED4337"));


            return view
        }

        // current date template 2
        fun getWidgetWeatherCurremtTemp2(ctx:Context,item: Item, weather_data: WeatherFive):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_weather_t2,null)
            var iv = view.findViewById<ImageView>(R.id.iv_weather_temp2_image)
            var tv_temp = view.findViewById<TextView>(R.id.tv_weather_temp2_temp)
            var tv_temp_min = view.findViewById<TextView>(R.id.tv_weather_temp2_tempmin)
            var tv_temp_max = view.findViewById<TextView>(R.id.tv_weather_temp2_tempmax)
            var tv_devide = view.findViewById<TextView>(R.id.tv_weather_temp2_divide)

            var text_size = (getBoxHight(item)/3).toFloat()
            iv.layoutParams = getLayoutParam(item)
            tv_temp.textSize = text_size
            tv_temp_min.textSize = text_size/2
            tv_temp_max.textSize = text_size/2
            tv_devide.textSize = text_size/3

            var color = DataParsingSetting.getTintColor(item.settings)
            tv_temp.setTextColor(color)
            tv_temp_min.setTextColor(color)
            tv_temp_max.setTextColor(color)
            tv_devide.setTextColor(color)
            tv_devide.setBackgroundColor(color)

            var unit = DataParsingSetting.getUnit(item.settings)

            tv_temp.text = "${weather_data.current?.temp}°$unit"
            tv_temp_min.text = "L: ${weather_data.current?.tempMin}°$unit"
            tv_temp_max.text = "H: ${weather_data.current?.tempMax}°$unit"
            UiUtils.setWeatherVecotImage(iv,weather_data.current?.icon,color,ctx)


            var font_label = DataParsingSetting.getFontLabel(item.settings)
            FontUtil.setFonts(ctx,tv_temp,font_label)
            FontUtil.setFonts(ctx,tv_temp_min,font_label)
            FontUtil.setFonts(ctx,tv_temp_max,font_label)

            return view
        }

        // current date template 3
        fun getWidgetWeatherCurremtTemp3(ctx:Context,item: Item, weather_data: WeatherFive):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_weather_t3,null)

            var iv = view.findViewById<ImageView>(R.id.iv_weather_temp3_image)
            var up_iv = view.findViewById<ImageView>(R.id.iv_weather_temp3_higharrow)
            var down_iv = view.findViewById<ImageView>(R.id.iv_weather_temp3_downarrow)
            var temp_tv = view.findViewById<TextView>(R.id.tv_weather_temp3_temp)
            var temp_high_tv = view.findViewById<TextView>(R.id.tv_weather_temp3_temphigh)
            var temp_low_tv = view.findViewById<TextView>(R.id.tv_weather_temp3_templow)
            var divider_tv = view.findViewById<TextView>(R.id.tv_weather_temp3_divide)

            var text_size = (getBoxHight(item)/3).toFloat()
            iv.layoutParams = getLayoutParam(item)
            up_iv.layoutParams = getIconLayoutParam(text_size)
            down_iv.layoutParams = getIconLayoutParam(text_size)
            temp_tv.textSize = text_size
            temp_high_tv.textSize = text_size/2
            temp_low_tv.textSize = text_size/2
            divider_tv.textSize = text_size/3

            var color = DataParsingSetting.getTintColor(item.settings)
            temp_tv.setTextColor(color)
            temp_high_tv.setTextColor(color)
            temp_low_tv.setTextColor(color)
            divider_tv.setTextColor(color)
            divider_tv.setBackgroundColor(color)

            var unit = DataParsingSetting.getUnit(item.settings)

            temp_tv.text = "${weather_data.current?.temp}°$unit"
            temp_low_tv.text = "${weather_data.current?.tempMin}°$unit"
            temp_high_tv.text = "${weather_data.current?.tempMax}°$unit"
            UiUtils.setWeatherVecotImage(iv,weather_data.current?.icon,color,ctx)
            UiUtils.setWeatherVecotImage(up_iv,1000,color,ctx)
            UiUtils.setWeatherVecotImage(down_iv,1001,color,ctx)

            var font_label = DataParsingSetting.getFontLabel(item.settings)
            FontUtil.setFonts(ctx,temp_tv,font_label)
            FontUtil.setFonts(ctx,temp_high_tv,font_label)
            FontUtil.setFonts(ctx,temp_low_tv,font_label)

            return view
        }

        // four day horizontal
        fun getWidgetWeatherFourDayHori(ctx:Context,item: Item, weather_data: WeatherFive):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_weather_4day_h,null)

            var ll_main = view.findViewById<LinearLayout>(R.id.ll_waether4day_main_h)
            var ll_main_box = view.findViewById<LinearLayout>(R.id.ll_main_bg)
            var ll_main_bottom = view.findViewById<LinearLayout>(R.id.ll_main_bottomview)

            var main_iv = view.findViewById<ImageView>(R.id.iv_weat4h_main_image)
            var main_temp_tv = view.findViewById<TextView>(R.id.tv_weat4h_main_temp)
            var main_desc_tv = view.findViewById<TextView>(R.id.tv_weat4h_main_desc)
            var main_minmax_tv = view.findViewById<TextView>(R.id.tv_weat4h_main_tempminmax)
            var main_city_tv = view.findViewById<TextView>(R.id.tv_weat4h_main_city)
            var main_humidity_iv = view.findViewById<ImageView>(R.id.iv_weat4h_main_humadity)
            var main_humidity_tv = view.findViewById<TextView>(R.id.tv_weat4h_main_humadity)
            var main_wind_tv = view.findViewById<TextView>(R.id.tv_weat4h_main_wind)
            var main_wind_iv = view.findViewById<ImageView>(R.id.iv_weat4h_main_wind)

            var ll_day1 = view.findViewById<LinearLayout>(R.id.ll_waether4day_day1_h)
            var iv_day1 = view.findViewById<ImageView>(R.id.iv_weat4h_day1)
            var temp_day1 = view.findViewById<TextView>(R.id.tv_weat4h_day1_minmax)
            var desc_day1 = view.findViewById<TextView>(R.id.tv_weat4h_day1_desc)
            var date_day1 = view.findViewById<TextView>(R.id.tv_weat4h_day1_date)

            var ll_day2 = view.findViewById<LinearLayout>(R.id.ll_waether4day_day2_h)
            var iv_day2 = view.findViewById<ImageView>(R.id.iv_weat4h_day2)
            var temp_day2 = view.findViewById<TextView>(R.id.tv_weat4h_day2_minmax)
            var desc_day2 = view.findViewById<TextView>(R.id.tv_weat4h_day2_desc)
            var date_day2 = view.findViewById<TextView>(R.id.tv_weat4h_day2_date)

            var ll_day3 = view.findViewById<LinearLayout>(R.id.ll_waether4day_day3_h)
            var iv_day3 = view.findViewById<ImageView>(R.id.iv_weat4h_day3)
            var temp_day3 = view.findViewById<TextView>(R.id.tv_weat4h_day3_minmax)
            var desc_day3 = view.findViewById<TextView>(R.id.tv_weat4h_day3_desc)
            var date_day3 = view.findViewById<TextView>(R.id.tv_weat4h_day3_date)

            var ll_day4 = view.findViewById<LinearLayout>(R.id.ll_waether4day_day4_h)
            var iv_day4 = view.findViewById<ImageView>(R.id.iv_weat4h_day4)
            var temp_day4 = view.findViewById<TextView>(R.id.tv_weat4h_day4_minmax)
            var desc_day4 = view.findViewById<TextView>(R.id.tv_weat4h_day4_desc)
            var date_day4 = view.findViewById<TextView>(R.id.tv_weat4h_day4_date)

            var trans_color = DataParsingSetting.getTintColorTrans(item.settings)
            var color = DataParsingSetting.getTintColor(item.settings)
            var label = DataParsingSetting.getFontLabel(item.settings)

            var mainiv_lp = getDay4MainIvLayoutParam(item)
            mainiv_lp.setMargins(0,10,10,0)
            main_iv.layoutParams = mainiv_lp
            var btm_layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                getDay4HMainBoxHight(item)/7
            )
            ll_main_bottom.layoutParams = btm_layoutParams

            var main_box_hi = getDay4HMainBoxHight(item)
            var child_box_hi = getDay4ChildBoxHight(item)
            var child_box_wi = getDay4ChildBoxWidth(item)

            // main box
            var main_size = LinearLayout.LayoutParams(main_box_hi,main_box_hi)
            main_size.setMargins(2,2,2,2)
            ll_main.layoutParams = main_size

            // child box
            var child_size = LinearLayout.LayoutParams(child_box_wi,child_box_hi)
            child_size.setMargins(2,2,2,2)
            ll_day1.layoutParams = child_size
            ll_day2.layoutParams = child_size
            ll_day3.layoutParams = child_size
            ll_day4.layoutParams = child_size


            var temp_size = (getDay4HMainBoxHight(item) /15).toFloat()
            var main_desc_size = (getDay4HMainBoxHight(item)/20).toFloat()
            var desc_size = (getDay4HMainBoxHight(item)/27).toFloat()
            var icon_size = (getDay4HMainBoxHight(item)/20).toFloat()

            main_humidity_iv.layoutParams = getLayoutParam(icon_size.toInt(),icon_size.toInt())
            main_wind_iv.layoutParams = getLayoutParam(icon_size.toInt(),icon_size.toInt())
            main_humidity_tv.textSize = desc_size
            main_wind_tv.textSize = desc_size

            main_temp_tv.textSize = temp_size
            main_desc_tv.textSize = main_desc_size
            main_minmax_tv.textSize = main_desc_size
            main_city_tv.textSize = main_desc_size

            var child_iv_layout = getDay4ChildIvLayoutParam(item)

            date_day1.textSize = desc_size
            temp_day1.textSize = desc_size
            desc_day1.textSize = desc_size
            iv_day1.layoutParams = child_iv_layout

            date_day2.textSize = desc_size
            temp_day2.textSize = desc_size
            desc_day2.textSize = desc_size
            iv_day2.layoutParams = child_iv_layout

            date_day3.textSize = desc_size
            temp_day3.textSize = desc_size
            desc_day3.textSize = desc_size
            iv_day3.layoutParams = child_iv_layout

            date_day4.textSize = desc_size
            temp_day4.textSize = desc_size
            desc_day4.textSize = desc_size
            iv_day4.layoutParams = child_iv_layout

            UiUtils.setDrawableView(ll_main,color,color)
            UiUtils.setDrawableOnlyTop(ll_main_box,trans_color)
            UiUtils.setDrawableOnlyBottom(ll_main_bottom,Color.parseColor("#ffffff"))
            UiUtils.setDrawableOnlyBottom(ll_main_bottom,Color.parseColor("#ffffff"))

            UiUtils.setDrawableView(ll_day1,Color.parseColor("#FFFFFF"),color)
            UiUtils.setDrawableOnlyBottom(date_day1,Color.parseColor("#EBF5FB"))

            UiUtils.setDrawableView(ll_day2,Color.parseColor("#FFFFFF"),color)
            UiUtils.setDrawableOnlyBottom(date_day2,Color.parseColor("#EBF5FB"))

            UiUtils.setDrawableView(ll_day3,Color.parseColor("#FFFFFF"),color)
            UiUtils.setDrawableOnlyBottom(date_day3,Color.parseColor("#EBF5FB"))

            UiUtils.setDrawableView(ll_day4,Color.parseColor("#FFFFFF"),color)
            UiUtils.setDrawableOnlyBottom(date_day4,Color.parseColor("#EBF5FB"))

            main_humidity_tv.setTextColor(color)
            main_wind_tv.setTextColor(color)
            main_city_tv.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity("#000000","0.1")))
            date_day1.setTextColor(color)
            temp_day1.setTextColor(color)
            desc_day1.setTextColor(color)
            date_day2.setTextColor(color)
            temp_day2.setTextColor(color)
            desc_day2.setTextColor(color)
            date_day3.setTextColor(color)
            temp_day3.setTextColor(color)
            desc_day3.setTextColor(color)
            date_day4.setTextColor(color)
            temp_day4.setTextColor(color)
            desc_day4.setTextColor(color)

            // set fonts
            FontUtil.setFonts(ctx,main_temp_tv,label)
            FontUtil.setFonts(ctx,main_desc_tv,label)
            FontUtil.setFonts(ctx,main_minmax_tv,label)
            FontUtil.setFonts(ctx,main_city_tv,label)
            FontUtil.setFonts(ctx,main_humidity_tv,label)
            FontUtil.setFonts(ctx,main_wind_tv,label)
            FontUtil.setFonts(ctx,temp_day1,label)
            FontUtil.setFonts(ctx,temp_day2,label)
            FontUtil.setFonts(ctx,temp_day3,label)
            FontUtil.setFonts(ctx,temp_day4,label)
            FontUtil.setFonts(ctx,desc_day1,label)
            FontUtil.setFonts(ctx,desc_day2,label)
            FontUtil.setFonts(ctx,desc_day3,label)
            FontUtil.setFonts(ctx,desc_day4,label)
            FontUtil.setFonts(ctx,date_day1,label)
            FontUtil.setFonts(ctx,date_day2,label)
            FontUtil.setFonts(ctx,date_day3,label)
            FontUtil.setFonts(ctx,date_day4,label)


            main_humidity_tv.setPadding(desc_size.toInt(),0,0,0)
            main_wind_tv.setPadding(desc_size.toInt(),0,0,0)

            UiUtils.setWeatherVecotImage(main_humidity_iv,1002,color, ctx)
            UiUtils.setWeatherVecotImage(main_wind_iv,1003,color, ctx)


            // set dynamic data
            var unit = DataParsingSetting.getUnit(item.settings)
            UiUtils.setWeatherVecotImage(main_iv,weather_data?.current?.icon!!,ctx.resources.getColor(R.color.white),ctx)

            main_temp_tv.text = "${weather_data?.current?.temp}°${unit.toUpperCase()}"
            main_desc_tv.text = "${weather_data?.current?.desc}"
            main_minmax_tv.text = "${weather_data?.current?.tempMax}°$unit/${weather_data?.current?.tempMin}°$unit"
            main_city_tv.text = "${weather_data?.current?.city}, ${weather_data?.current?.country}"
            main_humidity_tv.text = "${weather_data?.current?.humidity}%"
            main_wind_tv.text = "${weather_data?.current?.wind} m/s"

            // day 1
            date_day1.text = "${weather_data?.forecast?.get(0)?.dt}"
            temp_day1.text = "${weather_data?.forecast?.get(0)?.tempMax}°$unit/${weather_data?.forecast?.get(0)?.tempMin}°$unit"
            desc_day1.text = "${weather_data?.forecast?.get(0)?.desc}"
            UiUtils.setWeatherVecotImage(iv_day1,weather_data?.forecast?.get(0)?.icon,color,ctx)
            // day 2
            date_day2.text = "${weather_data?.forecast?.get(1)?.dt}"
            temp_day2.text = "${weather_data?.forecast?.get(1)?.tempMax}°$unit/${weather_data?.forecast?.get(0)?.tempMin}°$unit"
            desc_day2.text = "${weather_data?.forecast?.get(1)?.desc}"
            UiUtils.setWeatherVecotImage(iv_day2,weather_data?.forecast?.get(1)?.icon,color,ctx)
            // day 3
            date_day3.text = "${weather_data?.forecast?.get(2)?.dt}"
            temp_day3.text = "${weather_data?.forecast?.get(2)?.tempMax}°$unit/${weather_data?.forecast?.get(0)?.tempMin}°$unit"
            desc_day3.text = "${weather_data?.forecast?.get(2)?.desc}"
            UiUtils.setWeatherVecotImage(iv_day3,weather_data?.forecast?.get(2)?.icon,color,ctx)
            // day 4
            date_day4.text = "${weather_data?.forecast?.get(2)?.dt}"
            temp_day4.text = "${weather_data?.forecast?.get(2)?.tempMax}°$unit/${weather_data?.forecast?.get(0)?.tempMin}°$unit"
            desc_day4.text = "${weather_data?.forecast?.get(2)?.desc}"
            UiUtils.setWeatherVecotImage(iv_day4,weather_data?.forecast?.get(3)?.icon,color,ctx)

            return view
        }

        // four day vertical
        fun getWidgetWeatherFourDayVerti(ctx:Context,item: Item, weather_data: WeatherFive):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_weather_4day_v,null)

            var ll_main = view.findViewById<LinearLayout>(R.id.ll_waether4day_main_v)
            var ll_main_box = view.findViewById<LinearLayout>(R.id.ll_main_bg)
            var ll_main_bottom = view.findViewById<LinearLayout>(R.id.ll_main_bottomview_v)

            var main_iv = view.findViewById<ImageView>(R.id.iv_weat4v_main_image)
            var main_temp_tv = view.findViewById<TextView>(R.id.tv_weat4v_main_temp)
            var main_desc_tv = view.findViewById<TextView>(R.id.tv_weat4v_main_desc)
            var main_minmax_tv = view.findViewById<TextView>(R.id.tv_weat4v_main_tempminmax)
            var main_city_tv = view.findViewById<TextView>(R.id.tv_weat4v_main_city)
            var main_humidity_iv = view.findViewById<ImageView>(R.id.iv_weat4v_main_humadity)
            var main_humidity_tv = view.findViewById<TextView>(R.id.tv_weat4v_main_humadity)
            var main_wind_tv = view.findViewById<TextView>(R.id.tv_weat4v_main_wind)
            var main_wind_iv = view.findViewById<ImageView>(R.id.iv_weat4v_main_wind)

            var ll_day1 = view.findViewById<LinearLayout>(R.id.ll_waether4day_day1_v)
            var iv_day1 = view.findViewById<ImageView>(R.id.iv_weat4v_day1)
            var temp_day1 = view.findViewById<TextView>(R.id.tv_weat4v_day1_minmax)
            var desc_day1 = view.findViewById<TextView>(R.id.tv_weat4v_day1_desc)
            var date_day1 = view.findViewById<TextView>(R.id.tv_weat4v_day1_date)

            var ll_day2 = view.findViewById<LinearLayout>(R.id.ll_waether4day_day2_v)
            var iv_day2 = view.findViewById<ImageView>(R.id.iv_weat4v_day2)
            var temp_day2 = view.findViewById<TextView>(R.id.tv_weat4v_day2_minmax)
            var desc_day2 = view.findViewById<TextView>(R.id.tv_weat4v_day2_desc)
            var date_day2 = view.findViewById<TextView>(R.id.tv_weat4v_day2_date)

            var ll_day3 = view.findViewById<LinearLayout>(R.id.ll_waether4day_day3_v)
            var iv_day3 = view.findViewById<ImageView>(R.id.iv_weat4v_day3)
            var temp_day3 = view.findViewById<TextView>(R.id.tv_weat4v_day3_minmax)
            var desc_day3 = view.findViewById<TextView>(R.id.tv_weat4v_day3_desc)
            var date_day3 = view.findViewById<TextView>(R.id.tv_weat4v_day3_date)

            var ll_day4 = view.findViewById<LinearLayout>(R.id.ll_waether4day_day4_v)
            var iv_day4 = view.findViewById<ImageView>(R.id.iv_weat4v_day4)
            var temp_day4 = view.findViewById<TextView>(R.id.tv_weat4v_day4_minmax)
            var desc_day4 = view.findViewById<TextView>(R.id.tv_weat4v_day4_desc)
            var date_day4 = view.findViewById<TextView>(R.id.tv_weat4v_day4_date)

            var color = DataParsingSetting.getTintColor(item.settings)
            var label = DataParsingSetting.getFontLabel(item.settings)

            // main box
            var main_box_hi = getDay4VMainBoxHight(item)
            var main_box_wi = getDay4VMainBoxWidth(item)

            var main_size = LinearLayout.LayoutParams(main_box_wi,main_box_hi)
            main_size.setMargins(2,1,2,0)
            ll_main.layoutParams = main_size

            var padding = main_box_hi/20
            main_temp_tv.setPadding(padding,padding/2,0,0)
            main_desc_tv.setPadding(padding,0,0,0)
            main_minmax_tv.setPadding(padding,0,0,0)

            // child box
            var child_box_hi = getDay4VChildBoxHight(item)
            var child_size = LinearLayout.LayoutParams(main_box_wi,child_box_hi)
            child_size.setMargins(2,2,2,0)
            ll_day1.layoutParams = child_size
            ll_day2.layoutParams = child_size
            ll_day3.layoutParams = child_size
            ll_day4.layoutParams = child_size

            var mainiv_lp = LinearLayout.LayoutParams((main_box_hi/2.5).toInt(),(main_box_hi/2.5).toInt())
            mainiv_lp.setMargins(0,main_box_hi/15,main_box_hi/15,0)
            main_iv.layoutParams = mainiv_lp
            var btm_layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, main_box_hi/7)
            ll_main_bottom.layoutParams = btm_layoutParams

            var text_padding = child_box_hi/14
            temp_day1.setPadding(text_padding,text_padding,0,0)
            desc_day1.setPadding(text_padding,0,0,0)

            temp_day2.setPadding(text_padding,text_padding,0,0)
            desc_day2.setPadding(text_padding,0,0,0)

            temp_day3.setPadding(text_padding,text_padding,0,0)
            desc_day3.setPadding(text_padding,0,0,0)

            temp_day4.setPadding(text_padding,text_padding,0,0)
            desc_day4.setPadding(text_padding,0,0,0)

            // main box
            var temp_size = (main_box_hi /15).toFloat()
            var desc_size = (main_box_hi/25).toFloat()
            var icon_size = (main_box_hi/20).toFloat()

            main_humidity_iv.layoutParams = getLayoutParam(icon_size.toInt(),icon_size.toInt())
            main_wind_iv.layoutParams = getLayoutParam(icon_size.toInt(),icon_size.toInt())
            main_humidity_tv.textSize = desc_size
            main_wind_tv.textSize = desc_size

            main_temp_tv.textSize = temp_size
            main_desc_tv.textSize = desc_size
            main_minmax_tv.textSize = desc_size
            main_city_tv.textSize = desc_size

            var child_iv_layout = LinearLayout.LayoutParams(child_box_hi/2,child_box_hi/2)
            child_iv_layout.setMargins(0,5,5,0)

            date_day1.textSize = desc_size
            temp_day1.textSize = desc_size
            desc_day1.textSize = desc_size
            iv_day1.layoutParams = child_iv_layout

            date_day2.textSize = desc_size
            temp_day2.textSize = desc_size
            desc_day2.textSize = desc_size
            iv_day2.layoutParams = child_iv_layout

            date_day3.textSize = desc_size
            temp_day3.textSize = desc_size
            desc_day3.textSize = desc_size
            iv_day3.layoutParams = child_iv_layout

            date_day4.textSize = desc_size
            temp_day4.textSize = desc_size
            desc_day4.textSize = desc_size
            iv_day4.layoutParams = child_iv_layout

            UiUtils.setDrawableView(ll_main,Color.parseColor("#FFFFFF"),Color.parseColor("#FFEB3B"))
            UiUtils.setDrawableOnlyTop(ll_main_box,color)
            UiUtils.setDrawableOnlyBottom(ll_main_bottom,Color.parseColor("#ffffff"))
            UiUtils.setDrawableOnlyBottom(ll_main_bottom,Color.parseColor("#ffffff"))

            UiUtils.setDrawableView(ll_day1,Color.parseColor("#FFFFFF"),color)
            UiUtils.setDrawableOnlyBottom(date_day1,Color.parseColor("#EBF5FB"))

            UiUtils.setDrawableView(ll_day2,Color.parseColor("#FFFFFF"),color)
            UiUtils.setDrawableOnlyBottom(date_day2,Color.parseColor("#EBF5FB"))

            UiUtils.setDrawableView(ll_day3,Color.parseColor("#FFFFFF"),color)
            UiUtils.setDrawableOnlyBottom(date_day3,Color.parseColor("#EBF5FB"))

            UiUtils.setDrawableView(ll_day4,Color.parseColor("#FFFFFF"),color)
            UiUtils.setDrawableOnlyBottom(date_day4,Color.parseColor("#EBF5FB"))

            main_humidity_tv.setTextColor(color)
            main_wind_tv.setTextColor(color)
            main_city_tv.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity("#000000","0.1")))
            date_day1.setTextColor(color)
            temp_day1.setTextColor(color)
            desc_day1.setTextColor(color)
            date_day2.setTextColor(color)
            temp_day2.setTextColor(color)
            desc_day2.setTextColor(color)
            date_day3.setTextColor(color)
            temp_day3.setTextColor(color)
            desc_day3.setTextColor(color)
            date_day4.setTextColor(color)
            temp_day4.setTextColor(color)
            desc_day4.setTextColor(color)

            // set fonts
            FontUtil.setFonts(ctx,main_temp_tv,label)
            FontUtil.setFonts(ctx,main_desc_tv,label)
            FontUtil.setFonts(ctx,main_minmax_tv,label)
            FontUtil.setFonts(ctx,main_city_tv,label)
            FontUtil.setFonts(ctx,main_humidity_tv,label)
            FontUtil.setFonts(ctx,main_wind_tv,label)
            FontUtil.setFonts(ctx,temp_day1,label)
            FontUtil.setFonts(ctx,temp_day2,label)
            FontUtil.setFonts(ctx,temp_day3,label)
            FontUtil.setFonts(ctx,temp_day4,label)
            FontUtil.setFonts(ctx,desc_day1,label)
            FontUtil.setFonts(ctx,desc_day2,label)
            FontUtil.setFonts(ctx,desc_day3,label)
            FontUtil.setFonts(ctx,desc_day4,label)
            FontUtil.setFonts(ctx,date_day1,label)
            FontUtil.setFonts(ctx,date_day2,label)
            FontUtil.setFonts(ctx,date_day3,label)
            FontUtil.setFonts(ctx,date_day4,label)

            // set dynamic data
            var unit = DataParsingSetting.getUnit(item.settings)
            UiUtils.setWeatherVecotImage(main_iv,weather_data?.current?.icon!!,ctx.resources.getColor(R.color.white),ctx)

            main_temp_tv.text = "${weather_data?.current?.temp}°${unit.toUpperCase()}"
            main_desc_tv.text = "${weather_data?.current?.desc}"
            main_minmax_tv.text = "${weather_data?.current?.tempMax}°$unit/${weather_data?.current?.tempMin}°$unit"
            main_city_tv.text = "${weather_data?.current?.city}, ${weather_data?.current?.country}"
            main_humidity_tv.text = "${weather_data?.current?.humidity}%"
            main_wind_tv.text = "${weather_data?.current?.wind} m/s"

            main_humidity_tv.setPadding(desc_size.toInt(),0,0,0)
            main_wind_tv.setPadding(desc_size.toInt(),0,0,0)

            UiUtils.setWeatherVecotImage(main_humidity_iv,1002,color, ctx)
            UiUtils.setWeatherVecotImage(main_wind_iv,1003,color, ctx)

            // day 1
            date_day1.text = "${weather_data?.forecast?.get(0)?.dt}"
            temp_day1.text = "${weather_data?.forecast?.get(0)?.tempMax}°$unit/${weather_data?.forecast?.get(0)?.tempMin}°$unit"
            desc_day1.text = "${weather_data?.forecast?.get(0)?.desc}"
            UiUtils.setWeatherVecotImage(iv_day1,weather_data?.forecast?.get(0)?.icon,color,ctx)
            // day 2
            date_day2.text = "${weather_data?.forecast?.get(1)?.dt}"
            temp_day2.text = "${weather_data?.forecast?.get(1)?.tempMax}°$unit/${weather_data?.forecast?.get(0)?.tempMin}°$unit"
            desc_day2.text = "${weather_data?.forecast?.get(1)?.desc}"
            UiUtils.setWeatherVecotImage(iv_day2,weather_data?.forecast?.get(1)?.icon,color,ctx)
            // day 3
            date_day3.text = "${weather_data?.forecast?.get(2)?.dt}"
            temp_day3.text = "${weather_data?.forecast?.get(2)?.tempMax}°$unit/${weather_data?.forecast?.get(0)?.tempMin}°$unit"
            desc_day3.text = "${weather_data?.forecast?.get(2)?.desc}"
            UiUtils.setWeatherVecotImage(iv_day3,weather_data?.forecast?.get(2)?.icon,color,ctx)
            // day 4
            date_day4.text = "${weather_data?.forecast?.get(2)?.dt}"
            temp_day4.text = "${weather_data?.forecast?.get(2)?.tempMax}°$unit/${weather_data?.forecast?.get(0)?.tempMin}°$unit"
            desc_day4.text = "${weather_data?.forecast?.get(2)?.desc}"
            UiUtils.setWeatherVecotImage(iv_day4,weather_data?.forecast?.get(3)?.icon,color,ctx)

            return view
        }

        // five day horizontal
        fun getWidgetWeatherFiveDayHori(ctx:Context,item: Item, weather_data: WeatherFive):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_weather_5day_h,null)

            var ll_1 = view.findViewById<LinearLayout>(R.id.ll_5day_hori1)
            var ll_2 = view.findViewById<LinearLayout>(R.id.ll_5day_hori2)
            var ll_3 = view.findViewById<LinearLayout>(R.id.ll_5day_hori3)
            var ll_4 = view.findViewById<LinearLayout>(R.id.ll_5day_hori4)
            var ll_5 = view.findViewById<LinearLayout>(R.id.ll_5day_hori5)

            var day_tv_1 = view.findViewById<TextView>(R.id.tv_weat5h_desc1)
            var day_tv_2 = view.findViewById<TextView>(R.id.tv_weat5h_desc2)
            var day_tv_3 = view.findViewById<TextView>(R.id.tv_weat5h_desc3)
            var day_tv_4 = view.findViewById<TextView>(R.id.tv_weat5h_desc4)
            var day_tv_5 = view.findViewById<TextView>(R.id.tv_weat5h_desc5)

            var day_tv_1_temp = view.findViewById<TextView>(R.id.tv_weat5h_day1)
            var day_tv_2_temp = view.findViewById<TextView>(R.id.tv_weat5h_day2)
            var day_tv_3_temp = view.findViewById<TextView>(R.id.tv_weat5h_day3)
            var day_tv_4_temp = view.findViewById<TextView>(R.id.tv_weat5h_day4)
            var day_tv_5_temp = view.findViewById<TextView>(R.id.tv_weat5h_day5)

            var iv_1 = view.findViewById<ImageView>(R.id.iv_weat5h_day1)
            var iv_2 = view.findViewById<ImageView>(R.id.iv_weat5h_day2)
            var iv_3 = view.findViewById<ImageView>(R.id.iv_weat5h_day3)
            var iv_4 = view.findViewById<ImageView>(R.id.iv_weat5h_day4)
            var iv_5 = view.findViewById<ImageView>(R.id.iv_weat5h_day5)

            var text_size = if(item.frame_h<item.frame_w/3) (getBoxHight(item)/16).toFloat()
            else (getBoxHight(item)/22).toFloat()

            day_tv_1.textSize = text_size
            day_tv_2.textSize = text_size
            day_tv_3.textSize = text_size
            day_tv_4.textSize = text_size
            day_tv_5.textSize = text_size

            day_tv_1_temp.textSize = text_size
            day_tv_2_temp.textSize = text_size
            day_tv_3_temp.textSize = text_size
            day_tv_4_temp.textSize = text_size
            day_tv_5_temp.textSize = text_size

            var unit = DataParsingSetting.getUnit(item.settings)
            var color = DataParsingSetting.getTintColor(item.settings)

            var forcast = weather_data.forecast
            if(forcast[0].dt!=null) {
                var names = forcast[0].dt?.split(",")
                day_tv_1.text = "${names!![0]}"
                day_tv_1_temp.text = "${forcast[0].tempMin}°$unit/${forcast[0].tempMax}°$unit"
                UiUtils.setWeatherVecotImage(iv_1,forcast[0].icon,color,ctx)
            }
            if(forcast[1].dt!=null) {
                var names = forcast[1].dt?.split(",")
                day_tv_2.text = "${names!![0]}"
                day_tv_2_temp.text = "${forcast[1].tempMin}°$unit/${forcast[1].tempMax}°$unit"
                UiUtils.setWeatherVecotImage(iv_2,forcast[1].icon,color,ctx)
            }
            if(forcast[2].dt!=null) {
                var names = forcast[2].dt?.split(",")
                day_tv_3.text = "${names!![0]}"
                day_tv_3_temp.text = "${forcast[2].tempMin}°$unit/${forcast[2].tempMax}°$unit"
                UiUtils.setWeatherVecotImage(iv_3,forcast[2].icon,color,ctx)
            }
            if(forcast[3].dt!=null) {
                var names = forcast[3].dt?.split(",")
                day_tv_4.text = "${names!![0]}"
                day_tv_4_temp.text = "${forcast[3].tempMin}°$unit/${forcast[3].tempMax}°$unit"
                UiUtils.setWeatherVecotImage(iv_4,forcast[3].icon,color,ctx)
            }
            if(forcast[4].dt!=null) {
                var names = forcast[4].dt?.split(",")
                day_tv_5.text = "${names!![0]}"
                day_tv_5_temp.text = "${forcast[4].tempMin}°$unit/${forcast[4].tempMax}°$unit"
                UiUtils.setWeatherVecotImage(iv_5,forcast[4].icon,color,ctx)
            }

            var size = getDay5LayoutParam((item.frame_w/5.5).toFloat(),item)
            size.setMargins(2,2,2,2)

            ll_1.layoutParams = size
            ll_2.layoutParams = size
            ll_3.layoutParams = size
            ll_4.layoutParams = size
            ll_5.layoutParams = size


            var label = DataParsingSetting.getFontLabel(item.settings)
            FontUtil.setFonts(ctx,day_tv_1,label)
            FontUtil.setFonts(ctx,day_tv_2,label)
            FontUtil.setFonts(ctx,day_tv_3,label)
            FontUtil.setFonts(ctx,day_tv_4,label)
            FontUtil.setFonts(ctx,day_tv_5,label)
            FontUtil.setFonts(ctx,day_tv_1_temp,label)
            FontUtil.setFonts(ctx,day_tv_2_temp,label)
            FontUtil.setFonts(ctx,day_tv_3_temp,label)
            FontUtil.setFonts(ctx,day_tv_4_temp,label)
            FontUtil.setFonts(ctx,day_tv_5_temp,label)

            day_tv_1.setTextColor(color)
            day_tv_2.setTextColor(color)
            day_tv_3.setTextColor(color)
            day_tv_4.setTextColor(color)
            day_tv_5.setTextColor(color)
            day_tv_1_temp.setTextColor(color)
            day_tv_2_temp.setTextColor(color)
            day_tv_3_temp.setTextColor(color)
            day_tv_4_temp.setTextColor(color)
            day_tv_5_temp.setTextColor(color)

            UiUtils.setDrawableView(ll_1,Color.parseColor("#ffffff"),color)
            UiUtils.setDrawableView(ll_2,Color.parseColor("#ffffff"),color)
            UiUtils.setDrawableView(ll_3,Color.parseColor("#ffffff"),color)
            UiUtils.setDrawableView(ll_4,Color.parseColor("#ffffff"),color)
            UiUtils.setDrawableView(ll_5,Color.parseColor("#ffffff"),color)

            return view
        }

        // five day vertical
        fun getWidgetWeatherFiveDayVerti(ctx:Context,item: Item, weather_data: WeatherFive):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_weather_5day_v,null)

            var ll_1 = view.findViewById<LinearLayout>(R.id.ll_5day_vert1)
            var ll_2 = view.findViewById<LinearLayout>(R.id.ll_5day_vert2)
            var ll_3 = view.findViewById<LinearLayout>(R.id.ll_5day_vert3)
            var ll_4 = view.findViewById<LinearLayout>(R.id.ll_5day_vert4)
            var ll_5 = view.findViewById<LinearLayout>(R.id.ll_5day_vert5)

            var day_tv_1 = view.findViewById<TextView>(R.id.tv_weat5v_desc1)
            var day_tv_2 = view.findViewById<TextView>(R.id.tv_weat5v_desc2)
            var day_tv_3 = view.findViewById<TextView>(R.id.tv_weat5v_desc3)
            var day_tv_4 = view.findViewById<TextView>(R.id.tv_weat5v_desc4)
            var day_tv_5 = view.findViewById<TextView>(R.id.tv_weat5v_desc5)

            var day_tv_1_temp = view.findViewById<TextView>(R.id.tv_weat5v_temp1)
            var day_tv_2_temp = view.findViewById<TextView>(R.id.tv_weat5v_temp2)
            var day_tv_3_temp = view.findViewById<TextView>(R.id.tv_weat5v_temp3)
            var day_tv_4_temp = view.findViewById<TextView>(R.id.tv_weat5v_temp4)
            var day_tv_5_temp = view.findViewById<TextView>(R.id.tv_weat5v_temp5)

            var iv_1 = view.findViewById<ImageView>(R.id.iv_weat5v_image1)
            var iv_2 = view.findViewById<ImageView>(R.id.iv_weat5v_image2)
            var iv_3 = view.findViewById<ImageView>(R.id.iv_weat5v_image3)
            var iv_4 = view.findViewById<ImageView>(R.id.iv_weat5v_image4)
            var iv_5 = view.findViewById<ImageView>(R.id.iv_weat5v_image5)


            var text_size = (item.frame_h/55).toFloat()
            day_tv_1.textSize = text_size
            day_tv_2.textSize = text_size
            day_tv_3.textSize = text_size
            day_tv_4.textSize = text_size
            day_tv_5.textSize = text_size

            day_tv_1_temp.textSize = text_size
            day_tv_2_temp.textSize = text_size
            day_tv_3_temp.textSize = text_size
            day_tv_4_temp.textSize = text_size
            day_tv_5_temp.textSize = text_size

            var size = getDay5LayoutParamVertical(item)
            size.setMargins(2,2,2,2)

            ll_1.layoutParams = size
            ll_2.layoutParams = size
            ll_3.layoutParams = size
            ll_4.layoutParams = size
            ll_5.layoutParams = size

            var unit = DataParsingSetting.getUnit(item.settings)
            var color = DataParsingSetting.getTintColor(item.settings)

            var forcast = weather_data.forecast
            if(forcast[0].dt!=null) {
                var names = forcast[0].dt?.split(",")
                day_tv_1.text = "${names!![0]}"
                day_tv_1_temp.text = "${forcast[0].tempMin}°$unit/${forcast[0].tempMax}°$unit"
                UiUtils.setWeatherVecotImage(iv_1,forcast[0].icon,color,ctx)
            }
            if(forcast[1].dt!=null) {
                var names = forcast[1].dt?.split(",")
                day_tv_2.text = "${names!![0]}"
                day_tv_2_temp.text = "${forcast[1].tempMin}°$unit/${forcast[1].tempMax}°$unit"
                UiUtils.setWeatherVecotImage(iv_2,forcast[1].icon,color,ctx)
            }
            if(forcast[2].dt!=null) {
                var names = forcast[2].dt?.split(",")
                day_tv_3.text = "${names!![0]}"
                day_tv_3_temp.text = "${forcast[2].tempMin}°$unit/${forcast[2].tempMax}°$unit"
                UiUtils.setWeatherVecotImage(iv_3,forcast[2].icon,color,ctx)
            }
            if(forcast[3].dt!=null) {
                var names = forcast[3].dt?.split(",")
                day_tv_4.text = "${names!![0]}"
                day_tv_4_temp.text = "${forcast[3].tempMin}°$unit/${forcast[3].tempMax}°$unit"
                UiUtils.setWeatherVecotImage(iv_4,forcast[3].icon,color,ctx)
            }
            if(forcast[4].dt!=null) {
                var names = forcast[4].dt?.split(",")
                day_tv_5.text = "${names!![0]}"
                day_tv_5_temp.text = "${forcast[4].tempMin}°$unit/${forcast[4].tempMax}°$unit"
                UiUtils.setWeatherVecotImage(iv_5,forcast[4].icon,color,ctx)
            }

            var label = DataParsingSetting.getFontLabel(item.settings)
            FontUtil.setFonts(ctx,day_tv_1,label)
            FontUtil.setFonts(ctx,day_tv_2,label)
            FontUtil.setFonts(ctx,day_tv_3,label)
            FontUtil.setFonts(ctx,day_tv_4,label)
            FontUtil.setFonts(ctx,day_tv_5,label)
            FontUtil.setFonts(ctx,day_tv_1_temp,label)
            FontUtil.setFonts(ctx,day_tv_2_temp,label)
            FontUtil.setFonts(ctx,day_tv_3_temp,label)
            FontUtil.setFonts(ctx,day_tv_4_temp,label)
            FontUtil.setFonts(ctx,day_tv_5_temp,label)

            day_tv_1.setTextColor(color)
            day_tv_2.setTextColor(color)
            day_tv_3.setTextColor(color)
            day_tv_4.setTextColor(color)
            day_tv_5.setTextColor(color)
            day_tv_1_temp.setTextColor(color)
            day_tv_2_temp.setTextColor(color)
            day_tv_3_temp.setTextColor(color)
            day_tv_4_temp.setTextColor(color)
            day_tv_5_temp.setTextColor(color)

            UiUtils.setDrawableView(ll_1,Color.parseColor("#ffffff"),color)
            UiUtils.setDrawableView(ll_2,Color.parseColor("#ffffff"),color)
            UiUtils.setDrawableView(ll_3,Color.parseColor("#ffffff"),color)
            UiUtils.setDrawableView(ll_4,Color.parseColor("#ffffff"),color)
            UiUtils.setDrawableView(ll_5,Color.parseColor("#ffffff"),color)

            UiUtils.setDrawableViewDay(day_tv_1,Color.parseColor("#F3F3F3"))
            UiUtils.setDrawableViewDay(day_tv_2,Color.parseColor("#F3F3F3"))
            UiUtils.setDrawableViewDay(day_tv_3,Color.parseColor("#F3F3F3"))
            UiUtils.setDrawableViewDay(day_tv_4,Color.parseColor("#F3F3F3"))
            UiUtils.setDrawableViewDay(day_tv_5,Color.parseColor("#F3F3F3"))


            return view
        }

        fun getBoxHight(item: Item): Int {
            if(item.frame_h<item.frame_w/3) return (item.frame_h/1.2).toInt()
            else return (item.frame_w/3).toInt()
        }

        fun getLayoutParam(item: Item): LinearLayout.LayoutParams {
            if(item.frame_h<item.frame_w/3) return LinearLayout.LayoutParams((item.frame_h/1.6).toInt(),(item.frame_h/1.6).toInt())
            else return LinearLayout.LayoutParams((item.frame_w/3.5).toInt(),(item.frame_w/3.5).toInt())
        }

        fun getIconLayoutParam(hight: Float): LinearLayout.LayoutParams {
            return LinearLayout.LayoutParams(hight.toInt(),hight.toInt())
        }

        fun getDay5LayoutParam(hight: Float, item: Item): LinearLayout.LayoutParams {
            if(hight*1.2>item.frame_h) return LinearLayout.LayoutParams(hight.toInt(),hight.toInt())
            else return LinearLayout.LayoutParams(hight.toInt(),(hight*1.2).toInt())
        }

        fun getDay4LayoutParam(item: Item): LinearLayout.LayoutParams {
            var width = (item.frame_w/3.5).toInt()
            var hight = (item.frame_w/5.2).toInt()
            return LinearLayout.LayoutParams(width,hight)
        }

        fun getLayoutParam(width: Int, hight: Int): LinearLayout.LayoutParams {
            return LinearLayout.LayoutParams(width,hight)
        }

        fun getDay4MainIvLayoutParam(item: Item): LinearLayout.LayoutParams {
            var hight = getDay4HMainBoxHight(item)/3
            return LinearLayout.LayoutParams(hight,hight)
        }

        fun getDay4ChildIvLayoutParam(item: Item): LinearLayout.LayoutParams {
            var hight = (getDay4ChildBoxHight(item)/2.3).toInt()
            return LinearLayout.LayoutParams(hight,hight)
        }


        fun getDay5LayoutParamVertical(item: Item): LinearLayout.LayoutParams {
            var width = item.frame_h/5
            var hight = item.frame_h/5.8
            return LinearLayout.LayoutParams(width,hight.toInt())
        }

        fun getDay4HMainBoxHight(item: Item): Int {
            var width = (item.frame_w/2.6).toInt()
            return if (width>item.frame_h) (item.frame_h/1.1).toInt() else width
        }

        fun getDay4ChildBoxHight(item: Item): Int {
            var hight_child = (item.frame_w/5.2).toInt()
            var width = (item.frame_w/2.6).toInt()
            Log.d("TAG", "getDay4ChildBoxHight: hi - ${item.frame_h} , wi - $width")
            return if (width>item.frame_h) (item.frame_h/2.1).toInt() else hight_child
        }

        fun getDay4ChildBoxWidth(item: Item): Int {
            return (getDay4ChildBoxHight(item)*1.5).toInt()
        }

        // vertical
        fun getDay4VMainBoxHight(item: Item): Int {
            return (item.frame_h/3.3).toInt()
        }

        fun getDay4VMainBoxWidth(item: Item): Int {
            return (item.frame_h/2.9).toInt()
        }

        fun getDay4VChildBoxHight(item: Item): Int {
            return (item.frame_h/6.1).toInt()
        }

    }
}