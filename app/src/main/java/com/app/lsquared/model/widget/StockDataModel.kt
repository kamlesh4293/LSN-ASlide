package com.app.lsquared.model.widget

import com.google.gson.annotations.SerializedName

data class StockDataModel (

    @SerializedName("data"     ) var data     : ArrayList<Data> = arrayListOf(),
    @SerializedName("settings" ) var settings : Settings?       = Settings()

)

data class Data (

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

data class Settings (

    @SerializedName("bg"      ) var bg      : String?  = null,
    @SerializedName("bga"     ) var bga     : String?  = null,
    @SerializedName("s"       ) var s       : String?  = null,
    @SerializedName("sSize"   ) var sSize   : Int?     = null,
    @SerializedName("lang"    ) var lang    : String?  = null,
    @SerializedName("sFont"   ) var sFont   : SFont?   = SFont(),
    @SerializedName("n"       ) var n       : String?  = null,
    @SerializedName("nSize"   ) var nSize   : Int?     = null,
    @SerializedName("nFont"   ) var nFont   : NFont?   = NFont(),
    @SerializedName("p"       ) var p       : String?  = null,
    @SerializedName("pSize"   ) var pSize   : Int?     = null,
    @SerializedName("pFont"   ) var pFont   : PFont?   = PFont(),
    @SerializedName("hp"      ) var hp      : String?  = null,
    @SerializedName("hpSize"  ) var hpSize  : Int?     = null,
    @SerializedName("hpFont"  ) var hpFont  : HpFont?  = HpFont(),
    @SerializedName("lp"      ) var lp      : String?  = null,
    @SerializedName("lpSize"  ) var lpSize  : Int?     = null,
    @SerializedName("lpFont"  ) var lpFont  : LpFont?  = LpFont(),
    @SerializedName("hpv"     ) var hpv     : String?  = null,
    @SerializedName("hpvSize" ) var hpvSize : Int?     = null,
    @SerializedName("hpvFont" ) var hpvFont : HpvFont? = HpvFont(),
    @SerializedName("lpv"     ) var lpv     : String?  = null,
    @SerializedName("lpvSize" ) var lpvSize : Int?     = null,
    @SerializedName("lpvFont" ) var lpvFont : LpvFont? = LpvFont(),
    @SerializedName("cp"      ) var cp      : String?  = null,
    @SerializedName("cn"      ) var cn      : String?  = null,
    @SerializedName("cpnSize" ) var cpnSize : Int?     = null,
    @SerializedName("cpnFont" ) var cpnFont : CpnFont? = CpnFont(),
    @SerializedName("e"       ) var e       : String?  = null,
    @SerializedName("eSize"   ) var eSize   : Int?     = null,
    @SerializedName("eFont"   ) var eFont   : EFont?   = EFont()

)

data class SFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class NFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class PFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class CpnFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class EFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class LpvFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class HpvFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class LpFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class HpFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)