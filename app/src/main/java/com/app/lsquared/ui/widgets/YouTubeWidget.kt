package com.app.lsquared.ui.widgets

import android.content.Context
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.app.lsquared.model.Content
import com.app.lsquared.model.Item
import com.app.lsquared.ui.MainActivity
import com.app.lsquared.utils.Constant
import org.json.JSONObject

class YouTubeWidget {

    companion object{

        fun getYouTubeWidget(ctx:Context, item: Item):WebView{
            var web = WebView(ctx)
            web.setWebChromeClient(WebChromeClient())
            web.getSettings().setJavaScriptEnabled(true)

            web.getSettings()?.setMediaPlaybackRequiresUserGesture(false)
            web.getSettings()?.setUserAgentString("1");//for desktop 1 or mobile 0.

            var mute = "0"
            if (item.sound=="no") mute ="1" else mute = "0"

            var setting_obj = JSONObject(item.settings)
            var cc = setting_obj.getBoolean("cc")
            try {
                if(item.youtube_param != null && item.youtube_param.equals(""))
                    web.loadUrl("${Constant.BASE_URL_YOUTUBE}${item.src}?autoplay=1&rel=0&loop=1&cc_load_policy=$cc&mute=$mute&controls=0&enablejsapi=1")
                else
                    web.loadUrl("${Constant.BASE_URL_YOUTUBE}${item.src}?autoplay=1&rel=0&loop=1&cc_load_policy=$cc&mute=$mute&controls=0&enablejsapi=1${item.youtube_param}")
            } catch (e: java.lang.Exception) {
                Log.w("TAG", "Exception : ", e)
            }
            return web
        }


    }
}