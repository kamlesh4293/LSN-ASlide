package com.app.lsquared.ui.widgets

import android.content.Context
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.app.lsquared.model.Item
import com.app.lsquared.ui.MainActivity
import com.app.lsquared.utils.Constant

class PowerBIWidget {

    companion object{

        fun getPowerBIWidget(ctx:Context, item: Item):WebView{
            var web = WebView(ctx)
            web.setWebChromeClient(WebChromeClient())
            web.getSettings().setJavaScriptEnabled(true)
            val ids: List<String> = item.id.split("-")
            try {
                web.loadUrl("${Constant.BASE_FILE_FEED_URL}/powerbiHtml/${ids[2]}")
            } catch (e: java.lang.Exception) {
                Log.w("TAG", "Exception : ", e)
            }
            return web
        }
    }
}