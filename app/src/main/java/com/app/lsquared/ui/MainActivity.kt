package com.app.lsquared.ui

import WeatherFive
import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.Toolbar.LayoutParams
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androidnetworking.AndroidNetworking
import com.app.lsquared.R
import com.app.lsquared.databinding.ActivityMainMultifameBinding
import com.app.lsquared.model.*
import com.app.lsquared.model.news_setting.BeingNewsData
import com.app.lsquared.model.news_setting.News
import com.app.lsquared.model.widget.CalendarResponseData
import com.app.lsquared.model.widget.MeetingCalendarResponseData
import com.app.lsquared.model.widget.RssItem
import com.app.lsquared.network.NetworkConnectivity
import com.app.lsquared.network.Status
import com.app.lsquared.network.isConnected
import com.app.lsquared.ui.device.WaterMarkWidget
import com.app.lsquared.ui.viewmodel.CodViewModel
import com.app.lsquared.ui.widgets.*
import com.app.lsquared.ui.widgets.WidgetNewsList.Companion.getWidgetNewsListAllFixContent
import com.app.lsquared.usbserialconnection.ResponseStatus
import com.app.lsquared.usbserialconnection.USBSerialConnector
import com.app.lsquared.usbserialconnection.USBSerialListener
import com.app.lsquared.utils.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NotRegisterDalogListener , USBSerialListener{

    var TAG = "MainActivity"

    // view list
    var layout_list : MutableList<LayoutFrames> = mutableListOf()
    var screen_layout_list : MutableList<LinearLayout> = mutableListOf()

    var multiframe_items: MutableList<MutableList<Item>> = mutableListOf()
    var items: MutableList<Item> = mutableListOf()

    var current_size_list : MutableList<Int> = mutableListOf()
    var content_confirmation_list : MutableList<Downloadable> = mutableListOf()

    // downloading count
    var downloading = 0

    var play_activate = false

    // share prefernce
    @Inject
    lateinit var pref: MySharePrefernce
    @Inject
    lateinit var dataParsing: DataParsing

    // view  binding
    private lateinit var binding: ActivityMainMultifameBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var vimeoViewModel: CodViewModel


    @Inject
    lateinit var vimeo_retro: Retrofit

    // live data
    lateinit var connectionLiveData: NetworkConnectivity

    var temp = 0

    lateinit var ctx : Context

    lateinit var checkVersionHandler: Handler
    lateinit var timeScehdulerHandler: Handler
    lateinit var screenshot_handler: Handler
    lateinit var od_screenshot_handler: Handler
    lateinit var temp_handler: Handler
    lateinit var report_handler: Handler

    var time = 0
    var fullScreen = false
    var aManager : AudioManager? = null
    var volume = 0

    // fpg method
    lateinit var mConnector: USBSerialConnector

    private val timeSchedularTask = object : Runnable {
        override fun run() {
            var running_signature_lay = pref?.getStringData(MySharePrefernce.KEY_TIME_SIGNATURE)
            var running_signature_over = pref?.getStringData(MySharePrefernce.KEY_TIME_SIGNATURE_OVERRIDE)
            var is_override = dataParsing.isOverrideAvailable(pref)

            var ovr_current_signature = DataParsing.getTimeSignatureOverride(pref)
            var lay_current_signature = DataParsing.getTimeSignature(pref)

            Log.d(TAG, "run check: override - $is_override , curr - $ovr_current_signature , lay - $lay_current_signature")
            var current_signature = if(is_override) ovr_current_signature else lay_current_signature
            var running_signature = if(is_override) running_signature_over else running_signature_lay

            Log.d(TAG, "run: current sig - $current_signature , running sig - $running_signature")
            if(!running_signature.equals("") && !current_signature.equals("") && !running_signature.equals(current_signature)){
                changeContent()
            }else{
                // check content restriction
                checkValidDownloadable()
            }

            timeScehdulerHandler.postDelayed(this, viewModel.customtimedelay.toLong())
        }
    }

    private fun checkValidDownloadable() {
        var list = dataParsing.getDownloableList(pref)
        if(list.size>0 && downloading==0) changeContent()
    }

    private val versionTask = object : Runnable {
        override fun run() {
            // device on-off
            if(viewModel.is_device_registered){
                var days = dataParsing.getOnOffDays()
                if(days!=null && days.label!=null && days.st!!.contains(":") && days.et!!.contains(":") && days!!.st!!.length==8 && days.et!!.length==8){
                    var current_time = DateTimeUtil.createDateForCustomTimeForDeviceOnOffString()
                    var st = days!!.st!!.substring(0,5)
                    var et = days!!.et!!.substring(0,5)
//                    if(current_time.equals(st)) mConnector.writeAsync(UsbUtilities.getDeviceONByteArray())
//                    if(current_time.equals(et)) mConnector.writeAsync(UsbUtilities.getDeviceOffByteArray())
                    if(current_time.equals(st)) mConnector.writeAsync(RSUtility.hexDecimalToByteArray(dataParsing.getDeviceONCode()))
                    if(current_time.equals(et)) mConnector.writeAsync(RSUtility.hexDecimalToByteArray(dataParsing.getDeviceOffCode()))
                }
            }
            checkDeviceVersion()
            checkVersionHandler.postDelayed(this, viewModel.delay.toLong())
        }
    }

    private val ssTask = object : Runnable {
        override fun run() {
            captureScreen(Constant.SS_TYPE_SS)
            screenshot_handler.postDelayed(this, viewModel.screen_delay* 1000.toLong())
        }
    }

    private val odssTask = object : Runnable {
        override fun run() {
            checkDemandScreenShot()
            od_screenshot_handler.postDelayed(this, 10* 1000.toLong())
        }
    }

    fun captureScreen(type:String){
        var is_frames = dataParsing.isFrameAvailable()
        var is_override = dataParsing.isOverrideAvailable(pref)
        var isVideo = DataParsing.isVideoPlaying(layout_list)
        if(!is_frames && !is_override){
            val file = ImageUtil.screenshot(binding.mainLayout,"Screen_final_"+Utility.getCurrentdate())
            if(file!=null)
                viewModel.submitScreenShot(
                    Utility.getScreenshotJson(DeviceInfo.getDeviceId(ctx,pref),Utility.getFileToByte(file?.absolutePath),type),
                    type
                )
        }else if(isVideo){
            for(i in 0..layout_list.size-1){
                if(layout_list[i].active_widget.equals(Constant.CONTENT_VIDEO)){
                    val currentPosition: Int? = layout_list[i].videoView?.getCurrentPosition() //in millisecond
                    val pos = currentPosition?.times(1000) //unit in microsecond
                    val bmFrame = layout_list[i].myMediaMetadataRetriever?.getFrameAtTime(pos!!.toLong())
//                    screen_layout_list[i].addView(ImageWidget.getSSImageWidget(this@MainActivity,bmFrame!!))
                }else{
                    val file = ImageUtil.screenshot(layout_list[i].frame_view_ll!!,"Screen_final_"+Utility.getCurrentdate())
                    if(file!=null)
                        screen_layout_list[i].addView(ImageWidget.getSSImageWidget(this@MainActivity,BitmapFactory.decodeFile(file?.absolutePath)))
                }
            }
            val file = ImageUtil.screenshot(binding.screenRootLayout,"Screen_final_"+Utility.getCurrentdate())
            if(file!=null)
                viewModel.submitScreenShot(
                    Utility.getScreenshotJson(DeviceInfo.getDeviceId(ctx,pref),Utility.getFileToByte(file?.absolutePath),type),
                    type
                )
        }else{
            val file = ImageUtil.screenshot(binding.rootLayout,"Screen_final_"+Utility.getCurrentdate())
            if(file!=null)
                viewModel.submitScreenShot(
                    Utility.getScreenshotJson(DeviceInfo.getDeviceId(ctx,pref),Utility.getFileToByte(file?.absolutePath),type),
                    type
                )
        }

    }

    private val tempTask = object : Runnable {
        override fun run() {
            viewModel.updateTempratureData(temp.toString())
            temp_handler.postDelayed(this, viewModel.temp_delay.toLong())
        }
    }

    private val reportTask = object : Runnable {
        override fun run() {
            viewModel.submitRecords(DeviceInfo.getDeviceId(ctx,pref))
            report_handler.postDelayed(this, viewModel.report_delay.toLong())
        }
    }

    private val broadcastreceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10
            setIdentifyRequest()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ctx = this
        initXml()
        Handler(Looper.getMainLooper()).postDelayed(Runnable { initObserver() },2000)
        // broadcast reciver for temp
        val intentfilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(broadcastreceiver,intentfilter)

        checkVersionHandler = Handler(Looper.getMainLooper())
        timeScehdulerHandler = Handler(Looper.getMainLooper())
        screenshot_handler = Handler(Looper.getMainLooper())
        od_screenshot_handler = Handler(Looper.getMainLooper())
        temp_handler = Handler(Looper.getMainLooper())
        report_handler = Handler(Looper.getMainLooper())
    }


    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        timeScehdulerHandler.post(timeSchedularTask)
        checkVersionHandler.post(versionTask)
        screenshot_handler.post(ssTask)
        od_screenshot_handler.post(odssTask)
        temp_handler.post(tempTask)
        report_handler.post(reportTask)

        // dvice on -off
        mConnector.setUsbSerialListener(this)
        mConnector.init(this,9600)

        changeContent()
    }

    private fun initXml() {
        // remove status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        window.decorView.systemUiVisibility = flags
        // screen always on mode
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = ActivityMainMultifameBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        vimeoViewModel = ViewModelProvider(this).get(CodViewModel::class.java)

        setContentView(binding.root)

        mConnector = USBSerialConnector.getInstance()

        Utility.checkAllPermissionGranted(this)
        @RequiresApi(Build.VERSION_CODES.R)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            var d=hasAllFilesPermission()
            if (!d){
                takePermissions()
            }
        }

        // initialize netwrok lib
        AndroidNetworking.initialize(getApplicationContext())
        // live data initiate
        connectionLiveData = NetworkConnectivity(application)

        viewModel.internet = isConnected
        viewModel.device_id = DeviceInfo.getDeviceId(ctx,pref)

        if (pref?.getIntData(MySharePrefernce.KEY_SCREENSHOT_INTERVAL)!! != 0)
            viewModel.screen_delay = pref?.getIntData(MySharePrefernce.KEY_SCREENSHOT_INTERVAL)!!

        aManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
        volume = aManager?.getStreamVolume(AudioManager.STREAM_MUSIC)!!
    }

    private fun initObserver() {

        // internet observer
        binding.ivNoInternet.visibility = if(!isConnected) View.VISIBLE else View.GONE
        connectionLiveData.observe(this, Observer { isInternet ->
            viewModel.internet = isInternet
            binding.ivNoInternet.visibility = if(!isInternet) View.VISIBLE else View.GONE
        })

        // new device register observer
        viewModel.reister_new_device_data_result.observe(this, Observer {
            response ->
            if (response.status == Status.SUCCESS) {
                if(response.pos == Constant.DEVICE_REGISTERED){
                    DialogView.showSuccess()
                }else{
                    pref.putStringData(MySharePrefernce.KEY_DEVICE_REGISTERED_ID,"")
                    DialogView.showError(response.message)
                }
            }
        })

        // device registred observer
        viewModel.device_register_api_result.observe(this, Observer { response ->
            if (response.status == Status.SUCCESS) {
                var device_obj = Gson().fromJson(response.data, ResponseCheckDeviceData::class.java)
                if (device_obj.desc.equals(Constant.DEVICE_NOT_FOUND)) {
                    viewModel.is_device_registered = false
                    viewModel.is_deviceinfo_submitted = false
                    deviceNotRegistered()
                    setIdentifyRequest()
                } else {
                    DialogView.hideKeyBoardShowing()
                    pref?.putBooleanData(MySharePrefernce.KEY_DEVICE_REGISTERED,true)
                    viewModel.is_device_registered = true
                    pref?.putVersionFromDeviceAPI(device_obj.desc.toInt())
                    hideDialog()
                    var device_version = pref?.getVersionOfDeviceAPI()
                    var content_version = pref?.getVersionOfConytentAPI()
                    if (device_version != content_version) {
                        viewModel.fetchContentData()
                    }else{
                        if(downloading==0) viewModel.deleteFiles(DataParsing.getDownloableFileNameList(pref!!))
                    }
                }
            } else viewModel.is_device_registered = false

            if (response.status == Status.ERROR) {
                showSnackBar(getString(R.string.error_msg))
            }
            viewModel.submitDeviceInfo(this,pref)
        })

        // content result observer
        viewModel.content_api_result.observe(this, Observer { response ->
            if (response.status == Status.SUCCESS) {
                try {
                    pref?.setLocalStorage(response.data!!)
                    changeContent()
                } catch (ex: Exception) { }
            }
        })


        // submit device info
        viewModel.devcieinfo_api_result.observe(this, Observer { response ->
            viewModel.is_deviceinfo_submitted = true
//                showSnackBar("Device info Result - ${response.status}")
//                Log.d("device_info_result", response.message)
        })

        // Widget API Observer

        // Quotes API
        viewModel.quote_api_result.observe(this, Observer {
                response ->
            if(response.status == Status.SUCCESS){
                if(multiframe_items.size==0) return@Observer
                var pos = response.pos
                var item = multiframe_items[pos].get(current_size_list[pos])
                layout_list[pos].frame_view_ll?.removeAllViews()
                layout_list[pos].frame_view_ll?.
                    addView(WidgetQuotes.getWidgetQuotes(this,item,response.data),item.frame_w,item.frame_h)
                setQuoteRotation(pos,item)
            }
        })

        // news api
        viewModel.rss_api_result.observe(this, Observer { response ->
            if (response.status == Status.SUCCESS) {
                if(multiframe_items.size==0) return@Observer
                var pos = response.pos
                var item = multiframe_items[pos].get(current_size_list[pos])
                if(item.dType.equals(Constant.CONTENT_WIDGET_NEWS_CRAWL)){
                    // crowling
                    var builder  = StringBuilder()
                    var list_title = mutableListOf<String>()
                    if(!item.src.equals("")){
                        val inputStream: InputStream = response.data!!.byteInputStream()
                        var list = DataParsing.parse(inputStream)
                        if(list!=null && list.size>0){
                            for (i in list){
                                builder.append(i.title)
                                builder.append("        ")
                                list_title.add(i.title!!)
                            }
                        }
                    }else{
                        var being_news = Gson().fromJson(response.data, BeingNewsData::class.java)
                        if(being_news!=null && being_news.news!=null && being_news.news.size>0){
                            for (i in being_news.news){
                                builder.append(i.title)
                                builder.append("        ")
                                list_title.add(i.title!!)
                            }
                        }
                    }
                    layout_list[pos].frame_view_ll?.removeAllViews()
                    layout_list[pos].frame_view_ll?.
                    addView(WidgetNewsCrowling.getWidgetNewsCrowling(this,item,builder.toString(), list_title),item.frame_w,item.frame_h)
                }else{
                    // listview
                    if(!item.src.equals("")){
                        val inputStream: InputStream = response.data!!.byteInputStream()
                        val inputStream2: InputStream = response.data!!.byteInputStream()
                        var list = DataParsing.parse(inputStream)
                        var title = DataParsing.parse2(inputStream2)
                        setAllNewsListRotationRSS(pos,list,item,title,0)
                    }else{
                        // being news
                        var being_news = Gson().fromJson(response.data,BeingNewsData::class.java)
                        setBeingNewsListRotationRSS(pos,being_news.news,item,0)
                    }
                }
            }
        })

        // Rss api
        viewModel.news_api_result.observe(this, Observer { response ->
            if (response.status == Status.SUCCESS) {
                if(multiframe_items.size==0) return@Observer
                var pos = response.pos
                var item = multiframe_items[pos].get(current_size_list[pos])
                if(item.dType.equals(Constant.CONTENT_WIDGET_NEWS_CRAWL)){
                    // crowling
                    var builder  = StringBuilder()
                    var list_title = mutableListOf<String>()
                    if(!item.src.equals("")){
                        val inputStream: InputStream = response.data!!.byteInputStream()
                        var list = DataParsing.parse(inputStream)
                        if(list!=null && list.size>0){
                            for (i in list){
                                builder.append(i.title)
                                builder.append("        ")
                                list_title.add(i.title!!)
                            }
                        }
                    }else{
                        var being_news = Gson().fromJson(response.data, BeingNewsData::class.java)
                        if(being_news!=null && being_news.news!=null && being_news.news.size>0){
                            for (i in being_news.news){
                                builder.append(i.title)
                                builder.append("        ")
                                list_title.add(i.title!!)
                            }
                        }
                    }
                    layout_list[pos].frame_view_ll?.removeAllViews()
                    layout_list[pos].frame_view_ll?.
                    addView(WidgetNewsCrowling.getWidgetNewsCrowling(this,item,builder.toString(), list_title),item.frame_w,item.frame_h)
                }else{
                    // listview
                    if(!item.src.equals("")){
                        val inputStream: InputStream = response.data!!.byteInputStream()
                        val inputStream2: InputStream = response.data!!.byteInputStream()
                        var list = DataParsing.parse(inputStream)
                        var title = DataParsing.parse2(inputStream2)
                        setAllNewsListRotation(pos,list,list,item,title,0)
                    }else{
                        // being news
                        var being_news = Gson().fromJson(response.data,BeingNewsData::class.java)
                        setBeingNewsListRotation(pos,being_news.news,being_news.news,item,0)
                    }
                }
            }
        })

        // Meeting
        viewModel.meeting_api_result.observe(this){ response->
            if(response.status==Status.SUCCESS){
                if(layout_list!=null && layout_list.size>0){
                    var meeting_obj = Gson().fromJson(response.data,MeetingCalendarResponseData::class.java)
                    setMeetingView(response.pos,response.item!!,meeting_obj)
                }
            }
        }

        // google calendar
        viewModel.google_cal_api_result.observe(this){ response->
            if(response.status==Status.SUCCESS){
                if(layout_list!=null && layout_list.size>0){
                    Log.d(TAG, "initObserver google_cal_api_result : ${response.data}")
                    var cal_obj = Gson().fromJson(response.data, CalendarResponseData::class.java)
                    setCalendarView(response.pos,response.item!!,cal_obj)
                }
            }
        }

        // Outlook
        viewModel.outlook_api_result.observe(this){ response->
            if(response.status==Status.SUCCESS){
                if(layout_list!=null && layout_list.size>0){
                    Log.d(TAG, "initObserver google_cal_api_result : ${response.data}")
                    var cal_obj = Gson().fromJson(response.data, CalendarResponseData::class.java)
                    setCalendarView(response.pos,response.item!!,cal_obj)
                }
            }
        }


        // text api
        viewModel.text_api_result.observe(this, Observer {
                response ->
            if(response.status == Status.SUCCESS){
                if(multiframe_items.size==0) return@Observer
                var pos = response.pos
                var item = multiframe_items[pos].get(current_size_list[pos])
                var layout = layout_list[pos].frame_view_ll
                layout?.removeAllViews()
                if(item.dType.equals(Constant.TEXT_CROWLING))
                    layout?.addView(WidgetText.getWidgetTextCrowling(this,item,response.data),item.frame_w,item.frame_h)
                else
                    setStaticTextWithRotate(pos,item,response.data,0)
            }
        })

        // emergency message response
        viewModel.emergency_req_result.observe(this, Observer {
                response ->
            if(response.status == Status.SUCCESS){
                var data = response.data
                setEmergencyMessage(data)
            }
        })

        // STOCK result
        viewModel.stock_api_result.observe(this, Observer { response ->
            if(response.status==Status.SUCCESS){
                if(multiframe_items.size==0) return@Observer
                var pos = response.pos
                var layout = layout_list[pos].frame_view_ll
                var item = multiframe_items[pos].get(current_size_list[pos])
                var item_ids  = item.id.split("-")
                layout?.removeAllViews()
                if(item.dType.equals("s"))
                    layout?.addView(WidgetStocks.getSingleStockWidget(this,item,response.data),item.frame_w,item.frame_h)
                else{
                    if(UtilitySetting.getTemplate(item.settings).equals("t1"))
                        layout?.addView(WidgetStocks.getWidgetStockCrowling(this,item,response.data!!),item.frame_w,item.frame_h)
                    else
                        setStockWebView(pos,item,Constant.API_STOCK_TABLE_HTML+item_ids[2])
                }
            }
        })

        // file downloading observer
        viewModel.download_file_result.observe(this, Observer { response ->
            if(response.status == Status.SUCCESS){
                changeContent()
            }
        })

        // re-launch observer
        viewModel.relaunch_result.observe(this, Observer { response ->
            if(response.status == Status.SUCCESS){
                finishAffinity()
                startActivity(intent)
            }
        })

        // vimeo
        vimeoViewModel.vimeo_api_result.observe(this){ response->
            val mediaItem = MediaItem.fromUri(response?.url!!)
            layout_list[response.pos].frame_view_ll?.addView(VimeoWidget.getVimeoWidget(this,layout_list[response.pos].exoPlayer!!,fullScreen))
//            binding.rlCodContent?.addView()
            layout_list[response.pos].exoPlayer!!.setMediaItem(mediaItem)
            layout_list[response.pos].exoPlayer?.play()
        }

        // weather
        viewModel.weather_api_result.observe(this){ response->
            if(response.status==Status.SUCCESS){
                var weather_obj = Gson().fromJson(response.data,WeatherFive::class.java)
                setWatherFragment(response.pos,response.item!!,weather_obj)
            }
        }

    }

    fun setQuoteRotation(pos: Int, item: Item) {
        layout_list[pos].item_job = CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(DataParsing.getSettingRotate(item).toLong()))
            withContext(Dispatchers.Main) {
                if(play_activate){
                    WidgetQuotes.pos = WidgetQuotes.pos+1
                    WidgetQuotes.setText()
                    setQuoteRotation(pos,item)
                }
            }
        }
    }

    private fun setWaterMark() {
        if(dataParsing.isWatermarkAvailable(pref)){
            WaterMarkWidget.getWaterMark(binding,dataParsing.getWatermark())
        }else binding.llWatermark.visibility = View.GONE
    }

    private fun changeContent() {
        if(!pref!!.getBooleanData(MySharePrefernce.KEY_DEVICE_REGISTERED)) {
            showNotRegisterDialog()
            return
        }
        if(!pref.getStringData(MySharePrefernce.KEY_RELAUNCH_ONDEMAND).equals("false")
            && !pref.getStringData(MySharePrefernce.KEY_RELAUNCH_ONDEMAND).equals("")) {
            viewModel.getRelaunchAcknowledge()
        }
        var list = dataParsing.getDownloableList(pref)
        if(list.size>0 && isConnected){
            downloading = 1
            binding.rlDownloading.visibility = View.VISIBLE
            viewModel.downloadFile(list[0])
            content_confirmation_list.add(list[0])
            ImageUtil.loadGifImage(this,binding.ivDownloading)
        }else{
            var downloaded_list = dataParsing.getDownloadedList()
            binding.rlDownloading.visibility = View.GONE
            if(downloading==1) viewModel.submitContentConfirmation(viewModel.device_id,downloaded_list)
            content_confirmation_list = mutableListOf()
            downloading = 0
            removeData()
            setWaterMark()
            setIdentifyRequest()
            isEmergencyMessage()
            pref.putBooleanData(MySharePrefernce.KEY_ODSS_ACTIVE,dataParsing.checkDemandSs())
            var is_frames = dataParsing.isFrameAvailable()
            var is_override = dataParsing.isOverrideAvailable(pref)

            if(is_override || is_frames) contentPlaying(is_override) else loadWaiting()
        }
    }

    private fun contentPlaying(is_override: Boolean) {

        if(is_override){
            // create single frame for override
            var frame = DataParsing.getOverrideFrame(ctx!!,100)
            var ss_frame = DataParsing.getOverrideFrame(ctx!!,1000)

            var videoView = VideoView(this)
            var exoPlayerView  = StyledPlayerView(ctx)
            var player = SimpleExoPlayer.Builder(ctx).build()
            var myMediaMetadataRetriever = MediaMetadataRetriever()

            // add frame in layout list
            layout_list.add(LayoutFrames(frame,videoView,exoPlayerView,player,false,"",null,null,myMediaMetadataRetriever))    // add frame in list
            screen_layout_list.add(ss_frame)    // add frame in list

            binding.rootLayout.addView(frame)   // attach frame in root
            binding.screenRootLayout.addView(ss_frame)   // attach frame in root

            var all_frames = DataParsing.getOverrideFrames(pref)
            // add multi-fame items
            if (all_frames?.get(0)?.item != null && all_frames.get(0).item.size > 0) {

                var child_items: MutableList<Item> = mutableListOf()
                var items_array = all_frames.get(0).item
                for (j in 0..items_array.size - 1) {
                    var item = items_array[j]
                    item.frame_h = DeviceInfo.getScreenHeight(this)
                    item.frame_w = DeviceInfo.getScreenWidth(this)
                    item.frame_setting = all_frames.get(0).settings
                    items.add(item)
                    items.get(items.size-1).pos = 0
                    if(Validation.isItemDownloaded(item)) child_items.add(item)
                }
                multiframe_items?.add(child_items)
                current_size_list.add(0)
            }else {
                multiframe_items?.add(mutableListOf())
                current_size_list.add(0)
            }
            // start playing
            startPlayingContent()
        }else {

            var all_frames = DataParsing.getFilterdFrames(pref)
            var is_items = DataParsing.isItemvailable(all_frames)

            if(!is_items){
                loadWaiting()
                return
            }

            for (i in 0..all_frames!!.size-1) {
                // create availble frames with bg
                var frame = DataParsing.getLayoutFrame(ctx!!,all_frames.get(i),i,10)
                var ss_frame = DataParsing.getLayoutFrame(ctx!!,all_frames.get(i),i,1000)

                var videoView = VideoView(this)
                var exoPlayerView  = StyledPlayerView(ctx)
                var md_Retriever = MediaMetadataRetriever()

                // add frame in layout list
                var layout_frame  = LayoutFrames(
                    frame_view_ll = frame,videoView = videoView, exoPlayerView = exoPlayerView,
                    myMediaMetadataRetriever = md_Retriever, frame_items = all_frames.get(i).item
                )

                layout_list.add(layout_frame)    // add frame in list
                binding.rootLayout.addView(frame)   // attach frame in root

                screen_layout_list.add(ss_frame)    // add frame in list
                binding.screenRootLayout.addView(ss_frame)

                binding.rootLayout.setBackgroundColor(Color.parseColor(DataParsing.getRootBackground(pref)))    // set bg for root
                binding.screenRootLayout.setBackgroundColor(Color.parseColor(DataParsing.getRootBackground(pref)))    // set bg for root

                // add multi-fame items
                if (all_frames.get(i).item != null && all_frames.get(i).item.size > 0) {
                    var child_items: MutableList<Item> = mutableListOf()
                    var items_array = all_frames.get(i).item
                    for (j in 0..items_array.size - 1) {
                        var item = items_array[j]
                        item.frame_h = all_frames.get(i).h
                        item.frame_w = all_frames.get(i).w
                        item.frame_setting = all_frames.get(i).settings
                        items.add(item)
                        items.get(items.size-1).pos = i
                        if(Validation.isItemDownloaded(item)) child_items.add(item)
                    }
                    multiframe_items?.add(child_items)
                    current_size_list.add(0)
                }else {
                    multiframe_items?.add(mutableListOf())
                    current_size_list.add(0)
                }
            }
            startPlayingContent()
        }
    }


    private fun startPlayingContent() {
        if(multiframe_items != null && multiframe_items.size>0){
            for (i in 0..multiframe_items.size-1){
                if(multiframe_items.get(i) != null && multiframe_items.get(i).size>0){
                    if(current_size_list[i] < multiframe_items.get(i).size){
                        binding.rootLayout.visibility = View.VISIBLE
                        validateItem(multiframe_items[i].get(current_size_list[i]),i)
                    }
                }
            }
        }
    }

    private fun validateItem(item: Item, position: Int) {

        val path = DataManager.getDirectory()+File.separator+ item.fileName
        if(item.type == Constant.CONTENT_IMAGE ||item.type == Constant.CONTENT_VIDEO){
            if(!File(path).exists()){
                current_size_list[position] = current_size_list[position]+1
                nextPlay(position)
                return
            }
        }
        setWidget(position,item)
    }

    private fun setWidget(pos: Int, item: Item){

        var start_time = pref?.getStartTime()
        play_activate = true
        binding.rlBackground.visibility = View.GONE
        var layout = layout_list[pos].frame_view_ll
        var video = layout_list[pos].videoView
        var media_data = layout_list[pos].myMediaMetadataRetriever
        var exoPlayerView = layout_list[pos].exoPlayerView
        var exoPlayer = layout_list[pos].exoPlayer
        layout?.visibility = View.VISIBLE
        var widget_type = item.type

        if(!play_activate || !pref!!.getBooleanData(MySharePrefernce.KEY_DEVICE_REGISTERED)) return
        layout_list[pos].active_widget = widget_type

        // image
        if(widget_type.equals(Constant.CONTENT_IMAGE) ||
            widget_type.equals(Constant.CONTENT_VECTOR) ||
            widget_type.equals(Constant.CONTENT_POWERPOINT) ||
            widget_type.equals(Constant.CONTENT_WORD) ) {
            if(item.fs.equals("yes")){
                fullScreen = true
                binding.llFullScreen.visibility = View.VISIBLE
                binding.llFullScreen.removeAllViews()
                binding.llFullScreen.addView(ImageWidget.getImageWidget(this,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,item.fileName,item.filesize,item.frame_setting))
            }else{
                layout?.removeAllViews()
                layout?.addView(ImageWidget.getImageWidget(this,item.frame_w,item.frame_h,item.fileName,item.filesize,item.frame_setting))
            }
        }
        // QR-Code
        else if(widget_type.equals(Constant.CONTENT_WIDGET_QRCODE)){
            layout?.removeAllViews()
            layout?.addView(ImageWidget.getImageWidget(this,item.frame_w,item.frame_h,item.fileName,item.filesize,item.frame_setting))
        }
        // video
        else if(widget_type.equals(Constant.CONTENT_VIDEO)){
            if(item.fs.equals("yes")){
                fullScreen = true
                binding.llFullScreen.visibility = View.VISIBLE
                binding.llFullScreen.removeAllViews()
                video?.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
                binding.llFullScreen.addView(WidgetVideo.getWidgetVideo(this,video!!,media_data!!,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,item.fileName,item.sound))
            }else{
                layout?.removeAllViews()
                layout_list[pos].sound = item.sound
//                layout_list[pos].myMediaMetadataRetriever = WidgetVideo.getMediaData(this,item.fileName,media_data!!)
//                layout?.addView(WidgetVideo.getWidgetVideo(this,video!!,media_data!!,item.frame_w,item.frame_h,item.fileName,item.sound))
                layout_list[pos].exoPlayer = WidgetExoPlayer.getExoPlayer(ctx,Constant.PLAYER_SLIDE,item.sound)
                var video_new = layout_list[pos].exoPlayerView
                layout_list[pos].exoPlayerView = WidgetExoPlayer.getExoPlayerView(ctx)
                video_new?.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
                layout?.addView(WidgetVideo.playVideo(layout_list[pos].exoPlayerView!!,layout_list[pos].exoPlayer,item,fullScreen))
            }
        }
        // webview , Google SLide
        else if(widget_type == Constant.CONTENT_WEB || widget_type == Constant.CONTENT_WIDGET_GOOGLE){
            setWebViewWithReload(pos,item)
        }
        // iFrame
        else if(widget_type == Constant.CONTENT_WIDGET_IFRAME){
            layout?.removeAllViews()
            layout?.addView(WebViewWidget.getiFrameWidget(this,item?.ifr!!))
        }
        // youtube
        else if(widget_type == Constant.CONTENT_WIDGET_YOUTUBE) {
            layout?.removeAllViews()
            layout?.addView(YouTubeWidget.getYouTubeWidget(this,item),item.frame_w,item.frame_h)
        }
        // vimeo
        else if(widget_type == Constant.CONTENT_WIDGET_VIMEO){
            layout?.removeAllViews()
            var player = WidgetExoPlayer.getExoPlayer(this,Constant.PLAYER_SLIDE,item?.sound!!)
            layout_list[pos].exoPlayer = player
            exoPlayer = player
            vimeoViewModel.getVimeoUrl(item.src,item,pos,vimeo_retro)
        }
        // power BI
        else if(widget_type == Constant.CONTENT_WIDGET_POWER){
            layout?.removeAllViews()
            layout?.addView(WidgetPowerBI.getWidgetPowerBI(this,item),item.frame_w,item.frame_h)
        }
        // traffic
        else if(widget_type == Constant.CONTENT_WIDGET_TRAFFIC){
            layout?.removeAllViews()
            layout?.addView(WidgetTraffic.getWidgetTrafic(this,item.id),item.frame_w,item.frame_h)
        }
        // date time
        else if(widget_type == Constant.CONTENT_WIDGET_DATE_TIME){
            layout?.removeAllViews()
            var date_time = WidgetDateTime()
            layout?.addView(date_time.getDateTimeWidget(this,item),item.frame_w,item.frame_h)
            var view = layout?.getChildAt(0)
            date_time.setData(view!!,item)
        }
        // quotes
        else if(widget_type == Constant.CONTENT_WIDGET_QUOTES){
            if(!viewModel.isDataStoredForCurrentVersion(Constant.CONTENT_WIDGET_QUOTES,pos,item.id))
                viewModel.getQuoteText(item.id.split("-")[2],pos,item.id)
        }
        // text
        else if(widget_type == Constant.CONTENT_WIDGET_TEXT){
            if(!viewModel.isDataStoredForCurrentVersion(Constant.CONTENT_WIDGET_TEXT,pos,item.id))
                viewModel.getText(item.id.split("-")[2],pos,item.id)
        }
        // stock
        else if(widget_type == Constant.CONTENT_WIDGET_STOCK){
            if(item.dType.equals("m") && UtilitySetting.getTemplate(item.settings).equals("t2")){
                var item_ids = item.id.split("-")
                setStockWebView(pos,item,Constant.API_STOCK_TABLE_HTML+item_ids[2])
            }else if(!viewModel.isDataStoredForCurrentVersion(Constant.CONTENT_WIDGET_STOCK,pos,item.id))
                viewModel.getStockData(item.id.split("-")[2],pos,item.id)
        }
        // news
        else if(widget_type == Constant.CONTENT_WIDGET_NEWS){
            if(!viewModel.isDataStoredForCurrentVersion(Constant.CONTENT_WIDGET_NEWS,pos,item.id)){
                if(item.src.equals(""))
                    viewModel.getNews(Constant.API_WIDGET_BEING_NEWS +item.id.split("-")[2],pos,item.id)
                else
                    viewModel.getNews(item.src,pos,item.id)
            }
        }
        // RSS
        else if(widget_type == Constant.CONTENT_WIDGET_RSS){
            if(!viewModel.isDataStoredForCurrentVersion(Constant.CONTENT_WIDGET_RSS,pos,item.id))
                if(item.src.equals(""))
                    viewModel.getRss(Constant.API_WIDGET_BEING_NEWS +item.id.split("-")[2],pos,item.id)
                else
                    viewModel.getRss(item.src,pos,item.id)
        }
        // live streaming
        else if(item?.type.equals(Constant.CONTENT_WIDGET_LIVESTREAM)){
            layout?.removeAllViews()
            if(item?.dType.equals("yt"))
                layout?.addView(YouTubeWidget.getYouTubeWidgetLiveStream(this,item),item.frame_w,item.frame_h)
            else
                layout?.addView(WidgetExoPlayer.setExoPLayer(this,item,exoPlayerView!!),item.frame_w,item.frame_h)
        }
        // COD BUTTON
        else if(widget_type == Constant.CONTENT_WIDGET_COD){
            layout?.removeAllViews()
            var settings = Gson().fromJson(item.settings, com.app.lsquared.model.cod.Settings::class.java)
            layout?.background = DataParsing.getShape(Color.parseColor(UiUtils.getColorWithOpacity(settings?.bg!!,settings?.bga!!)),item.frame_setting)
            layout?.addView(WidgetCodButton.getWidgetCodButton(this,item,pref))
            layout?.setOnClickListener { WidgetCodButton.openCod(this,pref) }
        }
        // MESSAGE
        else if(widget_type == Constant.WIDGET_MESSAGE){
            layout?.removeAllViews()
            var image = dataParsing.getImageName(WidgetMessage.getData(item.data).contentid)
            layout?.addView(WidgetMessage.getMessageWidget(this,item,image))
        }
        // MEETING
        else if(widget_type == Constant.CONTENT_WIDGET_MEETING)
            viewModel.getMeetingData(dataParsing.getDevice(),item.id.split("-")[2],pos,item)
        // Google Calendar
        else if(widget_type == Constant.CONTENT_WIDGET_GOOGLE_CAL)
            viewModel.getGoogleCalData(dataParsing.getDevice(),item.id.split("-")[2],pos,item)
        // Outlook Calendar
        else if(widget_type == Constant.CONTENT_WIDGET_OUTLOOK_CAL)
            viewModel.getOutlookData(dataParsing.getDevice(),item.id.split("-")[2],pos,item)
        // weather
        else if(widget_type == Constant.CONTENT_WIDGET_WEATHER)
            viewModel.getWeather(item,pos)

        // not implemetated
        else {
            layout?.removeAllViews()
            layout?.addView(WidgetText.getBlankView(this),item.frame_w,item.frame_h)
        }
        if(multiframe_items[pos].size>1 || multiframe_items[pos].size==1 && item.type.equals(Constant.CONTENT_WIDGET_MEETING)){
            setVolume()
            var time = if(multiframe_items[pos].size==1 && item.type.equals(Constant.CONTENT_WIDGET_MEETING))
                300 else item.duration
            var job = CoroutineScope(Dispatchers.IO).launch {
                delay(TimeUnit.SECONDS.toMillis(time.toLong()))
                withContext(Dispatchers.Main) {
                    layout_list[pos].sound = ""

                    if(layout_list[pos].active_widget.equals(Constant.CONTENT_VIDEO)){
                        if(layout_list[pos].exoPlayer != null && layout_list[pos].exoPlayer!!.isPlaying){
                            layout_list[pos].exoPlayer!!.release()
                        }
                    }

                    if(exoPlayerView != null && exoPlayerView?.player != null && exoPlayerView?.player!!.isPlaying){
                        exoPlayerView?.player!!.release()
                        exoPlayerView?.player = null
                    }

                    if(play_activate){
                        pref?.createReport(item.id,item.duration,start_time!!)
                        current_size_list[pos] = current_size_list[pos]+1
                        if(item.fs.equals("yes") ){
                            fullScreen = false
                            setVolume()
                            binding.llFullScreen.visibility = View.GONE
                            binding.llFullScreen.removeAllViews()
                        }
                        nextPlay(pos)
                    }
                }
            }
            if(layout_list[pos].frame_job == null){
                layout_list[pos].frame_job = job
            }else{
                layout_list[pos].frame_job?.cancel()
                if(layout_list[pos].item_job != null)layout_list[pos].item_job?.cancel()
                layout_list[pos].frame_job = job
            }
        }

    }



    private fun checkDemandScreenShot() {
        if(pref.getBooleanData(MySharePrefernce.KEY_ODSS_ACTIVE)){
            captureScreen(Constant.SS_TYPE_OD)
        }else{
            od_screenshot_handler.removeCallbacks(odssTask)
        }
    }

    private fun setIdentifyRequest() {
        var content_version = pref?.getVersionOfConytentAPI()
        var set_version = pref.getIntData(MySharePrefernce.KEY_IDENTIFY_SIGNATURE)
        if(time!=0 && !viewModel.is_device_registered){
            IdentifyRequestWidget.setIdentifyRequest(binding,pref,this,isConnected,temp.toString(),viewModel.is_device_registered)
        }else if(DataParsing.isIdentifyRequestAvailable(pref) && set_version != content_version){
            viewModel.getIdentifyAcknowledge(pref!!)
            binding.llIdentifyRequest.visibility = View.VISIBLE
            IdentifyRequestWidget.setIdentifyRequest(binding,pref,this,isConnected,temp.toString(),viewModel.is_device_registered)
            startRequestIdentifyTimer((DataParsing.getIdentifyRequestDuration(pref)*1000).toLong())
        }else binding.llIdentifyRequest.visibility = View.GONE
    }

    private fun isEmergencyMessage(){
        var device = dataParsing.getDevice()
        if(device?.em ==1)
            if(!viewModel.isDataStoredForCurrentVersion(Constant.WIDGET_EMERGENCY_MESSAGE,0,""))
                viewModel.getEmergencyMessagedata(DeviceInfo.getDeviceIdFromDevice(this))
    }

    private fun setEmergencyMessage(data: String?) {
        if(!data.equals("{}") && data?.length!!>100){
            binding.llEmergencyMsg.visibility = View.VISIBLE
            binding.llEmergencyMsg.addView(EmergencyMessageView.getEmView(this,data))
            startEmergencyMsgTiner(EmergencyMessageView.getEmDuration(data))
        }
    }

    fun startRequestIdentifyTimer(seconds: Long) {
        time = (seconds/1000).toInt()
        object : CountDownTimer(seconds, 1000) {
            override fun onTick(duration: Long) {}
            override fun onFinish() {
                time = 0
                binding.llIdentifyRequest.visibility = View.GONE
                pref.putIntData(MySharePrefernce.KEY_IDENTIFY_SIGNATURE,pref?.getVersionOfDeviceAPI()!!)
            }
        }.start()
    }

    fun startEmergencyMsgTiner(seconds: Long) {
        time = (seconds/1000).toInt()
        object : CountDownTimer(seconds, 1000) {
            override fun onTick(duration: Long) {}
            override fun onFinish() {
                binding.llEmergencyMsg.visibility = View.GONE
            }
        }.start()
    }

    fun removeData() {
        play_activate = false
        fullScreen = false
        setVolume()
        binding.rootLayout.visibility = View.GONE
        binding.llWatermark.visibility = View.GONE
        binding.llFullScreen.visibility = View.GONE
        binding.llFullScreen.removeAllViews()
        binding.rootLayout.removeAllViews()
        items = mutableListOf()
        multiframe_items = mutableListOf()
        current_size_list = mutableListOf()
        WidgetNewsCrowling.mHandler.removeCallbacks(WidgetNewsCrowling.SCROLLING_RUNNABLE)
        WidgetText.mHandler.removeCallbacks(WidgetText.SCROLLING_RUNNABLE)

        if(layout_list!=null && layout_list.size>0){
            for(i in 0..layout_list.size-1){
                if(layout_list[i].exoPlayer != null && layout_list[i].exoPlayer!!.isPlaying){
                    layout_list[i].exoPlayer!!.release()
                    layout_list[i].exoPlayer = null
                }
                if(layout_list[i].exoPlayerView?.player!=null && layout_list[i].exoPlayerView?.player!!.isPlaying){
                    layout_list[i].exoPlayerView?.player?.stop()
                }
                if(layout_list[i].frame_job != null) layout_list[i].frame_job?.cancel()
                if(layout_list[i].item_job != null) layout_list[i].item_job?.cancel()
            }
        }
        layout_list = mutableListOf()
    }

    fun setVolume(){
        var vol = if(fullScreen) 0 else volume
        aManager?.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0)

