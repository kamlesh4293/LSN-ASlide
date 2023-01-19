package com.app.lsquared.model

import android.media.MediaMetadataRetriever
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.VideoView

data class LayoutView (
    var relative_layout:RelativeLayout?,
    var imageView: ImageView?,
    var video_imageView: ImageView?,
    var videoView: VideoView?,
    var webView: WebView?,
    var myMediaMetadataRetriever: MediaMetadataRetriever?
){

}