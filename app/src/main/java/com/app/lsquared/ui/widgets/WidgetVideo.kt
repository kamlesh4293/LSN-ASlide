package com.app.lsquared.ui.widgets

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.RelativeLayout
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.app.lsquared.ui.activity.CodContentActivity
import com.app.lsquared.utils.Constant
import com.app.lsquared.utils.DataManager
import com.app.lsquared.utils.Utility
import com.google.android.exoplayer2.ui.StyledPlayerView
import java.io.File


class WidgetVideo {

    companion object{

        fun getWidgetVideo(ctx: Context,video: VideoView,media_data:MediaMetadataRetriever,width: Int,height: Int,fileName: String,sound: String):View{
//            var video = VideoView(ctx)
            val params = RelativeLayout.LayoutParams(width,height)
            video.layoutParams = params

            val path = DataManager.getDirectory()+ File.separator+ fileName

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                var uri = FileProvider.getUriForFile(ctx,ctx.packageName+".provider",File(path))
                media_data.setDataSource(ctx, uri)
            }else{
                try {
                    media_data.setDataSource(path, HashMap())
                }catch (e :RuntimeException ){
                    var uri = FileProvider.getUriForFile(ctx,ctx.packageName+".provider",File(path))
                    media_data.setDataSource(ctx, uri)
                    e.printStackTrace();
                }
            }
            video.setVideoPath(path)
            video.start()

            var mc : MediaController? = MediaController(ctx)
            mc?.visibility = View.GONE
            video.setMediaController(mc)
            Log.d("TAG", "getWidgetVideo: video sound $sound")
            if(sound.equals("no")){
                video.setOnPreparedListener(
                    MediaPlayer.OnPreparedListener {
                            mp -> mp.setVolume(0f, 0f)
                    })
            }else{
                video.setOnPreparedListener(
                    MediaPlayer.OnPreparedListener {
                            mp -> mp.setVolume(100f, 100f)
                    })
            }
            video.setOnCompletionListener(OnCompletionListener {
                mp -> mp.start()
            })
            return video
        }

        fun getMediaData(ctx:Context, fileName:String,media:MediaMetadataRetriever): MediaMetadataRetriever? {
            var media_data = media
            val path = DataManager.getDirectory()+ File.separator+ fileName
            if(File(path).exists()){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    var uri = FileProvider.getUriForFile(ctx,ctx.packageName+".provider",File(path))
                    media_data!!.setDataSource(ctx, uri)
                }else{
                    try {
                        media_data!!.setDataSource(path, java.util.HashMap())
                    }catch (e :RuntimeException ){
                        var uri = FileProvider.getUriForFile(ctx,ctx.packageName+".provider",File(path))
                        media_data!!.setDataSource(ctx, uri)
                        e.printStackTrace();
                    }
                }
            }
            return media_data
        }

    }
}