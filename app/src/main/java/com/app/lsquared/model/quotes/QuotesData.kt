package com.app.lsquared.model.quotes

import com.google.gson.annotations.SerializedName

data class QuotesData(
    @SerializedName("info"  ) var info  : ArrayList<Info>  = arrayListOf(),
    @SerializedName("quote" ) var quote : ArrayList<Quote> = arrayListOf()
)

data class Info(
    @SerializedName("bgImg" ) var bgImg : String? = null
)

data class Quote(
    @SerializedName("id"     ) var id     : Int?    = null,
    @SerializedName("quote"  ) var quote  : String? = null,
    @SerializedName("author" ) var author : String? = null
)