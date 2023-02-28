package com.app.lsquared.model

import com.app.lsquared.model.over.Ovr
import com.google.gson.annotations.SerializedName

data class ResponseJsonData (

	@SerializedName("device") val device : List<Device>,
	@SerializedName("layout") val layout : List<Layout>,
	@SerializedName("downloadable") val downloadable : List<Downloadable>,
	@SerializedName("ovr" ) var ovr : ArrayList<Ovr> = arrayListOf()
)