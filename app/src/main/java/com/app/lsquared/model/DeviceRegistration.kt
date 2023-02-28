package com.app.lsquared.model

import com.google.gson.annotations.SerializedName

data class DeviceRegistration (

    @SerializedName("status" ) var status : String? = null,
    @SerializedName("desc"   ) var desc   : String? = null,
    @SerializedName("code"   ) var code   : Int?    = null

)
