package com.app.lsquared.model

import com.google.gson.annotations.SerializedName


data class Device (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("mac") val mac : String,
	@SerializedName("type") val type : String,
//	@SerializedName("apkvSource") val apkvSource : String,
//	@SerializedName("apkv") val apkv : String,
//	@SerializedName("esv") val esv : String,
//	@SerializedName("esrc") val esrc : String,
//	@SerializedName("wov") val wov : String,
//	@SerializedName("wsrc") val wsrc : String,
//	@SerializedName("tzv") val tzv : String,
//	@SerializedName("tsrc") val tsrc : String,
	@SerializedName("version") val version : Int,
//	@SerializedName("isRegistered") val isRegistered : Boolean,
//	@SerializedName("autoUpdate") val autoUpdate : String,
//	@SerializedName("au") val au : Int,
//	@SerializedName("offlineReboot") val offlineReboot : String,
//	@SerializedName("or") val or : Int,
//	@SerializedName("dr") val dr : Boolean,
//	@SerializedName("drt") val drt : String,
	@SerializedName("screenshotUpload") val screenshotUpload : String,
//	@SerializedName("ss") val ss : Boolean,
	@SerializedName("screenshotUploadInterval") val screenshotUploadInterval : Int = 300,
//	@SerializedName("ssi") val ssi : Int,
//	@SerializedName("screenshotUpdate") val screenshotUpdate : Boolean,
	@SerializedName("odss") val odss : Boolean,
	@SerializedName("timeZone") val timeZone : String,
	@SerializedName("wm") val wm : String,
//	@SerializedName("cods") val cods : String,
//	@SerializedName("cod") val cod : Boolean,
//	@SerializedName("codc") val codc : String,
//	@SerializedName("tzname") val tzname : String,
	@SerializedName("time") val time : String,
//	@SerializedName("isEmEnable") val isEmEnable : Boolean,
	@SerializedName("em") val em : Int,
	@SerializedName("weboss") val weboss : String = "",
//	@SerializedName("iotc") val iotc : Int,
//	@SerializedName("isIoTCEnable") val isIoTCEnable : Boolean,
//	@SerializedName("iotc_info") val iotc_info : String,
//	@SerializedName("isIcEnable") val isIcEnable : Boolean,
//	@SerializedName("emic") val emic : Int,
//	@SerializedName("isReportEnable") val isReportEnable : Boolean,
//	@SerializedName("reports") val reports : Int,
//	@SerializedName("rebootRequestedOn") val rebootRequestedOn : Boolean,
//	@SerializedName("reboot") val reboot : Boolean,
	@SerializedName("relaunchRequestedOn") val relaunchRequestedOn : String,
//	@SerializedName("relaunch") val relaunch : Boolean,
//	@SerializedName("downloadRequestedOn") val downloadRequestedOn : Boolean,
//	@SerializedName("redownload") val redownload : Boolean,
//	@SerializedName("firmwareRequestedOn") val firmwareRequestedOn : Boolean,
//	@SerializedName("fw") val fw : Boolean,
//	@SerializedName("transition") val transition : String,
//	@SerializedName("top") val top : Int,
//	@SerializedName("blankScreen") val blankScreen : Boolean,
//	@SerializedName("blank") val blank : Int,
	@SerializedName("wcoditime") val wcoditime : Int = 60,
//	@SerializedName("wcodpwd") val wcodpwd : Int,
//	@SerializedName("playback") val playback : String,
//	@SerializedName("changeDisplayMode") val changeDisplayMode : Boolean,
	@SerializedName("server") val server : String,
//	@SerializedName("protocol") val protocol : String,
//	@SerializedName("microphone") val microphone : Boolean,
	@SerializedName("feedRestriction") val feedRestriction : String,
//	@SerializedName("pollingFrequency") val pollingFrequency : Int,
	@SerializedName("defaultImageName") val defaultImageName : String,
	@SerializedName("defaultImageSrc") val defaultImageSrc : String,
//	@SerializedName("defaultImageSize") val defaultImageSize : Int,
	@SerializedName("identify") val identify : Boolean,
	@SerializedName("identifyDuration") val identifyDuration : Int,
	@SerializedName("ip") val ip : String,
//	@SerializedName("temperature_threshold") val temperature_threshold : Boolean,
	@SerializedName("prop") val prop : String,
//	@SerializedName("scheduledSize") val scheduledSize : Int,
//	@SerializedName("size") val size : Int
)

data class DeviceWaterMark (

	@SerializedName("img"       ) var img       : Img?       = Img(),
	@SerializedName("alignOpt"  ) var alignOpt  : String?    = null,
	@SerializedName("align"     ) var align     : String?    = null,
	@SerializedName("x"         ) var x         : Int?       = null,
	@SerializedName("y"         ) var y         : Int?       = null,
	@SerializedName("dateRange" ) var dateRange : DateRange? = DateRange(),
	@SerializedName("bg"        ) var bg        : String?    = null,
	@SerializedName("bga"       ) var bga       : String?    = null,
	@SerializedName("ctype"     ) var ctype     : Ctype?     = Ctype()

)

data class Img (

	@SerializedName("id"    ) var id    : Int?    = null,
	@SerializedName("extn"  ) var extn  : String? = null,
	@SerializedName("type"  ) var type  : String? = null,
	@SerializedName("label" ) var label : String? = null,
	@SerializedName("thumb" ) var thumb : String? = null,
	@SerializedName("path"  ) var path  : String? = null,
	@SerializedName("w"     ) var w     : Int?    = null,
	@SerializedName("h"     ) var h     : Int?    = null

)

data class DateRange (

	@SerializedName("startDate" ) var startDate : String? = null,
	@SerializedName("endDate"   ) var endDate   : String? = null

)

data class Ctype (

	@SerializedName("bg" ) var bg : String? = null

)

// for device on-off

data class Days (

	var label : Int?     = null,
	var name  : String?  = null,
	var valu  : Boolean?  = false,
	var st    : String?  = null,
	var et    : String?  = null

)

abstract class SettingCommon : java.io.Serializable{
	abstract var bg: String
	abstract var bga: String
}
