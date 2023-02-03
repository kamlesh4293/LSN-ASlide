package com.app.lsquared.model.cod

import com.google.gson.annotations.SerializedName

data class Cods (

    @SerializedName("settings" ) var settings : Settings? = Settings(),
    @SerializedName("ctype"    ) var ctype    : Ctype?    = Ctype()

)

data class Settings (

    @SerializedName("bg"        ) var bg        : String?   = null,
    @SerializedName("bga"       ) var bga       : String?      = null,
    @SerializedName("text"      ) var text      : String?   = null,
    @SerializedName("iconP"     ) var iconP     : String?   = null,
    @SerializedName("textStyle" ) var textStyle : String?   = null,
    @SerializedName("textSize"  ) var textSize  : Int?      = null,
    @SerializedName("textColor" ) var textColor : String?   = null,
    @SerializedName("textFont"  ) var textFont  : TextFont? = TextFont()
)

data class Ctype (

    @SerializedName("bg"        ) var bg        : String? = null,
    @SerializedName("textColor" ) var textColor : String? = null

)

data class TextFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)