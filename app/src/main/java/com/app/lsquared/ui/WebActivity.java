package com.app.lsquared.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebViewClient;

import com.app.lsquared.databinding.ActivityWebBinding;

public class WebActivity extends AppCompatActivity {

    ActivityWebBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.webView.setWebChromeClient(new WebChromeClient());


        binding.webView.setWebViewClient(new WebViewClient());

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl("https://constant-media.biodigital.com/");

    }
}