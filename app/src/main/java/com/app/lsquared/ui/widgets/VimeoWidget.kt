package com.app.lsquared.ui.widgets

import android.content.Context
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.app.lsquared.utils.Constant
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.vimeo.networking.Configuration
import com.vimeo.networking.VimeoClient

class VimeoWidget {

    companion object{

        fun getVimeoWidget(ctx:Context,sound:String,src:String):WebView{

            var web = WebView(ctx)
            web.setWebChromeClient(WebChromeClient())
            web.getSettings().setJavaScriptEnabled(true)

            val webSettings: WebSettings = web.getSettings()
            webSettings.javaScriptEnabled = true
            webSettings.allowFileAccess = true
            webSettings.setAppCacheEnabled(true)
            webSettings.mediaPlaybackRequiresUserGesture = false

            var mute = "0"
            if (sound=="no") mute ="1" else mute = "0"

            var url = "${Constant.BASE_URL_VIMEO}$src"+"?autoplay=1&loop=1&muted=$mute"
            Log.d("TAG", "getVimeoWidget: $url")
            try {
                Log.d("TAG", "getVimeoWidget: loading")
                web.loadUrl(url)
            } catch (e: java.lang.Exception) {
                Log.w("TAG", "getVimeoWidget: Exception : ", e)
            }
            return web
        }

        fun getVimeoWidget(ctx: Context,player: SimpleExoPlayer): StyledPlayerView {
            val configBuilder =
                Configuration.Builder(Constant.VIMEO_ACCESS_TOKEN) //Pass app access token
                    .setCacheDirectory(ctx.cacheDir)
            VimeoClient.initialize(configBuilder.build())
            var exoPlayer = WidgetExoPlayer.getExoPlayerView(ctx)
            exoPlayer.player = player
            return exoPlayer
        }


    }
}