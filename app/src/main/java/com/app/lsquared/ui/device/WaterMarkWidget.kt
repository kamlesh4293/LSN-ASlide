package com.app.lsquared.ui.device

import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import com.app.lsquared.databinding.ActivityMainMultifameBinding
import com.app.lsquared.model.DeviceWaterMark
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.Constant
import com.app.lsquared.utils.DataManager
import com.app.lsquared.utils.ImageUtil
import com.app.lsquared.utils.Utility
import java.io.File

class WaterMarkWidget {

    companion object{

        fun getWaterMark(binding: ActivityMainMultifameBinding, watermark: DeviceWaterMark) {

            var layout = binding.llWatermark
            layout.visibility = View.VISIBLE


            if(watermark.align.equals(Constant.ALIGN_TOP_LEFT)) layout.gravity = Gravity.TOP or Gravity.LEFT
            if(watermark.align.equals(Constant.ALIGN_TOP_CENTER)) layout.gravity = Gravity.TOP or Gravity.CENTER
            if(watermark.align.equals(Constant.ALIGN_TOP_RIGHT)) layout.gravity = Gravity.TOP or Gravity.RIGHT

            if(watermark.align.equals(Constant.ALIGN_MIDDLE_LEFT)) layout.gravity = Gravity.LEFT or Gravity.CENTER
            if(watermark.align.equals(Constant.ALIGN_MIDDLE_CENTER)) layout.gravity = Gravity.CENTER
            if(watermark.align.equals(Constant.ALIGN_MIDDLE_RIGHT)) layout.gravity = Gravity.RIGHT or Gravity.CENTER

            if(watermark.align.equals(Constant.ALIGN_BOTTOM_LEFT)) layout.gravity = Gravity.BOTTOM or Gravity.LEFT
            if(watermark.align.equals(Constant.ALIGN_BOTTOM_CENTER)) layout.gravity = Gravity.BOTTOM or Gravity.CENTER
            if(watermark.align.equals(Constant.ALIGN_BOTTOM_RIGHT)) layout.gravity = Gravity.BOTTOM or Gravity.RIGHT

            val path = DataManager.getDirectory()+ File.separator+watermark.img?.thumb?.replace("images/","")
            var file = File(path)
            binding.ivMainWatermark.visibility = View.VISIBLE
            binding.ivMainWatermark.setImageBitmap(BitmapFactory.decodeFile(path,ImageUtil.getImageOption()))
            layout.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity( watermark.bg!!,watermark.bga!!)))
        }

    }


}