package com.app.lsquared.model.news_setting

import com.google.gson.annotations.SerializedName

data class NewsListSettingData(

    @SerializedName("rotationOpt"  ) var rotationOpt  : String?     = null,
    @SerializedName("rotate"       ) var rotate       : Int?        = null,
    @SerializedName("hTextOpt"     ) var hTextOpt     : String?     = null,
    @SerializedName("hText"        ) var hText        : String?     = null,
    @SerializedName("headerBg"     ) var headerBg     : String?     = null,
    @SerializedName("headerText"   ) var headerText   : String?     = null,
    @SerializedName("headerFont"   ) var headerFont   : HeaderFont? = HeaderFont(),
    @SerializedName("isbg"         ) var isbg         : Boolean?    = null,
    @SerializedName("bg"           ) var bg           : String?     = null,
    @SerializedName("rowBg"        ) var rowBg        : String?     = null,
    @SerializedName("altRowBg"     ) var altRowBg     : String?     = null,
    @SerializedName("titleText"    ) var titleText    : String?     = null,
    @SerializedName("descText"     ) var descText     : String?     = null,
    @SerializedName("rowFont"      ) var rowFont      : RowFont?    = RowFont(),
    @SerializedName("altTitleText" ) var altTitleText : String?     = null,
    @SerializedName("altDescText"  ) var altDescText  : String?     = null,
    @SerializedName("altRowFont"   ) var altRowFont   : AltRowFont? = AltRowFont(),
    @SerializedName("headerSize"   ) var headerSize   : Int?        = null,
    @SerializedName("titleSize"    ) var titleSize    : Int?        = null,
    @SerializedName("descSize"     ) var descSize     : Int?        = null
)

data class HeaderFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class RowFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class AltRowFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)