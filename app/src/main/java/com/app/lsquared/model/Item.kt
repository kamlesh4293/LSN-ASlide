package com.app.lsquared.model

import com.google.gson.annotations.SerializedName

data class Item (

    @SerializedName("id") val id : String,
    @SerializedName("sid") val sid : Int,
    @SerializedName("duration") val duration : Double,
    @SerializedName("ifr") var ifr : String? = null,
    @SerializedName("type") val type : String,
    @SerializedName("filesize") val filesize : Int,
    @SerializedName("url") var url : String? = null,
    @SerializedName("fileName") val fileName : String,
    @SerializedName("src") var src : String,
    @SerializedName("fs") val fs : String? = null,
    @SerializedName("dType") val dType : String = "",
    @SerializedName("downloadable") val downloadable : Boolean,
    @SerializedName("active") val active : Int,
    @SerializedName("scale") val scale : String,
    @SerializedName("mute") val mute : Int = -1,
    @SerializedName("sound") val sound : String = "",
    @SerializedName("settings") val settings : String = "",
    @SerializedName("data") val data : String = "",
    @SerializedName("actualDuration") val actualDuration : Float = -1.0f,
    @SerializedName("forecast") val forecast : Int = 0,
    @SerializedName("params") val params : String = "",
    @SerializedName("content"  ) var content  : ArrayList<ItemContent> = arrayListOf(),
    var frame_h : Int = 0,
    var frame_w : Int = 0,
    var frame_setting : String = "",
    var pos : Int = -1
)

data class ItemContent (

    @SerializedName("id"           ) var id           : String? = null,
    @SerializedName("fileName"     ) var fileName     : String = "",
    @SerializedName("src"          ) var src          : String? = null,
    @SerializedName("filesize"     ) var filesize     : Int?    = null,
    @SerializedName("downloadable" ) var downloadable : String? = null,
    @SerializedName("type"         ) var type         : String? = null

)