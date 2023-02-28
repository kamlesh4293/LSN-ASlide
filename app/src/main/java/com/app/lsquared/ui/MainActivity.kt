package com.app.lsquared.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
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
import com.app.lsquared.model.widget.RssItem
import com.app.lsquared.network.NetworkConnectivity
import com.app.lsquared.network.Status
import com.app.lsquared.network.isConnected
import com.app.lsquared.ui.adapter.BeingNewsAdapter
import com.app.lsquared.ui.adapter.NewsAdapter
import com.app.lsquared.ui.viewmodel.CodViewModel
import com.app.lsquared.ui.widgets.*
import com.app.lsquared.ui.widgets.WidgetNewsList.Companion.getWidgetNewsListAll
import com.app.lsquared.utils.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.material.snackbar.Snackbar
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
class MainActivity : AppCompatActivity(), NotRegisterDalogListener{

    var TAG = "MainActivity"

    // view list
    var layout_list : MutableList<NewLayoutView> = mutableListOf()

    var multiframe_items: MutableList<MutableList<Item>> = mutableListOf()
    var items: MutableList<Item> = mutableListOf()

    var current_size_list : MutableList<Int> = mutableListOf()

    // downloading count
    var downloading = 0

    var play_activate = false

    // share prefernce
    lateinit var pref: MySharePrefernce


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
    lateinit var screenshot_handler: Handler
    lateinit var temp_handler: Handler
    lateinit var report_handler: Handler


    private val versionTask = object : Runnable {
        override fun run() {
            checkDeviceVersion()
            checkVersionHandler.postDelayed(this, viewModel.delay.toLong())
        }
    }

