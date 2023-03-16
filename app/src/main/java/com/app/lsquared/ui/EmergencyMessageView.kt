package com.app.lsquared.ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.app.lsquared.R
import com.app.lsquared.model.EmSetting
import com.app.lsquared.model.EmergencyMessage
import com.app.lsquared.utils.FontUtil
import com.app.lsquared.utils.ImageUtil
import com.google.gson.Gson

class EmergencyMessageView {

    companion object{

        fun getEmDuration(data: String?): Long {
            var msg_obj = Gson().fromJson(data,EmergencyMessage::class.java)
            return (msg_obj.messages.get(0).msg.get(0).duration!!*1000).toLong()
        }

        fun getEmView(ctx: Context, data: String?): View? {
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.feature_emergency_msg,null)

            var msg_obj = Gson().fromJson(data,EmergencyMessage::class.java)
            var setting_obj = Gson().fromJson(msg_obj.messages.get(0).settings, EmSetting::class.java)

            var title_tv = view.findViewById<TextView>(R.id.tv_em_title)
            var msg_tv = view.findViewById<TextView>(R.id.tv_em_msg)
            var layout_main = view.findViewById<LinearLayout>(R.id.ll_em_main)
            var layout_header = view.findViewById<LinearLayout>(R.id.ll_em_header)
            var layout_title = view.findViewById<LinearLayout>(R.id.ll_em_titlebg)
            var imageview = view.findViewById<ImageView>(R.id.iv_em_image)

            val params = LinearLayout.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT)
            layout_main.layoutParams = params
            layout_main.setBackgroundColor(Color.parseColor(setting_obj.bBgColor))

            title_tv.text = setting_obj.hText
            title_tv.setTextColor(Color.parseColor(setting_obj.hTextColor))
            layout_title.setBackgroundColor(Color.parseColor(setting_obj.hBgColor))
            FontUtil.setFonts(ctx,title_tv,setting_obj.hFont?.label!!)

            msg_tv.text = msg_obj.messages.get(0).msg.get(0).title
            msg_tv.setTextColor(Color.parseColor(setting_obj.mTextColor))
            layout_header.setBackgroundColor(Color.parseColor(setting_obj.mBgColor))
            FontUtil.setFonts(ctx,msg_tv,setting_obj.mFont?.label!!)

            ImageUtil.loadImage(ctx,msg_obj.messages.get(0).img!!,imageview)

//            layout_title.post {
//                var hi = layout_title.height
//                title_tv.textSize = hi.toFloat()/10
//                Log.d("TAG", "getEmView: layout_title  $hi")
//            }
//            msg_tv.post {
//                var hi = msg_tv.height
//                msg_tv.textSize = hi.toFloat()
//                Log.d("TAG", "getEmView: msg_tv  $hi")/10
//            }

            return view
        }

    }
}