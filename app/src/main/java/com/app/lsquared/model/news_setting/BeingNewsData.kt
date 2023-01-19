package com.app.lsquared.model.news_setting

import com.google.gson.annotations.SerializedName

data class BeingNewsData(
    @SerializedName("news"   ) var news   : ArrayList<News> = arrayListOf(),
    @SerializedName("source" ) var source : String?         = null
)

data class News (

    @SerializedName("title" ) var title : String? = null,
    @SerializedName("desc"  ) var desc  : String? = null

)
