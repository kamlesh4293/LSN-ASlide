package com.app.lsquared.ui.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.app.lsquared.model.Content
import com.app.lsquared.model.Item
import com.app.lsquared.ui.MainActivity
import com.app.lsquared.utils.DataManager
import com.app.lsquared.utils.ImageUtil
import com.app.lsquared.utils.Utility
import java.io.File

class ImageWidget {

    companion object{


        fun getImageWidget(ctx: Context,width: Int,height: Int,fileName: String,filesize: Int): ImageView {
            Log.d("TAG", "getImageWidget: $width , $height, $fileName , $filesize")
            var image = ImageView(ctx)
            val params = RelativeLayout.LayoutParams(width,height)
            image.layoutParams = params
//            val options = BitmapFactory.Options()
//            options.inSampleSize = 1
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888
//            options.inJustDecodeBounds = false

            val path = DataManager.getDirectory()+ File.separator+fileName
            if(Utility.isFileCompleteDownloaded(fileName,filesize)){
                image.setImageBitmap(BitmapFactory.decodeFile(path, ImageUtil.getImageOption()))
            }
            return image
        }

    }
}