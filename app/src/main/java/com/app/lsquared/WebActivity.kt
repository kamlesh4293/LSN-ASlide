package com.app.lsquared

import android.os.Bundle
import android.webkit.WebChromeClient
import androidx.appcompat.app.AppCompatActivity
import com.app.lsquared.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {

    private var binding: ActivityWebBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())

        binding!!.webView.webChromeClient = WebChromeClient()
        binding!!.webView.settings.javaScriptEnabled = true
        binding!!.webView.loadUrl("https://constant-media.biodigital.com/")


    }
}