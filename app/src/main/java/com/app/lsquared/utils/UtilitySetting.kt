package com.app.lsquared.utils

import com.app.lsquared.model.Item
import org.json.JSONObject

class UtilitySetting {

    companion object{

        const val SETTING_KEY_TEMPLATE = "template"
        const val SETTING_KEY_ROTATE_OPT = "rotationOpt"
        const val SETTING_KEY_ROTATE = "rotate"
        const val SETTING_KEY_HEADER_BG = "headerBg"
        const val SETTING_KEY_HEADER_BGA = "headerBga"
        const val SETTING_KEY_HEADER_FONT = "headerFont"
        const val SETTING_KEY_HEADER_FONT_SIZE = "headerSize"
        const val SETTING_KEY_LABEL = "label"
        const val SETTING_KEY_HEADER_TEXT_OPT = "hTextOpt"
        const val SETTING_KEY_HEADER_TEXT = "hText"

        fun getObject(data:String): JSONObject {
            return JSONObject(data)
        }

        fun getTemplate(setting:String) = getObject(setting).getString(SETTING_KEY_TEMPLATE)


        fun getRotation(item: Item): Int {
            var obj = JSONObject(item.settings)
            return  if(obj.getString(SETTING_KEY_ROTATE_OPT).equals("a")) 12 else obj.getInt(SETTING_KEY_ROTATE)
        }

        fun getHeaderBg(setting:String) = getObject(setting).getString(SETTING_KEY_HEADER_BG)

        fun getHeaderBga(setting:String) = getObject(setting).getString(SETTING_KEY_HEADER_BGA)

        fun getFontLable(setting:String) = getHeaderFontData(getObject(setting).getString(SETTING_KEY_HEADER_FONT))

        fun getHeaderFontData(fontsetting:String) = getObject(fontsetting).getString(SETTING_KEY_LABEL)

        fun getHeaderTextOpt(fontsetting:String) = getObject(fontsetting).getString(SETTING_KEY_HEADER_TEXT_OPT)

        fun getHeaderText(fontsetting:String) = getObject(fontsetting).getString(SETTING_KEY_HEADER_TEXT)


        fun getHeaderFontSize(settings: String): Float = getObject(settings).getInt(SETTING_KEY_HEADER_FONT_SIZE).toFloat()



        // get speed
        fun getSpeed(speed: Int): Float {
            var sp = 20000000/speed
            return sp.toFloat()
        }

        // moving pixel
        fun getMovingPixel(speed: Int): Int {
            return  if(speed/2<1) 1 else speed/5
        }



    }
}