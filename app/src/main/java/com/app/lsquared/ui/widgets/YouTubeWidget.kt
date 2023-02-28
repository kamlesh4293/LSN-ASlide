package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.app.lsquared.R
import com.app.lsquared.model.Content
import com.app.lsquared.model.Item
import com.app.lsquared.utils.Constant
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.android.youtube.player.YouTubePlayerView
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

            if(item.settings != null && !item.settings.equals("")){
                var setting_obj = JSONObject(item.settings)
                var cc = setting_obj.getBoolean("cc")
                try {
                    if(item.params != null && !item.params.equals(""))
                        web.loadUrl("${Constant.BASE_URL_YOUTUBE}${item.src}?autoplay=1&rel=0&loop=1&cc_load_policy=$cc&mute=$mute&controls=0&enablejsapi=1${item.params}")
                    else
                        web.loadUrl("${Constant.BASE_URL_YOUTUBE}${item.src}?autoplay=1&rel=0&loop=1&cc_load_policy=$cc&mute=$mute&controls=0&enablejsapi=1")
                } catch (e: java.lang.Exception) {
                    Log.w("TAG", "Exception : ", e)
                }
            }else{
                try {
                    if(item.params != null && !item.params.equals(""))
                        web.loadUrl("${Constant.BASE_URL_YOUTUBE}${item.src}?autoplay=1&rel=0&loop=1&mute=$mute&controls=0&enablejsapi=1${item.params}")
                    else
                        web.loadUrl("${Constant.BASE_URL_YOUTUBE}${item.src}?autoplay=1&rel=0&loop=1&mute=$mute&controls=0&enablejsapi=1")
                } catch (e: java.lang.Exception) {
                    Log.w("TAG", "Exception : ", e)
                }

            }
            return web
        }

        fun getYouTubeWidgetLiveStream(ctx:Context, item: Item):WebView{
            var web = WebView(ctx)
            web.setWebChromeClient(WebChromeClient())
            web.getSettings().setJavaScriptEnabled(true)

            web.getSettings()?.setMediaPlaybackRequiresUserGesture(false)
            web.getSettings()?.setUserAgentString("1");//for desktop 1 or mobile 0.

            var mute = "0"
            if (item.sound=="no") mute ="1" else mute = "0"

            var src = item?.url!!.substring(item?.url!!.lastIndexOf("=")+1,item?.url!!.length)
            web.loadUrl("${Constant.BASE_URL_YOUTUBE}$src?autoplay=1&rel=0&loop=1&mute=$mute&controls=0&enablejsapi=1")

            return web
        }

        fun getYouTubeWidget(ctx:Context, item: Content):WebView{
            var web = WebView(ctx)
            web.setWebChromeClient(WebChromeClient())
            web.getSettings().setJavaScriptEnabled(true)

            web.getSettings()?.setMediaPlaybackRequiresUserGesture(false)
            web.getSettings()?.setUserAgentString("1");//for desktop 1 or mobile 0.

            var mute = "0"
            if (item.sound=="no") mute ="1" else mute = "0"

            if(item.settings != null && !item.settings.equals("")){
                var setting_obj = JSONObject(item.settings)
                var cc = setting_obj.getBoolean("cc")
                try {
                    if(item.params != null && !item.params.equals(""))
                        web.loadUrl("${Constant.BASE_URL_YOUTUBE}${item.src}?autoplay=1&rel=0&loop=1&cc_load_policy=$cc&mute=$mute&controls=0&enablejsapi=1${item.params}")
                    else
                        web.loadUrl("${Constant.BASE_URL_YOUTUBE}${item.src}?autoplay=1&rel=0&loop=1&cc_load_policy=$cc&mute=$mute&controls=0&enablejsapi=1")
                } catch (e: java.lang.Exception) {
                    Log.w("TAG", "Exception : ", e)
                }
            }else{
                try {
                    if(item.params != null && !item.params.equals(""))
                        web.loadUrl("${Constant.BASE_URL_YOUTUBE}${item.src}?autoplay=1&rel=0&loop=1&mute=$mute&controls=0&enablejsapi=1${item.params}")
                    else
                        web.loadUrl("${Constant.BASE_URL_YOUTUBE}${item.src}?autoplay=1&rel=0&loop=1&mute=$mute&controls=0&enablejsapi=1")
                } catch (e: java.lang.Exception) {
                    Log.w("TAG", "Exception : ", e)
                }

            }
            return web
        }

        fun getYouTubePlayerWidget(ctx:Context):View{
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.view_youtube, null)
            var youTubePlayerFragment = view.findViewById<YouTubePlayerView>(R.id.youtube_player)
            val onInitializedListener: YouTubePlayer.OnInitializedListener =
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationSuccess(
                        provider: YouTubePlayer.Provider,
                        youTubePlayer: YouTubePlayer,
                        b: Boolean
                    ) {
                        youTubePlayer.loadVideo("PLlTzZajuBAh8XFv-XzJf5326hoJl2kPuG")
                    }

                    override fun onInitializationFailure(
                        provider: YouTubePlayer.Provider,
                        youTubeInitializationResult: YouTubeInitializationResult
                    ) {
                    }
                }
            youTubePlayerFragment.initialize(ctx.resources.getString(R.string.youtube_api_key), onInitializedListener)

            return view
        }

    }
}