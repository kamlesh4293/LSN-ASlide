package com.app.lsquared.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import com.app.lsquared.databinding.ActivityCodContentBinding
import com.app.lsquared.model.Caption
import com.app.lsquared.model.Content
import com.app.lsquared.network.isConnected
import com.app.lsquared.ui.MainViewModel
import com.app.lsquared.ui.widgets.*
import com.app.lsquared.utils.*
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable
import org.json.JSONObject
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class CodContentActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCodContentBinding
    private lateinit var viewModel: MainViewModel

    var player: SimpleExoPlayer? = null
    var exoPlayer: StyledPlayerView? = null

    @Inject
    lateinit var myPerf: MySharePrefernce


    var item : Content? = null

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
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initXml()
        setContent()

        checkVersionHandler = Handler(Looper.getMainLooper())
        screenshot_handler = Handler(Looper.getMainLooper())
    }

    private fun checkDeviceVersion() {
        viewModel.isDeviceRegistered(this)
    }

    private fun submitScreen(file: File) {
        viewModel.submitScreenShot(
            Utility.getScreenshotJson(
                DeviceInfo.getDeviceId(this),
                Utility.getFileToByte(file?.absolutePath)
            )
        )
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
        if(item?.type.equals(Constant.CONTENT_VIDEO)){
//            setUpPlayer()

            exoPlayer = StyledPlayerView(this)
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
            exoPlayer?.layoutParams = params
            binding.rlCodContent?.addView(WidgetExoPlayer.getExoPLayer(this,item!!,exoPlayer!!))
        }
        /// webview
        if(item?.type.equals(Constant.CONTENT_WEB)|| item?.type.equals(Constant.CONTENT_WIDGET_GOOGLE))
            binding.rlCodContent.addView(WebViewWidget.getWebViewWidget(this,item?.src!!))
//            loadWebViewWidget()
        /// iframe
        if(item?.type.equals(Constant.CONTENT_WIDGET_IFRAME))
            binding.rlCodContent.addView(WebViewWidget.getiFrameWidget(this,item?.ifr!!),width,height)
        // power bi
        if(item?.type.equals(Constant.CONTENT_WIDGET_POWER))
            binding.rlCodContent.addView(WidgetPowerBI.getWidgetPowerBI(this,item,width,height))
        // live streaming
        if(item?.type.equals(Constant.CONTENT_WIDGET_LIVESTREAM)){
            exoPlayer  = StyledPlayerView(this)
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
            exoPlayer?.layoutParams = params

            if(item?.dType.equals("yt"))
                binding.rlCodContent?.addView(YouTubeWidget.getYouTubeWidget(this,item!!))
            else
                binding.rlCodContent?.addView(WidgetExoPlayer.getExoPLayer(this,item!!,exoPlayer!!))

//            if(item?.dType.equals("yt")){
//                playYoutube()
//            }else{
//                setUpPlayer()
//            }
        }
        // vimeo
        if(item?.type.equals(Constant.CONTENT_WIDGET_VIMEO)) playVimeo()
        // youtube
        if(item?.type.equals(Constant.CONTENT_WIDGET_YOUTUBE)) {
            startActivity(
                Intent(this,YouTubeActivity::class.java)
                    .putExtra("data",item)
            )
            finish()
//            playYoutube()
        }

    }


    fun playYoutube(){

        binding.rlCodContent.visibility = View.GONE
        binding.wvCodContent.visibility = View.VISIBLE

        binding.wvCodContent.setWebChromeClient(WebChromeClient())
        binding.wvCodContent.getSettings().setJavaScriptEnabled(true)

        binding.wvCodContent.getSettings()?.setMediaPlaybackRequiresUserGesture(false)
        binding.wvCodContent.getSettings()?.setUserAgentString("1");//for desktop 1 or mobile 0.


        if(item?.type.equals(Constant.CONTENT_WIDGET_LIVESTREAM)){
            binding.wvCodContent.loadUrl(item?.url!!)
        }else{
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
        viewModel.internet = isConnected
        viewModel.is_device_registered = true

        item = intent.getSerializableExtra(EXTRA_ITEM_DATA) as Content?
        // click
        binding.ivCodContentClose.setOnClickListener { finish() }
        binding.ivCodContentCaption.setOnClickListener {
            showCaptionDialog(item!!.caption)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if(player!=null){
            player?.stop()
        }
        binding.wvCodContent.removeAllViews()
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

        if(exoPlayer?.player != null && exoPlayer?.player!!.isPlaying){
            exoPlayer?.player?.stop()
            exoPlayer?.removeAllViews()
        }
        if(player != null && player!!.isPlaying){
            player?.stop()
        }
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


    private fun setUpPlayer(){
        binding.idExoPlayerView?.visibility = View.VISIBLE

        // RTSP
        if(item?.dType!=null && item?.dType.equals("rtsp")){
            //initializing exoplayer
            player = SimpleExoPlayer.Builder(this)
                .setMediaSourceFactory(RtspMediaSource.Factory().setForceUseRtpTcp(true))
                .setSeekBackIncrementMs(10000)
                .setSeekForwardIncrementMs(10000)
                .build()

            val mediaItem = MediaItem.Builder()
                .setUri(item?.url)
                .setMimeType(MimeTypes.APPLICATION_RTSP) //m3u8 is the extension used with HLS sources
                .build()
            player?.setMediaItem(mediaItem)

            //set up audio attributes
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MOVIE)
                .build()
            player?.setAudioAttributes(audioAttributes, false)
            binding.idExoPlayerView?.player = player


            player?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(@Player.State state: Int) {
                    if (state == Player.STATE_ENDED) {
                        finish()
                    }
                }
            })

            if (item?.sound=="no") player?.volume = 0f
            else player?.volume = 100f

            player?.prepare()
            player?.repeatMode = Player.REPEAT_MODE_OFF //repeating the video from start after it's over
            player?.play()

        }else{
            //initializing exoplayer
            player = SimpleExoPlayer.Builder(this)
                .setSeekBackIncrementMs(10000)
                .setSeekForwardIncrementMs(10000)
                .build()
            //set up audio attributes
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MOVIE)
                .build()
            player?.setAudioAttributes(audioAttributes, false)


            binding.idExoPlayerView?.player = player

            //hiding all the ui StyledPlayerView comes with
            binding.idExoPlayerView?.setShowNextButton(false)
            binding.idExoPlayerView?.setShowPreviousButton(false)

            //setting the scaling mode to scale to fit
            player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT

            if(item?.type.equals("livestream"))
                addMediaItem("l")
            else
                addMediaItem("v")
        }
        binding.idExoPlayerView.player?.setVideoTextureView(TextureView(this))

    }

    private fun addMediaItem(type:String) {

        if(type.equals("l")){
            val mediaItem = MediaItem.Builder()
                .setUri(item?.url)
                .setMimeType(MimeTypes.APPLICATION_M3U8) //m3u8 is the extension used with HLS sources
                .build()
            player?.setMediaItem(mediaItem)
        }else{
            var filename = item?.fileName
            val path = DataManager.getDirectory()+ File.separator+ filename
            var file = File(path)
            val uri = Uri.fromFile(file)

            if(item?.caption!=null && item?.caption?.size!!>0){
                binding.ivCodContentCaption.visibility = View.VISIBLE
            }

            val mediaItem = MediaItem.Builder()
                .setUri(uri)
                .setMimeType(MimeTypes.BASE_TYPE_VIDEO) // play local files
                .build()
            player?.setMediaItem(mediaItem)
        }

        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(@Player.State state: Int) {
                if (state == Player.STATE_ENDED) {
                    finish()
                }
            }
        })

        if (item?.sound=="no") player?.volume = 0f
        else player?.volume = 100f

        player?.prepare()
