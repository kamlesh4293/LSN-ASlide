package com.app.lsquared.ui.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.app.lsquared.model.LayoutFrames;
import com.app.lsquared.model.cod.Settings;
import com.app.lsquared.pasring.DataParsingSetting;
import com.app.lsquared.ui.UiUtils;
import com.app.lsquared.utils.DataParsing;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class WebViewChromium {

   @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static LinearLayout getWebChromeWidget(Context ctx, String src, GradientDrawable shape,String settings){

        LinearLayout layout = new LinearLayout(ctx);
        try {
            JSONObject obj = new JSONObject(settings);
            int x = obj.getInt("x");
            int y = obj.getInt("y");
            layout.setX(-1*x);
            layout.setY(-1*y);
        } catch (JSONException e) { e.printStackTrace(); }

        WebView webView = new WebView(ctx);
        setUpWebViewDefaults(webView);
        webView.loadUrl(src);

        LinearLayout.LayoutParams wv_lp = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(wv_lp);

       try {
           String bg = DataParsingSetting.getBgWithOpacity(new JSONObject(settings));
           webView.setBackgroundColor(Color.parseColor(bg));
       } catch (JSONException e) {
           e.printStackTrace();
       }
        webView.getSettings().setUseWideViewPort(true);
        webView.setBackground(shape);
        webView.setVerticalScrollBarEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Activity activity = (Activity) ctx;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(request.getOrigin().toString().equals(src)) {
                            request.grant(request.getResources());
                        } else {
                            request.deny();
                        }
                    }
                });
            }

        });
        layout.addView(webView);
        return layout;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        // Allow use of Local Storage
        settings.setDomStorageEnabled(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient());

        // AppRTC requires third party cookies to work
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(webView, true);
    }


}
