
import com.app.lsquared.model.Forecast
import com.google.gson.annotations.SerializedName


data class WeatherFive (

  @SerializedName("current"  ) var current  : Current?            = Current(),
  @SerializedName("forecast" ) var forecast : ArrayList<Forecast> = arrayListOf()

)