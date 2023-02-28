package com.app.lsquared.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.lsquared.model.Item
import com.app.lsquared.model.vimeo.VimeoResponse
import com.app.lsquared.network.ApiResponse
import com.app.lsquared.network.ApiResponseVimeo
import com.app.lsquared.network.Status
import com.app.lsquared.network.VimeoInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject

class CodViewModel : ViewModel(){

    private val vimeo_data = MutableLiveData<ApiResponseVimeo>()
    val vimeo_api_result : LiveData<ApiResponseVimeo> get() = vimeo_data

    fun getVimeoUrl(src:String,item: Item?,pos:Int,vimeo_client:Retrofit){
        val vimeoInterface: VimeoInterface = vimeo_client.create(VimeoInterface::class.java)
        vimeoInterface.getVimeoUrlResponse(src)
            .enqueue(object : Callback<VimeoResponse?> {
                override fun onResponse(
                    call: Call<VimeoResponse?>,
                    response: Response<VimeoResponse?>
                ) {
                    if (response.isSuccessful() && response.body() != null) {
                        //Create media item
                        if (response.body()!!.getRequest().getFiles().getProgressive().size > 0)
                            vimeo_data.postValue(ApiResponseVimeo(Status.SUCCESS,response.body()!!.getRequest().getFiles().getProgressive().get(0).getUrl(),item,pos))
                    }
                }

                override fun onFailure(call: Call<VimeoResponse?>, t: Throwable) {
                    Log.e("TAG", Objects.requireNonNull(t.message)!!)
                }
            })
    }

}