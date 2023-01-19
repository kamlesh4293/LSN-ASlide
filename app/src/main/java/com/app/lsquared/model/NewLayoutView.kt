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
import kotlinx.coroutines.Job

data class NewLayoutView (
    var relative_layout:RelativeLayout?,
    var active_widget :String,
    var job: Job?,
    var rotate_job: Job?
){

}