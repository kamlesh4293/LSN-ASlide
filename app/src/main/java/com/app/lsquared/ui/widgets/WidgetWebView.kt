package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.app.lsquared.databinding.WidgetWebviewBinding

class WidgetWebView {

    private lateinit var binding: WidgetWebviewBinding

    fun getGoogleSlide(ctx:Context): LinearLayout {
        binding = WidgetWebviewBinding.inflate((ctx as Activity).layoutInflater)

//        binding.wvWidgetMainweb.settings.javaScriptEnabled = true
//        binding.wvWidgetMainweb.loadUrl("https://us.lsquared.com")

        binding.wvWidgetMainweb.webViewClient = WebViewClient()
        binding.wvWidgetMainweb.loadUrl("https://www.geeksforgeeks.org/")
        binding.wvWidgetMainweb.settings.javaScriptEnabled = true
        binding.wvWidgetMainweb.settings.setSupportZoom(true)

        return binding.root
    }

}