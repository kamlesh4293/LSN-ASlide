package com.app.lsquared.ui.activity

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import com.androidnetworking.common.ANConstants.MAX_CACHE_SIZE
import com.app.lsquared.databinding.ActivityCodContentBinding
import com.app.lsquared.model.Caption
import com.app.lsquared.model.Content
import com.app.lsquared.model.Item
import com.app.lsquared.network.isConnected
import com.app.lsquared.ui.MainViewModel
import com.app.lsquared.ui.widgets.*
import com.app.lsquared.utils.*
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Format.NO_VALUE
import com.google.android.exoplayer2.Format.OFFSET_SAMPLE_RELATIVE
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class CodContentActivity : AppCompatActivity(), ExoPlayer.EventListener {

    private lateinit var binding : ActivityCodContentBinding
    private lateinit var viewModel: MainViewModel
    var job: Job? = null

    @Inject
    lateinit var myPerf: MySharePrefernce

    // for screenshot
    var screenshot_handler: Handler = Handler()
    var screenshot_runnable: java.lang.Runnable? = null

    var item : Content? = null

    companion object{
        const val EXTRA_ITEM_DATA = "item_data"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initXml()
        setContent()
    }

    private fun setContent() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

        if(item==null)return

        Log.d("TAG", "setContent: widget - ${item?.type}")
        // image
        if(item!!.type.equals(Constant.CONTENT_IMAGE))
            binding.rlCodContent.addView(ImageWidget.getImageWidget(this,width,height,item!!.fileName!!,item!!.filesize!!))
        // video
        if(item?.type.equals(Constant.CONTENT_VIDEO))
            initializePlayer(item!!)
        /// webview
        if(item?.type.equals(Constant.CONTENT_WEB)|| item?.type.equals(Constant.CONTENT_WIDGET_GOOGLE))
            binding.rlCodContent.addView(WebViewWidget.getWebViewWidget(this,item?.src!!),width,height)
        // power bi
        if(item?.type.equals(Constant.CONTENT_WIDGET_POWER))
            binding.rlCodContent.addView(WidgetPowerBI.getWidgetPowerBI(this,item,width,height))
        // vimeo
        if(item?.type.equals(Constant.CONTENT_WIDGET_VIMEO)) playVimeo()
        // youtube
        if(item?.type.equals(Constant.CONTENT_WIDGET_YOUTUBE)) playYoutube()

    }

    fun playYoutube(){

        binding.rlCodContent.visibility = View.GONE
        binding.wvCodContent.visibility = View.VISIBLE

        binding.wvCodContent.setWebChromeClient(WebChromeClient())
        binding.wvCodContent.getSettings().setJavaScriptEnabled(true)

        binding.wvCodContent.getSettings()?.setMediaPlaybackRequiresUserGesture(false)
        binding.wvCodContent.getSettings()?.setUserAgentString("1");//for desktop 1 or mobile 0.

        var mute = "0"
        if (item?.sound=="no") mute ="1" else mute = "0"

        var setting_obj = JSONObject(item?.settings)
        var cc = setting_obj.getBoolean("cc")
        try {
            if(item?.params != null && item?.params.equals(""))
                binding.wvCodContent.loadUrl("${Constant.BASE_URL_YOUTUBE}${item?.src}?autoplay=1&rel=0&loop=1&cc_load_policy=$cc&mute=$mute&controls=0&enablejsapi=1")
            else
                binding.wvCodContent.loadUrl("${Constant.BASE_URL_YOUTUBE}${item?.src}?autoplay=1&rel=0&loop=1&cc_load_policy=$cc&mute=$mute&controls=0&enablejsapi=1${item?.params}")
        } catch (e: java.lang.Exception) {
            Log.w("TAG", "Exception : ", e)
        }
    }


    private fun playVimeo() {
        binding.rlCodContent.visibility = View.GONE
        binding.wvCodContent.visibility = View.VISIBLE

        binding.wvCodContent.setWebChromeClient(WebChromeClient())
        binding.wvCodContent.getSettings().setJavaScriptEnabled(true)

        val webSettings: WebSettings = binding.wvCodContent.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.setAppCacheEnabled(true)
        webSettings.mediaPlaybackRequiresUserGesture = false

        var mute = "0"
        if (item?.sound=="no") mute ="1" else mute = "0"

        var url = "${Constant.BASE_URL_VIMEO}${item?.src}"+"?autoplay=1&loop=1&muted=$mute"

        Log.d("TAG", "setContent: $url")
        binding.wvCodContent.loadUrl(url)
    }

    private fun initXml() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = ActivityCodContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init view model
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        item = intent.getSerializableExtra(EXTRA_ITEM_DATA) as Content?
        // click
        binding.ivCodContentClose.setOnClickListener { finish() }
        binding.ivCodContentCaption.setOnClickListener {
            showCaptionDialog(item!!.caption)
        }
    }

    private fun scheduleTimeOut(item: Content?) {
        job = CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(item?.duration!!.toLong()))
            withContext(Dispatchers.Main) {
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(job!=null)job!!.cancel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.internet = isConnected
        viewModel.is_device_registered = true
        viewModel.screen_delay = myPerf.getIntData(MySharePrefernce.KEY_SCREENSHOT_INTERVAL)

        // screen shot
        screenshot_handler.postDelayed(Runnable {
            screenshot_handler.postDelayed(
                screenshot_runnable!!,
                viewModel.screen_delay * 1000.toLong()
            )
            var file = ImageUtil.screenshot(binding.rootCodContent, "Screen_final_" + Utility.getCurrentdate())
            if(file!=null)
                viewModel.submitScreenShot(
                    Utility.getScreenshotJson(
                        DeviceInfo.getDeviceId(this),
                        Utility.getFileToByte(file?.absolutePath)
                    )
                )
        }.also { screenshot_runnable = it },
            viewModel.screen_delay * 1000.toLong())
    }

    override fun onPause() {
        super.onPause()
        screenshot_handler.removeCallbacks(screenshot_runnable!!)
    }

    private fun initializePlayer(item: Content) {
        binding.idExoPlayerView.visibility = View.VISIBLE

        if(item.caption!=null && item.caption.size>0){
            binding.ivCodContentCaption.visibility = View.VISIBLE
        }

        val player = ExoPlayerFactory.newSimpleInstance(
            DefaultRenderersFactory(this),
            DefaultTrackSelector(), DefaultLoadControl()
        )
//        var filename = item.fileName?.replace("1602585267.1767-mailbox-and-other-services.mp4","1602585267.1767-mailbox-and-other-services123.mp4")
        var filename = item.fileName
        val path = DataManager.getDirectory()+ File.separator+ filename
        var file = File(path)
        val uri = Uri.fromFile(file)
        val audioSource = ExtractorMediaSource(
            uri,
            DefaultDataSourceFactory(this, "MyExoplayer"),
            DefaultExtractorsFactory(),
            null,
            null
        )


        player.prepare(audioSource)
        binding.idExoPlayerView.setPlayer(player)
        binding.idExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)
        player.addListener(this)
        player.playWhenReady = true
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {   }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when(playbackState){
            ExoPlayer.STATE_ENDED->{
                finish()
            }
        }
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
    }

    override fun onPositionDiscontinuity() {
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
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


}
