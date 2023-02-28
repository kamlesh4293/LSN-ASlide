package com.app.lsquared.ui.activity

import android.media.AudioManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.app.lsquared.R
import com.app.lsquared.model.Content
import com.app.lsquared.utils.Constant
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView


class YouTubeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private val TAG = "YoutubeActivity"
    var yourCountDownTimer: CountDownTimer? = null
    var item:Content? = null
    var currentVolume: Int = 50


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        window.decorView.systemUiVisibility = flags
        // screen always on mode
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_youtube)
        item = intent.getSerializableExtra("data") as Content?

        var playerView = YouTubePlayerView(this)
        playerView?.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        findViewById<FrameLayout>(R.id.frame_youtube).addView(playerView)
        findViewById<ImageView>(R.id.iv_youtube_close).setOnClickListener { finish() }
        playerView?.initialize(getString(R.string.youtube_api_key), this)

        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        wasRestored: Boolean
    ) {

        youTubePlayer?.setPlayerStateChangeListener(playerStateChangeListener)
        youTubePlayer?.setPlaybackEventListener(playbackEventListener)


        var video_id = getVideoId(item)
        Log.d(TAG, "onInitializationSuccess: video_id - $video_id")
        if (!wasRestored) {
            if(item?.params !=null && !item?.params.equals("")){
                Log.d(TAG, "onInitializationSuccess: video with param")
                youTubePlayer?.cuePlaylist(video_id)
            } else {
                Log.d(TAG, "onInitializationSuccess: video with src")
                youTubePlayer?.cueVideo(video_id)
            }
        }
        if(item?.params !=null && !item?.params.equals("")) youTubePlayer?.loadPlaylist(video_id)
        else youTubePlayer?.loadVideo(video_id)

        if(item?.mute ==1){
            val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0)
//            volumeControlStream = AudioManager.STREAM_MUSIC
//            val am = getSystemService(AUDIO_SERVICE) as AudioManager
//            am?.setStreamVolume(AudioManager.ADJUST_MUTE, 0, 0)
        }

    }

    private fun getVideoId(item: Content?): String {

        if(item?.type.equals(Constant.CONTENT_WIDGET_LIVESTREAM)){
            return item?.url!!.substring(item?.url!!.lastIndexOf("=")+1,item?.url!!.length)
        }
        return if(item?.params !=null && !item?.params.equals(""))
            item?.params!!.replace("&list=","")
        else item?.src!!
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?,
                                         youTubeInitializationResult: YouTubeInitializationResult?) {
        val REQUEST_CODE = 0

        if (youTubeInitializationResult?.isUserRecoverableError == true) {
            youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show()
        } else {
            val errorMessage = "There was an error initializing the YoutubePlayer ($youTubeInitializationResult)"
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private val playbackEventListener = object: YouTubePlayer.PlaybackEventListener {
        override fun onSeekTo(p0: Int) {
        }

        override fun onBuffering(p0: Boolean) {
        }

        override fun onPlaying() {
        }

        override fun onStopped() {
        }

        override fun onPaused() {
        }
    }

    private val playerStateChangeListener = object: YouTubePlayer.PlayerStateChangeListener {
        override fun onAdStarted() {
        }

        override fun onLoading() {
        }

        override fun onVideoStarted() {
            stopCounter()
        }

        override fun onLoaded(p0: String?) {
        }

        override fun onVideoEnded() {
            initCountDownTimer()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {
        }
    }

    fun initCountDownTimer() {
        if (yourCountDownTimer != null) yourCountDownTimer!!.cancel()
        yourCountDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                finish()
            }
        }.start()
    }

    fun stopCounter() {
        if (yourCountDownTimer != null) yourCountDownTimer!!.cancel()
    }

    override fun onStop() {
        super.onStop()
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume,0)
    }


}