    private val ssTask = object : Runnable {
        override fun run() {
            val file = ImageUtil.screenshot(binding.rootLayout,"Screen_final_"+Utility.getCurrentdate())
            if(file!=null)
                viewModel.submitScreenShot(Utility.getScreenshotJson(DeviceInfo.getDeviceId(ctx,pref),Utility.getFileToByte(file?.absolutePath)))
            screenshot_handler.postDelayed(this, viewModel.screen_delay* 1000.toLong())
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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ctx = this
        initXml()
        initObserver()
        // broadcast reciver for temp
        val intentfilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(broadcastreceiver,intentfilter)

        checkVersionHandler = Handler(Looper.getMainLooper())
        screenshot_handler = Handler(Looper.getMainLooper())
        temp_handler = Handler(Looper.getMainLooper())
        report_handler = Handler(Looper.getMainLooper())

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        checkVersionHandler.post(versionTask)
        screenshot_handler.post(ssTask)
        temp_handler.post(tempTask)
        report_handler.post(reportTask)
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
        pref = MySharePrefernce(ctx)
        Utility.checkAllPermissionGranted(this)
        @RequiresApi(Build.VERSION_CODES.R)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            var d=hasAllFilesPermission()
            Log.d("TAGXZ", d.toString())
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
                    DialogView.hideDialog()
                }else{
                    pref.putStringData(MySharePrefernce.KEY_DEVICE_REGISTERED_ID,"")
                    DialogView.showError(response.message)
                }
            }
        })


        // device registred observer
        viewModel.device_register_api_result.observe(this, Observer { response ->
            Log.d(TAG, "initObserver: observ register api")
            if (response.status == Status.SUCCESS) {
                var device_obj = Gson().fromJson(response.data, ResponseCheckDeviceData::class.java)
                if (device_obj.desc.equals("device not found")) {
                    viewModel.is_device_registered = false
                    viewModel.is_deviceinfo_submitted = false
                    deviceNotRegistered()
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
                } catch (ex: Exception) {

                }
            }
        })



        // submit device info
        viewModel.devcieinfo_api_result.observe(this, Observer { response ->
            viewModel.is_deviceinfo_submitted = true
//                showSnackBar("Device info Result - ${response.status}")
//                Log.d("device_info_result", response.message)
        })

        // submit temp
        viewModel.temprature_api_result.observe(this, Observer { response ->
//            showSnackBar("Device temp Result - ${response.status}")
        })

        // submit screenshot
        viewModel.screenshot_api_result.observe(this, Observer { response ->
        })

        // Widget API Observer

        // Quotes API
        viewModel.quote_api_result.observe(this, Observer {
                response ->
            if(response.status == Status.SUCCESS){
                var pos = response.pos
                var item = multiframe_items[pos].get(current_size_list[pos])
                layout_list[pos].relative_layout?.removeAllViews()
                layout_list[pos].relative_layout?.
                addView(WidgetQuotes.getWidgetQuotes(this,item,response.data),item.frame_w,item.frame_h)
            }
        })

        // news api
        viewModel.rss_api_result.observe(this, Observer { response ->
            if (response.status == Status.SUCCESS) {
                var pos = response.pos
                var item = multiframe_items[pos].get(current_size_list[pos])

                if(item.dType.equals(Constant.CONTENT_WIDGET_NEWS_CRAWL)){
                    // crowling
                    var builder  = StringBuilder()
                    if(!item.src.equals("")){
                        val inputStream: InputStream = response.data!!.byteInputStream()
                        var list = DataParsing.parse(inputStream)
                        if(list!=null && list.size>0){
                            for (i in list){
                                builder.append(i.title)
                                builder.append("        ")
                            }
                        }
                    }else{
                        var being_news = Gson().fromJson(response.data, BeingNewsData::class.java)
                        if(being_news!=null && being_news.news!=null && being_news.news.size>0){
                            for (i in being_news.news){
                                builder.append(i.title)
                                builder.append("        ")
                            }
                        }
                    }
                    layout_list[pos].relative_layout?.removeAllViews()
                    layout_list[pos].relative_layout?.
                    addView(WidgetNewsCrowling.getWidgetNewsCrowling(this,item,builder.toString()),item.frame_w,item.frame_h)
                }else{
                    // listview
                    if(!item.src.equals("")){
                        val inputStream: InputStream = response.data!!.byteInputStream()
                        var list = DataParsing.parse(inputStream)
                        setAllNewsListRotation(pos,list,item)
//                        if(list!=null && list.size>0){
//                            layout_list[pos].relative_layout?.removeAllViews()
//                            layout_list[pos].relative_layout?.
//                            addView(WidgetNewsList.getWidgetNewsListAll(this,item,list),item.frame_w,item.frame_h)
//                        }
                    }else{
                        // being news
                        var being_news = Gson().fromJson(response.data,BeingNewsData::class.java)
                        setBeingNewsListRotation(pos,being_news.news,item)
                    }
                }
            }
        })

        // text api
        viewModel.text_api_result.observe(this, Observer {
                response ->
            if(response.status == Status.SUCCESS){
                var pos = response.pos
                var item = multiframe_items[pos].get(current_size_list[pos])
                var layout = layout_list[pos].relative_layout
                layout?.removeAllViews()
                Log.d(TAG, "initObserver: item id - ${item.id}")
                if(item.dType.equals(Constant.TEXT_CROWLING))
                    layout?.addView(WidgetText.getWidgetTextCrowling(this,item,response.data),item.frame_w,item.frame_h)
                else
                    setStaticTextWithRotate(pos,item,response.data,0)
//                10286-2694-41659
            }
        })

        // file downloading observer
        viewModel.download_file_result.observe(this, Observer { response ->
            if(response.status == Status.SUCCESS){
                changeContent()
            }
        })

        // vimeo
        vimeoViewModel.vimeo_api_result.observe(this){ response->
            val mediaItem = MediaItem.fromUri(response?.url!!)
            layout_list[response.pos].relative_layout?.addView(VimeoWidget.getVimeoWidget(this,layout_list[response.pos].exoPlayer!!))
//            binding.rlCodContent?.addView()
            layout_list[response.pos].exoPlayer!!.setMediaItem(mediaItem)
            layout_list[response.pos].exoPlayer?.play()
        }

    }

    private fun changeContent() {

        if(!pref!!.getBooleanData(MySharePrefernce.KEY_DEVICE_REGISTERED)) {
            showNotRegisterDialog()
            return
        }

        var list = DataParsing.getDownloableList(pref)
        if(list.size>0 && isConnected){
            downloading = 1
            binding.rlDownloading.visibility = View.VISIBLE
            viewModel.downloadFile(list[0])
            ImageUtil.loadGifImage(this,binding.ivDownloading)
        }else{
            binding.rlDownloading.visibility = View.GONE
            downloading = 0
            removeData()
            var is_frames = DataParsing.isFrameAvailable(pref)
//            var is_override = DataParsing.isOverrideAvailable(pref)
            if(is_frames) contentPlaying(is_frames)
            else loadWaiting()
        }
    }

    fun removeData() {
        play_activate = false
        binding.rootLayout.visibility = View.GONE
        binding.rootLayout.removeAllViews()
        items = mutableListOf()
        multiframe_items = mutableListOf()
        current_size_list = mutableListOf()
        if(layout_list!=null && layout_list.size>0){
            for(i in 0..layout_list.size-1){
                if(layout_list[i].exoPlayer != null && layout_list[i].exoPlayer!!.isPlaying){
                    layout_list[i].exoPlayer!!.release()
                    layout_list[i].exoPlayer = null
                }
                if(layout_list[i].exoPlayerView?.player!=null && layout_list[i].exoPlayerView?.player!!.isPlaying){
                    layout_list[i].exoPlayerView?.player?.stop()
                }
                if(layout_list[i].job != null) layout_list[i].job?.cancel()
            }
        }
        layout_list = mutableListOf()
    }

    @JvmName("getDownloading1")
    public fun getDownloading(): Int {
      return downloading
    }

    private fun contentPlaying(is_frames: Boolean) {

/*
        if(is_override){
            // create single frame for override
            var frame = DataParsing.getLayoutFrame(ctx!!,all_frames.get(i),i)
            var videoView = VideoView(this)
            var exoPlayerView  = StyledPlayerView(ctx)

            // add frame in layout list
            layout_list.add(NewLayoutView(frame,videoView,exoPlayerView,null,false,"",null,null))    // add frame in list
            binding.rootLayout.addView(frame)   // attach frame in root
            binding.rootLayout.setBackgroundColor(Color.parseColor(DataParsing.getRootBackground(pref)))    // set bg for root

            // add multi-fame items
            if (all_frames.get(i).item != null && all_frames.get(i).item.size > 0) {
                var child_items: MutableList<Item> = mutableListOf()
                var items_array = all_frames.get(i).item
                for (j in 0..items_array.size - 1) {
                    var item = items_array[j]
                    item.frame_h = all_frames.get(i).h
                    item.frame_w = all_frames.get(i).w
                    items.add(item)
                    items.get(items.size-1).pos = i
                    child_items.add(item)
                }
                multiframe_items?.add(child_items)
                current_size_list.add(0)
            }else {
                multiframe_items?.add(mutableListOf())
                current_size_list.add(0)
            }
            // start playing
            startPlayingContent()
        }else
*/
        if(is_frames){
            var all_frames = DataParsing.getFilterdFrames(pref)
            for (i in 0..all_frames!!.size-1) {
                // create availble frames with bg
                var frame = DataParsing.getLayoutFrame(ctx!!,all_frames.get(i),i)
                var videoView = VideoView(this)
                var exoPlayerView  = StyledPlayerView(ctx)

                // add frame in layout list
                layout_list.add(NewLayoutView(frame,videoView,exoPlayerView,null,false,"",null,null))    // add frame in list
                binding.rootLayout.addView(frame)   // attach frame in root
                binding.rootLayout.setBackgroundColor(Color.parseColor(DataParsing.getRootBackground(pref)))    // set bg for root

                // add multi-fame items
                if (all_frames.get(i).item != null && all_frames.get(i).item.size > 0) {
                    var child_items: MutableList<Item> = mutableListOf()
                    var items_array = all_frames.get(i).item
                    for (j in 0..items_array.size - 1) {
                        var item = items_array[j]
                        item.frame_h = all_frames.get(i).h
                        item.frame_w = all_frames.get(i).w
                        items.add(item)
                        items.get(items.size-1).pos = i
                        child_items.add(item)
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


    private fun setAllNewsListRotation(pos: Int, list: List<RssItem>, item: Item) {
        if(list!=null && list.size>0){
            var adapter = NewsAdapter(list,item,ctx!!)
            layout_list[pos].relative_layout?.removeAllViews()
            layout_list[pos].relative_layout?.
            addView(getWidgetNewsListAll(this,item,list,adapter),item.frame_w,item.frame_h)
            if(DataParsing.getSettingRotate(item)!=null && DataParsing.getSettingRotate(item) != 0){
                layout_list[pos].rotate_job = CoroutineScope(Dispatchers.IO).launch {
                    delay(TimeUnit.SECONDS.toMillis(DataParsing.getSettingRotate(item).toLong()))
                    withContext(Dispatchers.Main) {
                        if(play_activate){
                            var newlist = ArrayList<RssItem>()
                            var position = adapter.getlastIndex()+pos
                            Log.d("TAG", "list pos : $position")

                            if(position == list.size-1)position=0
                            list.forEachIndexed{
                                    index,item -> if(index>=position)newlist.add(item)
                            }
                            adapter.updateAdapter(newlist)
                            if(layout_list[pos].active_widget.equals(Constant.CONTENT_WIDGET_NEWS)){
                                setAllNewsListRotation(pos,newlist,item)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setBeingNewsListRotation(pos: Int, list: ArrayList<News>, item: Item) {
        if(list!=null && list.size>0){
            var adapter = BeingNewsAdapter(list,item,ctx!!)
            layout_list[pos].relative_layout?.removeAllViews()
            layout_list[pos].relative_layout?.
            addView(WidgetNewsList.getWidgetNewsListBeing(this,item,list,adapter),item.frame_w,item.frame_h)
            if(DataParsing.getSettingRotate(item)!=null && DataParsing.getSettingRotate(item) != 0){
                layout_list[pos].rotate_job = CoroutineScope(Dispatchers.IO).launch {
                    delay(TimeUnit.SECONDS.toMillis(DataParsing.getSettingRotate(item).toLong()))
                    withContext(Dispatchers.Main) {
                        if(play_activate){
                            var newlist = ArrayList<News>()
                            var position = adapter.getlastIndex()+pos
                            Log.d("TAG", "list pos : $position")

                            if(position == list.size-1)position=0
                            list.forEachIndexed{
                                    index,item -> if(index>=position)newlist.add(item)
                            }
                            adapter.updateAdapter(newlist)
                            if(layout_list[pos].active_widget.equals(Constant.CONTENT_WIDGET_NEWS)){
                                setBeingNewsListRotation(pos,newlist,item)
                            }
                        }
                    }
                }
            }
        }
    }

    fun loadWaiting() {
        if(layout_list!=null &&layout_list.size>0){
            for (i in 0..layout_list.size-1)
                layout_list[i].relative_layout?.visibility = View.GONE
        }
        binding.rlBackground.visibility = View.VISIBLE
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

        Log.d(TAG, "validateItem item type : ${item.type} ${item.duration} ${DateTimeUtil.getTimeSeconds()}")
        if(
            item.type == Constant.CONTENT_IMAGE ||
            item.type == Constant.CONTENT_VIDEO ||
            item.type == Constant.CONTENT_VECTOR ||
            item.type == Constant.CONTENT_POWERPOINT||
            item.type == Constant.CONTENT_WORD ||
            item.type == Constant.CONTENT_WEB ||
            item.type == Constant.CONTENT_WIDGET_GOOGLE ||
            item.type == Constant.CONTENT_WIDGET_DATE_TIME ||
            item.type == Constant.CONTENT_WIDGET_POWER ||
            item.type == Constant.CONTENT_WIDGET_QUOTES ||
            item.type == Constant.CONTENT_WIDGET_TRAFFIC ||
            item.type == Constant.CONTENT_WIDGET_YOUTUBE ||
            item.type == Constant.CONTENT_WIDGET_VIMEO ||
            item.type == Constant.CONTENT_WIDGET_NEWS ||
            item.type == Constant.CONTENT_WIDGET_TEXT ||
            item.type == Constant.CONTENT_WIDGET_COD ||
            item.type == Constant.CONTENT_WIDGET_IFRAME ||
            item.type == Constant.CONTENT_WIDGET_LIVESTREAM ||
//            item.type == Constant.CONTENT_WIDGET_WEATHER ||
            item.type == Constant.CONTENT_WIDGET_QRCODE
        ) setWidget(position,item)
    }

    private fun setWidget(pos: Int, item: Item){

        var start_time = pref?.getStartTime()

        play_activate = true
        binding.rlBackground.visibility = View.GONE
        var layout = layout_list[pos].relative_layout
        var video = layout_list[pos].videoView
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
            layout?.removeAllViews()
            layout?.addView(ImageWidget.getImageWidget(this,item.frame_w,item.frame_h,item.fileName,item.filesize))
        }
        // QR-Code
        if(widget_type.equals(Constant.CONTENT_WIDGET_QRCODE)){
            layout?.removeAllViews()
            layout?.addView(ImageWidget.getImageWidget(this,item.frame_w,item.frame_h,item.fileName,item.filesize))
        }
        // video
        if(widget_type.equals(Constant.CONTENT_VIDEO)){
            layout?.removeAllViews()
            layout?.addView(WidgetVideo.getWidgetVideo(this,video!!,item.frame_w,item.frame_h,item.fileName,item.sound,Constant.CALLING_MAIN))
        }
        // webview
        if(widget_type == Constant.CONTENT_WEB || widget_type == Constant.CONTENT_WIDGET_GOOGLE){
            setWebViewWithReload(pos,item)
        }
        // iFrame
        if(widget_type == Constant.CONTENT_WIDGET_IFRAME){
            layout?.removeAllViews()
            layout?.addView(WebViewWidget.getiFrameWidget(this,item?.ifr!!))
        }
        // youtube
        if(widget_type == Constant.CONTENT_WIDGET_YOUTUBE) {
            layout?.removeAllViews()
            layout?.addView(YouTubeWidget.getYouTubeWidget(this,item),item.frame_w,item.frame_h)
        }
        // vimeo
        if(widget_type == Constant.CONTENT_WIDGET_VIMEO){
            layout?.removeAllViews()
            var player = WidgetExoPlayer.getExoPlayer(this,Constant.PLAYER_SLIDE,item?.sound!!)
            layout_list[pos].exoPlayer = player
            exoPlayer = player
            vimeoViewModel.getVimeoUrl(item.src,item,pos,vimeo_retro)
        }
        // power BI
        if(widget_type == Constant.CONTENT_WIDGET_POWER){
            layout?.removeAllViews()
            layout?.addView(WidgetPowerBI.getWidgetPowerBI(this,item),item.frame_w,item.frame_h)
        }
        // traffic
        if(widget_type == Constant.CONTENT_WIDGET_TRAFFIC){
            layout?.removeAllViews()
            layout?.addView(WidgetTraffic.getWidgetTrafic(this,item.id),item.frame_w,item.frame_h)
        }
        // date time
        if(widget_type == Constant.CONTENT_WIDGET_DATE_TIME){
            layout?.removeAllViews()
            layout?.addView(DateTimeWidget.getDateTimeWidget(this,item),item.frame_w,item.frame_h)
        }
        // quotes
        if(widget_type == Constant.CONTENT_WIDGET_QUOTES)
            viewModel.getQuoteText(item.id.split("-")[2],pos)
        // text
        if(widget_type == Constant.CONTENT_WIDGET_TEXT)
            viewModel.getText(item.id.split("-")[2],pos)
        // news
        if(widget_type == Constant.CONTENT_WIDGET_NEWS){
            if(item.src.equals(""))
                viewModel.getNews(Constant.API_WIDGET_BEING_NEWS +item.id.split("-")[2],pos)
            else
                viewModel.getNews(item.src,pos)
        }
        // live streaming
        if(item?.type.equals(Constant.CONTENT_WIDGET_LIVESTREAM)){
            layout?.removeAllViews()
            if(item?.dType.equals("yt"))
                layout?.addView(YouTubeWidget.getYouTubeWidgetLiveStream(this,item),item.frame_w,item.frame_h)
            else
                layout?.addView(WidgetExoPlayer.setExoPLayer(this,item,exoPlayerView),item.frame_w,item.frame_h)
        }
        // COD BUTTON
        if(widget_type == Constant.CONTENT_WIDGET_COD){
            layout?.removeAllViews()
            var settings = Gson().fromJson(item.settings, com.app.lsquared.model.cod.Settings::class.java)
            layout?.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(settings?.bg!!,settings?.bga!!)))
            layout?.addView(WidgetCodButton.getWidgetCodButton(this,item,pref))
        }
        // weather
//        if(widget_type == Constant.CONTENT_WIDGET_WEATHER)
//            setWatherFragment(pos,item)

        if(multiframe_items[pos].size>1){
            var job = CoroutineScope(Dispatchers.IO).launch {
                delay(TimeUnit.SECONDS.toMillis(item.duration.toLong()))
                withContext(Dispatchers.Main) {
//                    if(exoPlayerView.player != null && exoPlayerView.player!!.isPlaying) exoPlayerView.player?.stop()
                    if(exoPlayer != null && exoPlayer!!.isPlaying){
                        exoPlayer!!.release()
                        exoPlayer = null
                    }
                    if(play_activate){
                        pref?.createReport(item.id,item.duration,start_time!!)
                        current_size_list[pos] = current_size_list[pos]+1
                        nextPlay(pos)
                    }
                }
            }
            if(layout_list[pos].job == null){
                layout_list[pos].job = job
            }else{
                layout_list[pos].job?.cancel()
                if(layout_list[pos].rotate_job != null)layout_list[pos].rotate_job?.cancel()
                layout_list[pos].job = job
            }
        }

    }

    // web view with reloading
    private fun setWebViewWithReload(pos: Int, item: Item) {

        var layout = layout_list[pos].relative_layout
        if(layout_list[pos].active_widget.equals(Constant.CONTENT_WEB)){
            layout?.removeAllViews()
            layout?.addView(WebViewWidget.getWebViewWidget(this,item.src),item.frame_w,item.frame_h)
            if(DataParsing.getWebInterval(item) !=0 && DataParsing.getWebInterval(item) < item.duration){
                layout_list[pos].rotate_job = CoroutineScope(Dispatchers.IO).launch {
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

        var layout = layout_list[pos].relative_layout
        if(layout_list[pos].active_widget.equals(Constant.CONTENT_WIDGET_TEXT)){
            layout?.removeAllViews()

            var list = mutableListOf<String>()
            var json_data = JSONObject(data)
            var channel_array = json_data.getJSONArray("channel")
            for (i in 0 until channel_array.length()){
                var title_obj = channel_array.getJSONObject(i).getString("title")
                list.add(title_obj)
            }
            layout?.addView(WidgetText.getWidgetTextStatic(this,item,list[text_pos]),item.frame_w,item.frame_h)
            if (item.dType.equals(Constant.TEXT_STATIC)){
                layout_list[pos].rotate_job = CoroutineScope(Dispatchers.IO).launch {
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
        }
    }


    private fun setWatherFragment(pos: Int, item: Item) {
        var setting_obj = JSONObject(item.settings)
        var template = setting_obj.getString("template")
        var lang = setting_obj.getString("lang")
        var orientation = setting_obj.getString("orientation")
        var forecast = item.forecast

        var layout = layout_list[pos].relative_layout

        layout?.removeAllViews()

        if(forecast == Constant.TEMPLATE_WEATHER_CURRENT_DATE){
            if(template.equals(Constant.TEMPLATE_TIME_T1))
                layout?.addView(WidgetWeather.getWidgetWeatherCurremtTemp1(this,item),item.frame_w,item.frame_h)
            if(template.equals(Constant.TEMPLATE_TIME_T2))
                layout?.addView(WidgetWeather.getWidgetWeatherCurremtTemp2(this,item),item.frame_w,item.frame_h)
            if(template.equals(Constant.TEMPLATE_TIME_T3))
                layout?.addView(WidgetWeather.getWidgetWeatherCurremtTemp3(this,item),item.frame_w,item.frame_h)
        }
        if(forecast == Constant.TEMPLATE_WEATHER_FIVE_DAY){
            if(orientation.equals(Constant.TEMPLATE_WEATHER_ORIENTATION_VERTICAL))
                layout?.addView(WidgetWeather.getWidgetWeatherFiveDayVerti(this,item),item.frame_w,item.frame_h)
            else
                layout?.addView(WidgetWeather.getWidgetWeatherFiveDayHori(this,item),item.frame_w,item.frame_h)
        }
        if(forecast == Constant.TEMPLATE_WEATHER_FOUR_DAY){
            if(orientation.equals(Constant.TEMPLATE_WEATHER_ORIENTATION_VERTICAL))
                layout?.addView(WidgetWeather.getWidgetWeatherFourDayVerti(this,item),item.frame_w,item.frame_h)
            else
                layout?.addView(WidgetWeather.getWidgetWeatherFourDayHori(this,item),item.frame_w,item.frame_h)
        }
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
        finishAffinity()
    }

    override fun onPause() {
        super.onPause()
        play_activate = false

        checkVersionHandler.removeCallbacks(versionTask)
        screenshot_handler.removeCallbacks(ssTask)
        temp_handler.removeCallbacks(tempTask)
        report_handler.removeCallbacks(reportTask)
        removeData()
    }

    private fun checkDeviceVersion() {
        if(packageManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,getPackageName())==PackageManager.PERMISSION_GRANTED)
            if(packageManager.checkPermission(Manifest.permission.READ_PHONE_STATE,getPackageName())==PackageManager.PERMISSION_GRANTED){
                if(isConnected)
                    if(downloading==0){
                        freeMemory()
                        viewModel.isDeviceRegistered(this,DeviceInfo.getDeviceId(this,pref))
                    }
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

    // toast & snack bar
    fun showSnackBar(msg: Int) {
        Snackbar.make(
            findViewById<View>(android.R.id.content),
            getString(msg),
            Snackbar.LENGTH_LONG
        ).show()
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
            101 ->{
            }
            100 ->{
            }
        }
    }


    fun deviceNotRegistered() {
        Log.d(TAG, "deviceNotRegistered: success")
        play_activate = false
        removeData()
        binding.rlBackground.visibility = View.VISIBLE
        pref?.putBooleanData(MySharePrefernce.KEY_DEVICE_REGISTERED,false)
        pref?.putStringData(MySharePrefernce.KEY_DEVICE_REGISTERED_ID,"")
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
//        pref.putStringData(MySharePrefernce.KEY_DEVICE_REGISTERED_ID,device_id)
        viewModel.registerNewDevce(this,pref,device_id)
    }

}
