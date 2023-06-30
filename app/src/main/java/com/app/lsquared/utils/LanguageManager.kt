package com.app.lsquared.utils

import android.content.Context
import android.os.Build
import java.util.*

class LanguageManager {


    companion object{

        fun setLocal(ctx:Context,dataParsing:DataParsing){

            var lang = dataParsing.getLanguage()
            val config = ctx.resources.configuration
            val locale = Locale(lang)
            Locale.setDefault(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                config.setLocale(locale)
            else
                config.locale = locale

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                ctx.createConfigurationContext(config)
            ctx.resources.updateConfiguration(config, ctx.resources.displayMetrics)
        }

    }

}