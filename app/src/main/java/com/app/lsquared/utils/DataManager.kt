package com.app.lsquared.utils

import android.util.Log
import com.app.lsquared.model.Downloadable
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter

class DataManager {


    companion object{

        val APP_DIRECTORY =  "LSquared"
        val APP_SDSTETH_DIRECTORY =  "HDSteth"
        val APP_SCREEN_DIRECTORY =  APP_DIRECTORY+"/Screen"
        val APP_REPORT_DIRECTORY =  APP_DIRECTORY+"/Report"

        fun getDirectory():String?{
//            val root = Environment.getExternalStorageDirectory().toString()
//            val myDir = File("$root/$APP_DIRECTORY")
//            if(!myDir.exists())myDir.mkdirs()
//            return myDir.absolutePath

            var file = MyApplication.applicationContext().getExternalFilesDir(APP_DIRECTORY)
            return file?.absolutePath
        }


        fun getScreenShotDirectory():String?{
//            val root = Environment.getExternalStorageDirectory().toString()
//            val myDir = File("$root/$APP_SCREEN_DIRECTORY")
//            if(!myDir.exists())myDir.mkdirs()
//            return myDir.absolutePath

            var file = MyApplication.applicationContext().getExternalFilesDir(APP_SCREEN_DIRECTORY)
            return file?.absolutePath

        }


        fun fileIsExist(downloadable: Downloadable):Boolean{
//            var filename = downloadable.name?.replace("1602585267.1767-mailbox-and-other-services.mp4","1602585267.1767-mailbox-and-other-services123.mp4")
//            var path = getDirectory()+File.separator+filename
            var path = getDirectory()+File.separator+downloadable.name
            val file = File(path)
            return file.exists()
        }

        fun deleteFile(filename:String){
            var path = getDirectory()+File.separator+filename
            val file = File(path)
            if(file.exists())file.delete()
        }

        fun getAllDirectoryFiles() : Array<File>{
            val path = getDirectory()
            val directory = File(path)
            val files = directory.listFiles()
            return files
        }

        fun getListDownloadable (downloadable: List<Downloadable>?) : MutableList<String>{
            var list = mutableListOf<String>()
            if(downloadable!=null && downloadable.size>0){
                for (i in 0..downloadable.size-1){
                    list.add(downloadable[i].name)
                }
            }
            return list
        }

        fun getReportDirectory():String?{
            var file = MyApplication.applicationContext().getExternalFilesDir(APP_REPORT_DIRECTORY)
            return file?.absolutePath
        }

        fun getReportFileList():Array<File> {
            var file = MyApplication.applicationContext().getExternalFilesDir(APP_REPORT_DIRECTORY)
            return file?.listFiles()!!
        }



        // create file for saved data
        fun createReportFile(pref: MySharePrefernce?) {
            var data = pref?.getStoreReportdata()
            if(!data.equals("")){
                Log.d("TAG", "ReportFile: data $data")
                var filename = System.currentTimeMillis().toString()+".csv"
                try {
                    val gpxfile = File(getReportDirectory(), filename)
                    val writer = FileWriter(gpxfile)
                    writer.append(data.toString().trim())
                    writer.flush()
                    writer.close()
                    pref?.clearReportdata()
                } catch (e: Exception) {
                }
            }else{
                Log.d("TAG", "ReportFile: No data For create File")
            }
        }

        // download content confirmation data
        fun getContentConfirmationData(downloads_list: List<Downloadable>): JSONObject {
            var data_obj = JSONObject()
            var array = JSONArray()
            var time = DateTimeUtil.createTimeForContentConfirmationUTC()
            if (downloads_list!=null && downloads_list.size>0){
                for (item in downloads_list){
                    var id = item.id.split("-")
                    if(id.size>1){
                        var obj = JSONObject()
                        obj.put("ctime",time)
                        obj.put("id",id[2].toInt())
                        obj.put("label",item.name)
                        obj.put("type",item.type)
                        array.put(obj)
                    }
                }
                data_obj.put("items",array)
            }
            return data_obj
        }


    }

}