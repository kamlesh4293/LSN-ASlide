package com.app.lsquared.pasring

import android.graphics.Color
import com.app.lsquared.ui.UiUtils
import org.json.JSONObject

class DataParsingSetting {

    companion object{

        const val KEY_TITLE_SIZE = "titleSize"
        const val KEY_DESC_SIZE = "descSize"

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

    }

}