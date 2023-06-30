package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.webkit.*
import androidx.annotation.RequiresApi
import com.app.lsquared.pasring.DataParsingSetting.Companion.getBgWithOpacity
import org.json.JSONException
import org.json.JSONObject

class WebViewWidget {

    companion object{

        fun getWebViewWidget(ctx:Context, src: String, setting: String):WebView{
            var web = WebView(ctx)
            web.setWebChromeClient(WebChromeClient())
            web.settings.javaScriptEnabled = true
            web.settings.useWideViewPort = true
            web.setVerticalScrollBarEnabled(true)
            try {
                Log.d("TAG", "getWebViewWidget: src - $src")
                web.loadUrl(src)
            } catch (e: java.lang.Exception) {
                Log.d("TAG", "Exception : ", e)
            }
            val bg = getBgWithOpacity(JSONObject(setting))
            web.setBackgroundColor(Color.parseColor(bg))

            web.setWebChromeClient(object : WebChromeClient() {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onPermissionRequest(request: PermissionRequest) {
                    (ctx as Activity).runOnUiThread(Runnable {
                        request.grant(request.resources) })
                }
            })

//            web.setWebViewClient(object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
//                    view.loadUrl(request.url.toString())
//                    return false
//                }
//            })
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