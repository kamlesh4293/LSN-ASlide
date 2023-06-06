package com.app.lsquared.utils

class Constant {

    companion object{

            // development environment
//        const val BASE_URL = "https://rc.lsquared.com/"
//        const val BASE_FILE_URL = BASE_URL+"rc-lsquared-hub/"
//        const val BASE_FILE_FEED_URL = BASE_URL+"api/v1/feed"
//        const val ENVIRONMENT = "LS-AZCARC-1001"

        // US production environment
        const val BASE_URL = "https://us.lsquared.com/"
        const val BASE_FILE_URL = BASE_URL+"lsquared-hub/"
        const val BASE_FILE_FEED_URL = BASE_URL+"api/v1/feed"
        const val ENVIRONMENT = "LS-AZUSUS-1001"

        // HUB production environment
//        const val BASE_URL = "https://hub.lsquared.com/"
//        const val BASE_FILE_URL = "https://s3-us-west-2.amazonaws.com/lsquared-hub/"
//        const val BASE_FILE_FEED_URL = BASE_URL+"api/v1/feed"
//        const val ENVIRONMENT = "LS-AWUSPRO-1001"

        // APIS FOR WIDGETS
        const val API_DEVICE_VERSION = BASE_URL+"api/v1/feed/deviceversion/"
        const val API_CONTENT = BASE_FILE_URL+"feed/json/"
        const val API_SUBMIT_INFO = BASE_URL+"api/v1/feed/setDeviceInfo"

        const val API_WIDGET_TEXT = BASE_URL+"api/v1/feed/crawlingtext/"
        const val API_WIDGET_WEATHER = BASE_URL+"api/v1/feed/azureweather/"
        const val API_WIDGET_QUOTE = BASE_URL+"api/v1/feed/quotes/"
        const val API_WIDGET_BEING_NEWS = BASE_URL+"api/v1/feed/bingnews/"
        const val API_WIDGET_STOCKS = BASE_FILE_FEED_URL+"/stock_wtd/"
        const val API_STOCK_TABLE_HTML = BASE_FILE_FEED_URL+"/stockListHtml/"


        // api for device register
        const val API_NEW_DEVICE_REGISTER = BASE_URL+"api/v1/feed/autoreg"

        // api for emergency message
        fun getApiEmergencyMessage(id:String) = "$BASE_FILE_FEED_URL/em/$id/1?format=json?did="

        // api for emergency acknowldge
        fun getApiEmergencyAcknowledge(id:String) = "$BASE_FILE_FEED_URL/em/$id/true?format=json&did="

        // api for identify request acknowledge
        fun getApiIdentifyAcknowledge(mac:String,id:String) = "$BASE_FILE_FEED_URL/acknowledgements/identify/$mac?did=$id"

        // api for content download confirmation
        fun getApiDownloadConfirmation(mac:String) = "$BASE_FILE_FEED_URL/contentConfirmation/$mac"

        // api for relaunch acknowledge
        fun getApiRelaunchAcknowledge(mac:String) = "$BASE_FILE_FEED_URL/acknowledgements/relaunch/$mac"

        // device register response
        const val DEVICE_REGISTERED = 22001
        const val DEVICE_NOT_FOUND = "device not found"
//        22001 - Device updated
//        22002 - Device with new mac already exists
//        22003 - Device not found
//        22004 - Invalid payload

        // downloadable thumb
        const val CONTENT_THUMB = "thumb"

        // widgets
        const val CONTENT_IMAGE = "image"
        const val CONTENT_VIDEO = "video"
        const val CONTENT_VECTOR = "vector"
        const val CONTENT_POWERPOINT = "powerpoint"
        const val CONTENT_WORD = "word"
        const val CONTENT_WIDGET_QRCODE = "qrCode"
        const val CONTENT_WEB = "webPage"
        const val CONTENT_WIDGET_GOOGLE = "googleSlide"
        const val CONTENT_WIDGET_POWER = "powerbi"
        const val CONTENT_WIDGET_TRAFFIC = "traffic"
        const val CONTENT_WIDGET_VIMEO = "vimeo"
        const val CONTENT_WIDGET_YOUTUBE = "youtube"
        const val CONTENT_WIDGET_IFRAME = "webWidget"
        const val CONTENT_WIDGET_LIVESTREAM = "livestream"

        // setting widgets
        const val CONTENT_WIDGET_COD = "contentOnDemandBtn"         // Done
        const val CONTENT_WIDGET_NEWS = "news"                      // Done
        const val CONTENT_WIDGET_RSS = "rss"                      // Done
        const val CONTENT_WIDGET_QUOTES = "quote"                   // Done
        const val CONTENT_WIDGET_TEXT = "crawlingText"
        const val CONTENT_WIDGET_STOCK = "stock"
        const val CONTENT_WIDGET_DATE_TIME = "date-time"
        const val WIDGET_MESSAGE = "message"
        const val WIDGET_EMERGENCY_MESSAGE = "emergency_message"
        const val CONTENT_WIDGET_WEATHER = "weather"


        // base url for other apis
        const val BASE_URL_YOUTUBE = "https://www.youtube.com/embed/"
        const val BASE_URL_VIMEO = "https://player.vimeo.com/video/"
        const val VIMEO_ACCESS_TOKEN = "b45cae7771a62a78b359dd059008a632"

        // news tyte
        const val CONTENT_WIDGET_NEWS_LIST = "normal"
        const val CONTENT_WIDGET_NEWS_CRAWL = "crawl"

        // date time templates

        const val TEMPLATE_TIME_T1 = "t1"
        const val TEMPLATE_TIME_T2 = "t2"
        const val TEMPLATE_TIME_T3 = "t3"
        const val TEMPLATE_TIME_T4 = "t4"
        const val TEMPLATE_TIME_T5 = "t5"
        const val TEMPLATE_TIME_T6 = "t6"
        const val TEMPLATE_TIME_T7 = "t7"
        const val TEMPLATE_TIME_T8 = "t8"

        // weather template
        const val TEMPLATE_WEATHER_CURRENT_DATE = 0
        const val TEMPLATE_WEATHER_FIVE_DAY = 1
        const val TEMPLATE_WEATHER_FOUR_DAY = 2
        const val TEMPLATE_WEATHER_ORIENTATION_VERTICAL = "v"
        const val TEMPLATE_WEATHER_ORIENTATION_HORIZONTAL = "h"


        // text type
        const val TEXT_CROWLING = "c"
        const val TEXT_STATIC = "s"


        // calling from
        const val CALLING_MAIN = "main"
        const val CALLING_COD = "cod"

        // frame content alignment
        const val ALIGN_TOP_LEFT ="t-l"
        const val ALIGN_TOP_CENTER ="t-c"
        const val ALIGN_TOP_RIGHT ="t-r"
        const val ALIGN_MIDDLE_LEFT ="m-l"
        const val ALIGN_MIDDLE_CENTER ="m-c"
        const val ALIGN_MIDDLE_RIGHT ="m-r"
        const val ALIGN_BOTTOM_LEFT ="b-l"
        const val ALIGN_BOTTOM_CENTER ="b-c"
        const val ALIGN_BOTTOM_RIGHT ="b-r"

        // video player type
        const val PLAYER_COD = 1
        const val PLAYER_SLIDE = 0

        // submit screen shot type
        const val SS_TYPE_SS = "ss"
        const val SS_TYPE_OD = "od"


    }

}