package com.app.lsquared.model.news_setting

import com.google.gson.annotations.SerializedName

data class NewsCrawlSettingData(
    @SerializedName("speed"       ) var speed       : Int?    = null,
    @SerializedName("bg"          ) var bg          : String? = null,
    @SerializedName("bga"         ) var bga         : Int?    = null,
    @SerializedName("bullet"      ) var bullet      : String? = null,
    @SerializedName("titleText"   ) var titleText   : String? = null,
    @SerializedName("font"        ) var font        : Font?   = Font(),
    @SerializedName("dir"         ) var dir         : String? = null,
    @SerializedName("align"       ) var align       : String? = null,
    @SerializedName("fontSizeOpt" ) var fontSizeOpt : String? = null,
    @SerializedName("fontSize"    ) var fontSize    : Int?    = null
)

data class Font (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)
