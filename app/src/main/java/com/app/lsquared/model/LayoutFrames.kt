package com.app.lsquared.model

import android.media.MediaMetadataRetriever
import android.widget.LinearLayout
import android.widget.VideoView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.Job

data class LayoutFrames (
    var frame_view_ll:LinearLayout? = null,
    var videoView: VideoView? = null,
    var exoPlayerView: StyledPlayerView? = null,
    var exoPlayer: SimpleExoPlayer? = null,
    var isvideoAvail: Boolean = false,
    var active_widget :String = "",
    var frame_job: Job? = null,
    var item_job: Job? = null,
    var myMediaMetadataRetriever: MediaMetadataRetriever? = null,
    var sound: String = "",
    var frame_items : List<Item> = arrayListOf(),
    var current_item_position :Int = 0
)