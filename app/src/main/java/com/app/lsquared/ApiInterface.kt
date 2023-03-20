package com.app.lsquared

import com.app.lsquared.utils.Constant
import com.test.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiInterface {

    // 1. check device is register or not
    @GET
    fun checkIsDeviceRegister(@Url url:String) : Call<ResponseBody>

    // 2. fetch playing content if device register
    @GET
    fun fetchPlayingContent(@Url url:String) : Call<ResponseBody>

    // 3. submit temprature
    @GET
    fun postTemperature(@Url url:String) : Call<ResponseBody>

    // 4. emergency message
    @GET
    fun getEmergencyMessage(@Url url:String) : Call<ResponseBody>

    // 5. Identify acknowlegde
    @GET
    fun getIdentifyAcknowledge(@Url url:String) : Call<ResponseBody>


    companion object {

        fun create() : ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .client(RetrofitClient.getOkhttpClient())
                .build()
            return retrofit.create(ApiInterface::class.java)
        }

        fun createRss() : ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("")
                .client(RetrofitClient.getOkhttpClient())
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }

}