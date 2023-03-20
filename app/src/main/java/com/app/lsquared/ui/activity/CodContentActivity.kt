package com.app.lsquared.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import com.app.lsquared.databinding.ActivityCodContentBinding
import com.app.lsquared.model.Caption
import com.app.lsquared.model.Cat
import com.app.lsquared.model.Content
import com.app.lsquared.network.isConnected
import com.app.lsquared.ui.MainViewModel
import com.app.lsquared.ui.viewmodel.CodViewModel
import com.app.lsquared.ui.widgets.*
import com.app.lsquared.utils.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable
import retrofit2.Retrofit
import java.io.File
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class CodContentActivity : AppCompatActivity() {

    val TAG = "CodContentActivity"

    private lateinit var binding : ActivityCodContentBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var vimeoViewModel: CodViewModel

    var player: SimpleExoPlayer? = null
    var playerView: StyledPlayerView? = null

    @Inject
    lateinit var myPerf: MySharePrefernce
    @Inject
    lateinit var vimeo_retro: Retrofit


    var item : Content? = null
    var item_pos = 0
    var image_list : ArrayList<Content>? = null
    var itemcate : Cat? = null

    lateinit var checkVersionHandler: Handler
    lateinit var screenshot_handler: Handler

    private val versionTask = object : Runnable {
        override fun run() {
            checkDeviceVersion()
            checkVersionHandler.postDelayed(this, viewModel.delay.toLong())
        }
    }

    private val ssTask = object : Runnable {
        override fun run() {
            var file = ImageUtil.screenshot(binding.rootCodContent, "Screen_final_" + Utility.getCurrentdate())
            if(file!=null) submitScreen(file)
            screenshot_handler.postDelayed(this, viewModel.screen_delay* 1000.toLong())
        }
    }

    companion object{
        const val EXTRA_ITEM_DATA = "item_data"
        const val EXTRA_ITEM_DATA_ARRAY = "item_data_array"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initXml()
        initObserver()
        setContent()
    }

    private fun initXml() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        window.decorView.systemUiVisibility = flags

        binding = ActivityCodContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init view model
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        vimeoViewModel = ViewModelProvider(this).get(CodViewModel::class.java)
        viewModel.internet = isConnected
        viewModel.is_device_registered = true

        item = intent.getSerializableExtra(EXTRA_ITEM_DATA) as Content?
        if(item?.type.equals(Constant.CONTENT_WORD)||item?.type.equals(Constant.CONTENT_POWERPOINT)
            ||item?.type.equals(Constant.CONTENT_VECTOR)){
            itemcate = intent.getSerializableExtra(EXTRA_ITEM_DATA_ARRAY) as Cat?
        }
        // click
        binding.ivCodContentClose.setOnClickListener { finish() }
        binding.ivCodContentCaption.setOnClickListener {
            showCaptionDialog(item!!.caption)
        }

        checkVersionHandler = Handler(Looper.getMainLooper())
        screenshot_handler = Handler(Looper.getMainLooper())

        //
        binding.ivNext.setOnClickListener{
            if(item_pos<image_list!!.size-1){
                binding.ivPrev.visibility = View.VISIBLE
                item_pos = item_pos+1
                setImageList()
            }
            if(item_pos==image_list!!.size-1) binding.ivNext.visibility = View.GONE
        }
        binding.ivPrev.setOnClickListener{
            if(item_pos!=0){
                binding.ivNext.visibility = View.VISIBLE
                item_pos = item_pos-1
                setImageList()
            }
            if(item_pos==0) binding.ivPrev.visibility = View.GONE

        }
    }

    private fun initObserver() {
        if(item?.type.equals(Constant.CONTENT_VIDEO) || item?.type.equals(Constant.CONTENT_WIDGET_VIMEO)
            || item?.type.equals(Constant.CONTENT_WIDGET_LIVESTREAM)
        ){
            player = WidgetExoPlayer.getExoPlayer(this,Constant.PLAYER_COD,item?.sound!!)
            binding.rlCodContent?.addView(VimeoWidget.getVimeoWidget(this,player!!))
        }
        vimeoViewModel.vimeo_api_result.observe(this){ response->
            val mediaItem = MediaItem.fromUri(response?.url!!)
            player!!.setMediaItem(mediaItem)
            player?.play()
        }
    }

    private fun setContent() {
        if(item==null)return

        var width = DeviceInfo.getScreenWidth(this)
        var height = DeviceInfo.getScreenHeight(this)

        Log.d(TAG, "setContent: widget - ${item?.type}")
        // image
        if(item!!.type.equals(Constant.CONTENT_IMAGE))
            binding.rlCodContent.addView(ImageWidget.getImageWidget(this,width,height,item!!.fileName!!,item!!.filesize!!))
        // video
        if(item?.type.equals(Constant.CONTENT_VIDEO)){
            playerView = WidgetExoPlayer.getExoPlayerView(this)
            binding.rlCodContent?.addView(WidgetExoPlayer.setExoPLayer(this,item!!,playerView!!))
        }
        /// webview
        if(item?.type.equals(Constant.CONTENT_WEB)|| item?.type.equals(Constant.CONTENT_WIDGET_GOOGLE))
            setWebView(item?.src)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                binding.rlCodContent.addView(WebViewChromium.getWebChromeWidget(this,item?.src!!))
//            else
//                binding.rlCodContent.addView(WebViewWidget.getWebViewWidget(this,item?.src!!))
        /// iframe
        if(item?.type.equals(Constant.CONTENT_WIDGET_IFRAME))
            binding.rlCodContent.addView(WebViewWidget.getiFrameWidget(this,item?.ifr!!),width,height)
        // power bi
        if(item?.type.equals(Constant.CONTENT_WIDGET_POWER))
            binding.rlCodContent.addView(WidgetPowerBI.getWidgetPowerBI(this,item,width,height))
        // live streaming - youtube
        if(item?.type.equals(Constant.CONTENT_WIDGET_LIVESTREAM) && item?.dType.equals("yt")){
            startActivity(Intent(this,YouTubeActivity::class.java).putExtra("data",item))
            finish()
        }
        // live streaming - RTSP, HLS, HTTPS
        if(item?.type.equals(Constant.CONTENT_WIDGET_LIVESTREAM) && !item?.dType.equals("yt")){
            playerView = WidgetExoPlayer.getExoPlayerView(this)
            binding.rlCodContent?.addView(WidgetExoPlayer.setExoPLayer(this,item!!,playerView!!))
        }
        // vimeo
        if(item?.type.equals(Constant.CONTENT_WIDGET_VIMEO))
            vimeoViewModel.getVimeoUrl(item?.src!!,null,0,vimeo_retro)
        // youtube
        if(item?.type.equals(Constant.CONTENT_WIDGET_YOUTUBE)) {
            startActivity(Intent(this,YouTubeActivity::class.java).putExtra("data",item))
            finish()
        }
        // powerpoint, word, vector
        if(item?.type.equals(Constant.CONTENT_WORD)||item?.type.equals(Constant.CONTENT_VECTOR)||item?.type.equals(Constant.CONTENT_POWERPOINT)) {
            image_list = getItemGrouping()
            setImageList()
        }

    }

    private fun setImageList() {
        item = image_list!![item_pos]
        if(image_list?.size!! > 1){
            binding.ivNext.visibility = View.VISIBLE
        }
        binding.rlCodContent.removeAllViews()
        binding.rlCodContent.addView(ImageWidget.getImageWidget(this,DeviceInfo.getScreenWidth(this),DeviceInfo.getScreenHeight(this),item!!.fileName!!,item!!.filesize!!))
    }

    fun getItemGrouping(): ArrayList<Content> {
        var list = ArrayList<Content>()
        for (cont in itemcate!!.content) if(item?.label.equals(cont.label)) list.add(cont)
        return list
    }

    private fun checkDeviceVersion() {
        viewModel.isDeviceRegistered(this,DeviceInfo.getDeviceId(this,myPerf))
    }

    private fun submitScreen(file: File) {
        viewModel.submitScreenShot(
            Utility.getScreenshotJson(
                DeviceInfo.getDeviceId(this,myPerf),
                Utility.getFileToByte(file?.absolutePath)
            )
        )
    }

    private fun showCaptionDialog(caption: ArrayList<Caption>) {
        val popupMenu = PopupMenu(this, binding.ivCodContentCaption)
        for (item in caption){
            popupMenu.getMenu().add(item.ccl)
        }
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            Toast.makeText(this, item?.title, Toast.LENGTH_SHORT).show()
            true
        })
        popupMenu.show()
    }

    private fun releasePlayer() {
        if(playerView!=null){
            if (playerView?.player!=null){
                playerView?.player!!.release()
                playerView?.player = null
            }
        }
        if (player != null) {
            player!!.release()
            player = null
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.internet = isConnected
        viewModel.is_device_registered = true
        viewModel.screen_delay = myPerf.getIntData(MySharePrefernce.KEY_SCREENSHOT_INTERVAL)

        checkVersionHandler.post(versionTask)
        screenshot_handler.post(ssTask)
    }

    override fun onPause() {
        super.onPause()
        checkVersionHandler.removeCallbacks(versionTask)
        screenshot_handler.removeCallbacks(ssTask)
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.wvCodContent.removeAllViews()
    }

    fun setWebView(src: String?) {
        Log.d(TAG, "setWebView: src = $src")
        binding.wvCodContent.visibility = View.VISIBLE
        val webView = binding.wvCodContent
        WebViewChromium.setUpWebViewDefaults(webView)
        webView.loadUrl(src!!)

        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                Log.d("TAG", "onPermissionRequest")
                runOnUiThread {
                    if (request.origin.toString() == src) {
                        request.grant(request.resources)
                    } else {
                        request.deny()
                    }
                }
            }
        }

    }


}
