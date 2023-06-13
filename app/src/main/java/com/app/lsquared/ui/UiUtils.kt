package com.app.lsquared.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.app.lsquared.R
import com.app.lsquared.pasring.DataParsingSetting
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable
import top.defaults.drawabletoolbox.DrawableBuilder


class UiUtils {

    companion object{

        fun getColorWithOpacity(color: String = "#000000",color_opacity: String="2"):String{
            var opacity = "#00"
            if(color_opacity.equals("0.1")) opacity = "#1A"
            if(color_opacity.equals("0.2")) opacity = "#33"
            if(color_opacity.equals("0.3")) opacity = "#4D"
            if(color_opacity.equals("0.4")) opacity = "#66"
            if(color_opacity.equals("0.5")) opacity = "#80"
            if(color_opacity.equals("0.6")) opacity = "#99"
            if(color_opacity.equals("0.7")) opacity = "#B3"
            if(color_opacity.equals("0.8")) opacity = "#CC"
            if(color_opacity.equals("0.9")) opacity = "#E6"
            if(color_opacity.equals("1")) opacity = "#FF"
            var color_code = color.replace("#","")
            Log.d("TAG", "getColorWithOpacity: $opacity$color_code")
            return "$opacity$color_code"
        }

        fun getColor(color: String): Int {
            return Color.parseColor(color)
        }

        fun getRoundedFilled(color: String): Drawable {
            return DrawableBuilder()
                .rectangle()
                .solidColor(Color.parseColor("#CC${color.replace("#","")}"))
                .topLeftRadius(20) // in pixels
                .topRightRadius(20) // in pixels
                .build()
        }

        fun getRoundedBorderwithFilled(color: String): Drawable {
            return DrawableBuilder()
                .rectangle()
                .solidColor(Color.parseColor("#EBF5FB"))
                .bottomLeftRadius(15) // in pixels
                .bottomRightRadius(15) // in pixels
                .build()
        }

        fun getRoundedBorder(color: String): Drawable {
            return DrawableBuilder()
                .rectangle()
                .strokeColor(Color.parseColor(color))
                .strokeWidth(3)
                .solidColor(Color.parseColor("#ffffff"))
                .topLeftRadius(10) // in pixels
                .topRightRadius(10) // in pixels
                .bottomLeftRadius(10) // in pixels
                .bottomRightRadius(10) // in pixels
                .build()
        }

        fun getDateMargin(height: Int): LinearLayout.LayoutParams {
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height)
            layoutParams.setMargins(3,3,3,3)
            return layoutParams
        }

        fun setImageSize(imageView: ImageView, imageSize: Int) {
            val layoutParams = LinearLayout.LayoutParams(imageSize,imageSize)
            layoutParams.setMargins(imageSize/10,imageSize/10,imageSize/10,imageSize/10)
            imageView.setLayoutParams(layoutParams)
        }

        fun setCityTextView(textView: TextView,height:Int){
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(height*1.5).toInt())
            layoutParams.setMargins(height/2,height/2,height/2,height/2)
            textView.setLayoutParams(layoutParams)
        }

        fun convertCamelCaps(msg:String): String {
            var msgs = msg.split(" ")
            var builder = StringBuilder()
            for (i in 0..msgs.size-1){
                var myString = msgs[i]
                val upperString: String = myString.substring(0, 1).toUpperCase() + myString.substring(1).toLowerCase()
                builder.append(upperString+" ")
            }
            return builder.toString()
        }

        fun getWindUnit(settings: String): String {
            var wku = DataParsingSetting.getWsu(settings)
            return if(wku.equals("ms")) "m/s" else if(wku.equals("mh")) "m/h" else "Km/h"
        }


        fun setWeatherVecotImage(imageView: ImageView, icon: Int?, color: Int,ctx:Context) {

            val vectorMasterDrawable = VectorMasterDrawable(ctx, R.drawable.a1)

            if(icon==1000)vectorMasterDrawable.resID = R.drawable.ic_up
            if(icon==1001)vectorMasterDrawable.resID = R.drawable.ic_down
            if(icon==1002)vectorMasterDrawable.resID = R.drawable.icw_2_showers
            if(icon==1003)vectorMasterDrawable.resID = R.drawable.icw__2_windy
            if(icon==1)vectorMasterDrawable.resID = R.drawable.a1
            if(icon==2)vectorMasterDrawable.resID = R.drawable.a2
            if(icon==3)vectorMasterDrawable.resID = R.drawable.a3
            if(icon==4)vectorMasterDrawable.resID = R.drawable.a4
            if(icon==5)vectorMasterDrawable.resID = R.drawable.a5
            if(icon==6)vectorMasterDrawable.resID = R.drawable.a6
            if(icon==7)vectorMasterDrawable.resID = R.drawable.a7
            if(icon==8)vectorMasterDrawable.resID = R.drawable.a8
            if(icon==11)vectorMasterDrawable.resID = R.drawable.a11
            if(icon==12)vectorMasterDrawable.resID = R.drawable.a12
            if(icon==13)vectorMasterDrawable.resID = R.drawable.a13
            if(icon==14)vectorMasterDrawable.resID = R.drawable.a14
            if(icon==15)vectorMasterDrawable.resID = R.drawable.a15
            if(icon==16)vectorMasterDrawable.resID = R.drawable.a16
            if(icon==17)vectorMasterDrawable.resID = R.drawable.a17
            if(icon==18)vectorMasterDrawable.resID = R.drawable.a18
            if(icon==19)vectorMasterDrawable.resID = R.drawable.a19
            if(icon==20)vectorMasterDrawable.resID = R.drawable.a20
            if(icon==21)vectorMasterDrawable.resID = R.drawable.a21
            if(icon==22)vectorMasterDrawable.resID = R.drawable.a22
            if(icon==23)vectorMasterDrawable.resID = R.drawable.a23
            if(icon==24)vectorMasterDrawable.resID = R.drawable.a24
            if(icon==25)vectorMasterDrawable.resID = R.drawable.a25
            if(icon==26)vectorMasterDrawable.resID = R.drawable.a26
            if(icon==29)vectorMasterDrawable.resID = R.drawable.a29
            if(icon==30)vectorMasterDrawable.resID = R.drawable.a30
            if(icon==31)vectorMasterDrawable.resID = R.drawable.a31
            if(icon==32)vectorMasterDrawable.resID = R.drawable.a32
            if(icon==33)vectorMasterDrawable.resID = R.drawable.a33
            if(icon==34)vectorMasterDrawable.resID = R.drawable.a34
            if(icon==35)vectorMasterDrawable.resID = R.drawable.a35
            if(icon==36)vectorMasterDrawable.resID = R.drawable.a36
            if(icon==37)vectorMasterDrawable.resID = R.drawable.a37
            if(icon==38)vectorMasterDrawable.resID = R.drawable.a38
            if(icon==39)vectorMasterDrawable.resID = R.drawable.a39
            if(icon==40)vectorMasterDrawable.resID = R.drawable.a40
            if(icon==41)vectorMasterDrawable.resID = R.drawable.a41
            if(icon==42)vectorMasterDrawable.resID = R.drawable.a42
            if(icon==43)vectorMasterDrawable.resID = R.drawable.a43
            if(icon==44)vectorMasterDrawable.resID = R.drawable.a44

            var vector = getVectorIconWithColor(vectorMasterDrawable,color)

            imageView.setImageDrawable(vector)
        }

        fun getVectorIconWithColor(vector_iv:VectorMasterDrawable,color: Int):VectorMasterDrawable{

            var group_fill = vector_iv.getGroupModelByName("fill_g1")
            var group_stroke = vector_iv.getGroupModelByName("stroke_g1")
            var group_stroke_fill = vector_iv.getGroupModelByName("stroke_fill_g3")

            // for fill color
            if(group_fill!=null) group_fill.pathModels.forEach { it.fillColor = color }
            // for stroke color
            if(group_stroke!=null) group_stroke.pathModels.forEach { it.strokeColor = color }
            // for stroke and fill color
            if(group_stroke_fill!=null) group_stroke_fill.pathModels.forEach {
                it.strokeColor = color
                it.fillColor = color
            }
            return vector_iv
        }

        fun setDrawableView(v: View, backgroundColor: Int, borderColor: Int) {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadii = floatArrayOf(5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f)
            shape.setColor(backgroundColor)
            shape.setStroke(2, borderColor)
            v.setBackground(shape)
        }

        fun setDrawableViewDay(v: View, backgroundColor: Int) {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadii = floatArrayOf(5f, 5f, 5f, 5f, 0f, 0f, 0f, 0f)
            shape.setColor(backgroundColor)
            v.setBackground(shape)
        }

        fun setDrawableOnlyTop(v: View, backgroundColor: Int) {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadii = floatArrayOf(5f, 5f, 5f, 5f, 0f, 0f, 0f, 0f)
            shape.setColor(backgroundColor)
            v.setBackground(shape)
        }

        fun setDrawableOnlyBottom(v: View, backgroundColor: Int) {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 5f, 5f, 5f, 5f)
            shape.setColor(backgroundColor)
            v.setBackground(shape)
        }
    }
}