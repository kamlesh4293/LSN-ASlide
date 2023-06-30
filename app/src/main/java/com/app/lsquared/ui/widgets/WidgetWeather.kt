package com.app.lsquared.ui.widgets

import WeatherFive
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toolbar.LayoutParams
import com.app.lsquared.R
import com.app.lsquared.databinding.*
import com.app.lsquared.model.Item
import com.app.lsquared.pasring.DataParsingSetting
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.FontUtil
import java.text.DecimalFormat

class WidgetWeather {

    // current date template 1
    fun getWidgetWeatherCurremtTemp1(ctx: Context, item: Item, weather_data: WeatherFive):View{

        var binding = TempWeatherT1Binding.inflate((ctx as Activity).layoutInflater)

        var width = item.frame_w
        var hight = item.frame_h
        binding.ivWeatherTemp1Image.layoutParams = LinearLayout.LayoutParams(width/3,hight)

        var color = DataParsingSetting.getTintColor(item.settings)
        binding.tvWeatherTemp1Temp.setTextColor(color)
        binding.tvWeatherTemp1Desc.setTextColor(color)
        binding.tvWeatherTemp1City.setTextColor(color)

        var unit = DataParsingSetting.getUnit(item.settings)

        var temp_text = "${weather_data.current?.temp.toString().substringBefore(".")}°$unit"
        var desc_text = UiUtils.convertCamelCaps("${weather_data.current?.desc}")
        var city_text = "${weather_data.current?.city}, ${weather_data.current?.country}"
        binding.tvWeatherTemp1Temp.text = temp_text
        binding.tvWeatherTemp1Desc.text = desc_text
        binding.tvWeatherTemp1City.text = city_text
        UiUtils.setWeatherVecotImage(binding.ivWeatherTemp1Image,weather_data.current?.icon,color,ctx)

        var tempsize = WeatherTextSize().getDesizeSize(temp_text,width/3,hight/4).toFloat()

        binding.tvWeatherTemp1Temp.textSize = tempsize/2
        binding.tvWeatherTemp1Desc.textSize = (tempsize/2.5).toFloat()
        binding.tvWeatherTemp1City.textSize = (tempsize/3).toFloat()

        var font_label = DataParsingSetting.getFontLabel(item.settings)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp1Temp,font_label)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp1Desc,font_label)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp1City,font_label)

        var padding = item.frame_w/20
        binding.llWeatherT1Content.setPadding(padding,0,0,0)

        return binding.root
    }

    // current date template 2
    fun getWidgetWeatherCurremtTemp2(ctx:Context,item: Item, weather_data: WeatherFive):View{

        var binding = TempWeatherT2Binding.inflate((ctx as Activity).layoutInflater)

        var width = item.frame_w
        var hight = item.frame_h
        binding.ivWeatherTemp2Image.layoutParams = LinearLayout.LayoutParams(width/3,hight/2)

        var color = DataParsingSetting.getTintColor(item.settings)
        binding.tvWeatherTemp2Temp.setTextColor(color)
        binding.tvWeatherTemp2Tempmin.setTextColor(color)
        binding.tvWeatherTemp2Tempmax.setTextColor(color)
        binding.tvWeatherTemp2Divide.setTextColor(color)
        binding.tvWeatherTemp2Divide.setBackgroundColor(color)

        var padding = item.frame_w/20
        binding.tvWeatherTemp2Temp.setPadding(padding,0,0,0)

        var param = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        param.setMargins(padding,0,padding,0)
        binding.tvWeatherTemp2Divide.layoutParams = param

        var unit = DataParsingSetting.getUnit(item.settings)

        var text_temp = "${weather_data.current?.temp.toString().substringBefore(".")}°$unit"
        var text_min = "L: ${weather_data.current?.tempMin.toString().substringBefore(".")}°$unit"
        var text_high = "H: ${weather_data.current?.tempMax.toString().substringBefore(".")}°$unit"
        binding.tvWeatherTemp2Temp.text = text_temp
        binding.tvWeatherTemp2Tempmin.text = text_min
        binding.tvWeatherTemp2Tempmax.text = text_high
        UiUtils.setWeatherVecotImage(binding.ivWeatherTemp2Image,weather_data.current?.icon,color,ctx)

        var tempsize = WeatherTextSize().getDesizeSize(text_temp,width/3,hight/3).toFloat()
        var descsize = WeatherTextSize().getDesizeSize(" $text_min  $text_high ",
            (width/1.5).toInt(),hight/3).toFloat()

        binding.tvWeatherTemp2Temp.textSize = tempsize/2
        binding.tvWeatherTemp2Tempmin.textSize = descsize/2
        binding.tvWeatherTemp2Tempmax.textSize = descsize/2
        binding.tvWeatherTemp2Divide.textSize = descsize/3

        var font_label = DataParsingSetting.getFontLabel(item.settings)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp2Temp,font_label)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp2Tempmin,font_label)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp2Tempmax,font_label)

        return binding.root
    }

    // current date template 3
    fun getWidgetWeatherCurremtTemp3(ctx:Context,item: Item, weather_data: WeatherFive):View{

        var binding = TempWeatherT3Binding.inflate((ctx as Activity).layoutInflater)

        var width = item.frame_w
        var hight = item.frame_h
        binding.ivWeatherTemp3Image.layoutParams = LinearLayout.LayoutParams(width/3,hight/2)

        var color = DataParsingSetting.getTintColor(item.settings)
        binding.tvWeatherTemp3Temp.setTextColor(color)
        binding.tvWeatherTemp3Temphigh.setTextColor(color)
        binding.tvWeatherTemp3Templow.setTextColor(color)
        binding.tvWeatherTemp3Divide.setTextColor(color)
        binding.tvWeatherTemp3Divide.setBackgroundColor(color)

        var unit = DataParsingSetting.getUnit(item.settings)

        var text_temp = "${weather_data.current?.temp.toString().substringBefore(".")}°$unit"
        var text_high = "${weather_data.current?.tempMin.toString().substringBefore(".")}°$unit"
        var text_low = "${weather_data.current?.tempMax.toString().substringBefore(".")}°$unit"

        binding.tvWeatherTemp3Temp.text = text_temp
        binding.tvWeatherTemp3Templow.text = text_high
        binding.tvWeatherTemp3Temphigh.text = text_low

        UiUtils.setWeatherVecotImage(binding.ivWeatherTemp3Image,weather_data.current?.icon,color,ctx)
        UiUtils.setWeatherVecotImage(binding.ivWeatherTemp3Higharrow,1000,color,ctx)
        UiUtils.setWeatherVecotImage(binding.ivWeatherTemp3Downarrow,1001,color,ctx)

        var tempsize = WeatherTextSize().getDesizeSize(text_temp,width/3,hight/3).toFloat()
        var descsize = WeatherTextSize().getDesizeSize(" $text_low  $text_high ", (width/1.5).toInt(),hight/3).toFloat()

        binding.tvWeatherTemp3Temp.textSize = tempsize/2
        binding.tvWeatherTemp3Templow.textSize = descsize/3
        binding.tvWeatherTemp3Temphigh.textSize = descsize/3
        binding.tvWeatherTemp3Divide.textSize = descsize/3
        binding.ivWeatherTemp3Higharrow.layoutParams = LinearLayout.LayoutParams((descsize/1).toInt(),(descsize/1).toInt())
        binding.ivWeatherTemp3Downarrow.layoutParams = LinearLayout.LayoutParams((descsize/1).toInt(),(descsize/1).toInt())


        var font_label = DataParsingSetting.getFontLabel(item.settings)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp3Temp,font_label)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp3Temphigh,font_label)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp3Templow,font_label)

        return binding.root
    }

    // current date template 4
    fun getWidgetWeatherCurremtTemp4(ctx: Context, item: Item, weather_data: WeatherFive):View{

        var binding = TempWeatherT4Binding.inflate((ctx as Activity).layoutInflater)

        var width = item.frame_w
        var hight = item.frame_h
        binding.ivWeatherTemp4Image.layoutParams = LinearLayout.LayoutParams(width,hight/3)

        var color = DataParsingSetting.getTintColor(item.settings)
        binding.tvWeatherTemp4Temp.setTextColor(color)
        binding.tvWeatherTemp4City.setTextColor(color)

        var unit = DataParsingSetting.getUnit(item.settings)

        var temp_text = "${weather_data.current?.temp.toString().substringBefore(".")}°$unit"
        var city_text = "${weather_data.current?.city}, ${weather_data.current?.country}"
        binding.tvWeatherTemp4Temp.text = temp_text
        binding.tvWeatherTemp4City.text = city_text
        UiUtils.setWeatherVecotImage(binding.ivWeatherTemp4Image,weather_data.current?.icon,color,ctx)

        var tempsize = WeatherTextSize().getDesizeSize(temp_text,width,hight/5).toFloat()
        var citysize = WeatherTextSize().getDesizeSize(city_text,width/2,hight/5).toFloat()

        binding.tvWeatherTemp4Temp.textSize = tempsize/2
        binding.tvWeatherTemp4City.textSize = citysize/2

        var font_label = DataParsingSetting.getFontLabel(item.settings)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp4Temp,font_label)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp4City,font_label)

        return binding.root
    }

    // four day horizontal
    fun getWidgetWeatherFourDayHori(ctx:Context,item: Item, weather_data: WeatherFive):View{

        var binding = FragmentWeather4dayHBinding.inflate ((ctx as Activity).layoutInflater)

        var trans_color = DataParsingSetting.getTintColorTrans(item.settings)
        var color = DataParsingSetting.getTintColor(item.settings)
        var label = DataParsingSetting.getFontLabel(item.settings)

        var width = item.frame_w
        var hight = item.frame_h

        // layout view
        var layout_hight = if(hight*2.5<width) (hight/1.1).toInt() else (width/2.6).toInt()
        var layout_width = (layout_hight*2.5).toInt()

        // main box
        var main_box_width = (layout_width/2.5).toInt()
        var main_lp = LinearLayout.LayoutParams(main_box_width,layout_hight)
        var margin = width/200
        main_lp.setMargins(margin,margin,margin,margin)
        binding.llWaether4dayMainH.layoutParams = main_lp

        // main images
//        var mainiv_lp = getDay4MainIvLayoutParam(item)
        var main_iv_width = main_box_width/3
        var main_iv_lp = LinearLayout.LayoutParams(main_iv_width,layout_hight/3)
        main_iv_lp.setMargins(0,margin,margin,0)
        binding.ivWeat4hMainImage.layoutParams = main_iv_lp

        // main bottom view
        var btm_layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,layout_hight/7)
        binding.llMainBottomview.layoutParams = btm_layoutParams

        // main city view
        var city_layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,layout_hight/7)
        city_layoutParams.setMargins(margin*3,margin,margin*3,margin*2)
        binding.tvWeat4hMainCity.layoutParams = city_layoutParams
        binding.tvWeat4hMainCity.setPadding(margin*3,0,0,0)
        // other
        binding.tvWeat4hMainTemp.setPadding(margin*3,0,0,0)
        binding.tvWeat4hMainTempminmax.setPadding(margin*3,0,0,0)
        binding.tvWeat4hMainDesc.setPadding(margin*3,0,0,0)


        // other boxes
        var small_box_width = (layout_width - main_box_width)/2
        var small_box_hight = layout_hight/2

        var small_box_lp = LinearLayout.LayoutParams(small_box_width,small_box_hight)
        small_box_lp.setMargins(margin/2,margin/2,margin/2,margin/2)
        binding.llWaether4dayDay1H.layoutParams = small_box_lp
        binding.llWaether4dayDay2H.layoutParams = small_box_lp
        binding.llWaether4dayDay3H.layoutParams = small_box_lp
        binding.llWaether4dayDay4H.layoutParams = small_box_lp

        // smal images
        var small_img_width = small_box_width/3
        var small_img_lp = LinearLayout.LayoutParams(small_img_width,small_box_hight/2)
        small_img_lp.setMargins(margin*2,margin*2,margin*2,margin*2)
        binding.ivWeat4hDay1.layoutParams = small_img_lp
        binding.ivWeat4hDay2.layoutParams = small_img_lp
        binding.ivWeat4hDay3.layoutParams = small_img_lp
        binding.ivWeat4hDay4.layoutParams = small_img_lp

        // small temp minmax
        var small_temp_lp = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        var small_desc_lp = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        small_temp_lp.setMargins(margin*2,margin*2,0,0)
        small_desc_lp.setMargins(margin*2,0,0,0)
        binding.tvWeat4hDay1Minmax.layoutParams = small_temp_lp
        binding.tvWeat4hDay2Minmax.layoutParams = small_temp_lp
        binding.tvWeat4hDay3Minmax.layoutParams = small_temp_lp
        binding.tvWeat4hDay4Minmax.layoutParams = small_temp_lp
        binding.tvWeat4hDay1Desc.layoutParams = small_desc_lp
        binding.tvWeat4hDay2Desc.layoutParams = small_desc_lp
        binding.tvWeat4hDay3Desc.layoutParams = small_desc_lp
        binding.tvWeat4hDay4Desc.layoutParams = small_desc_lp

        // date time
        var small_day_lp = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        small_day_lp.setMargins(margin,margin,margin,margin)
        binding.tvWeat4hDay1Date.layoutParams = small_day_lp
        binding.tvWeat4hDay2Date.layoutParams = small_day_lp
        binding.tvWeat4hDay3Date.layoutParams = small_day_lp
        binding.tvWeat4hDay4Date.layoutParams = small_day_lp

        binding.tvWeat4hDay1Date.setPadding(margin,0,0,0)
        binding.tvWeat4hDay2Date.setPadding(margin,0,0,0)
        binding.tvWeat4hDay3Date.setPadding(margin,0,0,0)
        binding.tvWeat4hDay4Date.setPadding(margin,0,0,0)

