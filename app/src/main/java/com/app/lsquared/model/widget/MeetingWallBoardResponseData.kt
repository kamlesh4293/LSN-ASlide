package com.app.lsquared.model.widget

import com.google.gson.annotations.SerializedName

data class MeetingWallBoardResponseData (
    @SerializedName("info"   ) var info   : InfoMeeting?      = InfoMeeting(),
    @SerializedName("ss"     ) var ss     : ArrayList<Ss>     = arrayListOf(),
    @SerializedName("events" ) var events : ArrayList<Events> = arrayListOf()
)

data class InfoMeeting (
    @SerializedName("version" ) var version : Int? = null
)

data class Ss (
    @SerializedName("src"  ) var src  : String? = null,
    @SerializedName("d"    ) var d    : Int?    = null,
    @SerializedName("type" ) var type : String? = null
)

data class Events (
    @SerializedName("id"        ) var id        : Int?    = null,
    @SerializedName("title"     ) var title     : String? = null,
    @SerializedName("subTitle"  ) var subTitle  : String? = null,
    @SerializedName("starttime" ) var starttime : String? = null,
    @SerializedName("endtime"   ) var endtime   : String? = null,
    @SerializedName("dType"     ) var dType     : String? = null,
    @SerializedName("stos"      ) var stos      : Int?    = null,
    @SerializedName("startdate" ) var startdate : String? = null,
    @SerializedName("enddate"   ) var enddate   : String? = null,
    @SerializedName("location"  ) var location  : String? = null,
    @SerializedName("floor"     ) var floor     : String? = null,
    @SerializedName("logo"      ) var logo      : String? = null,
    @SerializedName("logosrc"   ) var logosrc   : String? = null,
    @SerializedName("device"    ) var device    : String? = null
)

// room board

data class MeetingRoomBoardResponseData (

    @SerializedName("info"  ) var info  : Info?         = Info(),
    @SerializedName("event" ) var event : Event?        = Event(),
    @SerializedName("ss"    ) var ss    : ArrayList<Ss> = arrayListOf()

)

data class Event (

    @SerializedName("id"        ) var id        : Int?    = null,
    @SerializedName("title"     ) var title     : String? = null,
    @SerializedName("subTitle"  ) var subTitle  : String? = null,
    @SerializedName("starttime" ) var starttime : String? = null,
    @SerializedName("endtime"   ) var endtime   : String? = null,
    @SerializedName("dType"     ) var dType     : String? = null,
    @SerializedName("stos"      ) var stos      : Int?    = null,
    @SerializedName("startdate" ) var startdate : String? = null,
    @SerializedName("enddate"   ) var enddate   : String? = null,
    @SerializedName("location"  ) var location  : String? = null,
    @SerializedName("floor"     ) var floor     : String? = null,
    @SerializedName("logo"      ) var logo      : String? = null,
    @SerializedName("logosrc"   ) var logosrc   : String? = null,
    @SerializedName("device"    ) var device    : String? = null

)