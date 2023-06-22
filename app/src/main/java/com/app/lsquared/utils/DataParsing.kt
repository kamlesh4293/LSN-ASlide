package com.app.lsquared.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Gravity
import android.view.ViewOutlineProvider
import android.view.WindowManager.LayoutParams
import android.widget.ImageView
import android.widget.LinearLayout
import com.app.lsquared.model.*
import com.app.lsquared.model.widget.RssItem
import com.app.lsquared.ui.UiUtils
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject


class DataParsing @Inject constructor(
    private val prefernce: MySharePrefernce?
) {

    // get device
    fun getDevice(): Device? {
        var data = prefernce?.getContentData()
        if(data!=null && !data.equals("")){
            var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
            return data_obj.device[0]
        }
        return null
    }

    // get device relaunch
    fun getDeviceRelaunch(): String {
        var data = prefernce?.getContentData()
        if(data!=null && !data.equals("")){
            var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
            return data_obj.device[0].relaunchRequestedOn
        }
        return ""
    }

    // get response object
    fun getContent(): ResponseJsonData? {
        var data = prefernce?.getContentData()
        if(data!=null && !data.equals("")){
            var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
            return data_obj
        }
        return null
    }

    // check frames available
    fun isFrameAvailable(): Boolean{
        var data_obj = getContent()
        if(data_obj!=null){
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

    // get downloadble list
    fun getDownloableList(pref: MySharePrefernce): List<Downloadable>{
        var list = mutableListOf<Downloadable>()
        var data = pref?.getContentData()
        Log.d("TAG", "getDownloableList: $data")
        if(data!=null && !data.equals("")){
            var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
            DateTimeUtil.setTimeZone(data_obj.device.get(0).timeZone)
            if (data_obj.downloadable!=null && data_obj.downloadable.size>0){
                for(i in 0..data_obj.downloadable.size-1){
                    if(!DataManager.fileIsExist(data_obj.downloadable[i]) && isValidDownload(data_obj.downloadable[i])){
                        Log.d("TAG", "getDownloableList: not exist $i - ${data_obj.downloadable[i].name}")
                        list.add(data_obj.downloadable[i])
                    }else{
                        Log.d("TAG", "getDownloableList: not valid $i - ${data_obj.downloadable[i].name}")
                    }
                }
            }
        }
        return list
    }

    // get downloaded list
    fun getDownloadedList(): List<Downloadable>{
        var list = mutableListOf<Downloadable>()
        var data = prefernce?.getContentData()
        if(data!=null && !data.equals("")){
            var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
            if (data_obj.downloadable!=null && data_obj.downloadable.size>0){
                for(i in 0..data_obj.downloadable.size-1){
                    if(DataManager.fileIsExist(data_obj.downloadable[i])){
                        list.add(data_obj.downloadable[i])
                    }
                }
            }
        }
        return list
    }

    private fun isValidDownload(downloadable: Downloadable): Boolean {
        var time_frame = prefernce?.getStringData(MySharePrefernce.KEY_FEED_RESTRICTION)
        if(downloadable.d) return true
        else if(time_frame.equals("false")) return true
        else if(downloadable.id.equals("0-0-0")) return true
        else{
            var times = time_frame!!.split("-")
            return DateTimeUtil.isValidDownloadingTime(times[0],times[1])
        }
        return false
    }

    fun getAllDownloadingList(): List<Downloadable> {
        var data = prefernce?.getContentData()
        if(data!=null && !data.equals("")){
            var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
            DateTimeUtil.setTimeZone(data_obj.device.get(0).timeZone)
            if (data_obj.downloadable!=null && data_obj.downloadable.size>0){
                return data_obj.downloadable
            }
        }
        return mutableListOf<Downloadable>()
    }

    // get image name for message widget
    fun getImageName(content_id:Int?):String{
        var list = getAllDownloadingList()
        for (item in list){
            var id = item.id.split("-")
            Log.d("TAG", "getImageName: ${id[2]}")
            if(id.size>2 && id[2].equals(content_id.toString())){
                return item.name
                break
            }
        }
        return ""
    }

    // check identify request
    fun isIdentityRequest():Boolean{
        if(getDevice()!=null)
            return if(!getDevice()?.identify!!.equals("true")) true else false
        else
            return false
    }

    // identify duration
    fun getIdentifyRequestDuration(): Int{
        return if(getDevice()!=null) getDevice()?.identifyDuration!! else return 60
    }

    // get on-off days
    fun getOnOffDays(): Days {

        var dayname = DateTimeUtil.getShortDayName()

        var weboss = getDevice()?.weboss
        if(weboss!=null && !weboss.equals("")){
            var days = JSONObject(weboss).getJSONObject("gs").getJSONObject("as").getJSONArray("days")
            if(days==null ) return Days()
            for (i in 0..days.length()-1){
                var day = days.getJSONObject(i)
                var label = day.getInt("label")
                var name = day.getString("name")
                var valu = day.getBoolean("val")
                var st = day.getString("st")
                var et = day.getString("et")
                if(name.equals(dayname)){
                    return Days(label,name,valu,st,et)
                    break
                }
            }
        }
        return Days()
    }

    // get OFF device code
    fun getDeviceOffCode(): String{
        var prop = getDevice()?.prop
        var array = JSONArray(prop)
        if(array!=null && array.length()>0){
            for(i in 0..array.length()){
                var label = array.getJSONObject(i).getString("label")
                if(label.equals("RS232 off")) {
                    var value = array.getJSONObject(i).getString("value").replace(" ","")
                    return value
                    break
                }
            }
        }
        return "AA11FE010010"
    }

    // get ON device code
    fun getDeviceONCode(): String{
        var prop = getDevice()?.prop
        var array = JSONArray(prop)
        if(array!=null && array.length()>0){
            for(i in 0..array.length()){
                var label = array.getJSONObject(i).getString("label")
                if(label.equals("RS232 on")) {
                    var value = array.getJSONObject(i).getString("value").replace(" ","")
                    return value
                    break
                }
            }
        }
        return "AA11FE010111"
    }

    fun checkDemandSs(): Boolean {
        return getDevice()?.odss!!
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
    fun isDefaultImageAvailable(): String{
        var data = prefernce?.getContentData()
        if(data!=null && !data.equals("")){
            var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
            var image_name = data_obj.device[0].defaultImageName
            Log.d("TAG", "isDefaultImageAvailable: $image_name")
            return if (!image_name.equals("false") &&!image_name.equals("")) image_name else ""
        }
        Log.d("TAG", "isDefaultImageAvailable: no")
        return ""
    }

    // get watermark
    fun getWatermark(): DeviceWaterMark {
        var data = prefernce?.getContentData()
        var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
        var watermark_obj = Gson().fromJson(data_obj.device[0].wm, DeviceWaterMark::class.java)
        return watermark_obj
    }


    companion object{

        // get device
        fun getDevice(prefernce: MySharePrefernce?): Device? {
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                return data_obj.device[0]
            }
            return null
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

        // get time signature for override
        fun getTimeSignatureOverride(prefernce: MySharePrefernce?): String{
            var data = prefernce?.getContentData()
            var time_signature = StringBuilder()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.ovr!=null && data_obj.ovr.size>0 && data_obj.ovr.get(0).frame!=null && data_obj.ovr.get(0).frame.size>0 ){
                    var frames = data_obj.ovr.get(0).frame
                    var list = ArrayList<String>()
                    var list_frame = ArrayList<Frame>()
                    for(frame in frames){
                        Log.d("TAG", "getTimeSignatureOverride: frame - ${frame.tr}")
                        if(!list.contains(frame.tr)) {
                            if(DateTimeUtil.isValidWithTime(frame)){
                                Log.d("TAG", "getTimeSignatureOverride: frame added - ${frame.tr}")
                                time_signature.append(frame.tr)
                                list.add(frame.tr)
                                list_frame.add(frame)
                            }
                        }
                    }
                    Log.d("TAG", "getTimeSignatureOverride: timsig ${time_signature.toString()}")
                    return time_signature.toString()
                }
            }
            Log.d("TAG", "getTimeSignatureOverride: null")
            return ""
        }

        // get override frames
        fun getOverrideFrames(prefernce: MySharePrefernce?): List<Frame>?{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                if (data_obj.ovr!=null && data_obj.ovr.size>0 && data_obj.ovr.get(0).frame!=null && data_obj.ovr.get(0).frame.size>0 ){
                    var frames  =  data_obj.ovr.get(0).frame
                    var list = ArrayList<String>()
                    var list_frame = ArrayList<Frame>()
                    var time_signature = StringBuilder()
                    for(frame in frames){
                        if(!list.contains(frame.name)) {
                            if(DateTimeUtil.isValidWithTime(frame)){
                                time_signature.append(frame.tr)
                                Log.d("TAG", "getOverrideFrames: added frame : ${frame.tr}")
                                list.add(frame.name)
                                list_frame.add(frame)
                                break
                                return list_frame
                            }
                        }
                    }
                    prefernce?.putStringData(MySharePrefernce.KEY_TIME_SIGNATURE_OVERRIDE,time_signature.toString())
//                    return data_obj.ovr.get(0).frame
                    return list_frame
                }
            }
            return null
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
            try {
                var obj = JSONObject(item.settings)
                return  if(obj.getString("rotationOpt").equals("a")) 12 else obj.getInt("rotate")
            }catch (ex:Exception){
                var obj = JSONObject(item.settings)
                return obj.getInt("rotate")
            }
        }

        // override frame
        fun getOverrideFrame(ctx: Context,id:Int):LinearLayout{
            var ll_frame = LinearLayout(ctx)
            ll_frame.id = id
            val params = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
            ll_frame.layoutParams = params
            ll_frame.orientation = LinearLayout.VERTICAL
            return ll_frame
        }

        // dynamic frame
        fun getLayoutFrame(ctx: Context, frame: Frame, position: Int, id: Int):LinearLayout{
            var ll_frame = LinearLayout(ctx)
            ll_frame.id = position+id
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


            var  color = if(frame.bg.equals(""))Color.TRANSPARENT else Color.parseColor(UiUtils.getColorWithOpacity(frame.bg!!,frame.bga))

            if(frame.bg.equals(""))ll_frame.setBackgroundColor(Color.TRANSPARENT)
            else ll_frame.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(frame.bg!!,frame.bga)))

            ll_frame.background =  getShape(color,frame.settings)
            ll_frame.rotation = frame.r.toFloat()

            ll_frame.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
            ll_frame.setClipToOutline(true);

            return ll_frame
        }


        fun getFrameSetting(frame_setting:String) = Gson().fromJson(frame_setting,FrameSetting::class.java)

        fun getShape(color: Int, settings: String): GradientDrawable {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            var frame_setting = Gson().fromJson(settings,FrameSetting::class.java)
            var border = frame_setting.br
            if(border!=null && border.br!=null&& border.bl!=null&& border.tr!=null&& border.tl!=null){
                var tl = border.tl
                var tr = border.tr
                var bl = border.bl
                var br = border.br
                shape.cornerRadii = floatArrayOf(tl!!.toFloat(), tl.toFloat(), tr!!.toFloat(),tr.toFloat(),
                    bl!!.toFloat(), bl.toFloat(), br!!.toFloat(), br.toFloat())
            }
            shape.setColor(color)
            return shape
        }

        fun getDateTimeShape(color: Int,hight :Int): GradientDrawable {
            var hi = (hight/20).toFloat()
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
             shape.cornerRadii = floatArrayOf(hi, hi,hi, hi,hi, hi,hi, hi)
             shape.setColor(color)
            return shape
        }

        private fun getOnlyShape(color: Int, br: String): GradientDrawable {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            if(!br.equals("")){
                var br_obj = JSONObject(br)
                var tl = br_obj.getInt("tl")
                var tr = br_obj.getInt("tr")
                var bl = br_obj.getInt("bl")
                var br = br_obj.getInt("br")
                shape.cornerRadii = floatArrayOf(tl.toFloat(), tl.toFloat(), tr.toFloat(),tr.toFloat(),
                    bl.toFloat(), bl.toFloat(), br.toFloat(), br.toFloat())
//                shape.cornerRadii = floatArrayOf(50f, 16f, 16f, 16f, 30f, 20f, 10f, 100f)
            }
            return shape
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
                Log.d("TAG", "isCodItemContentAvailable: ${data_obj.layout.get(0).cod.toString()}")
                if (data_obj.layout.get(0).cod != null && data_obj.layout.get(0).cod!!.get(0).item.size > 0){
                    Log.d("TAG", "isCodItemContentAvailable: item 123")
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
                }else{
                    Log.d("TAG", "isCodItemContentAvailable: size 0 ${cate.label}")
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

        fun isItemvailable(allFrames: List<Frame>?): Boolean {
            var items = false
            allFrames?.forEach { frame ->
                Log.d("TAG", "isItemvailable: items - ${frame.item.size}")
                if (frame.item!= null && frame.item.size>0){
                    items = true
                }
            }
            return items
        }

        fun isVideoPlaying(layoutList: MutableList<LayoutFrames>): Boolean {
            if(layoutList!=null && layoutList.size>0){
                for (layout in layoutList){
                    if(layout.active_widget.equals(Constant.CONTENT_VIDEO)){
                        return true
                        break
                    }
                }
            }
            return false
        }

        // check identify requesr
        fun isIdentifyRequestAvailable(prefernce: MySharePrefernce?): Boolean{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                return if (!data_obj.device[0].identify.equals("true")) true else false
            }
            return false
        }

        // identify duration
        fun getIdentifyRequestDuration(prefernce: MySharePrefernce?): Int{
            var data = prefernce?.getContentData()
            if(data!=null && !data.equals("")){
                var data_obj = Gson().fromJson(data, ResponseJsonData::class.java)
                return data_obj.device[0].identifyDuration
            }
            return 60
        }


    }

}
