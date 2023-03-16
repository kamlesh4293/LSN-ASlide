package com.app.lsquared.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.app.lsquared.ApiInterface
import com.app.lsquared.network.ApiResponse
import com.app.lsquared.network.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.*
import com.androidnetworking.error.ANError
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.interfaces.DownloadListener
import com.androidnetworking.interfaces.DownloadProgressListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.app.lsquared.model.Downloadable
import com.app.lsquared.utils.*
import java.io.File
import com.test.RetrofitClient


class MainViewModel : ViewModel() {

    var internet = false
    var device_id = ""
    var is_device_registered = false
    var is_deviceinfo_submitted = false
    var is_devicereport_submitted = false

    var delay = 30000
    var customtimedelay = 60000
    var temp_delay = 60000
    var screen_delay = 300
    var report_delay = 120000


    // 1. device register
    private val _device_register_data = MutableLiveData<ApiResponse>()
    val device_register_api_result : LiveData<ApiResponse> get() = _device_register_data

    // 2. content data
    private val _content_data = MutableLiveData<ApiResponse>()
    val content_api_result : LiveData<ApiResponse> get() = _content_data

    // 3. device info
    private val _deviceinfo_data = MutableLiveData<ApiResponse>()
    val devcieinfo_api_result : LiveData<ApiResponse> get() = _deviceinfo_data

    // 4. temp data submit
    private val _temprature_data = MutableLiveData<ApiResponse>()
    val temprature_api_result : LiveData<ApiResponse> get() = _temprature_data

    // 5. post screen shot
    private val _screenshot_data = MutableLiveData<ApiResponse>()
    val screenshot_api_result : LiveData<ApiResponse> get() = _screenshot_data

    // 7. post screen shot
    private val reister_new_device_data = MutableLiveData<ApiResponse>()
    val reister_new_device_data_result : LiveData<ApiResponse> get() = reister_new_device_data

    // 8. emergency request
    private val emergenncy_req_data = MutableLiveData<ApiResponse>()
    val emergency_req_result : LiveData<ApiResponse> get() = emergenncy_req_data


