package com.app.lsquared.model.widget

import com.google.gson.annotations.SerializedName

data class MeetingCalendarResponseData (
    @SerializedName("info"   ) var info   : InfoMeeting?      = InfoMeeting(),
    @SerializedName("ss"     ) var ss     : ArrayList<Ss>     = arrayListOf(),
    @SerializedName("events" ) var events : ArrayList<MeetingEvents> = arrayListOf(),
    @SerializedName("event" ) var event   : MeetingEvent?            = null,
    )

data class CalendarResponseData (
    @SerializedName("info"   ) var info   : InfoCalendar?      = InfoCalendar(),
    @SerializedName("ss"     ) var ss     : ArrayList<Ss>     = arrayListOf(),
    @SerializedName("events" ) var events : ArrayList<CalendarEvents> = arrayListOf(),
    @SerializedName("event" ) var event   : CalendarEvent?            = null,
    )

data class InfoMeeting (
    @SerializedName("version" ) var version : Int? = null,
)

data class InfoCalendar (
    @SerializedName("version" ) var version : String? = null
)

data class Ss (
    @SerializedName("src"  ) var src  : String? = null,
    @SerializedName("d"    ) var d    : Int?    = null,
    @SerializedName("type" ) var type : String? = null
)

data class MeetingEvents (
    @SerializedName("id"        ) var id        : Int?    = null,
    @SerializedName("title"     ) var title     : String? = null,
    @SerializedName("subTitle"  ) var subTitle  : String? = null,
    @SerializedName("starttime" ) var starttime : String? = null,
    @SerializedName("endtime"   ) var endtime   : String? = null,
    @SerializedName("dType"     ) var dType     : String? = null,
    @SerializedName("stos"      ) var stos      : Int?    = null,
    @SerializedName("startdate" ) var startdate : String? = null,
    @SerializedName("start_date" ) var start_date : String? = null,
    @SerializedName("start_time" ) var start_time : String? = null,
    @SerializedName("enddate"   ) var enddate   : String? = null,
    @SerializedName("end_date"   ) var end_date   : String? = null,
    @SerializedName("end_time"   ) var end_time   : String? = null,
    @SerializedName("location"  ) var location  : String? = null,
    @SerializedName("location_e") var location_e: String? = null,
    @SerializedName("floor"     ) var floor     : String? = null,
    @SerializedName("logo"      ) var logo      : String? = null,
    @SerializedName("logosrc"   ) var logosrc   : String? = null,
    @SerializedName("device"    ) var device    : String? = null
)

data class MeetingEvent (

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

data class CalendarEvent (

    @SerializedName("id"         ) var id         : String? = null,
    @SerializedName("title"      ) var title      : String? = null,
    @SerializedName("subTitle"   ) var subTitle   : String? = null,
    @SerializedName("time"       ) var time       : String? = null,
    @SerializedName("screenTime" ) var screenTime : String? = null,
    @SerializedName("starttime"  ) var starttime  : String? = null,
    @SerializedName("endtime"    ) var endtime    : String? = null,
    @SerializedName("img"        ) var img        : String? = null
)

data class CalendarEvents (

    @SerializedName("id"         ) var id        : String? = null,
    @SerializedName("title"      ) var title     : String? = null,
    @SerializedName("logo"       ) var logo      : String? = null,
    @SerializedName("location"   ) var location  : String? = null,
    @SerializedName("location_e" ) var locationE : String? = null,
    @SerializedName("start_date" ) var startDate : String? = null,
    @SerializedName("start_time" ) var startTime : String? = null,
    @SerializedName("starttime"  ) var starttime : String? = null,
    @SerializedName("end_date"   ) var endDate   : String? = null,
    @SerializedName("end_time"   ) var endTime   : String? = null,
    @SerializedName("endtime"    ) var endtime   : String? = null

)