package com.app.lsquared.model.widget

import com.google.gson.annotations.SerializedName

data class StockMultipleDataNSetting (
    @SerializedName("data"     ) var data     : ArrayList<DataMS> = arrayListOf(),
    @SerializedName("settings" ) var settings : SettingsMs?       = SettingsMs(),
    @SerializedName("error"    ) var error    : Error?          = Error()
)

data class DataMS (

    @SerializedName("ccy"                  ) var ccy                : String? = null,
    @SerializedName("exch"                 ) var exch               : String? = null,
    @SerializedName("id"                   ) var id                 : String? = null,
    @SerializedName("src"                  ) var src                : String? = null,
    @SerializedName("day_change"           ) var dayChange          : String? = null,
    @SerializedName("change_pct"           ) var changePct          : String? = null,
    @SerializedName("price"                ) var price              : String? = null,
    @SerializedName("high"                 ) var high               : String? = null,
    @SerializedName("low"                  ) var low                : String? = null,
    @SerializedName("dateTime"             ) var dateTime           : String? = null,
    @SerializedName("symbol"               ) var symbol             : String? = null,
    @SerializedName("country"              ) var country            : String? = null,
    @SerializedName("name"                 ) var name               : String? = null,
    @SerializedName("stock_exchange_short" ) var stockExchangeShort : String? = null

)

data class Font (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class SettingsMs (

    @SerializedName("bg"          ) var bg          : String? = null,
    @SerializedName("bga"         ) var bga         : String?  = null,
    @SerializedName("s"           ) var s           : String? = null,
    @SerializedName("n"           ) var n           : String? = null,
    @SerializedName("p"           ) var p           : String? = null,
    @SerializedName("cp"          ) var cp          : String? = null,
    @SerializedName("cn"          ) var cn          : String? = null,
    @SerializedName("b"           ) var b           : String? = null,
    @SerializedName("speed"       ) var speed       : Int?    = null,
    @SerializedName("font"        ) var font        : Font?   = Font(),
    @SerializedName("fontSizeOpt" ) var fontSizeOpt : String? = null,
    @SerializedName("fontSize"    ) var fontSize    : Int?    = null

)

data class Info (

    @SerializedName("credit_count" ) var creditCount : Int? = null

)

data class Message (

    @SerializedName("status" ) var status : Boolean? = null,
    @SerializedName("code"   ) var code   : Int?     = null,
    @SerializedName("msg"    ) var msg    : String?  = null,
    @SerializedName("info"   ) var info   : Info?    = Info()

)

data class Error (

    @SerializedName("status"  ) var status  : String?  = null,
    @SerializedName("desc"    ) var desc    : String?  = null,
    @SerializedName("Message" ) var Message : Message? = Message(),
    @SerializedName("code"    ) var code    : Int?     = null,
    @SerializedName("symbol"  ) var symbol  : String?  = null

)