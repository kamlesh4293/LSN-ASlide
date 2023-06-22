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
import com.app.lsquared.databinding.FragmentWeather4dayHBinding
import com.app.lsquared.databinding.FragmentWeather4dayVBinding
import com.app.lsquared.databinding.TempWeather5dayHBinding
import com.app.lsquared.databinding.TempWeather5dayVBinding
import com.app.lsquared.databinding.TempWeatherT1Binding
import com.app.lsquared.databinding.TempWeatherT2Binding
import com.app.lsquared.databinding.TempWeatherT3Binding
import com.app.lsquared.model.Item
import com.app.lsquared.pasring.DataParsingSetting
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.FontUtil
import java.text.DecimalFormat


class WidgetWeather {

    // current date template 1
    fun getWidgetWeatherCurremtTemp1(ctx: Context, item: Item, weather_data: WeatherFive):View{

        var binding = TempWeatherT1Binding.inflate((ctx as Activity).layoutInflater)

        var text_size = (getBoxHight(item)/3).toFloat()
        binding.ivWeatherTemp1Image.layoutParams = getLayoutParam(item)
        binding.tvWeatherTemp1Temp.textSize = (text_size/1.2).toFloat()
        binding.tvWeatherTemp1Desc.textSize = (text_size/3).toFloat()
        binding.tvWeatherTemp1City.textSize = (text_size/3.5).toFloat()

        var color = DataParsingSetting.getTintColor(item.settings)
        binding.tvWeatherTemp1Temp.setTextColor(color)
        binding.tvWeatherTemp1Desc.setTextColor(color)
        binding.tvWeatherTemp1City.setTextColor(color)

        var unit = DataParsingSetting.getUnit(item.settings)

        binding.tvWeatherTemp1Temp.text = "${weather_data.current?.temp.toString().substringBefore(".")}°$unit"
        binding.tvWeatherTemp1Desc.text = UiUtils.convertCamelCaps("${weather_data.current?.desc}")
        binding.tvWeatherTemp1City.text = "${weather_data.current?.city}, ${weather_data.current?.country}"
        UiUtils.setWeatherVecotImage(binding.ivWeatherTemp1Image,weather_data.current?.icon,color,ctx)

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

        var text_size = (getBoxHight(item)/3).toFloat()
        binding.ivWeatherTemp2Image.layoutParams = getLayoutParam(item)
        binding.tvWeatherTemp2Temp.textSize = text_size
        binding.tvWeatherTemp2Tempmin.textSize = text_size/2
        binding.tvWeatherTemp2Tempmax.textSize = text_size/2
        binding.tvWeatherTemp2Divide.textSize = text_size/3

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

        binding.tvWeatherTemp2Temp.text = "${weather_data.current?.temp.toString().substringBefore(".")}°$unit"
        binding.tvWeatherTemp2Tempmin.text = "L: ${weather_data.current?.tempMin.toString().substringBefore(".")}°$unit"
        binding.tvWeatherTemp2Tempmax.text = "H: ${weather_data.current?.tempMax.toString().substringBefore(".")}°$unit"
        UiUtils.setWeatherVecotImage(binding.ivWeatherTemp2Image,weather_data.current?.icon,color,ctx)


        var font_label = DataParsingSetting.getFontLabel(item.settings)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp2Temp,font_label)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp2Tempmin,font_label)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp2Tempmax,font_label)

        return binding.root
    }

    // current date template 3
    fun getWidgetWeatherCurremtTemp3(ctx:Context,item: Item, weather_data: WeatherFive):View{

        var binding = TempWeatherT3Binding.inflate((ctx as Activity).layoutInflater)

        var text_size = (getBoxHight(item)/3).toFloat()
        binding.ivWeatherTemp3Image.layoutParams = getLayoutParam(item)
        binding.ivWeatherTemp3Higharrow.layoutParams = getIconLayoutParam(text_size)
        binding.ivWeatherTemp3Downarrow.layoutParams = getIconLayoutParam(text_size)
        binding.tvWeatherTemp3Temp.textSize = text_size
        binding.tvWeatherTemp3Temphigh.textSize = text_size/2
        binding.tvWeatherTemp3Templow.textSize = text_size/2
        binding.tvWeatherTemp3Divide.textSize = text_size/3

        var color = DataParsingSetting.getTintColor(item.settings)
        binding.tvWeatherTemp3Temp.setTextColor(color)
        binding.tvWeatherTemp3Temphigh.setTextColor(color)
        binding.tvWeatherTemp3Templow.setTextColor(color)
        binding.tvWeatherTemp3Divide.setTextColor(color)
        binding.tvWeatherTemp3Divide.setBackgroundColor(color)

        var unit = DataParsingSetting.getUnit(item.settings)

        binding.tvWeatherTemp3Temp.text = "${weather_data.current?.temp.toString().substringBefore(".")}°$unit"
        binding.tvWeatherTemp3Templow.text = "${weather_data.current?.tempMin.toString().substringBefore(".")}°$unit"
        binding.tvWeatherTemp3Temphigh.text = "${weather_data.current?.tempMax.toString().substringBefore(".")}°$unit"
        UiUtils.setWeatherVecotImage(binding.ivWeatherTemp3Image,weather_data.current?.icon,color,ctx)
        UiUtils.setWeatherVecotImage(binding.ivWeatherTemp3Higharrow,1000,color,ctx)
        UiUtils.setWeatherVecotImage(binding.ivWeatherTemp3Downarrow,1001,color,ctx)

        var font_label = DataParsingSetting.getFontLabel(item.settings)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp3Temp,font_label)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp3Temphigh,font_label)
        FontUtil.setFonts(ctx,binding.tvWeatherTemp3Templow,font_label)

        return binding.root
    }

    // four day horizontal
    fun getWidgetWeatherFourDayHori(ctx:Context,item: Item, weather_data: WeatherFive):View{

        var binding = FragmentWeather4dayHBinding.inflate ((ctx as Activity).layoutInflater)

        var trans_color = DataParsingSetting.getTintColorTrans(item.settings)
        var color = DataParsingSetting.getTintColor(item.settings)
        var label = DataParsingSetting.getFontLabel(item.settings)

        var mainiv_lp = getDay4MainIvLayoutParam(item)
        mainiv_lp.setMargins(0,10,10,0)
        binding.ivWeat4hMainImage.layoutParams = mainiv_lp
        var btm_layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
            getDay4HMainBoxHight(item)/7
        )
        binding.llMainBottomview.layoutParams = btm_layoutParams

        var main_box_hi = getDay4HMainBoxHight(item)
        var child_box_hi = getDay4ChildBoxHight(item)
        var child_box_wi = getDay4ChildBoxWidth(item)

        // main box
        var main_size = LinearLayout.LayoutParams(main_box_hi,main_box_hi)
        main_size.setMargins(2,2,2,2)
        binding.llWaether4dayMainH.layoutParams = main_size

        // child box
        var child_size = LinearLayout.LayoutParams(child_box_wi,child_box_hi)
        child_size.setMargins(2,2,2,2)
        binding.llWaether4dayDay1H.layoutParams = child_size
        binding.llWaether4dayDay2H.layoutParams = child_size
        binding.llWaether4dayDay3H.layoutParams = child_size
        binding.llWaether4dayDay4H.layoutParams = child_size

        var temp_size = (getDay4HMainBoxHight(item) /15).toFloat()
        var main_desc_size = (getDay4HMainBoxHight(item)/20).toFloat()
        var desc_size = (getDay4HMainBoxHight(item)/27).toFloat()
        var icon_size = (getDay4HMainBoxHight(item)/17).toFloat()

        binding.ivWeat4hMainHumadity.layoutParams = getLayoutParam((icon_size*1.5).toInt(),(icon_size*1.5).toInt())
        binding.ivWeat4hMainWind.layoutParams = getLayoutParam((icon_size*1.5).toInt(),(icon_size*1.5).toInt())
        binding.tvWeat4hMainHumadity.textSize = desc_size
        binding.tvWeat4hMainWind.textSize = desc_size

        binding.tvWeat4hMainTemp.textSize = temp_size
        binding.tvWeat4hMainDesc.textSize = main_desc_size
        binding.tvWeat4hMainTempminmax.textSize = main_desc_size
        binding.tvWeat4hMainCity.textSize = main_desc_size

        var child_iv_layout = getDay4ChildIvLayoutParam(item)

        binding.tvWeat4hDay1Date.textSize = desc_size
        binding.tvWeat4hDay1Minmax.textSize = desc_size
        binding.tvWeat4hDay1Desc.textSize = desc_size
        binding.ivWeat4hDay1.layoutParams = child_iv_layout

        binding.tvWeat4hDay2Date.textSize = desc_size
        binding.tvWeat4hDay2Minmax.textSize = desc_size
        binding.tvWeat4hDay2Desc.textSize = desc_size
        binding.ivWeat4hDay2.layoutParams = child_iv_layout

        binding.tvWeat4hDay3Date.textSize = desc_size
        binding.tvWeat4hDay3Minmax.textSize = desc_size
        binding.tvWeat4hDay3Desc.textSize = desc_size
        binding.ivWeat4hDay3.layoutParams = child_iv_layout

        binding.tvWeat4hDay4Date.textSize = desc_size
        binding.tvWeat4hDay4Minmax.textSize = desc_size
        binding.tvWeat4hDay4Desc.textSize = desc_size
        binding.ivWeat4hDay4.layoutParams = child_iv_layout

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

        binding.tvWeat4hMainHumadity.setPadding(desc_size.toInt(),0,0,0)
        binding.tvWeat4hMainWind.setPadding(desc_size.toInt(),0,0,0)

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

        return binding.root
    }

    // four day vertical
    fun getWidgetWeatherFourDayVerti(ctx:Context,item: Item, weather_data: WeatherFive):View{

        var binding = FragmentWeather4dayVBinding.inflate ((ctx as Activity).layoutInflater)

        var color = DataParsingSetting.getTintColor(item.settings)
        var label = DataParsingSetting.getFontLabel(item.settings)

        // main box
        var main_box_hi = getDay4VMainBoxHight(item)
        var main_box_wi = getDay4VMainBoxWidth(item)

        var main_size = LinearLayout.LayoutParams(main_box_wi,main_box_hi)
        main_size.setMargins(2,1,2,0)
        binding.llWaether4dayMainV.layoutParams = main_size

        var padding = main_box_hi/20
        binding.tvWeat4vMainTemp.setPadding(padding,padding/2,0,0)
        binding.tvWeat4vMainDesc.setPadding(padding,0,0,0)
        binding.tvWeat4vMainTempminmax.setPadding(padding,0,0,0)

        // child box
        var child_box_hi = getDay4VChildBoxHight(item)
        var child_size = LinearLayout.LayoutParams(main_box_wi,child_box_hi)
        child_size.setMargins(2,2,2,0)
        binding.llWaether4dayDay1V.layoutParams = child_size
        binding.llWaether4dayDay2V.layoutParams = child_size
        binding.llWaether4dayDay3V.layoutParams = child_size
        binding.llWaether4dayDay4V.layoutParams = child_size

        var mainiv_lp = LinearLayout.LayoutParams((main_box_hi/2.5).toInt(),(main_box_hi/2.5).toInt())
        mainiv_lp.setMargins(0,main_box_hi/15,main_box_hi/15,0)
        binding.ivWeat4vMainImage.layoutParams = mainiv_lp
        var btm_layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, main_box_hi/7)
        binding.llMainBottomviewV.layoutParams = btm_layoutParams

        var text_padding = child_box_hi/14
        binding.tvWeat4vDay1Minmax.setPadding(text_padding,text_padding,0,0)
        binding.tvWeat4vDay1Desc.setPadding(text_padding,0,0,0)

        binding.tvWeat4vDay2Minmax.setPadding(text_padding,text_padding,0,0)
        binding.tvWeat4vDay2Desc.setPadding(text_padding,0,0,0)

        binding.tvWeat4vDay3Minmax.setPadding(text_padding,text_padding,0,0)
        binding.tvWeat4vDay3Desc.setPadding(text_padding,0,0,0)

        binding.tvWeat4vDay4Minmax.setPadding(text_padding,text_padding,0,0)
        binding.tvWeat4vDay4Desc.setPadding(text_padding,0,0,0)

        // main box
        var temp_size = (main_box_hi /15).toFloat()
        var desc_size = (main_box_hi/25).toFloat()
        var icon_size = (main_box_hi/17).toFloat()

        binding.ivWeat4vMainHumadity.layoutParams = getLayoutParam((icon_size*1.5).toInt(),(icon_size*1.5).toInt())
        binding.ivWeat4vMainWind.layoutParams = getLayoutParam((icon_size*1.5).toInt(),(icon_size*1.5).toInt())
        binding.tvWeat4vMainHumadity.textSize = desc_size
        binding.tvWeat4vMainWind.textSize = desc_size

        binding.tvWeat4vMainTemp.textSize = temp_size
        binding.tvWeat4vMainDesc.textSize = desc_size
        binding.tvWeat4vMainTempminmax.textSize = desc_size
        binding.tvWeat4vMainCity.textSize = desc_size

        var child_iv_layout = LinearLayout.LayoutParams(child_box_hi/2,child_box_hi/2)
        child_iv_layout.setMargins(0,5,5,0)

        binding.tvWeat4vDay1Date.textSize = desc_size
        binding.tvWeat4vDay1Minmax.textSize = desc_size
        binding.tvWeat4vDay1Desc.textSize = desc_size
        binding.ivWeat4vDay1.layoutParams = child_iv_layout

        binding.tvWeat4vDay2Date.textSize = desc_size
        binding.tvWeat4vDay2Minmax.textSize = desc_size
        binding.tvWeat4vDay2Desc.textSize = desc_size
        binding.ivWeat4vDay2.layoutParams = child_iv_layout

        binding.tvWeat4vDay3Date.textSize = desc_size
        binding.tvWeat4vDay3Minmax.textSize = desc_size
        binding.tvWeat4vDay3Desc.textSize = desc_size
        binding.ivWeat4vDay3.layoutParams = child_iv_layout

        binding.tvWeat4vDay4Date.textSize = desc_size
        binding.tvWeat4vDay4Minmax.textSize = desc_size
        binding.tvWeat4vDay4Desc.textSize = desc_size
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

        binding.tvWeat4vMainHumadity.setPadding(desc_size.toInt(),0,0,0)
        binding.tvWeat4vMainWind.setPadding(desc_size.toInt(),0,0,0)

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

        return binding.root
    }

    // five day horizontal
    fun getWidgetWeatherFiveDayHori(ctx:Context,item: Item, weather_data: WeatherFive):View{

        var binding = TempWeather5dayHBinding.inflate ((ctx as Activity).layoutInflater)

        var text_size = if(item.frame_h<item.frame_w/3) (getBoxHight(item)/16).toFloat()
        else (getBoxHight(item)/22).toFloat()

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

        var unit = DataParsingSetting.getUnit(item.settings).toUpperCase()
        var color = DataParsingSetting.getTintColor(item.settings)

        var forcast = weather_data.forecast
        if(forcast.size==0) return binding.root
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

        var size = getDay5LayoutParam((item.frame_w/5.5).toFloat(),item)
        size.setMargins(2,2,2,2)

        binding.ll5dayHori1.layoutParams = size
        binding.ll5dayHori2.layoutParams = size
        binding.ll5dayHori3.layoutParams = size
        binding.ll5dayHori4.layoutParams = size
        binding.ll5dayHori5.layoutParams = size


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

        var text_size = (item.frame_h/55).toFloat()
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

        var size = getDay5LayoutParamVertical(item)
        size.setMargins(2,2,2,2)

        binding.ll5dayVert1.layoutParams = size
        binding.ll5dayVert2.layoutParams = size
        binding.ll5dayVert3.layoutParams = size
        binding.ll5dayVert4.layoutParams = size
        binding.ll5dayVert5.layoutParams = size

        var unit = DataParsingSetting.getUnit(item.settings).toUpperCase()
        var color = DataParsingSetting.getTintColor(item.settings)

        var forcast = weather_data.forecast
        Log.d("TAG", "getWidgetWeatherFiveDayVerti: ${forcast.size}")

        if(forcast.size==0) return binding.root
        if(forcast[0].dt!=null) {
            var names = forcast[0].dt?.split(",")
            binding.tvWeat5vDesc1.text = "Today"
            "%.2f".format(forcast[0].tempMax)
            binding.tvWeat5vTemp1.text = "${"%.0f".format(forcast[0].tempMax)}°$unit / " +
                    "${"%.0f".format(forcast[0].tempMin)}°$unit"
            UiUtils.setWeatherVecotImage(binding.ivWeat5vImage1,forcast[0].icon,color,ctx)
        }
        if(forcast[1].dt!=null) {
            var names = forcast[1].dt?.split(",")
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

        UiUtils.setDrawableView(binding.ll5dayVert1,Color.parseColor("#ffffff"),color)
        UiUtils.setDrawableView(binding.ll5dayVert2,Color.parseColor("#ffffff"),color)
        UiUtils.setDrawableView(binding.ll5dayVert3,Color.parseColor("#ffffff"),color)
        UiUtils.setDrawableView(binding.ll5dayVert4,Color.parseColor("#ffffff"),color)
        UiUtils.setDrawableView(binding.ll5dayVert5,Color.parseColor("#ffffff"),color)

        UiUtils.setDrawableViewDay(binding.tvWeat5vDesc1,Color.parseColor("#F3F3F3"))
        UiUtils.setDrawableViewDay(binding.tvWeat5vDesc2,Color.parseColor("#F3F3F3"))
        UiUtils.setDrawableViewDay(binding.tvWeat5vDesc3,Color.parseColor("#F3F3F3"))
        UiUtils.setDrawableViewDay(binding.tvWeat5vDesc4,Color.parseColor("#F3F3F3"))
        UiUtils.setDrawableViewDay(binding.tvWeat5vDesc5,Color.parseColor("#F3F3F3"))


        return binding.root
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