package com.app.lsquared.model

import android.widget.LinearLayout
import android.widget.VideoView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.Job

data class NewLayoutView (
    var relative_layout:LinearLayout?,
    var videoView: VideoView?,
    var exoPlayerView: StyledPlayerView,
    var exoPlayer: SimpleExoPlayer?,
    var isvideoAvail: Boolean,
    var active_widget :String,
    var job: Job?,
    var rotate_job: Job?
){

}