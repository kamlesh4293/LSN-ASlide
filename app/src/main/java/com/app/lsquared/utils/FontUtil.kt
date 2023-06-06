package com.app.lsquared.utils

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi


class FontUtil {

    companion object{

        const val FONT_Arial = "Arial"
        const val FONT_Arial_Black = "Arial Black"
        const val FONT_Breakers_Slab = "Breakers Slab"
        const val FONT_Breakers_Slab_Black = "Breakers Slab Black"
        const val FONT_Breakers_Slab_Light = "Breakers Slab Light"
        const val FONT_Breakers_Slab_Thin = "Breakers Slab Thin"
        const val FONT_Breakers_Slab_Ultra = "Breakers Slab Ultra"
        const val FONT_Cambria = "Cambria"
        const val FONT_Calibri = "Calibri"
        const val FONT_Calibri_Light = "Calibri Light"
        const val FONT_Calisto_MT = "Calisto MT"
        const val FONT_Candara = "Candara"
        const val FONT_Century_Gothic = "Century Gothic"
        const val FONT_Chalet_Pinkberry = "Chalet-Pinkberry"
        const val FONT_Comic_Sans_MS = "Comic Sans MS"
        const val FONT_Constantia = "Constantia"
        const val FONT_Copernicus = "Copernicus"
        const val FONT_Corbel = "Corbel"
        const val FONT_Franklin_Gothic = "Franklin Gothic"
        const val FONT_Futura_LT = "Futura LT"
        const val FONT_Futura_LT_Book = "Futura LT Book"
        const val FONT_Futura_LT_Book_Condensed = "Futura LT Book Condensed"
        const val FONT_Futura_LT_Condensed_Light = "Futura LT Condensed Light"
        const val FONT_Futura_LT_Condensed_Extra_Bold = "Futura LT Condensed Extra Bold"
        const val FONT_Futura_LT_Extra_Bold = "Futura LT Extra Bold"
        const val FONT_Futura_LT_Heavy = "Futura LT Heavy"
        const val FONT_Futura_LT_Light = "Futura LT Light"
        const val FONT_Georgia = "Georgia"
        const val FONT_Gotham = "Gotham"
        const val FONT_Impact = "Impact"
        const val FONT_IntelClear_Bds = "IntelClear_Bds"
        const val FONT_IntelClear_BdIt = "IntelClear_BdIt"
        const val FONT_IntelClear_It = "IntelClear_It"
        const val FONT_IntelClear_Lt = "IntelClear_Lt"
        const val FONT_IntelClear_LtIt = "IntelClear_LtIt"
        const val FONT_IntelClear_Rg = "IntelClear_Rg"
        const val FONT_IntelClearPro_Bd = "IntelClearPro_Bd"
        const val FONT_intelone_display_bold = "intelone-display-bold"
        const val FONT_intelone_display_light = "intelone-display-light"
        const val FONT_intelone_display_medium = "intelone-display-medium"
        const val FONT_intelone_display_regular = "intelone-display-regular"
        const val FONT_Intro = "Intro"
        const val FONT_Intro_Black_Alt = "Intro Black Alt"
        const val FONT_IntroBlack_Caps = "Intro Black Caps"
        const val FONT_Intro_Black_Inline = "Intro Black Inline"
        const val FONT_Intro_Black_Inline_Caps = "Intro Black Inline Caps"
        const val FONT_Intro_Bold_Alt = "Intro Bold Alt"
        const val FONT_Intro_Bold_Caps = "Intro Bold Caps"
        const val FONT_Intro_Book_Alt = "Intro Book Alt"
        const val FONT_Intro_Book_Caps = "Intro Book Caps"
        const val FONT_Intro_Light = "Intro Light"
        const val FONT_Intro_Light_Alt = "Intro Light Alt"
        const val FONT_Intro_Light_Caps = "Intro Light Caps"
        const val FONT_Intro_Regular_Caps = "Intro Regular Caps"
        const val FONT_Intro_Thin = "Intro Thin"
        const val FONT_Intro_Thin_Caps = "Intro Thin Caps"
        const val FONT_Lucida_Console = "Lucida Console"
        const val FONT_Lucida_Handwriting = "Lucida Handwriting"
        const val FONT_Martel_Light = "Martel Light"
        const val FONT_Martel_Regular = "Martel Regular"
        const val FONT_Mission_Script = "Mission Script"
        const val FONT_Myriad_Pro = "Myriad Pro"
        const val FONT_Myriad_Pro_Condensed = "Myriad Pro Condensed"
        const val FONT_Neutra_Text = "Neutra Text"
        const val FONT_Neutra_Text_Alt = "Neutra Text Alt"
        const val FONT_Neutraface_Text = "Neutraface Text"
        const val FONT_Neutraface_Text_Alt = "Neutraface Text Alt"

        const val FONT_OneDot = "OneDot"
        // {label:"OneDot-Bold", value:'OneDot-Bold'},
        // {label:"OneDot-BoldItalic", value:'OneDot-BoldItalic'},
        // {label:"OneDot-Italic", value:'OneDot-Italic'},
        // {label:"OneDot-Light", value:'OneDot-Light'},
        // {label:"OneDot-LightItalic", value:'OneDot-LightItalic'},
        const val FONT_OneDotCd = "OneDotCd"
        // {label:"OneDotCd-Bold", value:'OneDotCd-Bold'},
        // {label:"OneDotCd-BoldItalic", value:'OneDotCd-BoldItalic'},
        // {label:"OneDotCd-Italic", value:'OneDotCd-Italic'},
        // {label:"OneDotCd-Lt", value:'OneDotCd-Lt'},
        // {label:"OneDotCd-LtItalic", value:'OneDotCd-LtItalic'},
        const val FONT_OneDotExt = "OneDotExt"
        // {label:"OneDotExt-Bold", value:'OneDotExt-Bold'},
        // {label:"OneDotExt-BoldItalic", value:'OneDotExt-BoldItalic'},
        // {label:"OneDotExt-Italic", value:'OneDotExt-Italic'},
        // {label:"OneDotExt-Lt", value:'OneDotExt-Lt'},
        // {label:"OneDotExt-LtItalic", value:'OneDotExt-LtItalic'},

        const val FONT_Palatino_Linotype = "Palatino Linotype"
        const val FONT_PizzaPress_Antique = "PizzaPress-Antique"
        // {label:"PizzaPress-AntiqueDisplay", value:'PizzaPress-AntiqueDisplay'},
        const val FONT_PizzaPressExt_Fill = "PizzaPressExt-Fill"
        const val FONT_PizzaPressExt_Inline = "PizzaPressExt-Inline"
        const val FONT_PizzaPressExt_ReverseShadow = "PizzaPressExt-ReverseShadow"
        const val FONT_PizzaPress_Fill = "PizzaPress-Fill"
        const val FONT_PizzaPress_Inline = "PizzaPress-Inline"
        const val FONT_PizzaPress_OrnamentsNeue = "PizzaPress-OrnamentsNeue"
        const val FONT_PizzaPress_Outline = "PizzaPress-Outline"
        const val FONT_PizzaPress_Regular = "PizzaPress-Regular"
        const val FONT_PizzaPress_ReverseShadow = "PizzaPress-ReverseShadow"
        const val FONT_PizzaPress_Shadow = "PizzaPress-Shadow"

        const val FONT_Roboto_Bold = "Roboto Bold"
        const val FONT_Roboto_Condensed = "Roboto Condensed"
        const val FONT_Roboto_Light = "Roboto Light"
        const val FONT_Roboto_Regular = "Roboto Regular"
        const val FONT_Segoe_Print = "Segoe Print"
        const val FONT_Segoe_UI = "Segoe UI"
        const val FONT_Source_Sans_Pro_Bold = "Source Sans Pro Bold"
        const val FONT_Source_Sans_Pro_Bold_Italic = "Source Sans Pro Bold Italic"
        const val FONT_Source_Sans_Pro_Regular = "Source Sans Pro Regular"
        const val FONT_Source_Sans_Pro_Regular_Italic = "Source Sans Pro Regular Italic"
        const val FONT_SUNN_Serif_Bold = "SUNN Serif Bold"
        const val FONT_SUNN_Serif_Regular = "SUNN Serif Regular"
        const val FONT_Tahoma = "Tahoma"
        const val FONT_Times_New_Roman = "Times New Roman"
        const val FONT_Trebuchet_MS = "Trebuchet MS"
        const val FONT_TwoDots = "TwoDots"
        const val FONT_Verdana = "Verdana"
        const val FONT_Dogica_Pixel = "Dogica-Pixel"


        fun setFonts(ctx:Context, textView: TextView, font_label:String){
            var typeface: Typeface? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(font_label.equals(FONT_Arial))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.arial)
                else if(font_label.equals(FONT_Arial_Black))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.ariblk)
                else if(font_label.equals(FONT_Calibri))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.calibri)
                else if(font_label.equals(FONT_Calibri_Light))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.calibri_light)
                else if(font_label.equals(FONT_Calisto_MT))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.calist)
                else if(font_label.equals(FONT_Candara))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.candara)
                else if(font_label.equals(FONT_Century_Gothic))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.century_gothic)
                else if(font_label.equals(FONT_Comic_Sans_MS))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.comic)
                else if(font_label.equals(FONT_Constantia))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.constan)
                else if(font_label.equals(FONT_Copernicus))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.coprgtl)
                else if(font_label.equals(FONT_Corbel))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.corbel)
                else if(font_label.equals(FONT_Franklin_Gothic))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.framdcn)
                else if(font_label.equals(FONT_Futura_LT))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.futura_lt)
                else if(font_label.equals(FONT_Futura_LT_Book))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.futura_lt_book)
                else if(font_label.equals(FONT_Futura_LT_Book_Condensed))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.futura_lt_condensed)
                else if(font_label.equals(FONT_Futura_LT_Condensed_Light))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.futura_lt_condensed_light)
                else if(font_label.equals(FONT_Futura_LT_Condensed_Extra_Bold))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.futura_lt_condensed_extra_bold)
                else if(font_label.equals(FONT_Futura_LT_Extra_Bold))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.futura_lt_extra_bold)
                else if(font_label.equals(FONT_Futura_LT_Heavy))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.futura_lt_heavy)
                else if(font_label.equals(FONT_Futura_LT_Light))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.futura_lt_light)
                else if(font_label.equals(FONT_Georgia))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.georgia)
                else if(font_label.equals(FONT_Impact))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.impact)
                else if(font_label.equals(FONT_IntelClear_Bds))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intelclear_bd)
                else if(font_label.equals(FONT_IntelClear_BdIt))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intelclear_bd_it)
                else if(font_label.equals(FONT_IntelClear_It))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intelclear_it)
                else if(font_label.equals(FONT_IntelClear_Lt))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intelclear_lt)
                else if(font_label.equals(FONT_IntelClear_LtIt))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intelclear_lt_it)
                else if(font_label.equals(FONT_IntelClear_Rg))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intelclear_rg)
                else if(font_label.equals(FONT_IntelClearPro_Bd))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intelclear_pro_bd)
                else if(font_label.equals(FONT_intelone_display_bold))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intelone_display_bold)
                else if(font_label.equals(FONT_intelone_display_light))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intelone_display_light)
                else if(font_label.equals(FONT_intelone_display_medium))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intelone_display_medium)
                else if(font_label.equals(FONT_intelone_display_regular))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intelone_display_regular)
                else if(font_label.equals(FONT_Intro))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_regular)
                else if(font_label.equals(FONT_Intro_Black_Alt))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_black_alt)
                else if(font_label.equals(FONT_IntroBlack_Caps))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_black_caps)
                else if(font_label.equals(FONT_Intro_Black_Inline))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_black_inline)
                else if(font_label.equals(FONT_Intro_Black_Inline_Caps))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_black_inline_caps)
                else if(font_label.equals(FONT_Intro_Bold_Alt))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_boldalt)
                else if(font_label.equals(FONT_Intro_Bold_Caps))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_bold_caps)
                else if(font_label.equals(FONT_Intro_Book_Alt))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_book_alt)
                else if(font_label.equals(FONT_Intro_Book_Caps))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_book_caps)
                else if(font_label.equals(FONT_Intro_Light))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_light)
                else if(font_label.equals(FONT_Intro_Light_Alt))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_light_alt)
                else if(font_label.equals(FONT_Intro_Light_Caps))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_light_caps)
                else if(font_label.equals(FONT_Intro_Regular_Caps))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_regular_caps)
                else if(font_label.equals(FONT_Intro_Thin))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_thin)
                else if(font_label.equals(FONT_Intro_Thin_Caps))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.intro_thin_caps)
                else if(font_label.equals(FONT_Lucida_Handwriting))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.lhanddw)
                else if(font_label.equals(FONT_Lucida_Console))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.lucon)
                else if(font_label.equals(FONT_Martel_Light))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.martel_light)
                else if(font_label.equals(FONT_Martel_Regular))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.martel_regular)
                else if(font_label.equals(FONT_OneDot))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.onedot)
                else if(font_label.equals(FONT_OneDotCd))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.onedotcd)
                else if(font_label.equals(FONT_OneDotExt))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.onedotext)
                else if(font_label.equals(FONT_Palatino_Linotype))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.pala)
                else if(font_label.equals(FONT_PizzaPress_Antique))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.pizzapress_antique)
                else if(font_label.equals(FONT_PizzaPressExt_Fill))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.pizzapressext_fill)
                else if(font_label.equals(FONT_PizzaPressExt_Inline))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.pizzapressext_inline)
                else if(font_label.equals(FONT_PizzaPressExt_ReverseShadow))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.pizzapressext_reverseshadow)
                else if(font_label.equals(FONT_PizzaPress_Fill))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.pizzapress_fill)
                else if(font_label.equals(FONT_PizzaPress_Inline))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.pizzapress_inline)
                else if(font_label.equals(FONT_PizzaPress_OrnamentsNeue))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.pizzapress_ornamentsneue)
                else if(font_label.equals(FONT_PizzaPress_Outline))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.pizzapress_outline)
                else if(font_label.equals(FONT_PizzaPress_Regular))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.pizzapress_regular)
                else if(font_label.equals(FONT_PizzaPress_ReverseShadow))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.pizzapress_reverseshadow)
                else if(font_label.equals(FONT_PizzaPress_Shadow))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.pizzapress_shadow)
                else if(font_label.equals(FONT_Roboto_Bold))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.roboto_bold)
                else if(font_label.equals(FONT_Roboto_Condensed))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.robotocondensed_regular)
                else if(font_label.equals(FONT_Roboto_Light))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.roboto_light)
                else if(font_label.equals(FONT_Roboto_Regular))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.roboto_regular)
                else if(font_label.equals(FONT_Segoe_Print))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.segoepr)
                else if(font_label.equals(FONT_Segoe_UI))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.segoeui)
                else if(font_label.equals(FONT_Source_Sans_Pro_Bold))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.source_sans_pro_latin_700)
                else if(font_label.equals(FONT_Source_Sans_Pro_Bold_Italic))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.source_sans_pro_latin_700italic)
                else if(font_label.equals(FONT_Source_Sans_Pro_Regular))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.source_sans_pro_latin_regular)
                else if(font_label.equals(FONT_Source_Sans_Pro_Regular_Italic))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.source_sans_pro_latin_italic)
                else if(font_label.equals(FONT_SUNN_Serif_Bold))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.sunn_serif_bold)
                else if(font_label.equals(FONT_SUNN_Serif_Regular))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.sunn_serif_regular)
                else if(font_label.equals(FONT_Tahoma))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.tahoma)
                else if(font_label.equals(FONT_Times_New_Roman))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.times)
                else if(font_label.equals(FONT_Trebuchet_MS))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.trebuc)
                else if(font_label.equals(FONT_TwoDots))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.twodots)
                else if(font_label.equals(FONT_Verdana))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.verdana)
                else if(font_label.equals(FONT_Breakers_Slab))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.breakersslab_regular)
                else if(font_label.equals(FONT_Breakers_Slab_Black))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.breakersslab_black)
                else if(font_label.equals(FONT_Breakers_Slab_Light))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.breakersslab_light)
                else if(font_label.equals(FONT_Breakers_Slab_Thin))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.breakersslab_thin)
                else if(font_label.equals(FONT_Breakers_Slab_Ultra))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.breakersslab_ultra)
                else if(font_label.equals(FONT_Cambria))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.cambria)
                else if(font_label.equals(FONT_Chalet_Pinkberry))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.chalet_pinkberry)
                else if(font_label.equals(FONT_Gotham))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.gotham_book)
                else if(font_label.equals(FONT_Mission_Script))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.mission_script)
                else if(font_label.equals(FONT_Myriad_Pro))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.myriadpro_regular)
                else if(font_label.equals(FONT_Myriad_Pro_Condensed))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.myriad_pro_condensed)
                else if(font_label.equals(FONT_Neutra_Text))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.neutratext_book)
                else if(font_label.equals(FONT_Neutra_Text_Alt))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.neutratext_bookalt)
                else if(font_label.equals(FONT_Neutraface_Text))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.neutrafacetext_book)
                else if(font_label.equals(FONT_Neutraface_Text_Alt))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.neutratext_bookalt)
                else if(font_label.equals(FONT_Dogica_Pixel))
                    typeface = ctx.getResources().getFont(com.app.lsquared.R.font.dogica_pixel)

                textView.setTypeface(typeface)
            }else{

            }

        }
    }

}