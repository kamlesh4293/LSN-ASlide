package com.app.lsquared.ui

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
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
import com.app.lsquared.model.Device
import com.app.lsquared.model.Downloadable
import com.app.lsquared.model.Item
import com.app.lsquared.pasring.DataParsingSetting
import com.app.lsquared.utils.*
import java.io.File
import com.test.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pref: MySharePrefernce,
    private val dataParsing: DataParsing
): ViewModel() {

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

    // 6. register new device
    private val reister_new_device_data = MutableLiveData<ApiResponse>()
    val reister_new_device_data_result : LiveData<ApiResponse> get() = reister_new_device_data

    // external widget apis

    // 1. emergency request
    private val emergenncy_req_data = MutableLiveData<ApiResponse>()
    val emergency_req_result : LiveData<ApiResponse> get() = emergenncy_req_data

    // 2. quote data request
    private val quote_data = MutableLiveData<ApiResponse>()
    val quote_api_result : LiveData<ApiResponse> get() = quote_data

    // 3. Rss - feed data
    private val _rss_data = MutableLiveData<ApiResponse>()
    val rss_api_result : LiveData<ApiResponse> get() = _rss_data

    // 3. news - feed data
    private val _news_data = MutableLiveData<ApiResponse>()
    val news_api_result : LiveData<ApiResponse> get() = _news_data

    // 4. text data
    private val text_data = MutableLiveData<ApiResponse>()
    val text_api_result : LiveData<ApiResponse> get() = text_data

    // 5. stock -  data
    private val _stock_data = MutableLiveData<ApiResponse>()
    val stock_api_result : LiveData<ApiResponse> get() = _stock_data

    // 6. Wather -  data
    private val weather_data = MutableLiveData<ApiResponse>()
    val weather_api_result : LiveData<ApiResponse> get() = weather_data

    // 7. meeting -  data
    private val meeting_data = MutableLiveData<ApiResponse>()
    val meeting_api_result : LiveData<ApiResponse> get() = meeting_data

    // 8. calendar -  data
    private val Google_cal_data = MutableLiveData<ApiResponse>()
    val google_cal_api_result : LiveData<ApiResponse> get() = Google_cal_data

    // 9 . Outlook -  data
    private val Outlook_data = MutableLiveData<ApiResponse>()
    val outlook_api_result : LiveData<ApiResponse> get() = Outlook_data


    // 5. file downloading
    private val _downloadfile_data = MutableLiveData<ApiResponse>()
    val download_file_result : LiveData<ApiResponse> get() = _downloadfile_data

//    other feature
    // 1. Relaunch on demand
    private val _relaunch_data = MutableLiveData<ApiResponse>()
    val relaunch_result : LiveData<ApiResponse> get() = _relaunch_data



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
    fun submitScreenShot(body: JSONObject,type:String){
        if(internet && is_device_registered){
            var url = Constant.BASE_URL+"api/v1/feed/deviceSaveScreenshotBase64"
            if(type.equals(Constant.SS_TYPE_OD)) url = url+"?did="+dataParsing.getDevice()?.id
            else url = url+".php?did="+dataParsing.getDevice()?.id

            Log.d("submitScreenShot url - ",url)
            Log.d("TAG","body "+body.toString())
            viewModelScope.launch(Dispatchers.IO) {
                    AndroidNetworking.post(url)
                        .addJSONObjectBody(body) // posting json
                        .setOkHttpClient(RetrofitClient.getOkhttpClient())
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString(object : StringRequestListener{
                            override fun onResponse(response: String?) {
                                if(type.equals(Constant.SS_TYPE_OD)){
                                    Log.d("TAG","device_screen_success- type OD ${response.toString()}")
                                    pref.putBooleanData(MySharePrefernce.KEY_ODSS_ACTIVE,false)
                                }
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

    // 6. register new device
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

    // external widget api

    // 1. emergency request
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
                            setDataWithVersion(MySharePrefernce.KEY_DATA_EMERGENCY,res,MySharePrefernce.KEY_DATA_EMERGENCY_VERSION)
                            getEmergencyAcknowldge(device_id)
                            emergenncy_req_data.postValue(ApiResponse(Status.SUCCESS,res,"success"))
                        }else{
                            emergenncy_req_data.postValue(ApiResponse(Status.ERROR,null,"error"))
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        emergenncy_req_data.postValue(ApiResponse(Status.ERROR,null,"error"))
                    }
                })
        }
    }

    // 2. quote data request
    fun getQuoteText(id :String,frame_pos :Int,item_id: String) {
        var url = Constant.API_WIDGET_QUOTE+"$id?format=json"
        Log.d("TAG", "getQuoteText: url - $url")
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().checkIsDeviceRegister(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        var res = response?.body()!!.string()
                        Log.d("TAG", "getQuoteText: onResponse - $res")
                        if(response?.body() != null){
                            setDataWithVersion(MySharePrefernce.KEY_DATA_QUOTE+item_id,res,MySharePrefernce.KEY_DATA_QUOTE_VERSION)
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

    // 3. Rss - feed data
    fun getRss(url: String, pos: Int,item_id: String){
        Log.d("TAG", "fetchxmlData: $url")
        viewModelScope.launch(Dispatchers.IO) {

            AndroidNetworking.get(url)
                .setOkHttpClient(RetrofitClient.getOkhttpClient())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String?) {
                        Log.d("getRss_success-",response.toString())
                        setDataWithVersion(MySharePrefernce.KEY_DATA_RSS+item_id,response?:"",MySharePrefernce.KEY_DATA_RSS_VERSION)
                        _rss_data.postValue(ApiResponse(Status.SUCCESS,response,"success",pos))
                    }
                    override fun onError(anError: ANError?) {
                        Log.d("getRss_failed-",anError.toString())
                        _rss_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                })
        }
    }

    // 3. News - feed data
    fun getNews(url: String, pos: Int,item_id: String){
        Log.d("TAG", "fetchxmlData: $url")
        viewModelScope.launch(Dispatchers.IO) {

            AndroidNetworking.get(url)
                .setOkHttpClient(RetrofitClient.getOkhttpClient())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String?) {
                        Log.d("getNews_success-",response.toString())
                        setDataWithVersion(MySharePrefernce.KEY_DATA_NEWS+item_id,response?:"",MySharePrefernce.KEY_DATA_NEWS_VERSION)
                        _news_data.postValue(ApiResponse(Status.SUCCESS,response,"success",pos))
                    }
                    override fun onError(anError: ANError?) {
                        Log.d("getNews_failed-",anError.toString())
                        _news_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                })
        }
    }

    // 4. text data
    fun getText(id: String, pos: Int,item_id: String) {
        var url = Constant.API_WIDGET_TEXT+"$id?format=json"
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().checkIsDeviceRegister(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        if(response?.body() != null){
                            var res = response?.body()!!.string()
                            Log.d("TAG", "text api onResponse: $res")
                            setDataWithVersion(MySharePrefernce.KEY_DATA_TEXT+item_id,res,MySharePrefernce.KEY_DATA_TEXT_VERSION)
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

    // 5. STOCK WIDGET - data
    fun getStockData(widget_id :String, pos: Int,item_id: String){
        var url = Constant.API_WIDGET_STOCKS+widget_id
        Log.d("TAG", "fetchxmlData: $url")
        viewModelScope.launch(Dispatchers.IO) {
            AndroidNetworking.get(url)
                .setOkHttpClient(RetrofitClient.getOkhttpClient())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String?) {
                        Log.d("getNews_success-",response.toString())
                        setDataWithVersion(MySharePrefernce.KEY_DATA_STOCK+item_id,response?:"",MySharePrefernce.KEY_DATA_STOCK_VERSION)
                        _stock_data.postValue(ApiResponse(Status.SUCCESS,response,"success",pos))
                    }
                    override fun onError(anError: ANError?) {
                        Log.d("getNews_failed-",anError.toString())
                        _stock_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                })
        }
    }

    // 6. Weather widget
    fun getWeather(item: Item,pos: Int) {
        Log.d("TAG", "getWeather: pos - $pos")
        var lang = DataParsingSetting.getLang(item.settings)
        var unit = DataParsingSetting.getUnit(item.settings)
        var wsu = DataParsingSetting.getWsu(item.settings)
        var url = Constant.API_WIDGET_WEATHER+"${item.src}?forecast=${item.forecast}&lang=$lang&unit=$unit&wsu=$wsu"
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().checkIsDeviceRegister(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        Log.d("TAG", "onResponse: obser weather data")
                        if(response?.body() != null){
                            var res = response?.body()!!.string()
                            weather_data.value = ApiResponse(Status.SUCCESS,res,"success",pos,item)
                        }else weather_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        weather_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                })
        }
    }

    // 7. Meeting widget
    fun getMeetingData(device:Device?, widget_id: String,pos: Int,item: Item) {
        var url = Constant.getMeetingEventApi(device?.mac!!,device?.id.toString(),widget_id)
        Log.d("TAG", "getMeetingData: $url")
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().checkIsDeviceRegister(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        Log.d("TAG", "onResponse: obser weather data")
                        if(response?.body() != null){
                            var res = response?.body()!!.string()
                            meeting_data.value = ApiResponse(Status.SUCCESS,res,"success",pos,item)
                        }else meeting_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        meeting_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                })
        }
    }

    // 8. Google Calendar widget
    fun getGoogleCalData(device:Device?, widget_id: String,pos: Int,item: Item) {
        var url = if(item.dType.equals(Constant.CALENDAR_BOARD_ALL))
            Constant.getGoogleCalAllEventApi(device?.mac!!,device?.id.toString(),widget_id)
        else Constant.getGoogleCalEventApi(device?.mac!!,device?.id.toString(),widget_id)

        Log.d("TAG", "getGoogleCalData: $url")
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().checkIsDeviceRegister(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        if(response?.body() != null){
                            var res = response?.body()!!.string()
                            Google_cal_data.value = ApiResponse(Status.SUCCESS,res,"success",pos,item)
                        }else Google_cal_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Google_cal_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                })
        }
    }

    // 9. outlook widget
    fun getOutlookData(device:Device?, widget_id: String,pos: Int,item: Item) {
        var url = Constant.getOutlookEventApi(device?.mac!!,device?.id.toString(),widget_id)

        Log.d("TAG", "getOutlookData: $url")
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().checkIsDeviceRegister(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        if(response?.body() != null){
                            var res = response?.body()!!.string()
                            Outlook_data.value = ApiResponse(Status.SUCCESS,res,"success",pos,item)
                        }else Outlook_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Outlook_data.postValue(ApiResponse(Status.ERROR,null,"error",pos))
                    }
                })
        }
    }

    // 10. file downloading
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
                    Log.d("TAG", "onProgress: $totalBytes -  $bytesDownloaded")
                    // do anything with progress
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

    // not notifying apis

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

    // emergency ackowledge
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

    // identify request acknowledge
    fun getIdentifyAcknowledge(pref: MySharePrefernce) {
        var device = dataParsing.getDevice()
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

    // relaunch request acknowledge
    fun getRelaunchAcknowledge() {
        var url = Constant.getApiRelaunchAcknowledge(device_id)
        viewModelScope.launch(Dispatchers.IO) {
            ApiInterface.create().getRelaunchAcknowledge(url)
                .enqueue( object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        if(response?.body() != null){
                            var res = response?.body()!!.string()
                            pref.putStringData(MySharePrefernce.KEY_RELAUNCH_ONDEMAND,"")
                            _relaunch_data.postValue(ApiResponse(Status.SUCCESS,"","success"))
                            Log.d("TAG", "getRelaunchAcknowledge onResponse: $res")
                        }else{
                            var error = response?.errorBody()!!.toString()
                            Log.d("TAG", "getRelaunchAcknowledge onResponse error: $error")
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Log.d("TAG", "getRelaunchAcknowledge onFailure: $t")
                    }
                })
        }
    }

    // Submit download Confirmation
    fun submitContentConfirmation(device_id: String, downloads_list: List<Downloadable>){
        if(downloads_list==null || downloads_list.size==0) {
            Log.d("TAG", "submitContentConfirmation: no data available")
            return
        }
        var data  = DataManager.getContentConfirmationData(downloads_list)
        if(internet && is_device_registered ){
            Log.d("TAG", "submitContentConfirmation: api - ${Constant.getApiDownloadConfirmation(device_id)}")
            Log.d("TAG", "submitContentConfirmation: body - ${data.toString()}")
            viewModelScope.launch(Dispatchers.IO) {
                AndroidNetworking.post(Constant.getApiDownloadConfirmation(device_id))
                    .addJSONObjectBody(data)
                    .setOkHttpClient(RetrofitClient.getOkhttpClient())
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setUploadProgressListener { bytesUploaded, totalBytes ->}
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.d("TAG", "submitContentConfirmation: onResponse - $response")
                        }
                        override fun onError(anError: ANError?) {
                            Log.d("TAG", "submitContentConfirmation: onError - ${anError.toString()}")
                        }
                    })
            }
        }
    }

    // Submit records
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

    // set widget data with version
    fun setDataWithVersion(key:String,data: String,version_key:String){
        var version = dataParsing.getDevice()?.version?:0
        pref.putStringData(key,data)
        pref.putIntData(version_key,version)
    }

    // check external api calling
    fun isDataStoredForCurrentVersion(type:String,frame_pos: Int,item_id: String):Boolean{

        var content_version = dataParsing.getDevice()?.version?:0
        if(type.equals(Constant.CONTENT_WIDGET_QUOTES)){
            var data_version = pref.getIntData(MySharePrefernce.KEY_DATA_QUOTE_VERSION)
            if(data_version==content_version){
                var data = pref.getStringData(MySharePrefernce.KEY_DATA_QUOTE+"$item_id")
                if(data.equals("")) return false
                else{
                    quote_data.postValue(ApiResponse(Status.SUCCESS,data,"success",frame_pos))
                    return true
                }
            }
        }else if(type.equals(Constant.CONTENT_WIDGET_TEXT)){
            var data_version = pref.getIntData(MySharePrefernce.KEY_DATA_TEXT_VERSION)
            if(data_version==content_version){
                var data = pref.getStringData(MySharePrefernce.KEY_DATA_TEXT+"$item_id")
                if(data.equals("")) return false
                else{
                    text_data.postValue(ApiResponse(Status.SUCCESS,data,"success",frame_pos))
                    return true
                }
            }
        }else if(type.equals(Constant.CONTENT_WIDGET_NEWS)){
            var data_version = pref.getIntData(MySharePrefernce.KEY_DATA_NEWS_VERSION)
            if(data_version==content_version){
                var data = pref.getStringData(MySharePrefernce.KEY_DATA_NEWS+"$item_id")
                if(data.equals("")) return false
                else{
                    _news_data.postValue(ApiResponse(Status.SUCCESS,data,"success",frame_pos))
                    return true
                }
            }
        }else if(type.equals(Constant.CONTENT_WIDGET_RSS)){
            var data_version = pref.getIntData(MySharePrefernce.KEY_DATA_RSS_VERSION)
            if(data_version==content_version){
                var data = pref.getStringData(MySharePrefernce.KEY_DATA_RSS+"$item_id")
                if(data.equals("")) return false
                else{
                    _rss_data.postValue(ApiResponse(Status.SUCCESS,data,"success",frame_pos))
                    return true
                }
            }
        }else if(type.equals(Constant.CONTENT_WIDGET_STOCK)){
            var data_version = pref.getIntData(MySharePrefernce.KEY_DATA_STOCK_VERSION)
            if(data_version==content_version){
                var data = pref.getStringData(MySharePrefernce.KEY_DATA_STOCK+"$item_id")
                if(data.equals("")) return false
                else{
                    _stock_data.postValue(ApiResponse(Status.SUCCESS,data,"success",frame_pos))
                    return true
                }
            }
        }else if(type.equals(Constant.WIDGET_EMERGENCY_MESSAGE)){
            var data_version = pref.getIntData(MySharePrefernce.KEY_DATA_EMERGENCY_VERSION)
            if(data_version==content_version){
                var data = pref.getStringData(MySharePrefernce.KEY_DATA_EMERGENCY+"$item_id")
                emergenncy_req_data.postValue(ApiResponse(Status.SUCCESS,data,"success"))
                return true
            }
        }
        return false
    }

}