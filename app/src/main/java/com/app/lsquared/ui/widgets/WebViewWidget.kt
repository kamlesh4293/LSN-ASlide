package com.app.lsquared.ui.widgets

import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
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
            web.setWebViewClient(object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    view.loadUrl(request.url.toString())
                    return false
                }
            })
            return web
        }

//        fun getWebViewWidget(ctx:Context, src: String):WebView{
//            var web = WebView(ctx)
//            web.setWebChromeClient(WebChromeClient())
//            web.getSettings().setJavaScriptEnabled(true)
//            web.loadUrl(src)
//            web.setWebViewClient(object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(
//                    view: WebView,
//                    request: WebResourceRequest
//                ): Boolean {
//                    view.loadUrl(request.url.toString())
//                    return false
//                }
//            })
//            return web
//        }

        fun getiFrameWidget(ctx:Context, src: String):WebView{
            var web = WebView(ctx)
            web.setWebChromeClient(WebChromeClient())
            web.getSettings().setJavaScriptEnabled(true)
            web.loadData(src, "text/html", null);
            return web
        }

    }
}