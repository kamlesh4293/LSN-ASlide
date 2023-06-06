package com.app.lsquared.utils

class DimensionUtils {

    companion object{

        fun getCustomSize(hight: Int,div:Int): Int{
            return hight/div
        }

        fun getDoubleExtraSmallSize(hight: Int): Int{
            return hight/38
        }

        fun getExtraSmallSize2(hight: Int): Int{
            return hight/25
        }

        fun getExtraSmallSize(hight: Int): Int{
            return hight/22
        }

        fun getSmallSize(hight: Int): Int{
            return hight/18
        }

        fun getExtraLargeSize(hight: Int): Float{
            return (hight/4).toFloat()
        }

        fun getLargeSize(hight: Int): Float{
            return (hight/7.5).toFloat()
        }

        fun getMediumSize(hight: Int): Int{
            return hight/9
        }

        fun getMediumSize2(hight: Int, frameW: Int): Int{
            return hight/20
        }

        fun getMainImageSize(hight: Int): Int{
            return hight/4
        }

    }
}