package com.app.lsquared.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.*
import com.app.lsquared.model.*
import com.app.lsquared.model.widget.RssItem
import com.google.gson.Gson
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

class DataParsing() {


    companion object{

        fun getInactiveItems(data_obj: ResponseJsonData) :MutableList<String>{
            var items: MutableList<String> = mutableListOf()
            var layout = data_obj.layout.get(0)
            if (layout.inactive != null && layout.inactive!!.size > 0) {
                var inactive = layout.inactive
                for (i in 0..inactive.size - 1) {
                    if (inactive.get(i).item != null && inactive.get(i).item.size > 0) {
                        var items_array = inactive.get(i).item
                        for (j in 0..items_array.size - 1) {
                            items.add(items_array[j].fileName)
                        }
                    }
                }
            }
            return items
        }

        fun getFrame(prefernce: MySharePrefernce?,frame_position:Int): Frame?{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.layout!=null && data_obj.layout.size>0 && data_obj.layout.get(0).frame!=null && data_obj.layout.get(0).frame.size>0 ){
                    return data_obj.layout.get(0).frame.get(frame_position)
                }
            }
            return null
        }

        // check layout is availavle
        fun isLayoutAvailable(prefernce: MySharePrefernce?): Boolean{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)

                if (data_obj.device[0].screenshotUploadInterval != 0){
                    var ss_interval = if(data_obj.device[0].screenshotUploadInterval!=0)data_obj.device[0].screenshotUploadInterval else 300
                    prefernce?.putIntData(MySharePrefernce.KEY_SCREENSHOT_INTERVAL,ss_interval)
                }
                if (data_obj.device[0].wcoditime != 0){
                    var cod_ideal_time = if(data_obj.device[0].wcoditime!=null)data_obj.device[0].wcoditime else 0
                    prefernce?.putIntData(MySharePrefernce.KEY_COD_IDEAL_TIME,cod_ideal_time)
                }
                if (data_obj.layout!=null && data_obj.layout.size>0 && data_obj.layout.get(0).frame!=null && data_obj.layout.get(0).frame.size>0 ){
                    return true
                }
            }
            return false
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
                    return true
                }
            }
            return false
        }


        // get dynamic frame layout
        fun getFrameLayouts(prefernce: MySharePrefernce?,ctx:Activity): List<RelativeLayout>{
            var list = mutableListOf<RelativeLayout>()
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.layout!=null && data_obj.layout.size>0 && data_obj.layout.get(0).frame!=null && data_obj.layout.get(0).frame.size>0 ){

                    var layout = data_obj.layout.get(0)
                    var list_frame = Utility.getFilterFrameList(layout.frame)
                    for (i in 0..list_frame.size - 1) {
                        val params = RelativeLayout.LayoutParams(list_frame.get(i).w,list_frame.get(i).h)
                        var frame_layout = RelativeLayout(ctx)
                        frame_layout.layoutParams = params
                        frame_layout.id = i+11234
                        frame_layout.x = list_frame.get(i).x.toFloat()
                        frame_layout.y = list_frame.get(i).y.toFloat()

                        if(list_frame.get(i).bg.equals(""))frame_layout.setBackgroundColor(Color.TRANSPARENT)
                        else frame_layout.setBackgroundColor(Color.parseColor(list_frame.get(i).bg))
                        list.add(frame_layout)
                    }
                }
            }
            return list
        }

        // get frames
        fun getFilterdFrames(prefernce: MySharePrefernce?): List<Frame>?{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.layout!=null && data_obj.layout.size>0 && data_obj.layout.get(0).frame!=null && data_obj.layout.get(0).frame.size>0 ){
                    return Utility.getFilterFrameList(data_obj.layout.get(0).frame)
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
            var title_text: String? = null


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
                    Log.d("TAG", "parse: tagnemt - $tagname")
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
            Log.d("TAG", "parse: title - $title_text")
            return rssItems
        }

        // web view interval
        fun getWebInterval(item: Item):Int{
            var setting_obj = JSONObject(item.settings)
            var reload = setting_obj.getString("reload")
            return if(reload.equals("")) 0 else reload.toInt()
        }

        // setting rotate
        fun getSettingRotate(item: Item):Int{
            var setting_obj = JSONObject(item.settings)
            return setting_obj.getInt("rotate")
        }


        fun getFrame(ctx: Context,frame: Frame,position: Int):RelativeLayout{
            var relative_frame = RelativeLayout(ctx)
            relative_frame.id = position+10
            val params = RelativeLayout.LayoutParams(frame.w,frame.h)
            relative_frame.layoutParams = params
            relative_frame.x = frame.x.toFloat()
            relative_frame.y = frame.y.toFloat()
            if(frame.bg.equals(""))relative_frame.setBackgroundColor(Color.TRANSPARENT)
            else relative_frame.setBackgroundColor(Color.parseColor(frame.bg))
            return relative_frame
        }

        // Data parsing for content on demand

        // check cod available
        fun isCodAvailable(prefernce: MySharePrefernce?): Boolean{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.layout!=null && data_obj.layout.size>0 && data_obj.layout.get(0).cod!=null &&
                    data_obj.layout.get(0).cod.size>0 && data_obj.layout.get(0).cod.get(0).item!=null
                    && data_obj.layout.get(0).cod.get(0).item.size >0 ){
                    // cod items check scedule activatation
                    var items = data_obj.layout.get(0).cod.get(0).item
                    for(item in items){
                        if (item.active==1) return true
                    }
                    return false
                }
            }
            return false
        }

        // get cod categories
        fun getCodItems(prefernce: MySharePrefernce?): ArrayList<CodItem> {
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.layout.get(0).cod.get(0).item != null
                    && data_obj.layout.get(0).cod.get(0).item.size > 0  ){
                    return data_obj.layout.get(0).cod.get(0).item
                }
            }
            return ArrayList<CodItem>()
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