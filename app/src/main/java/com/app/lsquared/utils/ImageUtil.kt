package com.app.lsquared.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.HandlerThread
import android.text.format.DateFormat
import android.view.PixelCopy
import android.view.SurfaceView
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ImageUtil {

    companion object{

        fun getImageOption(): BitmapFactory.Options {
            var options = BitmapFactory.Options()
            options.inSampleSize = 1
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            options.inJustDecodeBounds = false
            return options
        }

        // relative layout
        fun screenshot(view: RelativeLayout, filename: String): File? {
            if(view==null) return null
            val date = Date()
            val format = DateFormat.format("yyyy-MM-dd_hh:mm:ss", date)
            try {
                val dirpath = DataManager.getScreenShotDirectory()
                val file = File(dirpath)
                if (!file.exists()) {
                    val mkdir = file.mkdir()
                }
                val path = "$dirpath/$filename-$format.jpeg"
                view.isDrawingCacheEnabled = true
                val bitmap = Bitmap.createBitmap(view.drawingCache)
                view.isDrawingCacheEnabled = false
                val imageurl = File(path)
                val outputStream = FileOutputStream(imageurl)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                outputStream.flush()
                outputStream.close()
                return imageurl
            } catch (io: FileNotFoundException) {
                io.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }catch (e: NullPointerException) {
                e.printStackTrace()
            }
            return null
        }


        // constraint layout
        fun screenshot(view: ConstraintLayout, filename: String): File? {
            val date = Date()
            val format = DateFormat.format("yyyy-MM-dd_hh:mm:ss", date)
            try {
                val dirpath = DataManager.getScreenShotDirectory()
                val file = File(dirpath)
                if (!file.exists()) {
                    val mkdir = file.mkdir()
                }
                val path = "$dirpath/$filename-$format.jpeg"
                view.isDrawingCacheEnabled = true
                val bitmap = Bitmap.createBitmap(view.drawingCache)
                view.isDrawingCacheEnabled = false
                val imageurl = File(path)
                val outputStream = FileOutputStream(imageurl)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                outputStream.flush()
                outputStream.close()
                return imageurl
            } catch (io: FileNotFoundException) {
                io.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        fun loadImage(context:Context,url:String,imageView: ImageView){
            Glide.with(context)
                .load(url)
                .into(imageView)
        }

        fun usePixelCopy(videoView: SurfaceView, callback: (Bitmap?) -> Unit) {
            val bitmap: Bitmap = Bitmap.createBitmap(
                videoView.width,
                videoView.height,
                Bitmap.Config.ARGB_8888
            );
            try {
                // Create a handler thread to offload the processing of the image.
                val handlerThread = HandlerThread("PixelCopier");
                handlerThread.start();
                PixelCopy.request(
                    videoView, bitmap,
                    PixelCopy.OnPixelCopyFinishedListener { copyResult ->
                        if (copyResult == PixelCopy.SUCCESS) {
                            callback(bitmap)
                        }
                        handlerThread.quitSafely();
                    },
                    Handler(handlerThread.looper)
                )
            } catch (e: IllegalArgumentException) {
                callback(null)
                // PixelCopy may throw IllegalArgumentException, make sure to handle it
                e.printStackTrace()
            }
        }

    }
}