package com.app.lsquared.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.WindowManager.LayoutParams
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.app.lsquared.model.*
import com.app.lsquared.model.widget.RssItem
import com.app.lsquared.ui.UiUtils
import com.google.gson.Gson
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

class DataParsing() {


    companion object{

        // check watermark available
        fun isWatermarkAvailable(prefernce: MySharePrefernce?): Boolean{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                return if (!data_obj.device[0].wm.equals("false") &&!data_obj.device[0].wm.equals("")) true else false
            }
            return false
        }

        // check watermark available
        fun isDefaultImageAvailable(prefernce: MySharePrefernce?): String{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                var image_name = data_obj.device[0].defaultImageName
                return if (!image_name.equals("false") &&!image_name.equals("")) image_name else ""
            }
            return ""
        }

        // get watermark
        fun getWatermark(prefernce: MySharePrefernce?): DeviceWaterMark {
            var data = prefernce?.getContentData()
            var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
            var watermark_obj = Gson().fromJson(data_obj.device[0].wm, DeviceWaterMark::class.java)
            return watermark_obj
        }

        // check frames available
        fun getRootBackground(prefernce: MySharePrefernce?): String{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.layout!=null && data_obj.layout.size>0 && data_obj.layout.get(0).frame!=null && data_obj.layout.get(0).frame.size>0 ){
                    return data_obj!!.layout[0].bg
                }
            }
            return "#000000"
        }

        // check frames available
        fun isFrameAvailable(prefernce: MySharePrefernce?): Boolean{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.layout!=null && data_obj.layout.size>0 && data_obj.layout.get(0).frame!=null && data_obj.layout.get(0).frame.size>0 ){

                    if (data_obj.device[0].screenshotUploadInterval != 0){
                        var ss_interval = if(data_obj.device[0].screenshotUploadInterval!=0)data_obj.device[0].screenshotUploadInterval else 300
                        prefernce?.putIntData(MySharePrefernce.KEY_SCREENSHOT_INTERVAL,ss_interval)
                    }
                    if (data_obj.device[0].wcoditime != 0){
                        var cod_ideal_time = if(data_obj.device[0].wcoditime!=null)data_obj.device[0].wcoditime else 0
                        prefernce?.putIntData(MySharePrefernce.KEY_COD_IDEAL_TIME,cod_ideal_time)
                    }

                    return true
                }
            }
            return false
        }

        // check frames available
        fun isOverrideAvailable(prefernce: MySharePrefernce?): Boolean{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.ovr!=null && data_obj.ovr.size>0 && data_obj.ovr.get(0).frame!=null && data_obj.ovr.get(0).frame.size>0 ){
                    var frames = data_obj.ovr.get(0).frame
                    var list = ArrayList<String>()
                    var list_frame = ArrayList<Frame>()
                    for(frame in frames){
                        if(!list.contains(frame.name)) {
                            if(DateTimeUtil.isValidWithTime(frame)){
                                list.add(frame.name)
                                list_frame.add(frame)
                            }
                        }
                    }
                    return if(list_frame.size>0) true else false
                }
            }
            return false
        }


        // get frames
        fun getFilterdFrames(prefernce: MySharePrefernce?): List<Frame>?{
            var data = prefernce?.getContentData()
            var time_signature = StringBuilder()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.layout!=null && data_obj.layout.size>0 && data_obj.layout.get(0).frame!=null && data_obj.layout.get(0).frame.size>0 ){
                    var frames = Utility.getFilterFrameList(data_obj.layout.get(0).frame)
                    var list = ArrayList<String>()
                    var list_frame = ArrayList<Frame>()
                    for(frame in frames){
                        if(!list.contains(frame.name)) {
                            if(DateTimeUtil.isValidWithTime(frame)){
                                time_signature.append(frame.sort)
                                Log.d("TAG", "getFilterdFrames: ${frame.sort}")
                                list.add(frame.name)
                                list_frame.add(frame)
                            }
                        }
                    }
                    prefernce?.putStringData(MySharePrefernce.KEY_TIME_SIGNATURE,time_signature.toString())
                    return list_frame
                }
            }
            return null
        }

        // get time signature
        fun getTimeSignature(prefernce: MySharePrefernce?): String{
            var data = prefernce?.getContentData()
            var time_signature = StringBuilder()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.layout!=null && data_obj.layout.size>0 && data_obj.layout.get(0).frame!=null && data_obj.layout.get(0).frame.size>0 ){
                    var frames = Utility.getFilterFrameList(data_obj.layout.get(0).frame)
                    var list = ArrayList<String>()
                    var list_frame = ArrayList<Frame>()
                    for(frame in frames){
                        if(!list.contains(frame.name)) {
                            if(DateTimeUtil.isValidWithTime(frame)){
                                time_signature.append(frame.sort)
                                Log.d("TAG", "getFilterdFrames: ${frame.sort}")
                                list.add(frame.name)
                                list_frame.add(frame)
                            }
                        }
                    }
                    return time_signature.toString()
                }
            }
            return ""
        }

        // get override frames
        fun getOverrideFrames(prefernce: MySharePrefernce?): List<Frame>?{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.ovr!=null && data_obj.ovr.size>0 && data_obj.ovr.get(0).frame!=null && data_obj.ovr.get(0).frame.size>0 ){
                    return data_obj.ovr.get(0).frame
                }
            }
            return null
        }

        // check download content
        fun getDownloableList(prefernce: MySharePrefernce?): List<Downloadable>{
            var list = mutableListOf<Downloadable>()
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.downloadable!=null && data_obj.downloadable.size>0){
                    for(i in 0..data_obj.downloadable.size-1){
                        Log.d("TAG", "getDownloableList: $i - ${data_obj.downloadable[i].name}")
                        if(!DataManager.fileIsExist(data_obj.downloadable[i])){
                            Log.d("TAG", "getDownloableList: not exist $i - ${data_obj.downloadable[i].name}")
                            list.add(data_obj.downloadable[i])
                        }
                    }
                }
            }
            return list
        }

        // check download content
        fun getDownloableFileNameList(prefernce: MySharePrefernce?): List<String>{
            var list = mutableListOf<String>()
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.downloadable!=null && data_obj.downloadable.size>0){
                    for(i in 0..data_obj.downloadable.size-1){
                        list.add(data_obj.downloadable[i].name)
                    }
                }
            }
            return list
        }


        fun getColor(item: Item): String {
            var setting_obj = JSONObject(item.settings)
            return setting_obj.getString("tint")
        }

        fun getSettingColor(item: Item,key:String): String {
            var setting_obj = JSONObject(item.settings)
            return setting_obj.getString(key)
        }

        fun parse(inputStream: InputStream):List<RssItem> {

            val rssItems = ArrayList<RssItem>()
            var rssItem : RssItem?= null
            var text: String? = null
            try {
                val factory = XmlPullParserFactory.newInstance()
                factory.isNamespaceAware = true
                val parser = factory.newPullParser()
                parser.setInput(inputStream, null)
                var eventType = parser.eventType
                var foundItem = false
                var title_foundItem = false
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    val tagname = parser.name
                    when (eventType) {

                        XmlPullParser.START_TAG -> if (tagname.equals("item", ignoreCase = true)) {
                            // create a new instance of employee
                            foundItem = true
                            rssItem = RssItem("","","")
                        }
                        XmlPullParser.TEXT -> text = parser.text
                        XmlPullParser.END_TAG -> if (tagname.equals("item", ignoreCase = true)) {
                            // add employee object to list
                            rssItem?.let { rssItems.add(it) }
                            foundItem = false
                        } else if ( foundItem && tagname.equals("title", ignoreCase = true)) {
                            rssItem!!.title = text.toString()
                        } else if (foundItem && tagname.equals("link", ignoreCase = true)) {
                            rssItem!!.link = text.toString()
                        } else if (foundItem && tagname.equals("description", ignoreCase = true)) {
                            rssItem!!.desc = text.toString()
                        }
                    }
                    eventType = parser.next()
                }
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return rssItems
        }

        fun parse2(inputStream: InputStream):String {

            var text: String? = null
            var title_text = ""
            try {
                val factory = XmlPullParserFactory.newInstance()
                factory.isNamespaceAware = true
                val parser = factory.newPullParser()
                parser.setInput(inputStream, null)
                var eventType = parser.eventType
                var foundItem = false
                var title_foundItem = false
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    val tagname = parser.name

                    Log.d("TAG", "parse: $tagname")
                    when (eventType) {

                        XmlPullParser.START_TAG -> if (tagname.equals("channel", ignoreCase = true)) {
                            // create a new instance of employee
                            foundItem = true
                        }
                        XmlPullParser.TEXT -> text = parser.text
                        XmlPullParser.END_TAG -> if (tagname.equals("channel", ignoreCase = true)) {
                            // add employee object to list
                            foundItem = false
                        } else if ( foundItem && tagname.equals("title", ignoreCase = true)) {
                            if(title_text.equals("")) title_text = text.toString()
                        }
                    }
                    eventType = parser.next()
                }
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return title_text
        }


        // web view interval
        fun getWebInterval(item: Item):Int{
            var setting_obj = JSONObject(item.settings)
            var reload_opt = setting_obj.getString("reloadOpt")
            if(reload_opt.equals("n")) return 0
            var reload = setting_obj.getString("reload")
            return if(reload.equals("")) 0 else reload.toInt()
        }

        // setting rotate
        fun getSettingRotate(item: Item):Int{
            var setting_obj = JSONObject(item.settings)
            return setting_obj.getInt("rotate")
        }

        // override frame
        fun getOverrideFrame(ctx: Context):LinearLayout{
            var id = 10
            var ll_frame = LinearLayout(ctx)
            ll_frame.id = id
            val params = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
            ll_frame.layoutParams = params
            return ll_frame
        }

        // create frame
        fun getLayoutFrame(ctx: Context, frame: Frame,position: Int):LinearLayout{
            var ll_frame = LinearLayout(ctx)
            ll_frame.id = position+10
            val params = LinearLayout.LayoutParams(frame?.w!!,frame.h)

            ll_frame.layoutParams = params
            ll_frame.x = frame.x.toFloat()
            ll_frame.y = frame.y.toFloat()

            if(frame.align.equals(Constant.ALIGN_TOP_LEFT)) ll_frame.gravity = Gravity.TOP or Gravity.LEFT
            if(frame.align.equals(Constant.ALIGN_TOP_CENTER)) ll_frame.gravity = Gravity.TOP or Gravity.CENTER
            if(frame.align.equals(Constant.ALIGN_TOP_RIGHT)) ll_frame.gravity = Gravity.TOP or Gravity.RIGHT

            if(frame.align.equals(Constant.ALIGN_MIDDLE_LEFT)) ll_frame.gravity = Gravity.LEFT or Gravity.CENTER
            if(frame.align.equals(Constant.ALIGN_MIDDLE_CENTER)) ll_frame.gravity = Gravity.CENTER
            if(frame.align.equals(Constant.ALIGN_MIDDLE_RIGHT)) ll_frame.gravity = Gravity.RIGHT or Gravity.CENTER

            if(frame.align.equals(Constant.ALIGN_BOTTOM_LEFT)) ll_frame.gravity = Gravity.BOTTOM or Gravity.LEFT
            if(frame.align.equals(Constant.ALIGN_BOTTOM_CENTER)) ll_frame.gravity = Gravity.BOTTOM or Gravity.CENTER
            if(frame.align.equals(Constant.ALIGN_BOTTOM_RIGHT)) ll_frame.gravity = Gravity.BOTTOM or Gravity.RIGHT


            if(frame.bg.equals(""))ll_frame.setBackgroundColor(Color.TRANSPARENT)
            else{
//                relative_frame.setBackgroundColor(Color.parseColor(frame.bg))
                ll_frame.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(frame.bg!!,frame.bga)))
            }
            return ll_frame
        }

        // screen capture frame
        fun getScreenCaptureFrame(ctx: Context,frame: Frame):ImageView{
            var image_frame = ImageView(ctx)
            val params = LinearLayout.LayoutParams(frame?.w!!,frame.h)

            image_frame.layoutParams = params
            image_frame.x = frame.x.toFloat()
            image_frame.y = frame.y.toFloat()

            if(frame.bg.equals(""))image_frame.setBackgroundColor(Color.TRANSPARENT)
            else image_frame.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(frame.bg!!,frame.bga)))
            return image_frame
        }

        // Data parsing for content on demand

        // check cod available
        fun isCodAvailable(prefernce: MySharePrefernce?): Boolean{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                try {
                    if (data_obj.layout!=null && data_obj.layout.size>0 && data_obj.layout.get(0).cod != null ){
                        return true
                    }
                }catch (ex:Exception) {
                    return false
                }
            }
            return false
        }

        fun getDataObject(prefernce: MySharePrefernce?): ResponseJsonData? {
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                return  Gson().fromJson(data, ResponseJsonData::class.java)
            }
            return null
        }

        // get cod categories
        fun getCodItems(prefernce: MySharePrefernce?): ArrayList<CodItem> {
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.layout.get(0).cod != null &&data_obj.layout.get(0).cod!!.get(0).item != null
                    && data_obj.layout.get(0).cod!!.get(0).item.size > 0  ){
                    return data_obj.layout.get(0).cod!!.get(0).item
                }
            }
            return ArrayList<CodItem>()
        }

        // is cod content
        fun isCodItemContentAvailable(prefernce: MySharePrefernce?): Boolean{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.layout.get(0).cod != null &&data_obj.layout.get(0).cod!!.get(0).item != null
                    && data_obj.layout.get(0).cod!!.get(0).item.size > 0  ){

                    var items =  data_obj.layout.get(0).cod!!.get(0).item
                    for (item in items) {
                        Log.d("TAG", "isCodItemContentAvailable: item - ${item.name}")
                        if (isValidCod(item)){
                            Log.d("TAG", "isCodItemContentAvailable: item return -true")
                            return true
                        }
                    }
                }
            }
            return false
        }

        private fun isValidCod(codItem: CodItem): Boolean {
            if(codItem.cat.size==0) return false
            for (cate in codItem.cat){
                if(cate.content.size>0){
                    Log.d("TAG", "isCodItemContentAvailable: cod item ${cate.label}")
                    return true
                    break
                }
            }
            return false
        }



        // filter category
        fun getFilterdContent(content: ArrayList<Content>, filter_type: String)
        : MutableList<Content> {
            var list = mutableListOf<Content>()
            for (item in content){
                if(filter_type.equals(Constant.CONTENT_WEB)){
                    if(item.type.equals(Constant.CONTENT_WEB)
                        || item.type.equals(Constant.CONTENT_WIDGET_GOOGLE)
                        || item.type.equals(Constant.CONTENT_WIDGET_POWER)
                        || item.type.equals(Constant.CONTENT_WIDGET_TRAFFIC)
                        || item.type.equals(Constant.CONTENT_WIDGET_VIMEO)
                        || item.type.equals(Constant.CONTENT_WIDGET_YOUTUBE)
                    ) list.add(item)
                }else if(item.type.equals(filter_type)) list.add(item)
            }
            return list
        }

    }
}