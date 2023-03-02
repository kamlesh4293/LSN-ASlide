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
import androidx.core.content.FileProvider
import com.app.lsquared.ui.activity.CodContentActivity
import com.app.lsquared.utils.Constant
import com.app.lsquared.utils.DataManager
import java.io.File


class WidgetVideo {

    companion object{

        fun getWidgetVideo(ctx: Context,video: VideoView,width: Int,height: Int,fileName: String,sound: String,type:String):View{
//            var video = VideoView(ctx)
            val params = RelativeLayout.LayoutParams(width,height)
            video.layoutParams = params

            var myMediaMetadataRetriever = MediaMetadataRetriever()
            val path = DataManager.getDirectory()+ File.separator+ fileName

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                var uri = FileProvider.getUriForFile(ctx,ctx.packageName+".provider",File(path))
                myMediaMetadataRetriever.setDataSource(ctx, uri)
            }else{
                try {
                    myMediaMetadataRetriever.setDataSource(path, HashMap())
                }catch (e :RuntimeException ){
                    var uri = FileProvider.getUriForFile(ctx,ctx.packageName+".provider",File(path))
                    myMediaMetadataRetriever.setDataSource(ctx, uri)
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
                mp ->
                if (type.equals(Constant.CALLING_MAIN)) mp.start()
                else (ctx as CodContentActivity).finish()
            })
            return video
        }

        fun setVideoSound(sound: String,video: VideoView){
            if(sound.equals("no")){
                video.pause()
//                video.setOnPreparedListener(
//                    MediaPlayer.OnPreparedListener {
//                            mp -> mp.setVolume(0f, 0f)
//                    })
            }else{
                video.resume()
//                video.setOnPreparedListener(
//                    MediaPlayer.OnPreparedListener {
//                            mp -> mp.setVolume(100f, 100f)
//                    })
            }

        }

        fun getWidgetVideoVimeo(ctx: Context, url: String):View{
            val videoView = VideoView(ctx)
//            val uri: Uri = Uri.parse("https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1")
            val uri: Uri = Uri.parse(url)
            videoView.setVideoURI(uri)
            val mediaController = MediaController(ctx)
            mediaController.setAnchorView(videoView)
            mediaController.setMediaPlayer(videoView)
            videoView.setMediaController(mediaController)
            videoView.start()
            return videoView
        }


    }
}