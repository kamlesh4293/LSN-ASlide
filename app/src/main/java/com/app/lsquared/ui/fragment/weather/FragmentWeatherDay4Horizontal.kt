package com.app.lsquared.ui.fragment.weather

import WeatherFive
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.lsquared.databinding.FragmentWeather4dayHBinding
import com.app.lsquared.model.Item
import com.app.lsquared.network.ApiResponse
import com.app.lsquared.network.Status
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.ui.viewmodel.ViewModelWeather
import com.app.lsquared.utils.DataParsing
import com.app.lsquared.utils.DimensionUtils
import com.google.gson.Gson
import org.json.JSONObject

class FragmentWeatherDay4Horizontal(var item: Item) : Fragment() {

    private lateinit var binding: FragmentWeather4dayHBinding
    private lateinit var viewModel: ViewModelWeather

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeather4dayHBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelWeather::class.java)

        var setting_obj = JSONObject(item.settings)
        var lang = setting_obj.getString("lang")
        var forecast = item.forecast

        // get Weather Data From API
        viewModel.getWeather(item,forecast,lang)
        initObserver()

        return binding.root
    }

    private fun initObserver() {
        viewModel.weather_api_result.observe(requireActivity(), Observer {
            response ->
            if(response.status == Status.SUCCESS){
                setData4h(response)
            }
        })
    }

    private fun setData4h(response: ApiResponse?) {
        var data_obj = Gson().fromJson(response?.data,WeatherFive::class.java)


        // main padding
        binding.llMainWeat4H.setPadding(5,DimensionUtils.getSmallSize(item.frame_h),5,DimensionUtils.getSmallSize(item.frame_h))
        binding.llMainBg.background = UiUtils.getRoundedFilled(DataParsing.getColor(item))


        // main image
        UiUtils.setWeatherIcon(binding.ivWeat4hMainImage,data_obj.current?.icon,DimensionUtils.getMainImageSize(item.frame_h),4,8,10,6)
        // main temp text
        binding.tvWeat4hMainTemp.setText("${data_obj.current?.temp} °C")
        binding.tvWeat4hMainTemp.layoutParams = getMargin()
        binding.tvWeat4hMainTemp.textSize = DimensionUtils.getExtraSmallSize(item.frame_h).toFloat()
        // main desc
        binding.tvWeat4hMainDesc.textSize = DimensionUtils.getCustomSize(item.frame_h,28).toFloat()
        binding.tvWeat4hMainDesc.setText("${data_obj.current?.desc}")
        // main min max temp
        binding.tvWeat4hMainTempminmax.textSize = DimensionUtils.getCustomSize(item.frame_h,35).toFloat()
        binding.tvWeat4hMainTempminmax.setText("${data_obj.current?.tempMax}°C / ${data_obj.current?.tempMin}°C")
        // main city
        binding.tvWeat4hMainCity.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        binding.tvWeat4hMainCity.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        binding.tvWeat4hMainCity.setBackgroundColor(Color.parseColor(DataParsing.getColor(item)))
        binding.tvWeat4hMainCity.setText("${data_obj.current?.city} ${data_obj.current?.country}")
        // main city
        UiUtils.setCityTextView(binding.tvWeat4hMainCity,DimensionUtils.getSmallSize(item.frame_h))
        // humidity text
        binding.tvWeat4hMainHumadity.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        binding.tvWeat4hMainHumadity.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        // wind text
        binding.tvWeat4hMainWind.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        binding.tvWeat4hMainWind.setTextColor(Color.parseColor(DataParsing.getColor(item)))


//      Day 1 layout

        // layout
        binding.llWaether4dayDay1H.background = UiUtils.getRoundedBorder(DataParsing.getColor(item))
        // image view
        UiUtils.setWeatherIcon(binding.ivWeat4hDay1,data_obj.forecast.get(0).icon,DimensionUtils.getMediumSize(item.frame_h),10,8,10,10)
        // text min max
        binding.tvWeat4hDay1Minmax.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        binding.tvWeat4hDay1Minmax.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        // text desc
        binding.tvWeat4hDay1Desc.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        binding.tvWeat4hDay1Desc.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        // text date
        binding.tvWeat4hDay1Date.textSize = DimensionUtils.getCustomSize(item.frame_h,40).toFloat()
        binding.tvWeat4hDay1Date.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        binding.tvWeat4hDay1Date.background = UiUtils.getRoundedBorderwithFilled(DataParsing.getColor(item))


//      Day 2 layout

        // layout
        binding.llWaether4dayDay2H.background = UiUtils.getRoundedBorder(DataParsing.getColor(item))
        // image view
        UiUtils.setWeatherIcon(binding.ivWeat4hDay2,data_obj.forecast.get(1).icon,DimensionUtils.getMediumSize(item.frame_h),10,8,10,10)
        // text min max
        binding.tvWeat4hDay2Minmax.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        binding.tvWeat4hDay2Minmax.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        // text desc
        binding.tvWeat4hDay2Desc.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        binding.tvWeat4hDay2Desc.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        // text date
        binding.tvWeat4hDay2Date.textSize = DimensionUtils.getCustomSize(item.frame_h,40).toFloat()
        binding.tvWeat4hDay2Date.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        binding.tvWeat4hDay2Date.background = UiUtils.getRoundedBorderwithFilled(DataParsing.getColor(item))


//      Day 3 layout

        // layout
        binding.llWaether4dayDay3H.background = UiUtils.getRoundedBorder(DataParsing.getColor(item))
        // image view
        UiUtils.setWeatherIcon(binding.ivWeat4hDay3,data_obj.forecast.get(2).icon,DimensionUtils.getMediumSize(item.frame_h),10,8,10,10)
        // text min max
        binding.tvWeat4hDay3Minmax.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        binding.tvWeat4hDay3Minmax.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        // text desc
        binding.tvWeat4hDay3Desc.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        binding.tvWeat4hDay3Desc.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        // text date
        binding.tvWeat4hDay3Date.textSize = DimensionUtils.getCustomSize(item.frame_h,40).toFloat()
        binding.tvWeat4hDay3Date.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        binding.tvWeat4hDay3Date.background = UiUtils.getRoundedBorderwithFilled(DataParsing.getColor(item))


//      Day 4 layout

        // layout
        binding.llWaether4dayDay4H.background = UiUtils.getRoundedBorder(DataParsing.getColor(item))
        // image view
        UiUtils.setWeatherIcon(binding.ivWeat4hDay4,data_obj.forecast.get(1).icon,DimensionUtils.getMediumSize(item.frame_h),10,8,10,10)
        // text min max
        binding.tvWeat4hDay4Minmax.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        binding.tvWeat4hDay4Minmax.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        // text desc
        binding.tvWeat4hDay4Desc.textSize = DimensionUtils.getDoubleExtraSmallSize(item.frame_h).toFloat()
        binding.tvWeat4hDay4Desc.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        // text date
        binding.tvWeat4hDay4Date.textSize = DimensionUtils.getCustomSize(item.frame_h,40).toFloat()
        binding.tvWeat4hDay4Date.setTextColor(Color.parseColor(DataParsing.getColor(item)))
        binding.tvWeat4hDay4Date.background = UiUtils.getRoundedBorderwithFilled(DataParsing.getColor(item))

    }

    fun getMargin(): LinearLayout.LayoutParams {
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0,item.frame_h/10,0,0)
        return layoutParams
    }

}