//        player?.repeatMode = Player.REPEAT_MODE_OFF //repeating the video from start after it's over
        player?.play()
    }

    private fun playWithCaption(){
/*
        val mediaSources = arrayOfNulls<MediaSource>(2)


        // for video content
        val uri = Uri.fromFile(File(DataManager.getDirectory()+ File.separator+ item?.fileName))
        val contentMediaSource = ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(uri))
        mediaSources[0] = contentMediaSource

        // for subtitle
        val sub_uri = Uri.fromFile(File(DataManager.getDirectory()+ File.separator+ item?.fileName))
        val dataSourceFactory = DefaultDataSourceFactory(
            this, Util.getUserAgent(this, "exo-demo"))
        val subtitleSource = SingleSampleMediaSource(
            sub_uri, dataSourceFactory,
            Format.createTextSampleFormat(
                null,
                MimeTypes.APPLICATION_SUBRIP,
                Format.NO_VALUE,
                "en",
                null
            ),
            C.TIME_UNSET
        )
        mediaSources[1] = subtitleSource



        val mediaSource = MergingMediaSource(mediaSources[0],mediaSources[1])
        player?.prepare(mediaSource)
        player?.setPlayWhenReady(true);
*/
    }

    fun loadWebViewWidget(){
        binding.wvCodContent.visibility = View.VISIBLE
        binding.wvCodContent.setWebChromeClient(WebChromeClient())
        binding.wvCodContent.getSettings().setJavaScriptEnabled(true)
        binding.wvCodContent.loadUrl(item?.src!!)
    }




}