//        var temp_size = (getDay4HMainBoxHight(item) /15).toFloat()
//        var main_desc_size = (getDay4HMainBoxHight(item)/20).toFloat()
//        var desc_size = (getDay4HMainBoxHight(item)/27).toFloat()
//        var icon_size = (getDay4HMainBoxHight(item)/17).toFloat()



        UiUtils.setDrawableView(binding.llWaether4dayMainH,color,color)
        UiUtils.setDrawableOnlyTop(binding.llMainBg,trans_color)
        UiUtils.setDrawableOnlyBottom(binding.llMainBottomview,Color.parseColor("#ffffff"))

        UiUtils.setDrawableView(binding.llWaether4dayDay1H,Color.parseColor("#FFFFFF"),color)
        UiUtils.setDrawableOnlyBottom(binding.tvWeat4hDay1Date,Color.parseColor("#EBF5FB"))

        UiUtils.setDrawableView(binding.llWaether4dayDay2H,Color.parseColor("#FFFFFF"),color)
        UiUtils.setDrawableOnlyBottom(binding.tvWeat4hDay2Date,Color.parseColor("#EBF5FB"))

        UiUtils.setDrawableView(binding.llWaether4dayDay3H,Color.parseColor("#FFFFFF"),color)
        UiUtils.setDrawableOnlyBottom(binding.tvWeat4hDay3Date,Color.parseColor("#EBF5FB"))

        UiUtils.setDrawableView(binding.llWaether4dayDay4H,Color.parseColor("#FFFFFF"),color)
        UiUtils.setDrawableOnlyBottom(binding.tvWeat4hDay4Date,Color.parseColor("#EBF5FB"))

        binding.tvWeat4hMainHumadity.setTextColor(color)
        binding.tvWeat4hMainWind.setTextColor(color)
        binding.tvWeat4hMainCity.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity("#000000","0.1")))
        binding.tvWeat4hDay1Date.setTextColor(color)
        binding.tvWeat4hDay1Minmax.setTextColor(color)
        binding.tvWeat4hDay1Desc.setTextColor(color)
        binding.tvWeat4hDay2Date.setTextColor(color)
        binding.tvWeat4hDay2Minmax.setTextColor(color)
        binding.tvWeat4hDay2Desc.setTextColor(color)
        binding.tvWeat4hDay3Date.setTextColor(color)
        binding.tvWeat4hDay3Minmax.setTextColor(color)
        binding.tvWeat4hDay3Desc.setTextColor(color)
        binding.tvWeat4hDay4Date.setTextColor(color)
        binding.tvWeat4hDay4Minmax.setTextColor(color)
        binding.tvWeat4hDay4Desc.setTextColor(color)

        // set fonts
        FontUtil.setFonts(ctx,binding.tvWeat4hMainTemp,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hMainDesc,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hMainTempminmax,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hMainCity,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hMainHumadity,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hMainWind,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hDay1Minmax,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hDay2Minmax,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hDay3Minmax,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hDay4Minmax,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hDay1Desc,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hDay2Desc,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hDay3Desc,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hDay4Desc,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hDay1Date,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hDay2Date,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hDay3Date,label)
        FontUtil.setFonts(ctx,binding.tvWeat4hDay4Date,label)

        UiUtils.setWeatherVecotImage(binding.ivWeat4hMainHumadity,1002,color, ctx)
        UiUtils.setWeatherVecotImage(binding.ivWeat4hMainWind,1003,color, ctx)


        // set dynamic data
        var unit = DataParsingSetting.getUnit(item.settings).toUpperCase()
        UiUtils.setWeatherVecotImage(binding.ivWeat4hMainImage,weather_data?.current?.icon!!,ctx.resources.getColor(R.color.white),ctx)

        binding.tvWeat4hMainTemp.text = "${DecimalFormat("0.#").format(weather_data?.current?.temp) }°${unit.toUpperCase()}"

        binding.tvWeat4hMainDesc.text = UiUtils.convertCamelCaps("${weather_data?.current?.desc}")
        binding.tvWeat4hMainTempminmax.text = "${DecimalFormat("0.#").format(weather_data?.current?.tempMax)}°$unit / " +
                "${DecimalFormat("0.#").format(weather_data?.current?.tempMin)}°$unit"
        binding.tvWeat4hMainCity.text = "${weather_data?.current?.city}, ${weather_data?.current?.country}"
        binding.tvWeat4hMainHumadity.text = "${weather_data?.current?.humidity}%"
        binding.tvWeat4hMainWind.text = "${DecimalFormat("0.#").format(weather_data?.current?.wind)} ${UiUtils.getWindUnit(item.settings)}"

        if(weather_data?.forecast?.size==0) return binding.root
        // day 1
        binding.tvWeat4hDay1Date.text = "${weather_data?.forecast?.get(0)?.dt}"
        binding.tvWeat4hDay1Minmax.text = "${"%.0f".format(weather_data?.forecast?.get(0)?.tempMax)}°$unit / " +
                "${"%.0f".format(weather_data?.forecast?.get(0)?.tempMin)}°$unit"
        binding.tvWeat4hDay1Desc.text = "${weather_data?.forecast?.get(0)?.desc}"
        UiUtils.setWeatherVecotImage(binding.ivWeat4hDay1,weather_data?.forecast?.get(0)?.icon,color,ctx)
        // day 2
        binding.tvWeat4hDay2Date.text = "${weather_data?.forecast?.get(1)?.dt}"
        binding.tvWeat4hDay2Minmax.text = "${"%.0f".format(weather_data?.forecast?.get(1)?.tempMax)}°$unit / " +
                "${"%.0f".format(weather_data?.forecast?.get(1)?.tempMin)}°$unit"
        binding.tvWeat4hDay2Desc.text = "${weather_data?.forecast?.get(1)?.desc}"
        UiUtils.setWeatherVecotImage(binding.ivWeat4hDay2,weather_data?.forecast?.get(1)?.icon,color,ctx)
        // day 3
        binding.tvWeat4hDay3Date.text = "${weather_data?.forecast?.get(2)?.dt}"
        binding.tvWeat4hDay3Minmax.text = "${"%.0f".format(weather_data?.forecast?.get(2)?.tempMax)}°$unit / " +
                "${"%.0f".format(weather_data?.forecast?.get(2)?.tempMin)}°$unit"
        binding.tvWeat4hDay3Desc.text = "${weather_data?.forecast?.get(2)?.desc}"
        UiUtils.setWeatherVecotImage(binding.ivWeat4hDay3,weather_data?.forecast?.get(2)?.icon,color,ctx)
        // day 4
        binding.tvWeat4hDay4Date.text = "${weather_data?.forecast?.get(3)?.dt}"
        binding.tvWeat4hDay4Minmax.text = "${"%.0f".format(weather_data?.forecast?.get(3)?.tempMax)}°$unit / " +
                "${"%.0f".format(weather_data?.forecast?.get(3)?.tempMin)}°$unit"
        binding.tvWeat4hDay4Desc.text = "${weather_data?.forecast?.get(3)?.desc}"
        UiUtils.setWeatherVecotImage(binding.ivWeat4hDay4,weather_data?.forecast?.get(3)?.icon,color,ctx)

        // set text size
        var main_temp_size = WeatherTextSize().getDesizeSize(" -14°c ", main_box_width-main_iv_width,layout_hight/7).toFloat()

        var desc_size = (getDay4HMainBoxHight(item)/27).toFloat()
        var icon_size = (getDay4HMainBoxHight(item)/17).toFloat()

        binding.ivWeat4hMainHumadity.layoutParams = getLayoutParam((icon_size*1.5).toInt(),(icon_size*1.5).toInt())
        binding.ivWeat4hMainWind.layoutParams = getLayoutParam((icon_size*1.5).toInt(),(icon_size*1.5).toInt())
        binding.tvWeat4hMainHumadity.textSize = desc_size
        binding.tvWeat4hMainWind.textSize = desc_size

        binding.tvWeat4hMainTemp.textSize = main_temp_size/2

        var main_desc_size = WeatherTextSize().getDesizeSize(weather_data?.current?.desc!!, main_box_width-main_iv_width,layout_hight/6).toFloat()
        binding.tvWeat4hMainDesc.textSize = main_desc_size/3

        var main_city_size = WeatherTextSize().getDesizeSize(weather_data?.current?.city!!,main_box_width,main_box_width/7).toFloat()
        binding.tvWeat4hMainCity.textSize = main_city_size/3

        var main_minmax_size = WeatherTextSize().getDesizeSize("32.32cc / 32.32cc", main_box_width-main_iv_width,layout_hight/6).toFloat()
        binding.tvWeat4hMainTempminmax.textSize = main_minmax_size/2


        // small box - temp
        var small_temp_size = WeatherTextSize().getDesizeSize("-14°c / -14°c",small_box_width-small_img_width,small_box_hight/7).toFloat()

        var temp_text = (small_temp_size/2.5).toFloat()
        binding.tvWeat4hDay1Minmax.textSize = temp_text
        binding.tvWeat4hDay2Minmax.textSize = temp_text
        binding.tvWeat4hDay3Minmax.textSize = temp_text
        binding.tvWeat4hDay4Minmax.textSize = temp_text

        // small box - desc
        var desc1 = weather_data?.forecast?.get(0)?.desc!!
        var desc2 = weather_data?.forecast?.get(1)?.desc!!
        var desc3 = weather_data?.forecast?.get(2)?.desc!!
        var desc4 = weather_data?.forecast?.get(3)?.desc!!
        var desc = if(desc4.length>desc1.length && desc4.length>desc2.length && desc4.length>desc3.length ) desc4
        else if(desc3.length>desc1.length && desc3.length>desc2.length && desc3.length>desc4.length) desc3
        else if(desc2.length>desc1.length && desc2.length>desc4.length && desc2.length>desc3.length) desc2
        else desc1
        var small_desc_size = WeatherTextSize().getDesizeSize(desc!!,small_box_width-small_img_width,small_box_hight/3).toFloat()
        var small_date_size = WeatherTextSize().getDesizeSize("Saturdat, 31, 2023",small_box_width,small_box_hight/7).toFloat()

        binding.tvWeat4hDay1Desc.textSize = small_desc_size
        binding.tvWeat4hDay2Desc.textSize = small_desc_size
        binding.tvWeat4hDay3Desc.textSize = small_desc_size
        binding.tvWeat4hDay4Desc.textSize = small_desc_size

        var date_text = (small_date_size/2.5).toFloat()
        binding.tvWeat4hDay1Date.textSize = date_text
        binding.tvWeat4hDay2Date.textSize = date_text
        binding.tvWeat4hDay3Date.textSize = date_text
        binding.tvWeat4hDay4Date.textSize = date_text


        binding.tvWeat4hMainHumadity.setPadding(desc_size.toInt(),0,0,0)
        binding.tvWeat4hMainWind.setPadding(desc_size.toInt(),0,0,0)


        return binding.root
    }

    // four day vertical
    fun getWidgetWeatherFourDayVerti(ctx:Context,item: Item, weather_data: WeatherFive):View{

        var binding = FragmentWeather4dayVBinding.inflate ((ctx as Activity).layoutInflater)

        var color = DataParsingSetting.getTintColor(item.settings)
        var label = DataParsingSetting.getFontLabel(item.settings)

        var width = item.frame_w
        var hight = item.frame_h

        // main box
        var main_box_hi = hight/4
        var main_box_wi = hight/4
        var margin = hight/150
        if(margin==0)margin = 1

        var main_size = LinearLayout.LayoutParams(main_box_wi,main_box_hi)
        main_size.setMargins(margin,margin,margin,margin)
        binding.llWaether4dayMainV.layoutParams = main_size

        var padding = main_box_hi/20
        binding.tvWeat4vMainTemp.setPadding(padding,padding/2,0,0)
        binding.tvWeat4vMainDesc.setPadding(padding,0,0,0)
        binding.tvWeat4vMainTempminmax.setPadding((padding*1.5).toInt(),0,0,0)

        // child box
        var child_box_hi = (hight*18/100)
        var child_size = LinearLayout.LayoutParams(main_box_wi,child_box_hi)
        child_size.setMargins(margin,margin,margin,0)
        binding.llWaether4dayDay1V.layoutParams = child_size
        binding.llWaether4dayDay2V.layoutParams = child_size
        binding.llWaether4dayDay3V.layoutParams = child_size
        binding.llWaether4dayDay4V.layoutParams = child_size

        Log.d("TAG", "getWidgetWeatherFourDayVerti: margin - $margin ,  $hight")
        var mainiv_lp = LinearLayout.LayoutParams(main_box_wi/3,main_box_wi/3)
        mainiv_lp.setMargins(0,margin*2,margin*2,0)
        binding.ivWeat4vMainImage.layoutParams = mainiv_lp

        var btm_layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, main_box_hi/7)
        binding.llMainBottomviewV.layoutParams = btm_layoutParams

        var main_city_lp = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, main_box_hi/7)
        if(margin==1) main_city_lp.setMargins(margin,margin,margin,margin)
        else main_city_lp.setMargins(margin*2,margin*2,margin*2,margin*2)
        binding.tvWeat4vMainCity.layoutParams = main_city_lp
        binding.tvWeat4vMainCity.setPadding( margin,0,0,0)

        var child_date_lp = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, main_box_hi/7)
        child_date_lp.setMargins(margin,margin,margin,margin)
        binding.tvWeat4vDay1Date.layoutParams = child_date_lp
        binding.tvWeat4vDay2Date.layoutParams = child_date_lp
        binding.tvWeat4vDay3Date.layoutParams = child_date_lp
        binding.tvWeat4vDay4Date.layoutParams = child_date_lp

        var text_padding = child_box_hi/14
        binding.tvWeat4vDay1Minmax.setPadding(text_padding,text_padding,0,0)
        binding.tvWeat4vDay1Desc.setPadding(text_padding,0,0,0)

        binding.tvWeat4vDay2Minmax.setPadding(text_padding,text_padding,0,0)
        binding.tvWeat4vDay2Desc.setPadding(text_padding,0,0,0)

        binding.tvWeat4vDay3Minmax.setPadding(text_padding,text_padding,0,0)
        binding.tvWeat4vDay3Desc.setPadding(text_padding,0,0,0)

        binding.tvWeat4vDay4Minmax.setPadding(text_padding,text_padding,0,0)
        binding.tvWeat4vDay4Desc.setPadding(text_padding,0,0,0)


        var child_iv_layout = LinearLayout.LayoutParams(child_box_hi/2,child_box_hi/2)
        child_iv_layout.setMargins(0,5,5,0)

        binding.ivWeat4vDay1.layoutParams = child_iv_layout
        binding.ivWeat4vDay2.layoutParams = child_iv_layout
        binding.ivWeat4vDay3.layoutParams = child_iv_layout
        binding.ivWeat4vDay4.layoutParams = child_iv_layout

        UiUtils.setDrawableView(binding.llWaether4dayMainV,Color.parseColor("#FFFFFF"),Color.parseColor("#FFEB3B"))
        UiUtils.setDrawableOnlyTop(binding.llMainBg,color)
        UiUtils.setDrawableOnlyBottom(binding.llMainBottomviewV,Color.parseColor("#ffffff"))
        UiUtils.setDrawableOnlyBottom(binding.llMainBottomviewV,Color.parseColor("#ffffff"))

        UiUtils.setDrawableView(binding.llWaether4dayDay1V,Color.parseColor("#FFFFFF"),color)
        UiUtils.setDrawableOnlyBottom(binding.tvWeat4vDay1Date,Color.parseColor("#EBF5FB"))

        UiUtils.setDrawableView(binding.llWaether4dayDay2V,Color.parseColor("#FFFFFF"),color)
        UiUtils.setDrawableOnlyBottom(binding.tvWeat4vDay2Date,Color.parseColor("#EBF5FB"))

        UiUtils.setDrawableView(binding.llWaether4dayDay3V,Color.parseColor("#FFFFFF"),color)
        UiUtils.setDrawableOnlyBottom(binding.tvWeat4vDay3Date,Color.parseColor("#EBF5FB"))

        UiUtils.setDrawableView(binding.llWaether4dayDay4V,Color.parseColor("#FFFFFF"),color)
        UiUtils.setDrawableOnlyBottom(binding.tvWeat4vDay4Date,Color.parseColor("#EBF5FB"))

        binding.tvWeat4vMainHumadity.setTextColor(color)
        binding.tvWeat4vMainWind.setTextColor(color)
        binding.tvWeat4vMainCity.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity("#000000","0.1")))
        binding.tvWeat4vDay1Date.setTextColor(color)
        binding.tvWeat4vDay1Minmax.setTextColor(color)
        binding.tvWeat4vDay1Desc.setTextColor(color)
        binding.tvWeat4vDay2Date.setTextColor(color)
        binding.tvWeat4vDay2Minmax.setTextColor(color)
        binding.tvWeat4vDay2Desc.setTextColor(color)
        binding.tvWeat4vDay3Date.setTextColor(color)
        binding.tvWeat4vDay3Minmax.setTextColor(color)
        binding.tvWeat4vDay3Desc.setTextColor(color)
        binding.tvWeat4vDay4Date.setTextColor(color)
        binding.tvWeat4vDay4Minmax.setTextColor(color)
        binding.tvWeat4vDay4Desc.setTextColor(color)

        // set fonts
        FontUtil.setFonts(ctx,binding.tvWeat4vMainTemp,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vMainDesc,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vMainTempminmax,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vMainCity,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vMainHumadity,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vMainWind,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vDay1Minmax,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vDay2Minmax,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vDay3Minmax,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vDay4Minmax,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vDay1Desc,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vDay2Desc,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vDay3Desc,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vDay4Desc,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vDay1Date,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vDay2Date,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vDay3Date,label)
        FontUtil.setFonts(ctx,binding.tvWeat4vDay4Date,label)

        // set dynamic data
        var unit = DataParsingSetting.getUnit(item.settings).toUpperCase()
        UiUtils.setWeatherVecotImage(binding.ivWeat4vMainImage,weather_data?.current?.icon!!,ctx.resources.getColor(R.color.white),ctx)

        binding.tvWeat4vMainTemp.text = "${DecimalFormat("0.#").format(weather_data?.current?.temp) }°${unit.toUpperCase()}"
        binding.tvWeat4vMainDesc.text = UiUtils.convertCamelCaps("${weather_data?.current?.desc}")
        binding.tvWeat4vMainTempminmax.text = "${DecimalFormat("0.#").format(weather_data?.current?.tempMax)}°$unit / " +
                "${DecimalFormat("0.#").format(weather_data?.current?.tempMin)}°$unit"
        binding.tvWeat4vMainCity.text = "${weather_data?.current?.city}, ${weather_data?.current?.country}"
        binding.tvWeat4vMainHumadity.text = "${weather_data?.current?.humidity}%"
        binding.tvWeat4vMainWind.text = "${DecimalFormat("0.#").format(weather_data?.current?.wind)} ${UiUtils.getWindUnit(item.settings)}"


        UiUtils.setWeatherVecotImage(binding.ivWeat4vMainHumadity,1002,color, ctx)
        UiUtils.setWeatherVecotImage(binding.ivWeat4vMainWind,1003,color, ctx)

        if(weather_data?.forecast?.size==0) return binding.root

        // day 1
        binding.tvWeat4vDay1Date.text = "${weather_data?.forecast?.get(0)?.dt}"
        binding.tvWeat4vDay1Minmax.text = "${"%.0f".format(weather_data?.forecast?.get(0)?.tempMax)}°$unit / " +
                "${"%.0f".format(weather_data?.forecast?.get(0)?.tempMin)}°$unit"
        binding.tvWeat4vDay1Desc.text = "${weather_data?.forecast?.get(0)?.desc}"
        UiUtils.setWeatherVecotImage(binding.ivWeat4vDay1,weather_data?.forecast?.get(0)?.icon,color,ctx)
        // day 2
        binding.tvWeat4vDay2Date.text = "${weather_data?.forecast?.get(1)?.dt}"
        binding.tvWeat4vDay2Minmax.text = "${"%.0f".format(weather_data?.forecast?.get(1)?.tempMax)}°$unit / " +
                "${"%.0f".format(weather_data?.forecast?.get(1)?.tempMin)}°$unit"
        binding.tvWeat4vDay2Desc.text = "${weather_data?.forecast?.get(1)?.desc}"
        UiUtils.setWeatherVecotImage(binding.ivWeat4vDay2,weather_data?.forecast?.get(1)?.icon,color,ctx)
        // day 3
        binding.tvWeat4vDay3Date.text = "${weather_data?.forecast?.get(2)?.dt}"
        binding.tvWeat4vDay3Minmax.text = "${"%.0f".format(weather_data?.forecast?.get(2)?.tempMax)}°$unit / " +
                "${"%.0f".format(weather_data?.forecast?.get(2)?.tempMin)}°$unit"
        binding.tvWeat4vDay3Desc.text = "${weather_data?.forecast?.get(2)?.desc}"
        UiUtils.setWeatherVecotImage(binding.ivWeat4vDay3,weather_data?.forecast?.get(2)?.icon,color,ctx)
        // day 4
        binding.tvWeat4vDay4Date.text = "${weather_data?.forecast?.get(3)?.dt}"
        binding.tvWeat4vDay4Minmax.text = "${"%.0f".format(weather_data?.forecast?.get(3)?.tempMax)}°$unit / " +
                "${"%.0f".format(weather_data?.forecast?.get(3)?.tempMin)}°$unit"
        binding.tvWeat4vDay4Desc.text = "${weather_data?.forecast?.get(3)?.desc}"
        UiUtils.setWeatherVecotImage(binding.ivWeat4vDay4,weather_data?.forecast?.get(3)?.icon,color,ctx)

        // main box
        var desc_size = (main_box_hi/25).toFloat()
        var icon_size = (main_box_hi/17).toFloat()

        var main_temp_text_size = TextSize().getDesizeSize("25.20  c",main_box_hi/3,main_box_hi/7,).toFloat()
        var main_desc_text_size = TextSize().getDesizeSize("${weather_data?.current!!.desc}", main_box_wi/3,main_box_hi/5,).toFloat()

        binding.tvWeat4vMainTemp.textSize = main_temp_text_size
        binding.tvWeat4vMainDesc.textSize = (main_desc_text_size/1.5).toFloat()
        binding.tvWeat4vMainTempminmax.textSize = main_temp_text_size/2
        binding.tvWeat4vMainCity.textSize = main_temp_text_size/2
        binding.tvWeat4vMainHumadity.textSize = main_temp_text_size/2
        binding.tvWeat4vMainWind.textSize = main_temp_text_size/2

        binding.ivWeat4vMainHumadity.layoutParams = getLayoutParam((icon_size*1.5).toInt(),(icon_size*1.5).toInt())
        binding.ivWeat4vMainWind.layoutParams = getLayoutParam((icon_size*1.5).toInt(),(icon_size*1.5).toInt())

        binding.tvWeat4vDay1Minmax.textSize = main_temp_text_size/2
        binding.tvWeat4vDay2Minmax.textSize = main_temp_text_size/2
        binding.tvWeat4vDay3Minmax.textSize = main_temp_text_size/2
        binding.tvWeat4vDay4Minmax.textSize = main_temp_text_size/2

        binding.tvWeat4vMainHumadity.setPadding(desc_size.toInt(),0,0,0)
        binding.tvWeat4vMainWind.setPadding(desc_size.toInt(),0,0,0)

        // small box - desc
        var desc1 = weather_data?.forecast?.get(0)?.desc!!
        var desc2 = weather_data?.forecast?.get(1)?.desc!!
        var desc3 = weather_data?.forecast?.get(2)?.desc!!
        var desc4 = weather_data?.forecast?.get(3)?.desc!!
        var desc = if(desc4.length>desc1.length && desc4.length>desc2.length && desc4.length>desc3.length ) desc4
        else if(desc3.length>desc1.length && desc3.length>desc2.length && desc3.length>desc4.length) desc3
        else if(desc2.length>desc1.length && desc2.length>desc4.length && desc2.length>desc3.length) desc2
        else desc1
        var small_desc_size = WeatherTextSize().getDesizeSize(desc!!,main_box_wi*2/3,child_box_hi/4).toFloat()
        var small_date_size = WeatherTextSize().getDesizeSize(" Wednesday, 31, 2023",main_box_wi,child_box_hi/8).toFloat()

        binding.tvWeat4vDay1Desc.textSize = small_desc_size
        binding.tvWeat4vDay2Desc.textSize = small_desc_size
        binding.tvWeat4vDay3Desc.textSize = small_desc_size
        binding.tvWeat4vDay4Desc.textSize = small_desc_size

        binding.tvWeat4vDay1Date.textSize = small_date_size/2
        binding.tvWeat4vDay2Date.textSize = small_date_size/2
        binding.tvWeat4vDay3Date.textSize = small_date_size/2
        binding.tvWeat4vDay4Date.textSize = small_date_size/2

        return binding.root
    }

    // five day horizontal
    fun getWidgetWeatherFiveDayHori(ctx:Context, item: Item, weather_data: WeatherFive):View{

        var binding = TempWeather5dayHBinding.inflate ((ctx as Activity).layoutInflater)

        var unit = DataParsingSetting.getUnit(item.settings).toUpperCase()
        var color = DataParsingSetting.getTintColor(item.settings)

        var forcast = weather_data.forecast
        if(forcast.size==0) return binding.root

        var box_width = (item.frame_w/5.5).toFloat()
        var box_hight = getDay5HoriHight(box_width,item)

        var size = getDay5LayoutParam(box_width,item)
        size.setMargins(2,2,2,2)

        binding.ll5dayHori1.layoutParams = size
        binding.ll5dayHori2.layoutParams = size
        binding.ll5dayHori3.layoutParams = size
        binding.ll5dayHori4.layoutParams = size
        binding.ll5dayHori5.layoutParams = size

        var day_temp_layout = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(box_hight/4).toInt())
        day_temp_layout.setMargins(2,2,2,2)

        binding.tvWeat5hDesc1.layoutParams = day_temp_layout
        binding.tvWeat5hDesc2.layoutParams = day_temp_layout
        binding.tvWeat5hDesc3.layoutParams = day_temp_layout
        binding.tvWeat5hDesc4.layoutParams = day_temp_layout
        binding.tvWeat5hDesc5.layoutParams = day_temp_layout
        binding.tvWeat5hDay1.layoutParams = day_temp_layout
        binding.tvWeat5hDay2.layoutParams = day_temp_layout
        binding.tvWeat5hDay3.layoutParams = day_temp_layout
        binding.tvWeat5hDay4.layoutParams = day_temp_layout
        binding.tvWeat5hDay5.layoutParams = day_temp_layout

        var text_size = (WeatherTextSize().getDesizeSize("Saturday",box_width.toInt(),(box_hight/5).toInt())/3).toFloat()

        binding.tvWeat5hDesc1.textSize = text_size
        binding.tvWeat5hDesc2.textSize = text_size
        binding.tvWeat5hDesc3.textSize = text_size
        binding.tvWeat5hDesc4.textSize = text_size
        binding.tvWeat5hDesc5.textSize = text_size

        binding.tvWeat5hDay1.textSize = text_size
        binding.tvWeat5hDay2.textSize = text_size
        binding.tvWeat5hDay3.textSize = text_size
        binding.tvWeat5hDay4.textSize = text_size
        binding.tvWeat5hDay5.textSize = text_size


        if(forcast[0].dt!=null) {
            binding.tvWeat5hDesc1.text = "Today"
            binding.tvWeat5hDay1.text = "${"%.0f".format(forcast[0].tempMax)}°$unit / " +
                    "${"%.0f".format(forcast[0].tempMin)}°$unit"
            UiUtils.setWeatherVecotImage(binding.ivWeat5hDay1,forcast[0].icon,color,ctx)
        }
        if(forcast[1].dt!=null) {
            binding.tvWeat5hDesc2.text = "Tomorrow"
            binding.tvWeat5hDay2.text = "${"%.0f".format(forcast[1].tempMax)}°$unit / " +
                    "${"%.0f".format(forcast[1].tempMin)}°$unit"
            UiUtils.setWeatherVecotImage(binding.ivWeat5hDay2,forcast[1].icon,color,ctx)
        }
        if(forcast[2].dt!=null) {
            var names = forcast[2].dt?.split(",")
            binding.tvWeat5hDesc3.text = "${names!![0]}"
            binding.tvWeat5hDay3.text = "${"%.0f".format(forcast[2].tempMax)}°$unit / " +
                    "${"%.0f".format(forcast[2].tempMin)}°$unit"
            UiUtils.setWeatherVecotImage(binding.ivWeat5hDay3,forcast[2].icon,color,ctx)
        }
        if(forcast[3].dt!=null) {
            var names = forcast[3].dt?.split(",")
            binding.tvWeat5hDesc4.text = "${names!![0]}"
            binding.tvWeat5hDay4.text = "${"%.0f".format(forcast[3].tempMax)}°$unit / " +
                    "${"%.0f".format(forcast[3].tempMin)}°$unit"
            UiUtils.setWeatherVecotImage(binding.ivWeat5hDay4,forcast[3].icon,color,ctx)
        }
        if(forcast[4].dt!=null) {
            var names = forcast[4].dt?.split(",")
            binding.tvWeat5hDesc5.text = "${names!![0]}"
            binding.tvWeat5hDay5.text = "${"%.0f".format(forcast[4].tempMax)}°$unit / " +
                    "${"%.0f".format(forcast[4].tempMin)}°$unit"
            UiUtils.setWeatherVecotImage(binding.ivWeat5hDay5,forcast[4].icon,color,ctx)
        }

        var label = DataParsingSetting.getFontLabel(item.settings)
        FontUtil.setFonts(ctx,binding.tvWeat5hDesc1,label)
        FontUtil.setFonts(ctx,binding.tvWeat5hDesc2,label)
        FontUtil.setFonts(ctx,binding.tvWeat5hDesc3,label)
        FontUtil.setFonts(ctx,binding.tvWeat5hDesc4,label)
        FontUtil.setFonts(ctx,binding.tvWeat5hDesc5,label)
        FontUtil.setFonts(ctx,binding.tvWeat5hDay1,label)
        FontUtil.setFonts(ctx,binding.tvWeat5hDay2,label)
        FontUtil.setFonts(ctx,binding.tvWeat5hDay3,label)
        FontUtil.setFonts(ctx,binding.tvWeat5hDay4,label)
        FontUtil.setFonts(ctx,binding.tvWeat5hDay5,label)

        binding.tvWeat5hDesc1.setTextColor(color)
        binding.tvWeat5hDesc2.setTextColor(color)
        binding.tvWeat5hDesc3.setTextColor(color)
        binding.tvWeat5hDesc4.setTextColor(color)
        binding.tvWeat5hDesc5.setTextColor(color)
        binding.tvWeat5hDay1.setTextColor(color)
        binding.tvWeat5hDay2.setTextColor(color)
        binding.tvWeat5hDay3.setTextColor(color)
        binding.tvWeat5hDay4.setTextColor(color)
        binding.tvWeat5hDay5.setTextColor(color)

        UiUtils.setDrawableView(binding.ll5dayHori1,Color.parseColor("#ffffff"),color)
        UiUtils.setDrawableView(binding.ll5dayHori2,Color.parseColor("#ffffff"),color)
        UiUtils.setDrawableView(binding.ll5dayHori3,Color.parseColor("#ffffff"),color)
        UiUtils.setDrawableView(binding.ll5dayHori4,Color.parseColor("#ffffff"),color)
        UiUtils.setDrawableView(binding.ll5dayHori5,Color.parseColor("#ffffff"),color)

        return binding.root
    }

    // five day vertical
    fun getWidgetWeatherFiveDayVerti(ctx:Context,item: Item, weather_data: WeatherFive):View{

        var binding = TempWeather5dayVBinding.inflate ((ctx as Activity).layoutInflater)

        var box_width = item.frame_h/5.8
        var box_hight = item.frame_h/5.5
        var size = getDay5LayoutParamVertical(box_width.toInt(),box_hight.toInt())
        var margin = (box_hight/70).toInt()
        if(margin<1) margin = 1
        size.setMargins(margin,margin,margin,margin)

        binding.ll5dayVert1.layoutParams = size
        binding.ll5dayVert2.layoutParams = size
        binding.ll5dayVert3.layoutParams = size
        binding.ll5dayVert4.layoutParams = size
        binding.ll5dayVert5.layoutParams = size

        var day_temp_layout = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(box_hight/4).toInt())
        var margin1 = margin*2
        day_temp_layout.setMargins(margin1,margin1,margin1,margin1)

        binding.tvWeat5vDesc1.layoutParams = day_temp_layout
        binding.tvWeat5vDesc2.layoutParams = day_temp_layout
        binding.tvWeat5vDesc3.layoutParams = day_temp_layout
        binding.tvWeat5vDesc4.layoutParams = day_temp_layout
        binding.tvWeat5vDesc5.layoutParams = day_temp_layout
        binding.tvWeat5vTemp1.layoutParams = day_temp_layout
        binding.tvWeat5vTemp2.layoutParams = day_temp_layout
        binding.tvWeat5vTemp3.layoutParams = day_temp_layout
        binding.tvWeat5vTemp4.layoutParams = day_temp_layout
        binding.tvWeat5vTemp5.layoutParams = day_temp_layout

        var text_size = (WeatherTextSize().getDesizeSize("Saturday",box_width.toInt(),(box_hight/5).toInt())/3).toFloat()

        binding.tvWeat5vDesc1.textSize = text_size
        binding.tvWeat5vDesc2.textSize = text_size
        binding.tvWeat5vDesc3.textSize = text_size
        binding.tvWeat5vDesc4.textSize = text_size
        binding.tvWeat5vDesc5.textSize = text_size

        binding.tvWeat5vTemp1.textSize = text_size
        binding.tvWeat5vTemp2.textSize = text_size
        binding.tvWeat5vTemp3.textSize = text_size
        binding.tvWeat5vTemp4.textSize = text_size
        binding.tvWeat5vTemp5.textSize = text_size

        var unit = DataParsingSetting.getUnit(item.settings).toUpperCase()
        var color = DataParsingSetting.getTintColor(item.settings)

        var forcast = weather_data.forecast

        if(forcast.size==0) return binding.root
        if(forcast[0].dt!=null) {
            binding.tvWeat5vDesc1.text = "Today"
            "%.2f".format(forcast[0].tempMax)
            binding.tvWeat5vTemp1.text = "${"%.0f".format(forcast[0].tempMax)}°$unit / " +
                    "${"%.0f".format(forcast[0].tempMin)}°$unit"
            UiUtils.setWeatherVecotImage(binding.ivWeat5vImage1,forcast[0].icon,color,ctx)
        }
        if(forcast[1].dt!=null) {
            binding.tvWeat5vDesc2.text = "Tomorrow"
            binding.tvWeat5vTemp2.text = "${"%.0f".format(forcast[1].tempMax)}°$unit / " +
                    "${"%.0f".format(forcast[1].tempMin)}°$unit"
            UiUtils.setWeatherVecotImage(binding.ivWeat5vImage2,forcast[1].icon,color,ctx)
        }
        if(forcast[2].dt!=null) {
            var names = forcast[2].dt?.split(",")
            binding.tvWeat5vDesc3.text = "${names!![0]}"
            binding.tvWeat5vTemp3.text = "${"%.0f".format(forcast[2].tempMax)}°$unit / " +
                    "${"%.0f".format(forcast[2].tempMin)}°$unit"
            UiUtils.setWeatherVecotImage(binding.ivWeat5vImage3,forcast[2].icon,color,ctx)
        }
        if(forcast[3].dt!=null) {
            var names = forcast[3].dt?.split(",")
            binding.tvWeat5vDesc4.text = "${names!![0]}"
            binding.tvWeat5vTemp4.text = "${"%.0f".format(forcast[3].tempMax)}°$unit / " +
                    "${"%.0f".format(forcast[3].tempMin)}°$unit"
            UiUtils.setWeatherVecotImage(binding.ivWeat5vImage4,forcast[3].icon,color,ctx)
        }
        if(forcast[4].dt!=null) {
            var names = forcast[4].dt?.split(",")
            binding.tvWeat5vDesc5.text = "${names!![0]}"
            binding.tvWeat5vTemp5.text = "${"%.0f".format(forcast[4].tempMax)}°$unit / " +
                    "${"%.0f".format(forcast[4].tempMin)}°$unit"
            UiUtils.setWeatherVecotImage(binding.ivWeat5vImage5,forcast[4].icon,color,ctx)
        }

        var label = DataParsingSetting.getFontLabel(item.settings)
        FontUtil.setFonts(ctx,binding.tvWeat5vDesc1,label)
        FontUtil.setFonts(ctx,binding.tvWeat5vDesc2,label)
        FontUtil.setFonts(ctx,binding.tvWeat5vDesc3,label)
        FontUtil.setFonts(ctx,binding.tvWeat5vDesc4,label)
        FontUtil.setFonts(ctx,binding.tvWeat5vDesc5,label)
        FontUtil.setFonts(ctx,binding.tvWeat5vTemp1,label)
        FontUtil.setFonts(ctx,binding.tvWeat5vTemp2,label)
        FontUtil.setFonts(ctx,binding.tvWeat5vTemp3,label)
        FontUtil.setFonts(ctx,binding.tvWeat5vTemp4,label)
        FontUtil.setFonts(ctx,binding.tvWeat5vTemp5,label)

        binding.tvWeat5vDesc1.setTextColor(color)
        binding.tvWeat5vDesc2.setTextColor(color)
        binding.tvWeat5vDesc3.setTextColor(color)
        binding.tvWeat5vDesc4.setTextColor(color)
        binding.tvWeat5vDesc5.setTextColor(color)
        binding.tvWeat5vTemp1.setTextColor(color)
        binding.tvWeat5vTemp2.setTextColor(color)
        binding.tvWeat5vTemp3.setTextColor(color)
        binding.tvWeat5vTemp4.setTextColor(color)
        binding.tvWeat5vTemp5.setTextColor(color)

        UiUtils.setDrawableView(binding.ll5dayVert1,Color.parseColor("#ffffff"),color,margin)
        UiUtils.setDrawableView(binding.ll5dayVert2,Color.parseColor("#ffffff"),color,margin)
        UiUtils.setDrawableView(binding.ll5dayVert3,Color.parseColor("#ffffff"),color,margin)
        UiUtils.setDrawableView(binding.ll5dayVert4,Color.parseColor("#ffffff"),color,margin)
        UiUtils.setDrawableView(binding.ll5dayVert5,Color.parseColor("#ffffff"),color,margin)

        UiUtils.setDrawableViewDay(binding.tvWeat5vDesc1,Color.parseColor("#F3F3F3"))
        UiUtils.setDrawableViewDay(binding.tvWeat5vDesc2,Color.parseColor("#F3F3F3"))
        UiUtils.setDrawableViewDay(binding.tvWeat5vDesc3,Color.parseColor("#F3F3F3"))
        UiUtils.setDrawableViewDay(binding.tvWeat5vDesc4,Color.parseColor("#F3F3F3"))
        UiUtils.setDrawableViewDay(binding.tvWeat5vDesc5,Color.parseColor("#F3F3F3"))


        return binding.root
    }


    fun getDay5LayoutParam(box_width: Float, item: Item): LinearLayout.LayoutParams {
        if(box_width*1.2>item.frame_h) return LinearLayout.LayoutParams(box_width.toInt(),box_width.toInt())
        else return LinearLayout.LayoutParams(box_width.toInt(),(box_width*1.2).toInt())
    }

    fun getDay5HoriHight(box_width: Float, item: Item): Float{
        return if(box_width*1.2>item.frame_h) box_width else (box_width*1.2).toFloat()
    }

    fun getLayoutParam(width: Int, hight: Int): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(width,hight)
    }



    fun getDay5LayoutParamVertical(box_width: Int, box_hight: Int): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(box_width,box_hight)
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


}

class WeatherTextSize{

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
