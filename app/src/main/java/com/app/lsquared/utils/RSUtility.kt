package com.app.lsquared.utils

import android.util.Log

class RSUtility {

    companion object{

        fun hexDecimalToByteArray(hexString: String): ByteArray {
            val byteArray = hexString.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
            Log.d("TAG", "bytearray : ${byteArray.contentToString()}")
            return byteArray
        }

    }
}