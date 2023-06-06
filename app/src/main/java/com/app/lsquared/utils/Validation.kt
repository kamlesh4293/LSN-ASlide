package com.app.lsquared.utils

import android.util.Log
import com.app.lsquared.model.Item
import java.io.File

class Validation {

    companion object{

        fun isItemDownloaded(item: Item): Boolean {

            val path = DataManager.getDirectory()+File.separator+ item.fileName
            if(item.type == Constant.CONTENT_IMAGE ||item.type == Constant.CONTENT_VIDEO
                || item.type == Constant.CONTENT_VECTOR ||item.type == Constant.CONTENT_POWERPOINT
                || item.type == Constant.CONTENT_WORD ||item.type == Constant.CONTENT_WIDGET_QRCODE)
            {
                return File(path).exists()
            }
            return true
        }

    }
}