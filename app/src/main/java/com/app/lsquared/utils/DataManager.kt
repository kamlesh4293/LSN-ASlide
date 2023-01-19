package com.app.lsquared.utils

import android.util.Log
import com.app.lsquared.model.Downloadable
import java.io.File

class DataManager {


    companion object{

        val APP_DIRECTORY =  "LSquared"
        val APP_SDSTETH_DIRECTORY =  "HDSteth"
        val APP_SCREEN_DIRECTORY =  APP_DIRECTORY+"/Screen"

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
            Log.d("DM Files", "Path: $path")
            val directory = File(path)
            val files = directory.listFiles()
            Log.d("DM Files", "Size: " + files.size)
            return files
        }

        fun getListDownloadable (downloadable: List<Downloadable>?) : MutableList<String>{
            var list = mutableListOf<String>()
            if(downloadable!=null && downloadable.size>0){
                for (i in 0..downloadable.size-1)list.add(downloadable[i].name)
            }
            return list
        }

    }



}