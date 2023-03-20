package com.app.lsquared.utils

import android.Manifest.*
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.core.app.ActivityCompat
import org.json.JSONObject

import android.util.Base64
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.io.*
import android.media.MediaMetadataRetriever
import com.app.lsquared.BuildConfig
import com.app.lsquared.R
import com.app.lsquared.model.Frame
import com.app.lsquared.model.Item


class Utility {

    companion object{

        const val STORAGE_PERMISSION_CODE: Int = 1000

        // check file downloaded for downloading
        fun isFileCompleteDownloaded(filepath: String,file_size :Int):Boolean{
//            var path = "/storage/emulated/0/Download"+ File.separator+filepath
            var path = DataManager.getDirectory()+File.separator+filepath
            val file = File(path)
            if (file.exists()) {
                if(file_size == file.length().toInt())return true
                else {
                    file.delete()
                    return false
                }
            } else return false
        }

        // check file downloaded for playing
        fun isFileCompleteDownloadedForPlay(filepath: String,file_size :Int,ctx: Context):Boolean{
//            var path = "/storage/emulated/0/Download"+ File.separator+filepath
            var path = DataManager.getDirectory()+File.separator+filepath
            val file = File(path)
            if (file.exists()) {
//                var length = file.length().toInt()
                val retriever = MediaMetadataRetriever()
                try {
                    retriever.setDataSource(ctx, Uri.parse(path))
                } catch (e: Exception) {
                    e.printStackTrace()
                    return false
                }
                val hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO)
                return "yes" == hasVideo
/*
                if(file_size == length){
                    return true
                }else {
                    file.delete()
                    return false
                }
*/
            } else return false
        }

/*
        // check all permission granted
        fun checkAllPermissionGranted(ctx: Context) : Boolean{
            val pm: PackageManager = ctx.getPackageManager()
            val hasPerm = pm.checkPermission(permission.WRITE_EXTERNAL_STORAGE,ctx.getPackageName())
            val hasPerm1 = pm.checkPermission(permission.READ_PHONE_STATE,ctx.getPackageName())
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                if (hasPerm1 == PackageManager.PERMISSION_GRANTED) {
                    return true
                }
            }
            ActivityCompat.requestPermissions(ctx as Activity,
                arrayOf(permission.READ_PHONE_STATE,permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE)
            return false
        }

*/
        // check all permission granted
        fun checkAllPermissionGranted(ctx: Context) : Boolean{
            val pm: PackageManager = ctx.getPackageManager()
            val hasPerm = pm.checkPermission(permission.WRITE_EXTERNAL_STORAGE,ctx.getPackageName())
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                return true
            }
            ActivityCompat.requestPermissions(ctx as Activity,
                arrayOf(permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE)
            return false
        }


        fun getDetailsText(ctx:Context) : SpannableStringBuilder{
            val builder = SpannableStringBuilder()
            val string1 = "For help with setup. please email support@LSquared.com or call us at 1-877-344-1548 and select option 2 for customer service."
            val redSpannable = SpannableString(string1)
            redSpannable.setSpan(ForegroundColorSpan(Color.BLACK), 0, string1.length, 0)
            redSpannable.setSpan(ForegroundColorSpan(ctx.resources.getColor(R.color.border_green111)), 34, 54, 0)
            redSpannable.setSpan(StyleSpan(Typeface.BOLD),68,84, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            builder.append(redSpannable)
            return builder
        }

        fun deviceInfoToJson(
            mac: String, res: String, device_name: String, ip_address: String,
            dis_total: Long, dis_used: Long, memory_total: Long, memory_used: Long,
            serial_number:String,model_name:String,connection_type:String,androidVersion:String,
            wifi_mac:String
        ): JSONObject {
            val rootObject = JSONObject()
            rootObject.put("mac",mac)
            rootObject.put("app",BuildConfig.VERSION_NAME)
            rootObject.put("watcher","")
            rootObject.put("os",7)
            rootObject.put("client",7)
            rootObject.put("res",res)
            rootObject.put("computerName",device_name)
            rootObject.put("local_addr",ip_address)
            rootObject.put("appStart","")

            // info object
            val infoObject = JSONObject()
            infoObject.put("DiskTotal",dis_total)
            infoObject.put("DiskUsed",dis_used)
            infoObject.put("MemoryTotal",memory_total)
            infoObject.put("MemoryUsed",memory_used)
            infoObject.put("AndroidVersion",androidVersion)
            infoObject.put("ModelSerialNumber",serial_number)
            infoObject.put("ModelName",model_name)
            infoObject.put("ConnecteionType",connection_type)
            infoObject.put("WiFi-MAC",wifi_mac)

            rootObject.put("info",infoObject)
            return rootObject
        }


        // submit screenshot
        fun getScreenshotJson(mac:String,base_64:String): JSONObject {
            val rootObject = JSONObject()
            rootObject.put("mac",mac)
            rootObject.put("base64",base_64)
            rootObject.put("type","ss")
            return rootObject
        }

        // submit record
        fun getRecords(device_id:String,report_data:String):JSONObject{
            val rootObject = JSONObject()
            rootObject.put("mac",device_id)
            rootObject.put("data",report_data)
            return rootObject
        }


        // image file to base 64
        open fun getFileToByte(filePath: String?): String {
            val baos = ByteArrayOutputStream()
            val bitmap = BitmapFactory.decodeFile(filePath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageBytes = baos.toByteArray()
            val encodeString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            return encodeString!!
        }

        // bitmap to base64
        open fun encodeImage(bm: Bitmap): String {
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        // get current date
        fun getCurrentdate():String{
            var dateFormat = SimpleDateFormat("yyyy-MM-dd")
            var timeFormat = SimpleDateFormat("HH:mm:ss")
            var date = Date()
            return dateFormat.format(date)+"T"+timeFormat.format(date)
        }
        
        fun deleteFiles(list: List<String>){
            if (list != null && list.size>0){
                for (i in 0..list.size-1){
                    var path = DataManager.getDirectory()+File.separator+list[i]
                    var file = File(DataManager.getDirectory()+File.separator+list[i])
                    if(file.exists()){
                        file.delete()
                        Log.d("UTILITY", "deleteFiles: $path")
                    }
                }
            }
        }

        fun getFilterFrameList(frame: List<Frame>): List<Frame> {
            val newlist = frame.sortedBy { f->f.z }
            return newlist
        }

        fun getFileName(item:Item): String{
            var id = item.id.substring(item.id.lastIndexOf("-"))
            var id_short = id.substring(1,id.length)
            val extension: String = item.src.substring(item.src.lastIndexOf("."))
            return "$id_short$extension"
        }

        fun get_Resized_Bitmap(bmp: Bitmap, newHeight: Int, newWidth: Int): Bitmap? {
            val width = bmp.width
            val height = bmp.height
            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height
            // CREATE A MATRIX FOR THE MANIPULATION
            val matrix = Matrix()
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight)

            // "RECREATE" THE NEW BITMAP
            return Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, false)
        }

        // for new codebase
        // check all permission granted
        fun checkPermissionGranted(ctx: Context) : Boolean{
            val pm: PackageManager = ctx.getPackageManager()
            val hasPerm = pm.checkPermission(permission.WRITE_EXTERNAL_STORAGE,ctx.getPackageName())
            val hasPerm1 = pm.checkPermission(permission.READ_PHONE_STATE,ctx.getPackageName())
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                if (hasPerm1 == PackageManager.PERMISSION_GRANTED) {
                    return true
                }
            }
            return false
        }


    }

}