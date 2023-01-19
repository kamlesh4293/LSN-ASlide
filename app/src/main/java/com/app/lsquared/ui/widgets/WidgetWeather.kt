package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.view.View
import com.app.lsquared.R
import com.app.lsquared.model.Item

class WidgetWeather {

    companion object{

        // current date template 1
        fun getWidgetWeatherCurremtTemp1(ctx:Context,item: Item):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_weather_t1,null)
            return view
        }

        // current date template 2
        fun getWidgetWeatherCurremtTemp2(ctx:Context,item: Item):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_weather_t2,null)
            return view
        }

        // current date template 3
        fun getWidgetWeatherCurremtTemp3(ctx:Context,item: Item):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_weather_t3,null)
            return view
        }

        // four day horizontal
        fun getWidgetWeatherFourDayHori(ctx:Context,item: Item):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_weather_4day_h,null)
            return view
        }

        // four day vertical
        fun getWidgetWeatherFourDayVerti(ctx:Context,item: Item):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_weather_4day_v,null)
            return view
        }

        // five day horizontal
        fun getWidgetWeatherFiveDayHori(ctx:Context,item: Item):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_weather_5day_h,null)
            return view
        }

        // five day vertical
        fun getWidgetWeatherFiveDayVerti(ctx:Context,item: Item):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_weather_5day_v,null)
            return view
        }
    }
}