package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.model.cod.Settings
import com.app.lsquared.ui.MainActivity
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.ui.activity.CODActivity
import com.app.lsquared.utils.DataParsing
import com.app.lsquared.utils.FontUtil
import com.app.lsquared.utils.ImageUtil
import com.google.gson.Gson

class WidgetCodButton {

    companion object{

        fun getWidgetCodButton(ctx: Context, item: Item,): View {
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.view_cod,null)

            var ll_main_cod = view.findViewById<LinearLayout>(R.id.ll_main_cod)
            var cod_bt = view.findViewById<TextView>(R.id.bt_cod)
            var iv_top = view.findViewById<ImageView>(R.id.iv_cod_top)
            var iv_bottom = view.findViewById<ImageView>(R.id.iv_cod_bottom)
            var iv_left = view.findViewById<ImageView>(R.id.iv_cod_left)
            var iv_right = view.findViewById<ImageView>(R.id.iv_cod_right)

            // setting
            var settings = Gson().fromJson(item.settings, Settings::class.java)
            cod_bt.text = settings?.text
            cod_bt.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(settings?.bg!!,settings?.bga!!)))
            cod_bt.setTextColor(Color.parseColor(settings?.textColor))
            cod_bt.textSize = settings?.textSize!!.toFloat()


            var text_style = settings?.textStyle
            // deafult
            if(text_style.equals("")) cod_bt.typeface = Typeface.DEFAULT
            // bold
            if(text_style.equals("b")) cod_bt.setTypeface(null, Typeface.BOLD)
            // italic
            if(text_style.equals("i")) cod_bt.setTypeface(null, Typeface.ITALIC)

            var font = settings?.textFont?.label!!
            if(!font.equals("")) FontUtil.setFonts(ctx,cod_bt,font)

            // underline
            if(text_style.equals("u")){
                cod_bt.typeface = Typeface.DEFAULT
                cod_bt.setPaintFlags(cod_bt.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            }else cod_bt.setPaintFlags(0)


            // for cod image
            if(item.content != null && item.content.size>0){
                var img_url = item.content[0].fileName
                var img_pos = settings?.iconP
                iv_top.visibility = if(img_pos.equals("t")) View.VISIBLE else View.GONE
                iv_bottom.visibility = if(img_pos.equals("b")) View.VISIBLE else View.GONE
                iv_left.visibility = if(img_pos.equals("l")) View.VISIBLE else View.GONE
                iv_right.visibility = if(img_pos.equals("r")) View.VISIBLE else View.GONE
                if(img_pos.equals("t")) ImageUtil.loadLocalImage(img_url!!,iv_top)
                if(img_pos.equals("l")) ImageUtil.loadLocalImage(img_url!!,iv_left)
                if(img_pos.equals("r")) ImageUtil.loadLocalImage(img_url!!,iv_right)
                if(img_pos.equals("b")) ImageUtil.loadLocalImage(img_url!!,iv_bottom)
//                if(img_pos.equals("l") || img_pos.equals("r")){
//                    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT)
//                    cod_bt.layoutParams = params
//                }
//                if(img_pos.equals("t") || img_pos.equals("b")){
//                    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
//                    cod_bt.layoutParams = params
//                }
            }else{
                iv_top.visibility = View.GONE
                iv_bottom.visibility =  View.GONE
                iv_left.visibility =  View.GONE
                iv_right.visibility =  View.GONE
            }

            ll_main_cod.setOnClickListener { openCod(ctx) }
//            iv_top.setOnClickListener { openCod(ctx) }
//            iv_bottom.setOnClickListener { openCod(ctx) }
//            iv_left.setOnClickListener { openCod(ctx) }
//            iv_right.setOnClickListener { openCod(ctx) }

            return view
        }

        fun openCod(ctx: Activity) {
            if((ctx as MainActivity).getDownloading()==0){
                ctx.removeData()
                ctx.startActivity(Intent(ctx,CODActivity::class.java))
            }

        }

    }

}