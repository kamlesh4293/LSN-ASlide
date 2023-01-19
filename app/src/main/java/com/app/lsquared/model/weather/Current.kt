import com.google.gson.annotations.SerializedName


data class Current (

  @SerializedName("city"     ) var city     : String? = null,
  @SerializedName("country"  ) var country  : String? = null,
  @SerializedName("desc"     ) var desc     : String? = null,
  @SerializedName("icon"     ) var icon     : Int?    = null,
  @SerializedName("temp"     ) var temp     : Double? = null,
  @SerializedName("temp_min" ) var tempMin  : Double? = null,
  @SerializedName("temp_max" ) var tempMax  : Double? = null,
  @SerializedName("humidity" ) var humidity : Int?    = null,
  @SerializedName("source"   ) var source   : String? = null,
  @SerializedName("wind"     ) var wind     : Double? = null

)