    // 1 check device is registered
    fun isDeviceRegistered(ctx:Context,device_id:String) {
//        var device_id = DeviceInfo.getDeviceId(ctx,)
        var url = Constant.BASE_URL+"api/v1/feed/deviceversion/$device_id"
        Log.d("TAG", "isDeviceRegistered: $url")
        if(internet){
            Log.d("TAG", "isDeviceRegistered: calling")
            viewModelScope.launch(Dispatchers.IO) {
                ApiInterface.create().checkIsDeviceRegister(url)
                    .enqueue( object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>,response: retrofit2.Response<ResponseBody>) {
                            Log.d("TAG", "isDeviceRegistered: onResponse")
                            if(response?.body() != null){
                                var res = response?.body()!!.string()
                                _device_register_data.postValue(ApiResponse(Status.SUCCESS,res,"success"))
                            }else{
                                var error = response?.errorBody()!!.toString()
                                _device_register_data.postValue(ApiResponse(Status.ERROR,null,"error"))
                            }
                        }
                        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                            Log.d("TAG", "isDeviceRegistered: onFailure")
                            _device_register_data.postValue(ApiResponse(Status.ERROR,null,"error"))
                        }
                    })
            }
        }else{
            _device_register_data.postValue(ApiResponse(Status.NO_INTERNET,null,""))
        }
    }


    // 2 fetch content data
    fun fetchContentData(){
        if(internet){
            var url =  if(Constant.BASE_FILE_URL.equals("https://hub.lsquared.com/"))
                Constant.BASE_URL+"feed/json/${device_id}.json"
            else Constant.BASE_FILE_URL+"feed/json/${device_id}.json"

            Log.d("TAG", "fetchContentData: $url")
            viewModelScope.launch(Dispatchers.IO) {
                ApiInterface.create().fetchPlayingContent(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>,response: retrofit2.Response<ResponseBody>) {
                        if(response?.body() != null){
                            var res = response?.body()!!.string()
                            Log.d("TAG", "fetchContentData: respone $res")
                            _content_data.postValue(ApiResponse(Status.SUCCESS,res,"success"))
                        }else{
                            _content_data.postValue(ApiResponse(Status.ERROR,null,"error"))
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Log.d("res_error-",t.toString())
                        _content_data.postValue(ApiResponse(Status.FAILURE,null,t.toString()))
                    }
                })
            }
        }
    }

    // 3 submit device info
    fun submitDeviceInfo(ctx:Activity,pref: MySharePrefernce){
        // get device info
        var info = Utility.deviceInfoToJson(
                DeviceInfo.getDeviceId(ctx, pref),
                DeviceInfo.getDeviceResolution(ctx),
                DeviceInfo.getDeviceName(),
                DeviceInfo.getLocalIpAddress(),
                DeviceInfo.getTotalDiscSize(),
                DeviceInfo.getUsedDiscSize(),
                DeviceInfo.getTotalRAMSize(ctx),
                DeviceInfo.getUsedRAMSize(ctx),
                DeviceInfo.getSerial(),
                DeviceInfo.getModelName(),
                DeviceInfo.getConnectedNetworkType(ctx),
                DeviceInfo.getDeviceVersion(),
                DeviceInfo.getWifiMacAddress(ctx)
            )

        if(internet && !is_deviceinfo_submitted && is_device_registered){
            Log.d("device_info_body-",info.toString())
            viewModelScope.launch(Dispatchers.IO) {
                AndroidNetworking.post(Constant.BASE_URL+"api/v1/feed/setDeviceInfo")
                    .addJSONObjectBody(info) // posting json
                    .setOkHttpClient(RetrofitClient.getOkhttpClient())
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(object : StringRequestListener{
                        override fun onResponse(response: String?) {
                            Log.d("device_info_success-",response.toString())
                            _deviceinfo_data.postValue(ApiResponse(Status.SUCCESS,response,response!!))
                        }
                        override fun onError(anError: ANError?) {
                            Log.d("device_info_failed-",anError.toString())
                            _deviceinfo_data.postValue(ApiResponse(Status.FAILURE,anError.toString(),"failed"))
                        }
                    })
            }
        }
    }

    // 4 submit temprature data
    fun updateTempratureData(temp:String){
        if(internet && is_device_registered){
            Log.d("TAG", "updateTempratureData: start")
            viewModelScope.launch(Dispatchers.IO) {
                val retroInstance = ApiInterface.create()
                Log.d("Temp_api", "api/v1/feed/dt/$device_id/$temp/${Utility.getCurrentdate()}")
                var call  = retroInstance.postTemperature("api/v1/feed/dt/$device_id/$temp/${Utility.getCurrentdate()}")
                call.enqueue( object : Callback<ResponseBody> {

                    override fun onResponse(call: Call<ResponseBody>,response: retrofit2.Response<ResponseBody>) {
                        if(response?.body() != null){
                            Log.d("TAG", "updateTempratureData: success")
                            var res = response?.body()!!.string()
                            _temprature_data.postValue(ApiResponse(Status.SUCCESS,res,"success"))
                        }else{
                            Log.d("TAG", "updateTempratureData: error")
                            _temprature_data.postValue(ApiResponse(Status.ERROR,null,"error"))
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Log.d("TAG", "updateTempratureData: failed")
                        _temprature_data.postValue(ApiResponse(Status.FAILURE,null,t.toString()))
                    }
                })
            }
        }else{
            _temprature_data.postValue(ApiResponse(Status.NO_INTERNET,null,""))
        }
    }

    // 5 submit screen shot
    fun submitScreenShot(body: JSONObject){
        if(internet && is_device_registered){
            Log.d("TAG","submitScreenShot - ${body.toString()}")
            viewModelScope.launch(Dispatchers.IO) {
                    AndroidNetworking.post(Constant.BASE_URL+"api/v1/feed/deviceSaveScreenshotBase64")
                        .addJSONObjectBody(body) // posting json
                        .setOkHttpClient(RetrofitClient.getOkhttpClient())
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString(object : StringRequestListener{
                            override fun onResponse(response: String?) {
                                Log.d("device_screen_success-",response.toString())
                                _screenshot_data.postValue(ApiResponse(Status.SUCCESS,response.toString(),"success"))
                            }
                            override fun onError(anError: ANError?) {
                                Log.d("device_screen_failed-",anError.toString())
                                _screenshot_data.postValue(ApiResponse(Status.ERROR,anError.toString(),"error"))
                            }
                        })
            }
        }
    }

    // 6 submit report
    fun submitRecords(device_id: String){

        var files  = DataManager.getReportFileList()
        Log.d("TAG", "submitRecords: ")
        if(internet && is_device_registered && files!=null && files.size>0){
            Log.d("TAG", "submitRecords: uploading")
            viewModelScope.launch(Dispatchers.IO) {
                AndroidNetworking.upload(Constant.BASE_URL+"api/v1/feed/writePoPReport_HDSteth")
//                    AndroidNetworking.upload(Constant.BASE_URL+"api/v1/feed/writePoPReport")
                        .addMultipartFile("file", files[0])
                        .addMultipartParameter("mac", device_id)
                        .setOkHttpClient(RetrofitClient.getOkhttpClient())
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .setUploadProgressListener { bytesUploaded, totalBytes ->}
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject?) {
                                files[0].delete()
                                Log.d("TAG", "submitRecords: onResponse")
                            }

                            override fun onError(anError: ANError?) {
                                Log.d("TAG", "submitRecords: onError - ${anError.toString()}")
                            }
                        })
            }
        }
    }

    fun submitRecordsold(device_id: String, data: String, pref: MySharePrefernce?){
        if(internet && is_device_registered && !data.equals("") && !is_devicereport_submitted){
            var body = Utility.getRecords(device_id,data)
            Log.d("record_body-",body.toString())
            viewModelScope.launch(Dispatchers.IO) {
                    AndroidNetworking.post(Constant.BASE_URL+"api/v1/feed/writePoPReport")
                        .addJSONObjectBody(body) // posting json
                        .setOkHttpClient(RetrofitClient.getOkhttpClient())
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString(object : StringRequestListener{
                            override fun onResponse(response: String?) {
                                Log.d("device_report_success-",response.toString())
                                is_devicereport_submitted = true
                                pref?.clearReportdata()
                            }
                            override fun onError(anError: ANError?) {
                                Log.d("device_report_failed-",anError.toString())
                            }
                        })
            }
        }
    }



    fun registerNewDevce(ctx:Activity,pref: MySharePrefernce,device_id :String){
        if(internet){
            var data = JSONObject()
//            data.put("id",DeviceInfo.getDeviceId(ctx,pref))
            data.put("id",device_id)
            data.put("hw",DeviceInfo.getDeviceIdFromDevice(ctx))
            viewModelScope.launch(Dispatchers.IO) {
                AndroidNetworking.post(Constant.API_NEW_DEVICE_REGISTER)
                    .addJSONObjectBody(data) // posting json
                    .setOkHttpClient(RetrofitClient.getOkhttpClient())
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(object : StringRequestListener{
                        override fun onResponse(response: String?) {
                            Log.d("device_info_success-",response.toString())
                            var code = JSONObject(response!!).getInt("code")
                            var msg = JSONObject(response!!).getString("desc")
                            reister_new_device_data.postValue(ApiResponse(Status.SUCCESS,response,msg,code))
                        }
                        override fun onError(anError: ANError?) {
                            Log.d("device_info_failed-",anError.toString())
                            reister_new_device_data.postValue(ApiResponse(Status.FAILURE,anError.toString(),"failed",0))
                        }
                    })
            }
        }
    }




    // delete file
    fun deleteFiles(downloable_file: List<String>?) {
        Log.d("TAG", "deleteFiles: downloadable size - ${downloable_file?.size}")
        viewModelScope.launch(Dispatchers.IO) {

            if(downloable_file !=null && downloable_file.size>0){

                // delete only that file not there in downloadable
                var dir_files = DataManager.getAllDirectoryFiles()
                if(dir_files!=null &&dir_files.size>0){
                    for (i in 0..dir_files.size-1){
                        if(!downloable_file.contains(dir_files[i].name)){
                            val file = File(dir_files[i].path)
                            if(file.exists()){
                                Log.d("TAG", "DeleteFileName - ex: ${file.name}")
                                file.delete()
                            }
                        }
                    }
                }
            }else{
                // delete all the files
                var dir_files = DataManager.getAllDirectoryFiles()
                if(dir_files!=null &&dir_files.size>0){
                    for(file in dir_files){
                        val file = File(file.path)
                        if(file.exists()){
                            Log.d("TAG", "DeleteFileName - all: ${file.name}")
                            file.delete()
                        }
                    }
                }
            }
        }
    }

    // widget api calling
    private val quote_data = MutableLiveData<ApiResponse>()
    val quote_api_result : LiveData<ApiResponse> get() = quote_data

    fun getQuoteText(id :String,frame_pos :Int) {
        var url = Constant.API_WIDGET_QUOTE+"$id?format=json"
        Log.d("TAG", "getQuoteText: url - $url")
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().checkIsDeviceRegister(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        var res = response?.body()!!.string()
                        Log.d("TAG", "getQuoteText: onResponse - $res")
                        if(response?.body() != null){
                            quote_data.postValue(ApiResponse(Status.SUCCESS,res,"success",frame_pos))
                        }else{
                            var error = response?.errorBody()!!.toString()
                            quote_data.postValue(ApiResponse(Status.ERROR,null,"error",frame_pos))
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Log.d("TAG", "onFailure: - $t")
                        quote_data.postValue(ApiResponse(Status.ERROR,null,"error",frame_pos))
                    }
                })
        }
    }


    private val _rss_data = MutableLiveData<ApiResponse>()
    val rss_api_result : LiveData<ApiResponse> get() = _rss_data

    fun getNews(url: String, pos: Int){
        Log.d("TAG", "fetchxmlData: $url")
        viewModelScope.launch(Dispatchers.IO) {

            AndroidNetworking.get(url)
                .setOkHttpClient(RetrofitClient.getOkhttpClient())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String?) {
                        Log.d("device_info_success-",response.toString())
                        _rss_data.postValue(ApiResponse(Status.SUCCESS,response,"success",pos))
                    }
                    override fun onError(anError: ANError?) {
                        Log.d("device_info_failed-",anError.toString())
                        _rss_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                })
        }
    }

    private val text_data = MutableLiveData<ApiResponse>()
    val text_api_result : LiveData<ApiResponse> get() = text_data

    fun getText(id: String, pos: Int) {
        var url = Constant.API_WIDGET_TEXT+"$id?format=json"
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().checkIsDeviceRegister(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        if(response?.body() != null){
                            var res = response?.body()!!.string()
                            Log.d("TAG", "text api onResponse: $res")
                            text_data.postValue(ApiResponse(Status.SUCCESS,res,"success",pos))
                        }else{
                            var error = response?.errorBody()!!.toString()
                            text_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        text_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                })
        }
    }

    fun getEmergencyMessagedata(device_id: String) {
        var url = Constant.getApiEmergencyMessage(device_id)
        Log.d("TAG", "getEmergencyMessagedata: $url")
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().getEmergencyMessage(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        if(response?.body() != null){
                            var res = response?.body()!!.string()
                            Log.d("TAG", "getEmergencyMessagedata onResponse: $res")
                            getEmergencyAcknowldge(device_id)
                            emergenncy_req_data.postValue(ApiResponse(Status.SUCCESS,res,"success"))
                        }else{
                            var error = response?.errorBody()!!.toString()
                            emergenncy_req_data.postValue(ApiResponse(Status.ERROR,null,"error"))
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        emergenncy_req_data.postValue(ApiResponse(Status.ERROR,null,"error"))
                    }
                })
        }
    }

    fun getEmergencyAcknowldge(device_id: String) {
        var url = Constant.getApiEmergencyAcknowledge(device_id)
        Log.d("TAG", "getEmergencyMessagedata: $url")
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().getEmergencyMessage(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        Log.d("TAG", "getEmergencyAcknowldge onResponse: ${response.body().toString()}")
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Log.d("TAG", "getEmergencyAcknowldge onFailure: $t")
                    }
                })
        }
    }

    fun getIdentifyAcknowledge(pref: MySharePrefernce) {
        var device = DataParsing.getDevice(pref)
        if(device == null) return
        var url = Constant.getApiIdentifyAcknowledge(device!!.mac,device!!.id.toString())
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().getIdentifyAcknowledge(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        if(response?.body() != null){
                            var res = response?.body()!!.string()
                            Log.d("TAG", "getIdentifyAcknowledge onResponse: $res")
                        }else{
                            var error = response?.errorBody()!!.toString()
                            Log.d("TAG", "getIdentifyAcknowledge onResponse error: $error")
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Log.d("TAG", "getIdentifyAcknowledge onFailure: $t")
                    }
                })
        }
    }


    // file downloading
    private val _downloadfile_data = MutableLiveData<ApiResponse>()
    val download_file_result : LiveData<ApiResponse> get() = _downloadfile_data

    fun downloadFile(downloadable: Downloadable) {

        var url = if(downloadable.type.equals(Constant.CONTENT_THUMB)) downloadable.src else Constant.BASE_FILE_URL + downloadable.src
        var fileName = downloadable.name
        Log.d("TAG", " : $fileName")
//        AndroidNetworking.download("https://us.lsquared.com/lsquared-hub/cl/videos/processed/1602585267.1767-mailbox-and-other-services.mp4", DataManager.getDirectory(), fileName)
        AndroidNetworking.download(url, DataManager.getDirectory(), fileName)
            .setTag("downloadTest")
            .setOkHttpClient(RetrofitClient.getOkhttpClient())
            .setPriority(Priority.MEDIUM)
            .build()
            .setDownloadProgressListener(object : DownloadProgressListener {
                override fun onProgress(bytesDownloaded: Long, totalBytes: Long) {
                    // do anything with progress
                    Log.d("TAG", "onProgress: $totalBytes/ $bytesDownloaded")
                }
            })
            .startDownload(object : DownloadListener {
                override fun onDownloadComplete() {
                    // do anything after completion
                    Log.d("TAG", "onSuccess: $fileName")
                    _downloadfile_data.postValue(ApiResponse(Status.SUCCESS,"","success"))
                }
                override fun onError(error: ANError?) {
                    // handle error
                    Log.d("TAG", "onError: ${error.toString()}")
                    _downloadfile_data.postValue(ApiResponse(Status.ERROR,"",error.toString()))
                }
            })
    }

}