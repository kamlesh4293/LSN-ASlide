package com.app.lsquared.ui.widgets

import android.content.Context
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.app.lsquared.model.Content
import com.app.lsquared.model.Item
import com.app.lsquared.ui.MainActivity

class WebViewWidget {

    companion object{

        fun getWebViewWidget(ctx:Context, src: String):WebView{
            var web = WebView(ctx)
            web.setWebChromeClient(WebChromeClient())
            web.getSettings().setJavaScriptEnabled(true)
            try {
                web.loadUrl(src)
            } catch (e: java.lang.Exception) {
                Log.w("TAG", "Exception : ", e)
            }
            return web
        }

    }
}