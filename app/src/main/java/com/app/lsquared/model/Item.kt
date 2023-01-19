package com.app.lsquared.model

import com.google.gson.annotations.SerializedName

data class Item (

    @SerializedName("id") val id : String,
    @SerializedName("sid") val sid : Int,
    @SerializedName("duration") val duration : Double,
    @SerializedName("type") val type : String,
    @SerializedName("filesize") val filesize : Int,
    @SerializedName("fileName") val fileName : String,
    @SerializedName("src") var src : String,
    @SerializedName("fs") val fs : String,
    @SerializedName("dType") val dType : String = "",
    @SerializedName("downloadable") val downloadable : Boolean,
    @SerializedName("active") val active : Int,
    @SerializedName("scale") val scale : String,
    @SerializedName("mute") val mute : Int = -1,
    @SerializedName("sound") val sound : String = "",
    @SerializedName("settings") val settings : String = "",
    @SerializedName("actualDuration") val actualDuration : Float = -1.0f,
    @SerializedName("forecast") val forecast : Int = 0,
    @SerializedName("params") val youtube_param : String = "",
    var frame_h : Int = 0,
    var frame_w : Int = 0,
    var pos : Int = -1
)