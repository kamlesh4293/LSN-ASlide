package com.app.lsquared.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lsquared.ApiInterface
import com.app.lsquared.model.Item
import com.app.lsquared.network.ApiResponse
import com.app.lsquared.network.Status
import com.app.lsquared.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class ViewModelWeather : ViewModel() {


    private val weather_data = MutableLiveData<ApiResponse>()
    val weather_api_result : LiveData<ApiResponse> get() = weather_data

    fun getWeather(item: Item, forecast: Int, lang: String) {

        var url = Constant.API_WIDGET_WEATHER+"${item.src}?forecast=$forecast&lang=$lang"
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().checkIsDeviceRegister(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        if(response?.body() != null){
                            var res = response?.body()!!.string()
                            weather_data.postValue(ApiResponse(Status.SUCCESS,res,"success"))
                        }else{
                            var error = response?.errorBody()!!.toString()
                            weather_data.postValue(ApiResponse(Status.ERROR,null,"error"))
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        weather_data.postValue(ApiResponse(Status.ERROR,null,"error"))
                    }
                })
        }
    }


}