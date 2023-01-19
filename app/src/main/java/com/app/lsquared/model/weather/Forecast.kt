package com.app.lsquared.model

import com.google.gson.annotations.SerializedName


data class Forecast (

  @SerializedName("temp_min" ) var tempMin : Double? = null,
  @SerializedName("temp_max" ) var tempMax : Double? = null,
  @SerializedName("icon"     ) var icon    : Int?    = null,
  @SerializedName("desc"     ) var desc    : String? = null,
  @SerializedName("dt"       ) var dt      : String? = null

)