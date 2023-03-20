package com.app.lsquared.model

import com.google.gson.annotations.SerializedName

data class EmergencyMessage(
    @SerializedName("messages" ) var messages : ArrayList<Messages> = arrayListOf()
)

data class Msg (

    @SerializedName("title"    ) var title    : String? = null,
    @SerializedName("duration" ) var duration : Int?    = null,
    @SerializedName("audio"    ) var audio    : String? = null

)

data class Messages (

    @SerializedName("img"      ) var img      : String?        = null,
    @SerializedName("settings" ) var settings : String?        = null,
    @SerializedName("msg"      ) var msg      : ArrayList<Msg> = arrayListOf()

)

data class EmSetting(
    @SerializedName("hText"      ) var hText      : String? = null,
    @SerializedName("hFont"      ) var hFont      : HFont?  = HFont(),
    @SerializedName("hBgColor"   ) var hBgColor   : String? = null,
    @SerializedName("hTextColor" ) var hTextColor : String? = null,
    @SerializedName("bBgColor"   ) var bBgColor   : String? = null,
    @SerializedName("mBgColor"   ) var mBgColor   : String? = null,
    @SerializedName("mTextColor" ) var mTextColor : String? = null,
    @SerializedName("mFont"      ) var mFont      : MFont?  = MFont()
)

data class HFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class MFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)