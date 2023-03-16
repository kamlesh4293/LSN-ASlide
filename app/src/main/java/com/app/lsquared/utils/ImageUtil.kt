package com.app.lsquared.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.HandlerThread
import android.text.format.DateFormat
import android.transition.Transition
import android.view.PixelCopy
import android.view.SurfaceView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.lsquared.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.exoplayer2.ui.StyledPlayerView
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



        // linear layout
        fun screenshot(view: LinearLayout, filename: String): File? {
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

        // relative layout
        fun screenshot(view: StyledPlayerView, filename: String): File? {
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
            if(!url.equals("")){
                imageView.visibility = View.VISIBLE
                Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .fitCenter()
                    .into(BitmapImageViewTarget(imageView))
            }else imageView.visibility = View.VISIBLE
        }

        fun loadImageWithoutBaseURL(context:Context,url:String,imageView: ImageView){
            Glide.with(context)
                .load(Constant.BASE_FILE_URL+url)
                .into(imageView)
        }

        fun loadLocalImage(fileName:String,imageView: ImageView){
            imageView.visibility = View.VISIBLE
            val options = BitmapFactory.Options()
            options.inSampleSize = 1
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            options.inJustDecodeBounds = false

            val path = DataManager.getDirectory()+ File.separator+fileName
            imageView.setImageBitmap(BitmapFactory.decodeFile(path,options))
        }


        fun loadGifImage(context:Context,imageView: ImageView){
            Glide.with(context).asGif()
                .load(R.raw.download)
                .into(imageView)
        }

    }
}