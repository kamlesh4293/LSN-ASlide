package com.app.lsquared.ui.fragment

import WeatherFive
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.lsquared.R
import com.app.lsquared.databinding.FragmentWeatherBinding
import com.app.lsquared.model.Item
import com.app.lsquared.network.ApiResponse
import com.app.lsquared.network.Status
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.ui.viewmodel.ViewModelWeather
import com.app.lsquared.utils.Constant
import com.app.lsquared.utils.DimensionUtils
import com.google.gson.Gson
import org.json.JSONObject

class FragmentWeather(var item: Item) : Fragment() {

    private lateinit var binding: FragmentWeatherBinding
    private lateinit var viewModel: ViewModelWeather
    private lateinit var myview: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelWeather::class.java)

        var setting_obj = JSONObject(item.settings)
        var template = setting_obj.getString("template")
        var lang = setting_obj.getString("lang")
        var orientation = setting_obj.getString("orientation")
        var forecast = item.forecast

        // get Weather Data From API

        if(forecast == Constant.TEMPLATE_WEATHER_CURRENT_DATE){
            if(template.equals(Constant.TEMPLATE_TIME_T1))
                binding.layoutStub.layoutResource = R.layout.temp_weather_t1
            if(template.equals(Constant.TEMPLATE_TIME_T2))
                binding.layoutStub.layoutResource = R.layout.temp_weather_t2
            if(template.equals(Constant.TEMPLATE_TIME_T3))
                binding.layoutStub.layoutResource = R.layout.temp_weather_t3
        }
        if(forecast == Constant.TEMPLATE_WEATHER_FIVE_DAY){
            if(orientation.equals(Constant.TEMPLATE_WEATHER_ORIENTATION_VERTICAL))
                binding.layoutStub.layoutResource = R.layout.temp_weather_5day_v
            else
                binding.layoutStub.layoutResource = R.layout.temp_weather_5day_h
        }
        if(forecast == Constant.TEMPLATE_WEATHER_FOUR_DAY){
            if(orientation.equals(Constant.TEMPLATE_WEATHER_ORIENTATION_VERTICAL))
                binding.layoutStub.layoutResource = R.layout.fragment_weather_4day_v
            else
                binding.layoutStub.layoutResource = R.layout.fragment_weather_4day_h
        }

        myview = binding.layoutStub.inflate()

        initObserver(forecast,orientation,template)

        return binding.root
    }

    private fun initObserver(forecast: Int, orientation: String, template: String) {
    }

    private fun setCurrentTemplate1(response: ApiResponse?) {
        var data_obj = Gson().fromJson(response?.data,WeatherFive::class.java)

        var curr_temp_tv = myview.findViewById<TextView>(R.id.tv_weather_temp1_temp)
        var curr_city_tv = myview.findViewById<TextView>(R.id.tv_weather_temp1_city)
        var curr_desc_tv = myview.findViewById<TextView>(R.id.tv_weather_temp1_desc)
        var weather_iv = myview.findViewById<ImageView>(R.id.iv_weather_temp1_image)

        var current_weather = data_obj.current
        curr_temp_tv.setText("${current_weather?.temp} ° C")
        curr_city_tv.setText("${current_weather?.city} ${current_weather?.country}" )
        curr_desc_tv.setText(current_weather?.desc)
//        UiUtils.setWeatherIcon(
//            weather_iv,
//            current_weather?.icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )
    }

    private fun setCurrentTemplate2(response: ApiResponse?) {
        var data_obj = Gson().fromJson(response?.data,WeatherFive::class.java)

        var curr_temp_tv = myview.findViewById<TextView>(R.id.tv_weather_temp2_temp)
        var curr_tempmin_tv = myview.findViewById<TextView>(R.id.tv_weather_temp2_tempmin)
        var curr_tempmax_tv = myview.findViewById<TextView>(R.id.tv_weather_temp2_tempmax)
        var weather_iv = myview.findViewById<ImageView>(R.id.iv_weather_temp2_image)

        var current_weather = data_obj.current
        curr_tempmin_tv.setText("L: ${current_weather?.tempMin} ° C")
        curr_tempmax_tv.setText("H: ${current_weather?.tempMax} ° C")
        curr_temp_tv.setText("${current_weather?.temp}")
//        UiUtils.setWeatherIcon(
//            weather_iv,
//            current_weather?.icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )

    }

    private fun setCurrentTemplate3(response: ApiResponse?) {
        var data_obj = Gson().fromJson(response?.data,WeatherFive::class.java)

        var curr_temp_tv = myview.findViewById<TextView>(R.id.tv_weather_temp3_temp)
        var curr_tempmin_tv = myview.findViewById<TextView>(R.id.tv_weather_temp3_templow)
        var curr_tempmax_tv = myview.findViewById<TextView>(R.id.tv_weather_temp3_temphigh)
        var weather_iv = myview.findViewById<ImageView>(R.id.iv_weather_temp3_image)

        var current_weather = data_obj.current
        curr_tempmin_tv.setText("L: ${current_weather?.tempMin} ° C")
        curr_tempmax_tv.setText("H: ${current_weather?.tempMax} ° C")
        curr_temp_tv.setText("${current_weather?.temp}")
//        UiUtils.setWeatherIcon(
//            weather_iv,
//            current_weather?.icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )

    }

    private fun setData4v(response: ApiResponse?) {



        var data_obj = Gson().fromJson(response?.data,WeatherFive::class.java)

//        main
        var main_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat4v_main_image)
        var main_city_tv = myview.findViewById<TextView>(R.id.tv_weat4v_main_city)
        var main_minmax_tv = myview.findViewById<TextView>(R.id.tv_weat4v_main_tempminmax)
        var main_wind_tv = myview.findViewById<TextView>(R.id.tv_weat4v_main_wind)
        var main_temp_tv = myview.findViewById<TextView>(R.id.tv_weat4v_main_temp)
        var main_humidty_tv = myview.findViewById<TextView>(R.id.tv_weat4v_main_humadity)
        var main_desc_tv = myview.findViewById<TextView>(R.id.tv_weat4v_main_desc)

        main_city_tv.setText("${data_obj.current?.city} ${data_obj.current?.country}")
        main_minmax_tv.setText("${data_obj.current?.tempMax} °C / ${data_obj.current?.tempMin} °C")
        main_wind_tv.setText("${data_obj.current?.wind} Km/h")
        main_temp_tv.setText("${data_obj.current?.temp}")
        main_humidty_tv.setText("${data_obj.current?.humidity} %")
        main_desc_tv.setText("${data_obj.current?.desc}")


