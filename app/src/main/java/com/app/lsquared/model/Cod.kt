package com.app.lsquared.model

import com.google.gson.annotations.SerializedName

data class Cod (

    @SerializedName("sort"      ) var sort      : String?         = null,
    @SerializedName("id"        ) var id        : Int?            = null,
    @SerializedName("name"      ) var name      : String?         = null,
    @SerializedName("w"         ) var w         : Int?            = null,
    @SerializedName("h"         ) var h         : Int?            = null,
    @SerializedName("x"         ) var x         : Int?            = null,
    @SerializedName("y"         ) var y         : Int?            = null,
    @SerializedName("z"         ) var z         : Int?            = null,
    @SerializedName("r"         ) var r         : Int?            = null,
    @SerializedName("bg"        ) var bg        : String?         = null,
    @SerializedName("align"     ) var align     : String?         = null,
    @SerializedName("a"         ) var a         : String?         = null,
    @SerializedName("bga"       ) var bga       : Int?            = null,
    @SerializedName("timeRange" ) var timeRange : Int?            = null,
    @SerializedName("tr"        ) var tr        : Int?            = null,
    @SerializedName("st"        ) var st        : String?         = null,
    @SerializedName("et"        ) var et        : String?         = null,
    @SerializedName("item"      ) var item      : ArrayList<CodItem> = arrayListOf()

)

data class CodItem (

    @SerializedName("id"       ) var id       : String?        = null,
    @SerializedName("sid"      ) var sid      : Int?           = null,
    @SerializedName("duration" ) var duration : Int?           = null,
    @SerializedName("type"     ) var type     : String?        = null,
    @SerializedName("ovr"      ) var ovr      : Int?           = null,
    @SerializedName("name"     ) var name     : String?        = null,
    @SerializedName("settings" ) var settings : String?        = null,
    @SerializedName("active"   ) var active   : Int?           = null,
    @SerializedName("scale"    ) var scale    : String?        = null,
    @SerializedName("cat"      ) var cat      : ArrayList<Cat> = arrayListOf()

)

data class Cat (

    @SerializedName("id"      ) var id      : Int?               = null,
    @SerializedName("label"   ) var label   : String?            = null,
    @SerializedName("content" ) var content : ArrayList<Content> = arrayListOf()

)

data class Content(

    @SerializedName("id"              ) var id              : String?            = null,
    @SerializedName("label"           ) var label           : String?            = null,
    @SerializedName("fileName"        ) var fileName        : String?            = null,
    @SerializedName("src"             ) var src             : String?            = null,
    @SerializedName("sound"           ) var sound           : String?            = null,
    @SerializedName("ifr"             ) var ifr             : String?            = null,
    @SerializedName("url"             ) var url             : String?            = null,
    @SerializedName("mute"            ) var mute            : Int?               = null,
    @SerializedName("captionName"     ) var captionName     : String?            = null,
    @SerializedName("settings"        ) var settings        : String?            = null,
    @SerializedName("params"          ) var params          : String?            = null,
    @SerializedName("thumb"           ) var thumb           : String?            = null,
    @SerializedName("captionSrc"      ) var captionSrc      : String?            = null,
    @SerializedName("captionVersion"  ) var captionVersion  : String?            = null,
    @SerializedName("captionFilesize" ) var captionFilesize : String?            = null,
    @SerializedName("ccn"             ) var ccn             : String?            = null,
    @SerializedName("ccp"             ) var ccp             : String?            = null,
    @SerializedName("dType"           ) var dType           : String?            = null,
    @SerializedName("ccv"             ) var ccv             : Int?               = null,
    @SerializedName("ccs"             ) var ccs             : Int?               = null,
    @SerializedName("cue"             ) var cue             : String?            = null,
    @SerializedName("duration"        ) var duration        : Float?             = null,
    @SerializedName("fileType"        ) var fileType        : String?            = null,
    @SerializedName("type"            ) var type            : String?            = null,
    @SerializedName("filesize"        ) var filesize        : Int?               = null,
    @SerializedName("downloadable"    ) var downloadable    : String?            = null,
    @SerializedName("active"          ) var active          : Int?               = null,
    @SerializedName("caption"         ) var caption         : ArrayList<Caption> = arrayListOf()
) :java.io.Serializable

data class Caption (

    @SerializedName("ccn" ) var ccn : String? = null,
    @SerializedName("ccp" ) var ccp : String? = null,
    @SerializedName("ccv" ) var ccv : Int?    = null,
    @SerializedName("ccs" ) var ccs : Int?    = null,
    @SerializedName("ccl" ) var ccl : String? = null

):java.io.Serializable