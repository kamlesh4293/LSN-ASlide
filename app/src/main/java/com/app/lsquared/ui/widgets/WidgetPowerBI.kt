package com.app.lsquared.ui.widgets

import android.content.Context
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.RelativeLayout
import com.app.lsquared.model.Content
import com.app.lsquared.model.Item
import com.app.lsquared.ui.MainActivity
import com.app.lsquared.utils.Constant

class WidgetPowerBI {

    companion object{

        fun getWidgetPowerBI(ctx:Context, item: Item):WebView{
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

        fun getWidgetPowerBI(ctx: Context, item: Content?, width: Int, height: Int):WebView{
            var web = WebView(ctx)
            val params = RelativeLayout.LayoutParams(width,height)
            web.layoutParams = params
            web.setWebChromeClient(WebChromeClient())
            web.getSettings().setJavaScriptEnabled(true)
            val ids: List<String> = item?.id!!.split("-")
            try {
                web.loadUrl("${Constant.BASE_FILE_FEED_URL}/powerbiHtml/${ids[2]}")
            } catch (e: java.lang.Exception) {
                Log.w("TAG", "Exception : ", e)
            }
            return web
        }
    }
}