//        4 days
        var day1_desc_tv = myview.findViewById<TextView>(R.id.tv_weat4v_day1_desc)
        var day2_desc_tv = myview.findViewById<TextView>(R.id.tv_weat4v_day2_desc)
        var day3_desc_tv = myview.findViewById<TextView>(R.id.tv_weat4v_day3_desc)
        var day4_desc_tv = myview.findViewById<TextView>(R.id.tv_weat4v_day4_desc)

        var day1_temp_tv = myview.findViewById<TextView>(R.id.tv_weat4v_day1_minmax)
        var day2_temp_tv = myview.findViewById<TextView>(R.id.tv_weat4v_day2_minmax)
        var day3_temp_tv = myview.findViewById<TextView>(R.id.tv_weat4v_day3_minmax)
        var day4_temp_tv = myview.findViewById<TextView>(R.id.tv_weat4v_day4_minmax)

        var day1_date_tv = myview.findViewById<TextView>(R.id.tv_weat4v_day1_date)
        var day2_date_tv = myview.findViewById<TextView>(R.id.tv_weat4v_day2_date)
        var day3_date_tv = myview.findViewById<TextView>(R.id.tv_weat4v_day3_date)
        var day4_date_tv = myview.findViewById<TextView>(R.id.tv_weat4v_day4_date)

        var day1_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat4v_day1)
        var day2_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat4v_day2)
        var day3_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat4v_day3)
        var day4_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat4v_day4)

        day1_desc_tv.setText("${data_obj.forecast.get(0).desc}")
        day2_desc_tv.setText("${data_obj.forecast.get(1).desc}")
        day3_desc_tv.setText("${data_obj.forecast.get(2).desc}")
        day4_desc_tv.setText("${data_obj.forecast.get(3).desc}")

        day1_temp_tv.setText("${data_obj.forecast.get(0).tempMax} °C / ${data_obj.forecast.get(0).tempMin} °C")
        day2_temp_tv.setText("${data_obj.forecast.get(1).tempMax} °C / ${data_obj.forecast.get(1).tempMin} °C")
        day3_temp_tv.setText("${data_obj.forecast.get(2).tempMax} °C / ${data_obj.forecast.get(2).tempMin} °C")
        day4_temp_tv.setText("${data_obj.forecast.get(3).tempMax} °C / ${data_obj.forecast.get(3).tempMin} °C")

        day1_date_tv.setText("${data_obj.forecast.get(0).dt}")
        day2_date_tv.setText("${data_obj.forecast.get(1).dt}")
        day3_date_tv.setText("${data_obj.forecast.get(2).dt}")
        day4_date_tv.setText("${data_obj.forecast.get(3).dt}")

