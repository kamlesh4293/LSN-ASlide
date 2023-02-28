package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.RelativeLayout
import com.app.lsquared.model.Content
import com.app.lsquared.model.Item
import com.app.lsquared.utils.Constant
import com.app.lsquared.utils.DataManager
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
import java.io.File

class WidgetExoPlayer {

    companion object{

        fun getExoPlayerView(ctx: Context): StyledPlayerView {
            var exoPlayer = StyledPlayerView(ctx)
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
            exoPlayer?.layoutParams = params
            return exoPlayer
        }

        fun getExoPlayer(ctx:Context,type:Int,sound:String): SimpleExoPlayer {
            var player = SimpleExoPlayer.Builder(ctx).build()
            player!!.playWhenReady = true
            player!!.seekTo(0, 0)
            player!!.prepare()
            player?.play()
            if(type==Constant.PLAYER_SLIDE) player?.repeatMode = Player.REPEAT_MODE_ONE
            player?.volume = if (sound=="no")  0f else 100f
            player?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(@Player.State state: Int) {
                    if (state == Player.STATE_ENDED) {
                        if(type==Constant.PLAYER_COD) (ctx as Activity).finish()
                    }
                }
            })
            return player
        }


        fun setExoPLayer(ctx: Context, item: Item, exoPlayer: StyledPlayerView): StyledPlayerView {
            setUpPlayer(ctx,exoPlayer,item)
            return exoPlayer
        }

        fun setExoPLayer(ctx: Context, item: Content, exoPlayer: StyledPlayerView): StyledPlayerView {
            setUpPlayer(ctx,exoPlayer,item)
            return exoPlayer
        }


        private fun setUpPlayer(ctx: Context,exoPlayer: StyledPlayerView, item: Item) {

            var player: SimpleExoPlayer? = null

            // RTSP
            if(item?.dType!=null && item?.dType.equals("rtsp")){
                //initializing exoplayer
                player = SimpleExoPlayer.Builder(ctx)
                    .setMediaSourceFactory(RtspMediaSource.Factory().setForceUseRtpTcp(true))
                    .setSeekBackIncrementMs(10000)
                    .setSeekForwardIncrementMs(10000)
                    .build()

                val mediaItem = MediaItem.Builder()
                    .setUri(item?.url)
                    .setMimeType(MimeTypes.APPLICATION_RTSP) //m3u8 is the extension used with HLS sources
                    .build()
                player?.setMediaItem(mediaItem)

                //set up audio attributes
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.CONTENT_TYPE_MOVIE)
                    .build()
                player?.setAudioAttributes(audioAttributes, false)
                exoPlayer?.player = player


                if (item?.sound=="no") player?.volume = 0f
                else player?.volume = 100f

                player?.prepare()
                player?.repeatMode = Player.REPEAT_MODE_ONE //repeating the video from start after it's over
                player?.play()

            }else{
                //initializing exoplayer
                player = SimpleExoPlayer.Builder(ctx)
                    .setSeekBackIncrementMs(10000)
                    .setSeekForwardIncrementMs(10000)
                    .build()
                //set up audio attributes
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.CONTENT_TYPE_MOVIE)
                    .build()
                player?.setAudioAttributes(audioAttributes, false)

                exoPlayer?.player = player

                //hiding all the ui StyledPlayerView comes with
                exoPlayer?.setShowNextButton(false)
                exoPlayer?.setShowPreviousButton(false)

                //setting the scaling mode to scale to fit
                player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT

                if(item?.type.equals("livestream"))
                    addMediaItem("l",item,player)
                else
                    addMediaItem("v",item,player)
            }

        }

        private fun addMediaItem(type: String, item: Item, player: SimpleExoPlayer) {

            if(type.equals("l")){
                val mediaItem = MediaItem.Builder()
                    .setUri(item?.url)
                    .setMimeType(MimeTypes.APPLICATION_M3U8) //m3u8 is the extension used with HLS sources
                    .build()
                player?.setMediaItem(mediaItem)
            }else{
                var filename = item?.fileName
                val path = DataManager.getDirectory()+ File.separator+ filename
                var file = File(path)
                val uri = Uri.fromFile(file)

                val mediaItem = MediaItem.Builder()
                    .setUri(uri)
                    .setMimeType(MimeTypes.BASE_TYPE_VIDEO) // play local files
                    .build()
                player?.setMediaItem(mediaItem)
            }

            if (item?.sound=="no") player?.volume = 0f
            else player?.volume = 100f

            player?.repeatMode = Player.REPEAT_MODE_ONE //repeating the video from start after it's over
            player?.prepare()
            player?.play()
        }

        // COD

        private fun setUpPlayer(ctx: Context,exoPlayer: StyledPlayerView, item: Content) {

            var player: SimpleExoPlayer? = null

            // RTSP
            if(item?.dType!=null && item?.dType.equals("rtsp")){
                //initializing exoplayer
                player = SimpleExoPlayer.Builder(ctx)
                    .setMediaSourceFactory(RtspMediaSource.Factory().setForceUseRtpTcp(true))
                    .setSeekBackIncrementMs(10000)
                    .setSeekForwardIncrementMs(10000)
                    .build()

                val mediaItem = MediaItem.Builder()
                    .setUri(item?.url)
                    .setMimeType(MimeTypes.APPLICATION_RTSP) //m3u8 is the extension used with HLS sources
                    .build()
                player?.setMediaItem(mediaItem)

                //set up audio attributes
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.CONTENT_TYPE_MOVIE)
                    .build()
                player?.setAudioAttributes(audioAttributes, false)
                exoPlayer?.player = player


                if (item?.sound=="no") player?.volume = 0f
                else player?.volume = 100f

                player?.prepare()
                player?.repeatMode = Player.REPEAT_MODE_OFF //repeating the video from start after it's over
                player?.play()

            }else{
                //initializing exoplayer
                player = SimpleExoPlayer.Builder(ctx)
                    .setSeekBackIncrementMs(10000)
                    .setSeekForwardIncrementMs(10000)
                    .build()
                //set up audio attributes
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.CONTENT_TYPE_MOVIE)
                    .build()
                player?.setAudioAttributes(audioAttributes, false)

                exoPlayer?.player = player

                //hiding all the ui StyledPlayerView comes with
                exoPlayer?.setShowNextButton(false)
                exoPlayer?.setShowPreviousButton(false)

                //setting the scaling mode to scale to fit
                player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT

                if(item?.type.equals("livestream"))
                    addMediaItem("l",item,player)
                else
                    addMediaItem("v",item,player)
            }

            player?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(@Player.State state: Int) {
                    if (state == Player.STATE_ENDED) {
                        (ctx as Activity).finish()
                    }
                }
            })


        }

        private fun addMediaItem(type: String, item: Content, player: SimpleExoPlayer) {

            if(type.equals("l")){
                val mediaItem = MediaItem.Builder()
                    .setUri(item?.url)
                    .setMimeType(MimeTypes.APPLICATION_M3U8) //m3u8 is the extension used with HLS sources
                    .build()
                player?.setMediaItem(mediaItem)
            }else{
                var filename = item?.fileName
                val path = DataManager.getDirectory()+ File.separator+ filename
                var file = File(path)
                val uri = Uri.fromFile(file)

                val mediaItem = MediaItem.Builder()
                    .setUri(uri)
                    .setMimeType(MimeTypes.BASE_TYPE_VIDEO) // play local files
                    .build()
                player?.setMediaItem(mediaItem)
            }

            if (item?.sound=="no") player?.volume = 0f
            else player?.volume = 100f

            player?.prepare()
            player?.play()
        }


    }
}