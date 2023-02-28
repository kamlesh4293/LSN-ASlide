package com.app.lsquared.model.over

import com.google.gson.annotations.SerializedName

data class Ovr (

    @SerializedName("id"    ) var id    : Int?             = null,
    @SerializedName("frame" ) var frame : ArrayList<Frame> = arrayListOf()

)

data class Frame (

    @SerializedName("id"   ) var id   : Int?            = null,
    @SerializedName("tr"   ) var tr   : String?         = null,
    @SerializedName("st"   ) var st   : String?         = null,
    @SerializedName("et"   ) var et   : String?         = null,
    @SerializedName("item" ) var item : ArrayList<Item> = arrayListOf()

)

data class Item (

    @SerializedName("id"           ) var id           : String? = null,
    @SerializedName("sid"          ) var sid          : Int?    = null,
    @SerializedName("duration"     ) var duration     : Int?    = null,
    @SerializedName("type"         ) var type         : String? = null,
    @SerializedName("ovr"          ) var ovr          : Int?    = null,
    @SerializedName("filesize"     ) var filesize     : Int?    = null,
    @SerializedName("fileName"     ) var fileName     : String? = null,
    @SerializedName("src"          ) var src          : String? = null,
    @SerializedName("fs"           ) var fs           : String? = null,
    @SerializedName("downloadable" ) var downloadable : String? = null,
    @SerializedName("active"       ) var active       : Int?    = null,
    @SerializedName("scale"        ) var scale        : String? = null

)