//        UiUtils.setWeatherIcon(
//            main_temp_iv,
//            data_obj.current?.icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )
//        UiUtils.setWeatherIcon(
//            day1_temp_iv,
//            data_obj.forecast.get(0).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )
//        UiUtils.setWeatherIcon(
//            day2_temp_iv,
//            data_obj.forecast.get(1).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )
//        UiUtils.setWeatherIcon(
//            day3_temp_iv,
//            data_obj.forecast.get(2).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )
//        UiUtils.setWeatherIcon(
//            day4_temp_iv,
//            data_obj.forecast.get(3).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )

    }

    private fun setData4h(response: ApiResponse?) {
        var data_obj = Gson().fromJson(response?.data,WeatherFive::class.java)

//        main
        var ll_main = myview.findViewById<LinearLayout>(R.id.ll_main_bg)
        var main_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat4h_main_image)
        var main_humd_iv = myview.findViewById<ImageView>(R.id.iv_weat4h_main_humadity)
        var main_wind_iv = myview.findViewById<ImageView>(R.id.iv_weat4h_main_wind)
        var main_city_tv = myview.findViewById<TextView>(R.id.tv_weat4h_main_city)
        var main_minmax_tv = myview.findViewById<TextView>(R.id.tv_weat4h_main_tempminmax)
        var main_wind_tv = myview.findViewById<TextView>(R.id.tv_weat4h_main_wind)
        var main_temp_tv = myview.findViewById<TextView>(R.id.tv_weat4h_main_temp)
        var main_humidty_tv = myview.findViewById<TextView>(R.id.tv_weat4h_main_humadity)
        var main_desc_tv = myview.findViewById<TextView>(R.id.tv_weat4h_main_desc)

        ll_main.background = UiUtils.getRoundedFilled(getColor())
        main_city_tv.setBackgroundColor(Color.parseColor(getColor()))
        main_temp_tv.layoutParams = getMargin()
        UiUtils.setImageSize(main_humd_iv,DimensionUtils.getSmallSize(item.frame_h))
        UiUtils.setImageSize(main_wind_iv,DimensionUtils.getSmallSize(item.frame_h))
        main_humidty_tv.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        main_wind_tv.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        UiUtils.setCityTextView(main_city_tv,DimensionUtils.getExtraSmallSize(item.frame_h))

        main_city_tv.setText("${data_obj.current?.city} ${data_obj.current?.country}")
        main_minmax_tv.setText("${data_obj.current?.tempMax} °C / ${data_obj.current?.tempMin} °C")
        main_wind_tv.setText("${data_obj.current?.wind} Km/h")
        main_temp_tv.setText("${data_obj.current?.temp} °C")
        main_humidty_tv.setText("${data_obj.current?.humidity} %")
        main_desc_tv.setText("${data_obj.current?.desc}")

