package com.app.lsquared.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import top.defaults.drawabletoolbox.DrawableBuilder


class UiUtils {

    companion object{

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

        fun setWeatherIcon(imageView: ImageView, icon: Int?, imageSize: Int,top : Int,bottom : Int,left : Int,right : Int) {

            val layoutParams = LinearLayout.LayoutParams(imageSize,imageSize)
            layoutParams.setMargins(imageSize/left,imageSize/top,imageSize/right,imageSize/bottom)
            imageView.setLayoutParams(layoutParams)

            if(icon==1)imageView.setImageResource(R.drawable.a1)
            if(icon==2)imageView.setImageResource(R.drawable.a2)
            if(icon==3)imageView.setImageResource(R.drawable.a3)
            if(icon==4)imageView.setImageResource(R.drawable.a4)
            if(icon==5)imageView.setImageResource(R.drawable.a5)
            if(icon==6)imageView.setImageResource(R.drawable.a6)
            if(icon==7)imageView.setImageResource(R.drawable.a7)
            if(icon==8)imageView.setImageResource(R.drawable.a8)
            if(icon==11)imageView.setImageResource(R.drawable.a11)
            if(icon==12)imageView.setImageResource(R.drawable.a12)
            if(icon==13)imageView.setImageResource(R.drawable.a13)
            if(icon==14)imageView.setImageResource(R.drawable.a14)
            if(icon==15)imageView.setImageResource(R.drawable.a15)
            if(icon==16)imageView.setImageResource(R.drawable.a16)
            if(icon==17)imageView.setImageResource(R.drawable.a17)
            if(icon==18)imageView.setImageResource(R.drawable.a18)
            if(icon==19)imageView.setImageResource(R.drawable.a19)
            if(icon==20)imageView.setImageResource(R.drawable.a20)
            if(icon==21)imageView.setImageResource(R.drawable.a21)
            if(icon==22)imageView.setImageResource(R.drawable.a22)
            if(icon==23)imageView.setImageResource(R.drawable.a23)
            if(icon==24)imageView.setImageResource(R.drawable.a24)
            if(icon==25)imageView.setImageResource(R.drawable.a25)
            if(icon==26)imageView.setImageResource(R.drawable.a26)
            if(icon==29)imageView.setImageResource(R.drawable.a29)
            if(icon==30)imageView.setImageResource(R.drawable.a30)
            if(icon==31)imageView.setImageResource(R.drawable.a31)
            if(icon==32)imageView.setImageResource(R.drawable.a32)
            if(icon==33)imageView.setImageResource(R.drawable.a33)
            if(icon==34)imageView.setImageResource(R.drawable.a34)
            if(icon==35)imageView.setImageResource(R.drawable.a35)
            if(icon==36)imageView.setImageResource(R.drawable.a36)
            if(icon==37)imageView.setImageResource(R.drawable.a37)
            if(icon==38)imageView.setImageResource(R.drawable.a38)
            if(icon==39)imageView.setImageResource(R.drawable.a39)
            if(icon==40)imageView.setImageResource(R.drawable.a40)
            if(icon==41)imageView.setImageResource(R.drawable.a41)
            if(icon==42)imageView.setImageResource(R.drawable.a42)
            if(icon==43)imageView.setImageResource(R.drawable.a43)
            if(icon==44)imageView.setImageResource(R.drawable.a44)
        }
    }
}