package com.app.lsquared.utils

import android.os.Environment
import java.io.File

class Constant {

    // release key  and pass - Lsquared

    // vivo - cd7598568b48f67
    // realme - 899077be05e0bc19
    // honour - 6d3ccf7cadabff68
    // samsung testing - 8f888a7c1385f848
    // mahesh sir device- c0de742442c77ccc

    // ankit mob - d06d4e782d67ca3f
    // emu-android17 - 7f256e818ca50e01


    companion object{

            // development environment
//        const val BASE_URL = "https://rc.lsquared.com/"
//        const val BASE_FILE_URL = BASE_URL+"rc-lsquared-hub/"

        // US production environment
        const val BASE_URL = "https://us.lsquared.com/"
        const val BASE_FILE_URL = BASE_URL+"lsquared-hub/"

        // HUB production environment
//        const val BASE_URL = "https://hub.lsquared.com/"
//        const val BASE_FILE_URL = "https://s3-us-west-2.amazonaws.com/lsquared-hub/"


        // refresh from
        const val REFRESH_FROM_CONTENT = "refresh_from_content"
        const val REFRESH_FROM_NODEVICE = "refresh_from_nodevice"
        const val REFRESH_FROM_WAITING = "refresh_from_waiting"
        const val REFRESH_FROM_CHANGE_INTERNET = "refresh_from_change_internet"
        const val REFRESH_FROM_BACKGROUND = "refresh_from_background"

        // content type

        const val CONTENT_IMAGE = "image"
        const val CONTENT_VECTOR = "vector"
        const val CONTENT_POWERPOINT = "powerpoint"
        const val CONTENT_WORD = "word"
        const val CONTENT_VIDEO = "video"
        const val CONTENT_WEB = "webPage"
    }

}