//        4 days
        var day1_desc_tv = myview.findViewById<TextView>(R.id.tv_weat4h_day1_desc)
        var day2_desc_tv = myview.findViewById<TextView>(R.id.tv_weat4h_day2_desc)
        var day3_desc_tv = myview.findViewById<TextView>(R.id.tv_weat4h_day3_desc)
        var day4_desc_tv = myview.findViewById<TextView>(R.id.tv_weat4h_day4_desc)

        var day1_temp_tv = myview.findViewById<TextView>(R.id.tv_weat4h_day1_minmax)
        var day2_temp_tv = myview.findViewById<TextView>(R.id.tv_weat4h_day2_minmax)
        var day3_temp_tv = myview.findViewById<TextView>(R.id.tv_weat4h_day3_minmax)
        var day4_temp_tv = myview.findViewById<TextView>(R.id.tv_weat4h_day4_minmax)

        var day1_date_tv = myview.findViewById<TextView>(R.id.tv_weat4h_day1_date)
        var day2_date_tv = myview.findViewById<TextView>(R.id.tv_weat4h_day2_date)
        var day3_date_tv = myview.findViewById<TextView>(R.id.tv_weat4h_day3_date)
        var day4_date_tv = myview.findViewById<TextView>(R.id.tv_weat4h_day4_date)

        var day1_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat4h_day1)
        var day2_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat4h_day2)
        var day3_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat4h_day3)
        var day4_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat4h_day4)

        // text size
        day1_desc_tv.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        day1_temp_tv.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        day1_date_tv.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()

        // text color
        day1_desc_tv.setTextColor(Color.parseColor(getColor()))
        day1_temp_tv.setTextColor(Color.parseColor(getColor()))
        day1_date_tv.setTextColor(Color.parseColor(getColor()))

        day1_desc_tv.setText("${data_obj.forecast.get(0).desc}")
        day2_desc_tv.setText("${data_obj.forecast.get(1).desc}")
        day3_desc_tv.setText("${data_obj.forecast.get(2).desc}")
        day4_desc_tv.setText("${data_obj.forecast.get(3).desc}")

        day1_temp_tv.setText("${data_obj.forecast.get(0).tempMax} °C / ${data_obj.forecast.get(0).tempMin} °C")
        day2_temp_tv.setText("${data_obj.forecast.get(1).tempMax} °C / ${data_obj.forecast.get(1).tempMin} °C")
        day3_temp_tv.setText("${data_obj.forecast.get(2).tempMax} °C / ${data_obj.forecast.get(2).tempMin} °C")
        day4_temp_tv.setText("${data_obj.forecast.get(3).tempMax} °C / ${data_obj.forecast.get(3).tempMin} °C")

        day1_date_tv.setText("${data_obj.forecast.get(0).dt}")
        day2_date_tv.setText("${data_obj.forecast.get(1).dt}")
        day3_date_tv.setText("${data_obj.forecast.get(2).dt}")
        day4_date_tv.setText("${data_obj.forecast.get(3).dt}")

//        UiUtils.setWeatherIcon(
//            main_temp_iv,
//            data_obj.current?.icon,
//            DimensionUtils.getMainImageSize(item.frame_h)
//        )
//        UiUtils.setWeatherIcon(
//            day1_temp_iv,
//            data_obj.forecast.get(0).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )
//        UiUtils.setWeatherIcon(
//            day2_temp_iv,
//            data_obj.forecast.get(1).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )
//        UiUtils.setWeatherIcon(
//            day3_temp_iv,
//            data_obj.forecast.get(2).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )
//        UiUtils.setWeatherIcon(
//            day4_temp_iv,
//            data_obj.forecast.get(3).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )

    }

    private fun setData5v(response: ApiResponse?) {
        var data_obj = Gson().fromJson(response?.data,WeatherFive::class.java)

        var day1_desc_tv = myview.findViewById<TextView>(R.id.tv_weat5v_desc1)
        var day2_desc_tv = myview.findViewById<TextView>(R.id.tv_weat5v_desc2)
        var day3_desc_tv = myview.findViewById<TextView>(R.id.tv_weat5v_desc3)
        var day4_desc_tv = myview.findViewById<TextView>(R.id.tv_weat5v_desc4)
        var day5_desc_tv = myview.findViewById<TextView>(R.id.tv_weat5v_desc5)

        var day1_temp_tv = myview.findViewById<TextView>(R.id.tv_weat5v_temp1)
        var day2_temp_tv = myview.findViewById<TextView>(R.id.tv_weat5v_temp2)
        var day3_temp_tv = myview.findViewById<TextView>(R.id.tv_weat5v_temp3)
        var day4_temp_tv = myview.findViewById<TextView>(R.id.tv_weat5v_temp4)
        var day5_temp_tv = myview.findViewById<TextView>(R.id.tv_weat5v_temp5)

        var day1_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat5v_image1)
        var day2_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat5v_image2)
        var day3_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat5v_image1)
        var day4_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat5v_image4)
        var day5_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat5v_image5)


        day1_temp_tv.setText("${data_obj.forecast.get(0).tempMin} °C / ${data_obj.forecast.get(0).tempMax} °C")
        day2_temp_tv.setText("${data_obj.forecast.get(1).tempMin} °C / ${data_obj.forecast.get(1).tempMax} °C")
        day3_temp_tv.setText("${data_obj.forecast.get(2).tempMin} °C / ${data_obj.forecast.get(2).tempMax} °C")
        day4_temp_tv.setText("${data_obj.forecast.get(3).tempMin} °C / ${data_obj.forecast.get(3).tempMax} °C")
        day5_temp_tv.setText("${data_obj.forecast.get(4).tempMin} °C / ${data_obj.forecast.get(4).tempMax} °C")

        day1_desc_tv.setText("${data_obj.forecast.get(0).desc}")
        day2_desc_tv.setText("${data_obj.forecast.get(1).desc}")
        day3_desc_tv.setText("${data_obj.forecast.get(2).desc}")
        day4_desc_tv.setText("${data_obj.forecast.get(3).desc}")
        day5_desc_tv.setText("${data_obj.forecast.get(4).desc}")

