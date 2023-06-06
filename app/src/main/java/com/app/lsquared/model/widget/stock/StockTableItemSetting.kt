package com.app.lsquared.model.widget.stock

import com.google.gson.annotations.SerializedName

data class StockTableItemSetting (

    @SerializedName("rotationOpt" ) var rotationOpt : String? = null,
    @SerializedName("rotate"      ) var rotate      : Int?    = null,
    @SerializedName("s"           ) var s           : String? = null,
    @SerializedName("n"           ) var n           : String? = null,
    @SerializedName("p"           ) var p           : String? = null,
    @SerializedName("cp"          ) var cp          : String? = null,
    @SerializedName("cn"          ) var cn          : String? = null,
    @SerializedName("header"      ) var header      : Header? = Header(),
    @SerializedName("hb"          ) var hb          : String? = null,
    @SerializedName("hba"         ) var hba         : Int?    = null,
    @SerializedName("ht"          ) var ht          : String? = null,
    @SerializedName("hf"          ) var hf          : Hf?     = Hf(),
    @SerializedName("hs"          ) var hs          : Int?    = null,
    @SerializedName("rb"          ) var rb          : String? = null,
    @SerializedName("rba"         ) var rba         : Int?    = null,
    @SerializedName("ab"          ) var ab          : String? = null,
    @SerializedName("aba"         ) var aba         : Int?    = null,
    @SerializedName("rf"          ) var rf          : Rf?     = Rf(),
    @SerializedName("rs"          ) var rs          : Int?    = null,
    @SerializedName("template"    ) var template    : String? = null

)

data class Header (

    @SerializedName("active"  ) var active  : Boolean? = null,
    @SerializedName("column1" ) var column1 : String?  = null,
    @SerializedName("column2" ) var column2 : String?  = null,
    @SerializedName("column3" ) var column3 : String?  = null,
    @SerializedName("column4" ) var column4 : String?  = null

)

data class Hf (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class Rf (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)