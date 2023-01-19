package com.app.lsquared.ui.fragment.weather

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
import com.app.lsquared.databinding.FragmentWeather4dayVBinding
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

class FragmentWeatherDay4Vertical(var item: Item) : Fragment() {

    private lateinit var binding: FragmentWeather4dayVBinding
    private lateinit var viewModel: ViewModelWeather

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeather4dayVBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelWeather::class.java)

        var setting_obj = JSONObject(item.settings)
        var template = setting_obj.getString("template")
        var lang = setting_obj.getString("lang")
        var orientation = setting_obj.getString("orientation")
        var forecast = item.forecast

        // get Weather Data From API
        viewModel.getWeather(item,forecast,lang)
        initObserver(forecast,orientation,template)

        return binding.root
    }

    private fun initObserver(forecast: Int, orientation: String, template: String) {
        viewModel.weather_api_result.observe(requireActivity(), Observer {
            response ->
            if(response.status == Status.SUCCESS){
                setData4v(response)
            }
        })
    }

    private fun setData4v(response: ApiResponse?) {



        var data_obj = Gson().fromJson(response?.data,WeatherFive::class.java)

//        main_city_tv.setText("${data_obj.current?.city} ${data_obj.current?.country}")
//        main_minmax_tv.setText("${data_obj.current?.tempMax} °C / ${data_obj.current?.tempMin} °C")
//        main_wind_tv.setText("${data_obj.current?.wind} Km/h")
//        main_temp_tv.setText("${data_obj.current?.temp}")
//        main_humidty_tv.setText("${data_obj.current?.humidity} %")
//        main_desc_tv.setText("${data_obj.current?.desc}")

//        day1_desc_tv.setText("${data_obj.forecast.get(0).desc}")
//        day2_desc_tv.setText("${data_obj.forecast.get(1).desc}")
//        day3_desc_tv.setText("${data_obj.forecast.get(2).desc}")
//        day4_desc_tv.setText("${data_obj.forecast.get(3).desc}")

//        day1_temp_tv.setText("${data_obj.forecast.get(0).tempMax} °C / ${data_obj.forecast.get(0).tempMin} °C")
//        day2_temp_tv.setText("${data_obj.forecast.get(1).tempMax} °C / ${data_obj.forecast.get(1).tempMin} °C")
//        day3_temp_tv.setText("${data_obj.forecast.get(2).tempMax} °C / ${data_obj.forecast.get(2).tempMin} °C")
//        day4_temp_tv.setText("${data_obj.forecast.get(3).tempMax} °C / ${data_obj.forecast.get(3).tempMin} °C")

//        day1_date_tv.setText("${data_obj.forecast.get(0).dt}")
//        day2_date_tv.setText("${data_obj.forecast.get(1).dt}")
//        day3_date_tv.setText("${data_obj.forecast.get(2).dt}")
//        day4_date_tv.setText("${data_obj.forecast.get(3).dt}")

    }


}