package com.app.lsquared.pasring

import android.graphics.Color
import com.app.lsquared.ui.UiUtils
import org.json.JSONObject

class DataParsingSetting {

    companion object{

        const val KEY_TITLE_SIZE = "titleSize"
        const val KEY_TIME_SIZE = "timeSize"
        const val KEY_ROOM_SIZE = "roomSize"
        const val KEY_FLOOR_SIZE = "floorSize"
        const val KEY_DESC_SIZE = "descSize"

        // meeting
        const val KEY_HEADER = "header"
        const val KEY_HEADER_BG = "headerBg"
        const val KEY_HEADER_BGA = "headerBga"
        const val KEY_HEADER_TEXT = "headerText"
        const val KEY_HEADER_SIZE = "headerSize"
        const val KEY_HEADER_FONT = "headerFont"
        const val KEY_TITLE_FONT = "titleFont"
        const val KEY_TIME_FONT = "timeFont"
        const val KEY_ROOM_FONT = "roomFont"
        const val KEY_FLOOR_FONT = "floorFont"
        const val KEY_SUBTITLE_FONT = "subtitleFont"
        const val KEY_HEADER_LABEL = "label"
        const val KEY_COLUMN1 = "column1"
        const val KEY_COLUMN2 = "column2"
        const val KEY_COLUMN3 = "column3"
        const val KEY_COLUMN4 = "column4"

        // COLOR
        const val KEY_TITLE_TEXT = "titleText"
        const val KEY_TITLE_ALT_TEXT = "altTitleText"
        const val KEY_TIME_TEXT = "timeText"
        const val KEY_TIME_ALT_TEXT = "altTimeText"
        const val KEY_ROOM_TEXT = "roomText"
        const val KEY_ROOM_ALT_TEXT = "altRoomText"
        const val KEY_FLOOR_TEXT = "floorText"
        const val KEY_FLOOR_ALT_TEXT = "altFloorText"
        const val KEY_ROW_BG = "rowBg"
        const val KEY_ROW_BGA = "rowBga"
        const val KEY_ROW_ALT_BG = "altBg"
        const val KEY_ROW_ALT_BGA = "altBga"
        const val KEY_BG = "bg"
        const val KEY_BGA = "bga"

        const val KEY_ROTATE = "rotate"



        fun getSettingObject(setting: String): JSONObject {
            return JSONObject(setting)
        }

        fun getTitleSize(setting: String): Int {
            return getSettingObject(setting).getInt(KEY_TITLE_SIZE)
        }

        fun getDescSize(setting: String): Int {
            return getSettingObject(setting).getInt(KEY_DESC_SIZE)
        }

        fun getTintColorTrans(setting:String): Int {
            var color_code = JSONObject(setting).getString("tint")
            var color = UiUtils.getColorWithOpacity(color_code,"0.5")
            return Color.parseColor(color)
        }

        fun getTintColor(setting:String): Int {
            var color_code = JSONObject(setting).getString("tint")
            return Color.parseColor(color_code)
        }

        fun getFontLabel(setting:String): String {
            return JSONObject(setting).getJSONObject("font").getString("label")
        }

        fun getLang(setting:String): String {
            return JSONObject(setting).getString("lang")
        }

        fun getUnit(setting:String): String {
            return JSONObject(setting).getString("unit")
        }

        fun getWsu(setting:String): String {
            return JSONObject(setting).getString("wsu")
        }

        // meeting setting
        @JvmStatic
        fun getBgWithOpacity(setting_obj:JSONObject) = UiUtils.getColorWithOpacity(setting_obj.getString(KEY_BG),setting_obj.getString(KEY_BGA))

        fun getRowBgWithOpacity(setting_obj:JSONObject) = UiUtils.getColorWithOpacity(setting_obj.getString(KEY_ROW_BG),setting_obj.getString(KEY_ROW_BGA))

        fun getAltRowBgWithOpacity(setting_obj:JSONObject) = UiUtils.getColorWithOpacity(setting_obj.getString(KEY_ROW_ALT_BG),setting_obj.getString(KEY_ROW_ALT_BGA))

        fun meetingHeaderBg(setting_obj:JSONObject) = UiUtils.getColorWithOpacity(setting_obj.getString(KEY_HEADER_BG),setting_obj.getString(KEY_HEADER_BGA))

        fun meetingHeaderTextColor(setting_obj:JSONObject) = setting_obj.getString(KEY_HEADER_TEXT)

        fun meetingHeaderTextSize(setting_obj:JSONObject) = setting_obj.getString(KEY_HEADER_SIZE)

        fun getFontLabel(setting_obj:JSONObject) = setting_obj.getJSONObject(KEY_HEADER_FONT).getString(KEY_HEADER_LABEL)

        fun getFontTitleLabel(setting_obj:JSONObject) = setting_obj.getJSONObject(KEY_TITLE_FONT).getString(KEY_HEADER_LABEL)

        fun getFontTimeLabel(setting_obj:JSONObject) = setting_obj.getJSONObject(KEY_TIME_FONT).getString(KEY_HEADER_LABEL)

        fun getFontRoomLabel(setting_obj:JSONObject) = setting_obj.getJSONObject(KEY_ROOM_FONT).getString(KEY_HEADER_LABEL)

        fun getFontFloorLabel(setting_obj:JSONObject) = setting_obj.getJSONObject(KEY_FLOOR_FONT).getString(KEY_HEADER_LABEL)

        fun getFontSubTitleLabel(setting_obj:JSONObject) = setting_obj.getJSONObject(KEY_SUBTITLE_FONT).getString(KEY_HEADER_LABEL)

        fun getMeetingTitleText1(setting_obj:JSONObject) = setting_obj.getJSONObject(KEY_HEADER).getString(KEY_COLUMN1)

        fun getMeetingTitleText2(setting_obj:JSONObject) = setting_obj.getJSONObject(KEY_HEADER).getString(KEY_COLUMN2)

        fun getMeetingTitleText3(setting_obj:JSONObject) = setting_obj.getJSONObject(KEY_HEADER).getString(KEY_COLUMN3)

        fun getMeetingTitleText4(setting_obj:JSONObject) = setting_obj.getJSONObject(KEY_HEADER).getString(KEY_COLUMN4)

        fun getRowTitleTextColor(setting_obj:JSONObject) = setting_obj.getString(KEY_TITLE_TEXT)
        fun getAltRowTitleTextColor(setting_obj:JSONObject) = setting_obj.getString(KEY_TITLE_ALT_TEXT)

        fun getRowTimeTextColor(setting_obj:JSONObject) = setting_obj.getString(KEY_TIME_TEXT)
        fun getAltRowTimeTextColor(setting_obj:JSONObject) = setting_obj.getString(KEY_TIME_ALT_TEXT)

        fun getRowRoomTextColor(setting_obj:JSONObject) = setting_obj.getString(KEY_ROOM_TEXT)
        fun getAltRowRoomTextColor(setting_obj:JSONObject) = setting_obj.getString(KEY_ROOM_ALT_TEXT)

        fun getRowFloorTextColor(setting_obj:JSONObject) = setting_obj.getString(KEY_FLOOR_TEXT)
        fun getAltRowFloorTextColor(setting_obj:JSONObject) = setting_obj.getString(KEY_FLOOR_ALT_TEXT)

        // size
        fun getTitleSize(setting_obj:JSONObject) = setting_obj.getInt(KEY_TITLE_SIZE)
        fun getTimeSize(setting_obj:JSONObject) = setting_obj.getInt(KEY_TIME_SIZE)
        fun getRoomSize(setting_obj:JSONObject) = setting_obj.getInt(KEY_ROOM_SIZE)
        fun getFloorSize(setting_obj:JSONObject) = setting_obj.getInt(KEY_FLOOR_SIZE)

        // ROTATE
        fun getRotate(setting_obj:JSONObject) = setting_obj.getInt(KEY_ROTATE)

        // logo option
        fun getLogoOption(setting_obj:JSONObject) = setting_obj.getBoolean("logoOpt")

        // logo option
        fun isRoomVisible(setting_obj:JSONObject) = setting_obj.getBoolean("isRoom")
        fun isFloorVisible(setting_obj:JSONObject) = setting_obj.getBoolean("isFloor")
        fun isRFVisible(setting_obj:JSONObject) = setting_obj.getBoolean("isRF")
        fun isTimeVisible(setting_obj:JSONObject) = setting_obj.getBoolean("isTime")

        // header visibility
        fun getheaderVisibility(setting_obj:JSONObject) = setting_obj.getJSONObject(KEY_HEADER).getBoolean("active")

    }

}