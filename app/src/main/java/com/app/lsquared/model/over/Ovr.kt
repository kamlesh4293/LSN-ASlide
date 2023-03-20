package com.app.lsquared.model.over

import com.app.lsquared.model.Frame
import com.google.gson.annotations.SerializedName

data class Ovr (

    @SerializedName("id"    ) var id    : Int?             = null,
    @SerializedName("frame" ) var frame : ArrayList<Frame> = arrayListOf()

)