//        UiUtils.setWeatherIcon(
//            day1_temp_iv,
//            data_obj.forecast.get(0).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )
//        UiUtils.setWeatherIcon(
//            day2_temp_iv,
//            data_obj.forecast.get(1).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )
//        UiUtils.setWeatherIcon(
//            day3_temp_iv,
//            data_obj.forecast.get(2).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )
//        UiUtils.setWeatherIcon(
//            day4_temp_iv,
//            data_obj.forecast.get(3).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )
//        UiUtils.setWeatherIcon(
//            day5_temp_iv,
//            data_obj.forecast.get(4).icon,
//            DimensionUtils.getMediumSize(item.frame_h)
//        )

    }

    private fun setData5h(response: ApiResponse?) {
        var data_obj = Gson().fromJson(response?.data,WeatherFive::class.java)

        var day1_desc_tv = myview.findViewById<TextView>(R.id.tv_weat5h_desc1)
        var day2_desc_tv = myview.findViewById<TextView>(R.id.tv_weat5h_desc2)
        var day3_desc_tv = myview.findViewById<TextView>(R.id.tv_weat5h_desc3)
        var day4_desc_tv = myview.findViewById<TextView>(R.id.tv_weat5h_desc4)
        var day5_desc_tv = myview.findViewById<TextView>(R.id.tv_weat5h_desc5)

        var day1_temp_tv = myview.findViewById<TextView>(R.id.tv_weat5h_day1)
        var day2_temp_tv = myview.findViewById<TextView>(R.id.tv_weat5h_day2)
        var day3_temp_tv = myview.findViewById<TextView>(R.id.tv_weat5h_day3)
        var day4_temp_tv = myview.findViewById<TextView>(R.id.tv_weat5h_day4)
        var day5_temp_tv = myview.findViewById<TextView>(R.id.tv_weat5h_day5)

        var day1_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat5h_day1)
        var day2_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat5h_day2)
        var day3_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat5h_day3)
        var day4_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat5h_day4)
        var day5_temp_iv = myview.findViewById<ImageView>(R.id.iv_weat5h_day5)


        day1_temp_tv.setText("${data_obj.forecast.get(0).tempMin} °C / ${data_obj.forecast.get(0).tempMax} °C")
        day2_temp_tv.setText("${data_obj.forecast.get(1).tempMin} °C / ${data_obj.forecast.get(1).tempMax} °C")
        day3_temp_tv.setText("${data_obj.forecast.get(2).tempMin} °C / ${data_obj.forecast.get(2).tempMax} °C")
        day4_temp_tv.setText("${data_obj.forecast.get(3).tempMin} °C / ${data_obj.forecast.get(3).tempMax} °C")
        day5_temp_tv.setText("${data_obj.forecast.get(4).tempMin} °C / ${data_obj.forecast.get(4).tempMax} °C")

        day1_desc_tv.setText("${data_obj.forecast.get(0).desc}")
        day2_desc_tv.setText("${data_obj.forecast.get(1).desc}")
        day3_desc_tv.setText("${data_obj.forecast.get(2).desc}")
        day4_desc_tv.setText("${data_obj.forecast.get(3).desc}")
        day5_desc_tv.setText("${data_obj.forecast.get(4).desc}")

//        UiUtils.setWeatherIcon(day1_temp_iv,data_obj.forecast.get(0).icon,DimensionUtils.getMediumSize(item.frame_h))
//        UiUtils.setWeatherIcon(day2_temp_iv,data_obj.forecast.get(1).icon,DimensionUtils.getMediumSize(item.frame_h))
//        UiUtils.setWeatherIcon(day3_temp_iv,data_obj.forecast.get(2).icon,DimensionUtils.getMediumSize(item.frame_h))
//        UiUtils.setWeatherIcon(day4_temp_iv,data_obj.forecast.get(3).icon,DimensionUtils.getMediumSize(item.frame_h))
//        UiUtils.setWeatherIcon(day5_temp_iv,data_obj.forecast.get(4).icon,DimensionUtils.getMediumSize(item.frame_h))

    }

    fun getColor(): String {
        var setting_obj = JSONObject(item.settings)
        return setting_obj.getString("tint")
    }

    fun getMargin(): LinearLayout.LayoutParams {
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0,item.frame_h/10,0,0)
        return layoutParams
    }

}