//        if(layout_list!=null && layout_list.size>0){
//            for(i in 0..layout_list.size-1){
//                if(layout_list[i].exoPlayer != null && layout_list[i].exoPlayer!!.isPlaying)
//                    layout_list[i].exoPlayer!!.volume = if(fullScreen) 0f else 50f
//            }
//        }
    }

    @JvmName("getDownloading1")
    public fun getDownloading(): Int {
      return downloading
    }


    private fun setAllNewsListRotation(pos: Int, list: List<RssItem>,all_list: List<RssItem>, item: Item, title: String?,start_pos:Int) {
        if(list!=null && list.size>0){

            layout_list[pos].frame_view_ll?.removeAllViews()
            layout_list[pos].frame_view_ll?.addView(getWidgetNewsListAllFixContent(this,item,list,title,start_pos),item.frame_w,item.frame_h)

            if(DataParsing.getSettingRotate(item)!=null && DataParsing.getSettingRotate(item) != 0){
                layout_list[pos].item_job = CoroutineScope(Dispatchers.IO).launch {
                    delay(TimeUnit.SECONDS.toMillis(DataParsing.getSettingRotate(item).toLong()))
                    withContext(Dispatchers.Main) {
                        if(play_activate){
                            WidgetNewsList.lastView = null
                            var position = WidgetNewsList.getLastPosition()
                            var nextlist = getNextList(list,all_list,position)
                            if(nextlist.size==0) setAllNewsListRotation(pos, all_list, all_list, item,title,0)
                            else setAllNewsListRotation(pos, getNextList(list,all_list,position),all_list, item,title,0)
                        }
                    }
                }
            }
        }
    }

    private fun getNextList(list: List<RssItem>, all_list: List<RssItem>, pos: Int): List<RssItem> {
//        if(list.size==0 && all_list.size>0) return all_list
        var nextlist = mutableListOf<RssItem>()
        for (item in pos..list.size-1){
            nextlist.add(list[item])
        }
        return nextlist
    }

    private fun setBeingNewsListRotation(pos: Int,list : ArrayList<News>,all_list : ArrayList<News>, item: Item,start_pos:Int) {

        if(list!=null && list.size>0){
            layout_list[pos].frame_view_ll?.removeAllViews()
            layout_list[pos].frame_view_ll?.
            addView(WidgetNewsList.getWidgetNewsListAllFixContentBeing(this,item,list,start_pos,item.fileName),item.frame_w,item.frame_h)
            if(DataParsing.getSettingRotate(item)!=null && DataParsing.getSettingRotate(item) != 0){
                layout_list[pos].item_job = CoroutineScope(Dispatchers.IO).launch {
                    delay(TimeUnit.SECONDS.toMillis(DataParsing.getSettingRotate(item).toLong()))
                    withContext(Dispatchers.Main) {
                        if(play_activate){
                            var position = WidgetNewsList.getLastPositionBeing()
                            if(position == list.size) setBeingNewsListRotation(pos, all_list,all_list, item,0)
                            else setBeingNewsListRotation(pos, getNextBeingList(list,position),all_list, item,0)
                        }
                    }
                }
            }
        }
    }

    private fun getNextBeingList(list: java.util.ArrayList<News>, position: Int): java.util.ArrayList<News> {
        var next_list = java.util.ArrayList<News>()
        for (item in position..list.size-1)next_list.add(list[item])
        return next_list
    }

    private fun setAllNewsListRotationRSS(pos: Int, list: List<RssItem>, item: Item, title: String?,start_pos:Int) {
        if(list!=null && list.size>0){

            layout_list[pos].frame_view_ll?.removeAllViews()
            layout_list[pos].frame_view_ll?.addView(
                WidgetRssList.getWidgetNewsListAllFixContent(this,item,list,title,start_pos),item.frame_w,item.frame_h)

            if(DataParsing.getSettingRotate(item)!=null && DataParsing.getSettingRotate(item) != 0){
                layout_list[pos].item_job = CoroutineScope(Dispatchers.IO).launch {
                    delay(TimeUnit.SECONDS.toMillis(DataParsing.getSettingRotate(item).toLong()))
                    withContext(Dispatchers.Main) {
                        if(play_activate){
                            var position = WidgetRssList.getLastPosition()
                            if(position == list.size) setAllNewsListRotationRSS(pos, list, item,title,0)
                            else setAllNewsListRotationRSS(pos, list, item,title,position)
                        }
                    }
                }
            }
        }
    }

    private fun setBeingNewsListRotationRSS(pos: Int,list : ArrayList<News>, item: Item,start_pos:Int) {
        if(list!=null && list.size>0){
            layout_list[pos].frame_view_ll?.removeAllViews()
            layout_list[pos].frame_view_ll?.
            addView(WidgetRssList.getWidgetNewsListAllFixContentBeing(this,item,list,start_pos),item.frame_w,item.frame_h)
            if(DataParsing.getSettingRotate(item)!=null && DataParsing.getSettingRotate(item) != 0){
                layout_list[pos].item_job = CoroutineScope(Dispatchers.IO).launch {
                    delay(TimeUnit.SECONDS.toMillis(DataParsing.getSettingRotate(item).toLong()))
                    withContext(Dispatchers.Main) {
                        if(play_activate){
                            var position = WidgetNewsList.getLastPositionBeing()
                            if(position == list.size) setBeingNewsListRotationRSS(pos, list, item,0)
                            else setBeingNewsListRotationRSS(pos, list, item,position-1)
                        }
                    }
                }
            }
        }
    }

    fun loadWaiting() {
        if(layout_list!=null &&layout_list.size>0){
            for (i in 0..layout_list.size-1)
                layout_list[i].frame_view_ll?.visibility = View.GONE
        }
        binding.rlBackground.visibility = View.VISIBLE
        var fileName = dataParsing.isDefaultImageAvailable()
        if(!fileName.equals("")){
            val path = DataManager.getDirectory()+ File.separator+fileName
            binding.ivMainDefaultimg.visibility = View.VISIBLE
            binding.llDefaultimgBg.visibility = View.VISIBLE
            binding.ivMainDefaultimg.setImageBitmap(BitmapFactory.decodeFile(path, ImageUtil.getImageOption()))
        }else {
            binding.ivMainDefaultimg.visibility = View.GONE
            binding.llDefaultimgBg.visibility = View.GONE
        }
    }


    // stock table view with webview
    private fun setStockWebView(pos: Int, item: Item,url:String) {

        var layout = layout_list[pos].frame_view_ll
        layout?.removeAllViews()
        layout?.background = DataParsing.getShape(Color.parseColor(UiUtils.getColorWithOpacity()),item.frame_setting)
        layout?.addView(WebViewWidget.getWebViewWidget(this,url),item.frame_w,item.frame_h)
    }

    // web view with reloading
    private fun setWebViewWithReload(pos: Int, item: Item) {

        var layout = layout_list[pos].frame_view_ll
        var active_widget = layout_list[pos].active_widget
        if(active_widget.equals(Constant.CONTENT_WEB) || active_widget.equals(Constant.CONTENT_WIDGET_GOOGLE) ){
            layout?.removeAllViews()
            layout?.background = DataParsing.getShape(Color.parseColor(UiUtils.getColorWithOpacity()),item.frame_setting)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                Log.d(TAG, "setWebViewWithReload: ${item.src}")
                var shape = DataParsing.getShape(Color.parseColor(UiUtils.getColorWithOpacity()),item.frame_setting)
                layout?.background = shape
                layout?.addView(WebViewChromium.getWebChromeWidget(this,item.src,shape,item.settings),item.frame_w,item.frame_h)
            } else layout?.addView(WebViewWidget.getWebViewWidget(this,item.src),item.frame_w,item.frame_h)


            if(DataParsing.getWebInterval(item) !=0 && DataParsing.getWebInterval(item) < item.duration){
                layout_list[pos].item_job = CoroutineScope(Dispatchers.IO).launch {
                    delay(TimeUnit.SECONDS.toMillis(DataParsing.getWebInterval(item).toLong()))
                    withContext(Dispatchers.Main) {
                        if(play_activate){
                            setWebViewWithReload(pos,item)
                        }
                    }
                }
            }
        }
    }

    // text-static with rotation
    private fun setStaticTextWithRotate(pos: Int, item: Item, data: String?,text_pos:Int) {

        var layout = layout_list[pos].frame_view_ll
        if(layout_list[pos].active_widget.equals(Constant.CONTENT_WIDGET_TEXT)){

            layout?.removeAllViews()

            var list = mutableListOf<String>()
            var json_data = JSONObject(data)
            var channel_array = json_data.getJSONArray("channel")
            if(channel_array.length()>0){
                for (i in 0 until channel_array.length()){
                    var title_obj = channel_array.getJSONObject(i).getString("title")
                    list.add(title_obj)
                }
                if(list.size>0){
                    layout?.addView(WidgetText.getWidgetTextStatic(this,item,list[text_pos]),item.frame_w,item.frame_h)
                    if (item.dType.equals(Constant.TEXT_STATIC)){
                        layout_list[pos].item_job = CoroutineScope(Dispatchers.IO).launch {
                            delay(TimeUnit.SECONDS.toMillis(DataParsing.getSettingRotate(item).toLong()))
                            withContext(Dispatchers.Main) {
                                if(play_activate){
                                    var new_pos = text_pos+1
                                    if(list.size>new_pos)
                                        setStaticTextWithRotate(pos,item,data,new_pos)
                                    else
                                        setStaticTextWithRotate(pos,item,data,0)
                                }
                            }
                        }
                    }
                }else{
                    layout?.addView(WidgetText.getWidgetTextStaticLayout(this,item),item.frame_w,item.frame_h)
                }
            }else{
                layout?.addView(WidgetText.getWidgetTextStaticLayout(this,item),item.frame_w,item.frame_h)
            }
        }
    }

    private fun setWatherFragment(pos: Int, item: Item, weather_data: WeatherFive) {
        var setting_obj = JSONObject(item.settings)
        var template = setting_obj.getString("template")
        var orientation = setting_obj.getString("orientation")
        var forecast = item.forecast
        var widgetWeather = WidgetWeather()

        var layout = layout_list[pos].frame_view_ll

        layout?.removeAllViews()

        if(forecast == Constant.TEMPLATE_WEATHER_CURRENT_DATE){
            if(template.equals(Constant.TEMPLATE_TIME_T1))
                layout?.addView(widgetWeather.getWidgetWeatherCurremtTemp1(this,item,weather_data),item.frame_w,item.frame_h)
            if(template.equals(Constant.TEMPLATE_TIME_T2))
                layout?.addView(widgetWeather.getWidgetWeatherCurremtTemp2(this,item,weather_data),item.frame_w,item.frame_h)
            if(template.equals(Constant.TEMPLATE_TIME_T3))
                layout?.addView(widgetWeather.getWidgetWeatherCurremtTemp3(this,item,weather_data),item.frame_w,item.frame_h)
        }
        if(forecast == Constant.TEMPLATE_WEATHER_FIVE_DAY){
            if(orientation.equals(Constant.TEMPLATE_WEATHER_ORIENTATION_VERTICAL))
                layout?.addView(widgetWeather.getWidgetWeatherFiveDayVerti(this,item,weather_data),item.frame_w,item.frame_h)
            else
                layout?.addView(widgetWeather.getWidgetWeatherFiveDayHori(this,item,weather_data),item.frame_w,item.frame_h)
        }
        if(forecast == Constant.TEMPLATE_WEATHER_FOUR_DAY){
            if(orientation.equals(Constant.TEMPLATE_WEATHER_ORIENTATION_VERTICAL))
                layout?.addView(widgetWeather.getWidgetWeatherFourDayVerti(this,item,weather_data),item.frame_w,item.frame_h)
            else
                layout?.addView(widgetWeather.getWidgetWeatherFourDayHori(this,item,weather_data),item.frame_w,item.frame_h)
        }
    }

    // set Meeting View
    fun setMeetingView(pos: Int, item: Item, meeting_obj: MeetingCalendarResponseData) {
        var layout = layout_list[pos].frame_view_ll
        layout?.removeAllViews()
        var meeting = WidgetMeeting()

        if(item.dType.equals(Constant.MEETING_BOARD_WALL) && meeting_obj.events.size>0)
            layout?.addView(meeting.getMeetingWallBoardWidget(ctx,item,meeting_obj,layout))
        else if(item.dType.equals(Constant.MEETING_BOARD_ROOM) && meeting_obj.event !=null && meeting_obj?.event?.starttime !=null)
            layout?.addView(meeting.getMeetingRoomBoardWidget(ctx,item,meeting_obj,layout))
        else if(meeting_obj.ss!=null && meeting_obj.ss.size>0)
            meeting.loadScreenSaver(meeting_obj.ss,0,layout,ctx)
    }

    // set Calendar View
    fun setCalendarView(pos: Int, item: Item, cal_obj: CalendarResponseData) {
        var layout = layout_list[pos].frame_view_ll
        layout?.removeAllViews()
        var calendar = WidgetCalendar()
        Log.d(TAG, "setCalendarView: ${cal_obj.events.size}")
        if(item.dType.equals(Constant.CALENDAR_BOARD_ALL) || item.dType.equals(Constant.CALENDAR_BOARD_WALL) && cal_obj.events.size>0)
            layout?.addView(calendar.getCalendarAllEvents(ctx,item,cal_obj))
        else if(item.dType.equals(Constant.CALENDAR_BOARD_ROOM) && cal_obj.event !=null && cal_obj?.event?.starttime !=null)
            layout?.addView(calendar.getCalendarRoomBoardWidget(ctx,item,cal_obj))
        else if(cal_obj.ss!=null && cal_obj.ss.size>0)
            calendar.loadScreenSaver(cal_obj.ss,0,layout,ctx)
    }

    fun setMeetingRoomBoardView(pos: Int, item: Item, meeting_obj: MeetingCalendarResponseData) {
        var layout = layout_list[pos].frame_view_ll
        layout?.removeAllViews()
        var meeting = WidgetMeeting()
        if(meeting_obj.event !=null) layout?.addView(meeting.getMeetingRoomBoardWidget(ctx,item,meeting_obj,layout))
        else meeting.getMeetingRoomBoardWidget(ctx,item,meeting_obj,layout)
    }

    private fun nextPlay(pos:Int){
        if(current_size_list[pos] < multiframe_items.get(pos).size){
            validateItem(multiframe_items.get(pos)[current_size_list[pos]],pos)
        }else{
            current_size_list[pos] = 0
            nextPlay(pos)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onPause() {
        super.onPause()
        play_activate = false
        fullScreen = false
        setVolume()

        checkVersionHandler.removeCallbacks(versionTask)
        timeScehdulerHandler.removeCallbacks(timeSchedularTask)
        screenshot_handler.removeCallbacks(ssTask)
        od_screenshot_handler.removeCallbacks(odssTask)
        temp_handler.removeCallbacks(tempTask)
        report_handler.removeCallbacks(reportTask)
        removeData()
    }

    private fun checkDeviceVersion() {
        if(packageManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,getPackageName())==PackageManager.PERMISSION_GRANTED)
            if(isConnected)
                if(downloading==0){
                    freeMemory()
                    viewModel.isDeviceRegistered(this,DeviceInfo.getDeviceId(this,pref))
                }
    }

    // dialog
    fun showNotRegisterDialog() {
        DialogView.showNotRegisterDialog(this,this,binding.mainLayout)
        loadWaiting()
    }

    fun hideDialog() {
        DialogView.hideDialog()
    }

    fun showSnackBar(msg: String) {
//        Snackbar.make(findViewById<View>(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }


    // permission
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Utility.STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    checkDeviceVersion()
                }
            }
        }
    }


    fun deviceNotRegistered() {
        Log.d(TAG, "deviceNotRegistered: success")
        play_activate = false
        removeData()
        binding.llEmergencyMsg.visibility = View.GONE
        binding.rlBackground.visibility = View.VISIBLE
        binding.ivMainDefaultimg.visibility = View.GONE
        pref?.putBooleanData(MySharePrefernce.KEY_DEVICE_REGISTERED,false)
        pref?.putStringData(MySharePrefernce.KEY_DEVICE_REGISTERED_ID,"")
        pref?.putStringData(MySharePrefernce.KEY_JSON_DATA,"")
        showNotRegisterDialog()
        viewModel.deleteFiles(null)

    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hasAllFilesPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager()
        }
        return false
    }

    private fun takePermissions() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(java.lang.String.format("Package:%s",getPackageName()))
                startActivityForResult(intent, 100)
            } catch (exception: java.lang.Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, 100)
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE"
                ),
                101
            )
        }
    }

    fun freeMemory() {
        System.runFinalization()
        Runtime.getRuntime().gc()
        System.gc()
    }

    fun showErrorMessage(msg:String){
        binding.tvNodata.visibility = View.VISIBLE
        binding.tvNodata.setText(msg)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvNodata.visibility = View.GONE
        }, 5000)
    }

    override fun clickOnRegister(device_id: String) {
        viewModel.registerNewDevce(this,pref,device_id)
    }

    // manage orientation
    var newBundy = Bundle()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle("newBundy", newBundy)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getBundle("newBundy")
    }

    override fun onDataReceived(data: ByteArray?) {
    }

    override fun onErrorReceived(data: String?) {
    }

    override fun onDeviceReady(responseStatus: ResponseStatus?) {
    }

    override fun onDeviceDisconnected() {
    }

}
