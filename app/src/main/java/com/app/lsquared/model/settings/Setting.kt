package com.app.lsquared.model.settings

import com.google.gson.annotations.SerializedName

data class SettingQuotes (

    @SerializedName("bg"          ) var bg          : String?     = null,
    @SerializedName("bga"         ) var bga         : String?     = null,
    @SerializedName("quotesText"  ) var quotesText  : String?     = null,
    @SerializedName("authorText"  ) var authorText  : String?     = null,
    @SerializedName("rotationOpt" ) var rotationOpt : String?     = null,
    @SerializedName("rotate"      ) var rotate      : Int?        = null,
    @SerializedName("quotesSize"  ) var quotesSize  : Int?        = null,
    @SerializedName("authorSize"  ) var authorSize  : Int?        = null,
    @SerializedName("quotesFont"  ) var quotesFont  : QuotesFont? = QuotesFont(),
    @SerializedName("authorFont"  ) var authorFont  : AuthorFont? = AuthorFont()

)

data class QuotesFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class AuthorFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)