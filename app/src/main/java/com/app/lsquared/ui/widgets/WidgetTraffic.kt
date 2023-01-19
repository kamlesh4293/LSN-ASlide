package com.app.lsquared.ui.widgets

import android.content.Context
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.app.lsquared.model.Item
import com.app.lsquared.utils.Constant

class WidgetTraffic {

    companion object{

        fun getWidgetTrafic(ctx: Context,id:String): WebView {

            var web = WebView(ctx)
            web.setWebChromeClient(WebChromeClient())
            web.getSettings().setJavaScriptEnabled(true)
            val ids: List<String> = id.split("-")
            try {
                web.loadUrl("${Constant.BASE_FILE_FEED_URL}/traffic/${ids[2]}")
            } catch (e: java.lang.Exception) {
                Log.w("TAG", "Exception : ", e)
            }
            return web
        }
    }
}