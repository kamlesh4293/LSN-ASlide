package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import android.widget.VideoView
import androidx.core.view.setMargins
import com.app.lsquared.R
import com.app.lsquared.databinding.WidgetMeetingWallboardTemp2Binding
import com.app.lsquared.model.Item
import com.app.lsquared.model.widget.CalendarResponseData
import com.app.lsquared.model.widget.Ss
import com.app.lsquared.pasring.DataParsingSetting
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.Constant
import com.app.lsquared.utils.DateTimeUtil
import com.app.lsquared.utils.FontUtil
import com.app.lsquared.utils.ImageUtil
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class WidgetCalendar {

    var frame_height = 0
    var title_height = 0
    var lastView : View? = null
    var main_ll : LinearLayout? = null
    var title_main_ll : LinearLayout? = null
    var cal_ll : LinearLayout? = null
    var ctx : Context? = null
    var item :Item? = null
    var position = 0
    var meeting_obj :CalendarResponseData? = null
    var view:View? = null
    var visible_room = true

    // outlook calendar
    var title_name = ""
    var outlook_temp2_hight = 0
    var outlook_temp2_pos = 1

    // set screen saver
    fun loadScreenSaver(ss_list: ArrayList<Ss>, position: Int,
                        layout: LinearLayout?, ctx: Context) {

        var pos = position
        if(pos>=ss_list.size) pos = 0


        val ss_item = ss_list[pos]
        if(ss_item.type.equals(Constant.CONTENT_IMAGE) ||
            ss_item.type.equals(Constant.CONTENT_VECTOR) ||
            ss_item.type.equals(Constant.CONTENT_POWERPOINT) ||
            ss_item.type.equals(Constant.CONTENT_WORD)){
            layout?.removeAllViews()
            layout?.addView(ImageWidget.getMeetingSsImageWidget(ctx,ss_item.src!!))
        }
        if(ss_item.type.equals(Constant.CONTENT_VIDEO)){
            val videoView = VideoView(ctx)
            videoView.setVideoURI(Uri.parse(Constant.BASE_FILE_URL+"cl/videos/processed/"+ss_item.src))
            videoView.requestFocus()
            videoView.start()
            layout?.removeAllViews()
            layout?.addView(videoView)
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(ss_item.d!!.toLong()))
            withContext(Dispatchers.Main) {
                loadScreenSaver(ss_list,pos+1,layout,ctx)
            }
        }
    }

    // Meeting - WALLBOARD ( Template 1)
    fun getCalendarAllEvents(ctx: Context, item: Item, meeting_obj: CalendarResponseData):View? {

        val view = (ctx as Activity).layoutInflater.inflate(R.layout.widget_meeting_wallboard,null)
        val main_bg_ll = view?.findViewById<LinearLayout>(R.id.ll_main_meeting_bg)
        main_ll = view?.findViewById<LinearLayout>(R.id.ll_main_meeeting)
        title_main_ll = view?.findViewById<LinearLayout>(R.id.ll_main_meeetingtitle)
        cal_ll = view?.findViewById<LinearLayout>(R.id.ll_cal_meeeting)
        main_ll?.layoutParams = LayoutParams(item.frame_w,item.frame_h)

        this.ctx = ctx
        this.item = item
        this.meeting_obj = meeting_obj

        val sett_obj = JSONObject(item.settings)
        val header_active = DataParsingSetting.getheaderVisibility(sett_obj)

        val bg = DataParsingSetting.getBgWithOpacity(sett_obj)
        main_bg_ll?.setBackgroundColor(Color.parseColor(bg))
        title_main_ll?.visibility = if(header_active) View.VISIBLE else View.GONE
        if(header_active) getTitleRow(ctx,item) else getMeetingRow()

        return view
    }

    // Meeting - WALLBOARD ( Template 2)
    fun getOutlookCalendarTemp2(
        ctx: Context,
        item: Item,
        meeting_obj: CalendarResponseData,
        layout: LinearLayout?
    ):View? {

        var binding = WidgetMeetingWallboardTemp2Binding.inflate((ctx as Activity).layoutInflater)

        var setting_obj = Gson().fromJson(item.settings,SettingOutlookCalendarTemp3::class.java)

        outlook_temp2_hight = item.frame_h
        outlook_temp2_hight = outlook_temp2_hight- (setting_obj.headerSize!!*2.5).toInt()

        // left text
        binding.tvLeftText.textSize = setting_obj.leftSize!!.toFloat()
        binding.tvLeftText.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(setting_obj.leftBg!!,setting_obj.leftBga!!)))
        binding.tvLeftText.setTextColor(Color.parseColor(setting_obj.leftText!!))
        FontUtil.setFonts(ctx,binding.tvLeftText,setting_obj.leftFont?.label!!)

        // header - layout
        var header_lp = LayoutParams(LayoutParams.MATCH_PARENT,(setting_obj.headerSize!!*2.5).toInt())
        binding.llOutlookTemp2Header.layoutParams = header_lp
        // header - day
        binding.tvOutlookTemp2Day.textSize = setting_obj.headerSize!!.toFloat()
        binding.tvOutlookTemp2Day.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(setting_obj.headerBg!!,setting_obj.headerBga!!)))
        binding.tvOutlookTemp2Day.setTextColor(Color.parseColor(setting_obj.headerText!!))
        binding.tvOutlookTemp2Day.text = "${DateTimeUtil.getDayName()} ${DateTimeUtil.getMonth()} ${DateTimeUtil.getDate()} "
        FontUtil.setFonts(ctx,binding.tvOutlookTemp2Day,setting_obj.headerFont?.label!!)

        // header - N/A
        binding.tvOutlookTemp2Na.textSize = setting_obj.headerSize!!.toFloat()
        binding.tvOutlookTemp2Na.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(setting_obj.headerBg!!,setting_obj.headerBga!!)))
        binding.tvOutlookTemp2Na.setTextColor(Color.parseColor(setting_obj.headerText!!))
        FontUtil.setFonts(ctx,binding.tvOutlookTemp2Na,setting_obj.headerFont?.label!!)

        var lp = LayoutParams(item.frame_w,item.frame_h)
        binding.llMainMeetingBgTemp2.layoutParams = lp

        if(outlook_temp2_pos>23) {
            outlook_temp2_pos = 1
            title_name = ""
        }
        for (i in outlook_temp2_pos..24) {
            var row_hight = setting_obj?.rowSize!!*4
            if(outlook_temp2_hight>row_hight){
                outlook_temp2_pos = i
                outlook_temp2_hight = outlook_temp2_hight-row_hight
                getTemp2Row(ctx,i,binding.llMeetingTemp2List,setting_obj!!,meeting_obj!!)
            }
        }
        layout?.removeAllViews()
        layout?.addView(binding.root)

        // rotate

        var rotate = if(setting_obj.rotationOpt.equals("a")) 12000 else setting_obj.rotate!!*1000
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable {
                getOutlookCalendarTemp2(ctx,item,meeting_obj,layout)
            }, rotate.toLong()
        )
        return binding.root
    }


    private fun getSessionIsFilled(meetingObj: CalendarResponseData?, i: Int, minute: Int): Boolean {
        var valid = false
        var events = meetingObj?.events
        var time = "0${getTimeText(i).replace(" ",":$minute ")}"
        if(events!=null && events.size>0){
            for (i in 0..events.size-1){
                valid = DateTimeUtil.isValidTimeForMeeting(events[i].starttime!!,events[i].endtime!!,time)
                if(valid)  return true
            }
        }
        return valid
    }

    private fun getSessionTitle(meetingObj: CalendarResponseData?, i: Int, minute: Int): String {
        var title = ""
        var events = meetingObj?.events
        var time = "0${getTimeText(i).replace(" ",":$minute ")}"
        if(events!=null && events.size>0){
            for (i in 0..events.size-1){
                var valid = DateTimeUtil.isValidTimeForMeeting(events[i].starttime!!,events[i].endtime!!,time)
                if(valid) title = events[i].title!!
            }
        }
        return title
    }


    // title Row
    fun getTitleRow(ctx: Context, item: Item){
        var layout = LinearLayout(ctx)
        layout.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
        layout.orientation = LinearLayout.HORIZONTAL
        layout.setBackgroundColor(Color.parseColor("#ffffff"))

        val title_lp = LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.6f)
        val time_lp = LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.35f)
        val room_lp = LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f)

        // title
        var title_tv = TextView(ctx)
        title_tv.layoutParams = title_lp
        title_tv.setPadding(10,10,0,10)

        // time
        var time_tv = TextView(ctx)
        time_tv.layoutParams = time_lp
        time_tv.gravity = Gravity.CENTER

        // room
        var room_tv = TextView(ctx)
        room_tv.layoutParams = room_lp
        room_tv.gravity = Gravity.CENTER

        var image = ImageView(ctx)
        image.layoutParams = LayoutParams(100,LayoutParams.MATCH_PARENT)

        // visibility
        var obj = JSONObject(item.settings)
        var logo_opt = DataParsingSetting.getLogoOption(obj)
        var isRoom = DataParsingSetting.isRoomVisible(obj)
        var isTime = DataParsingSetting.isTimeVisible(obj)

        layout.addView(title_tv)
        if(logo_opt) layout.addView(image)
        if(isTime){
            layout.addView(getVerticalLine(ctx))
            layout.addView(time_tv)
        }
        if(isRoom){
            layout.addView(getVerticalLine(ctx))
            layout.addView(room_tv)
        }


        // setting
        var size = DataParsingSetting.meetingHeaderTextSize(obj).toFloat()
        var color = Color.parseColor(DataParsingSetting.meetingHeaderTextColor(obj))
        var font = DataParsingSetting.getFontLabel(obj)
        var bg = DataParsingSetting.meetingHeaderBg(obj)

        //bg
        layout.setBackgroundColor(Color.parseColor(bg))

        // size
        title_tv.textSize = size
        time_tv.textSize = size
        room_tv.textSize = size
        // color
        title_tv.setTextColor(color)
        time_tv.setTextColor(color)
        room_tv.setTextColor(color)
        // font
        FontUtil.setFonts(ctx,title_tv,font)
        FontUtil.setFonts(ctx,time_tv,font)
        FontUtil.setFonts(ctx,room_tv,font)
        //text
        title_tv.text = DataParsingSetting.getMeetingTitleText1(obj)
        time_tv.text = DataParsingSetting.getMeetingTitleText2(obj)
        room_tv.text = DataParsingSetting.getMeetingTitleText3(obj)
        title_main_ll?.addView(layout)

        title_main_ll?.post(Runnable() {
            kotlin.run {
                title_height = layout.getHeight()
                frame_height = frame_height + layout.getHeight()
                getMeetingRow()
            }
        })
    }

    // Meeting Row
    fun getMeetingRow(){
        // add previous
        if(lastView!=null){
            cal_ll?.removeAllViews()
            main_ll?.addView(lastView)
            lastView = null
        }
        var obj = JSONObject(item!!.settings)
        if(meeting_obj?.events?.size!! > position) {

            // new
            var layout = LinearLayout(ctx)
            layout.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
            layout.orientation = LinearLayout.HORIZONTAL
            layout.gravity = Gravity.CENTER_VERTICAL

            val title_lp = LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.6f)
            val time_lp = LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.35f)
            val room_lp = LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f)

            var meeting = meeting_obj!!.events[position]

            var title_size = DataParsingSetting.getTitleSize(obj).toFloat()

            var title_font = DataParsingSetting.getFontTitleLabel(obj)
            var time_font = DataParsingSetting.getFontTimeLabel(obj)
            var room_font = DataParsingSetting.getFontRoomLabel(obj)

            // title
            var title_tv = TextView(ctx)
            title_tv.text = "${meeting.title}"
            title_tv.layoutParams = title_lp
            title_tv.setPadding(10,10,0,5)
            title_tv.gravity = Gravity.TOP
            FontUtil.setFonts(ctx!!,title_tv,title_font)

            var layout_time = LinearLayout(ctx)
            layout_time.orientation = LinearLayout.VERTICAL
            layout_time.layoutParams = time_lp

            // time  1
            var time_tv1 = TextView(ctx)
            time_tv1.text = "${meeting.starttime}"
            time_tv1.gravity = Gravity.CENTER
            time_tv1.setPadding(0,10,0,5)
            FontUtil.setFonts(ctx!!,time_tv1,time_font)

            // time row
            var time_sep_tv = TextView(ctx)
            var time_sep_lp = LayoutParams(LayoutParams.MATCH_PARENT,1)
            time_sep_lp.setMargins(10,0,10,0)
            time_sep_tv.layoutParams = time_sep_lp
            time_sep_tv.setPadding(10,5,10,5)
            time_sep_tv.setBackgroundColor(Color.parseColor("#000000"))

            // time  2
            var time_tv2 = TextView(ctx)
            time_tv2.text = "${meeting.endtime}"
            time_tv2.gravity = Gravity.CENTER
            time_tv2.setPadding(0,5,0,10)
            FontUtil.setFonts(ctx!!,time_tv2,time_font)

            layout_time.addView(time_tv1)
            layout_time.addView(time_sep_tv)
            layout_time.addView(time_tv2)

            // room
            var room_tv = TextView(ctx)
            room_tv.text = "${meeting.location}"
            room_tv.layoutParams = room_lp
            room_tv.gravity = Gravity.CENTER
            room_tv.setPadding(0,5,0,5)
            FontUtil.setFonts(ctx!!,room_tv,room_font)

            // setting
            var bg_color = Color.parseColor("#ffffff")
            var text_color_title = "#000000"
            var text_color_time = "#000000"
            var text_color_room = "#000000"

            var time_size = DataParsingSetting.getTimeSize(obj).toFloat()
            var room_size = DataParsingSetting.getRoomSize(obj).toFloat()

            // visibility
            var logo_opt = DataParsingSetting.getLogoOption(obj)
            var isRoom = DataParsingSetting.isRoomVisible(obj)
            var isTime = DataParsingSetting.isTimeVisible(obj)

            if(logo_opt){
                // icon
                var image = ImageView(ctx)
                var image_lp = LayoutParams((title_size*3).toInt(),(title_size*3).toInt())
                image_lp.setMargins(10)
                image.layoutParams = image_lp
                image.setPadding(10,10,10,10)
                if(!meeting.logo.equals("")){
                    image.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity("#000000","0.1")))
                    ImageUtil.loadImage(ctx!!,meeting.logo!!,image)
                } else{
                    image.background = UiUtils.getMeetingLogoDrawable("#1A000000")
                    image.setImageResource(R.drawable.ic_calendar)
                }
                layout.addView(image)
            }

            if(position%2==0){
                bg_color = Color.parseColor(DataParsingSetting.getRowBgWithOpacity(obj))
                text_color_title = DataParsingSetting.getRowTitleTextColor(obj)
                text_color_time = DataParsingSetting.getRowTimeTextColor(obj)
                text_color_room = DataParsingSetting.getRowRoomTextColor(obj)
            }else{
                bg_color = Color.parseColor(DataParsingSetting.getAltRowBgWithOpacity(obj))
                text_color_title = DataParsingSetting.getAltRowTitleTextColor(obj)
                text_color_time = DataParsingSetting.getAltRowTimeTextColor(obj)
                text_color_room = DataParsingSetting.getAltRowRoomTextColor(obj)
            }
            layout.setBackgroundColor(bg_color)
            title_tv.setTextColor(Color.parseColor(text_color_title))
            time_tv1.setTextColor(Color.parseColor(text_color_time))
            time_tv2.setTextColor(Color.parseColor(text_color_time))
            room_tv.setTextColor(Color.parseColor(text_color_room))

            title_tv.textSize = title_size
            time_tv1.textSize = time_size
            time_tv2.textSize = time_size
            room_tv.textSize = room_size

            layout.addView(title_tv)

            if(isTime){
                layout.addView(getVerticalLine(ctx!!))
                layout.addView(layout_time)
            }
            if(isRoom){
                layout.addView(getVerticalLine(ctx!!))
                layout.addView(room_tv)
            }
            cal_ll?.addView(layout)
            lastView = layout

            layout?.post(Runnable() {
                kotlin.run {
                    frame_height = frame_height + layout.getHeight()
                    if(frame_height< item?.frame_h!!){
                        position = position+1
                        getMeetingRow()
                    }else {
                        rotateItems(obj, meeting_obj)
                    }
                }
            })
        } else {
            rotateItems(obj, meeting_obj)
            visible_room = true
        }
    }

    // temp 2 row
    fun getTemp2Row(ctx: Context,pos:Int, main_ll: LinearLayout?, setting_obj: SettingOutlookCalendarTemp3
                    , meeting_obj:CalendarResponseData){

        // main layout
        var layout = LinearLayout(ctx)
        var lp = LayoutParams(LayoutParams.MATCH_PARENT,setting_obj.rowSize!!*4)
        layout.orientation = LinearLayout.HORIZONTAL
        layout.gravity = Gravity.CENTER
        layout.layoutParams = lp
        if(pos%2==0) layout.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(setting_obj.rowBg!!,setting_obj.rowBga!!)))
        else layout.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(setting_obj.altBg!!,setting_obj.altBga!!)))

        // Vertical Line layout - 1
        var vertical_line = View(ctx)
        vertical_line.layoutParams = LayoutParams(1,setting_obj.rowSize!!*4)
        vertical_line.setBackgroundColor(Color.parseColor(setting_obj.rowBdr))

        // time layout - 2
        var time_layout = LinearLayout(ctx)
        var time_lp = LayoutParams(0,LayoutParams.MATCH_PARENT,15f)
        time_layout.orientation = LinearLayout.VERTICAL
        time_layout.layoutParams = time_lp

        // time text
        var time_text = TextView(ctx)
        var time_text_lp = LayoutParams(LayoutParams.MATCH_PARENT,0,1f)
        time_text.text = getTimeText(pos)
        time_text.layoutParams = time_text_lp
        time_text.gravity = Gravity.CENTER
        time_text.textSize = setting_obj.rowSize!!.toFloat()
        var remainder = pos%2
        time_text.setLineSpacing(1.1f,1.1f)
        if(remainder==0) time_text.setTextColor(Color.parseColor(setting_obj.rowText))
        else time_text.setTextColor(Color.parseColor(setting_obj.altRowText))
        FontUtil.setFonts(ctx,time_text,setting_obj.rowFont?.label!!)

        // time bottom line
        var time_line_view = TextView(ctx)
        var time_linw_lp = LayoutParams(LayoutParams.MATCH_PARENT,1)
        time_line_view.layoutParams = time_linw_lp
        time_line_view.setBackgroundColor(Color.parseColor(setting_obj.rowBdr))

        time_layout.addView(time_text)
        time_layout.addView(time_line_view)

        // Vertical Line layout - 3
        var vertical_line2 = View(ctx)
        vertical_line2.layoutParams = LayoutParams(1,setting_obj.rowSize!!*4)
        vertical_line2.setBackgroundColor(Color.parseColor(setting_obj.rowBdr))


        // Description vertical layout - 4
        var desc_layout = LinearLayout(ctx)
        var des_lp = LayoutParams(0,setting_obj.rowSize!!*4,70f)
        desc_layout.orientation = LinearLayout.VERTICAL
        desc_layout.layoutParams = des_lp

        var session1 = getSessionIsFilled(meeting_obj,pos,0)
        var text_desc1 = TextView(ctx)
        var text_des_lp = LayoutParams(LayoutParams.MATCH_PARENT,0,1f)
        text_desc1.gravity = Gravity.CENTER
        text_desc1.layoutParams = text_des_lp
        text_desc1.setTextColor(Color.parseColor(setting_obj.eventText))
        if(session1 ){
            text_desc1.setBackgroundColor(Color.parseColor(
                UiUtils.getColorWithOpacity(setting_obj.eventBg!!,setting_obj.eventBga!!)))
            var title = getSessionTitle(meeting_obj,pos,0)
            Log.d("TAG", "getTemp2Row: s1 $title $title_name")
            if(!title_name.equals(title)){
                title_name = title
                text_desc1.text = title
            }
        }

        var session2 = getSessionIsFilled(meeting_obj,pos,30)
        var text_desc2 = TextView(ctx)
        text_desc2.gravity = Gravity.CENTER
        text_desc2.layoutParams = text_des_lp
        text_desc2.setTextColor(Color.parseColor(setting_obj.eventText))
        if(session2) {
            text_desc2.setBackgroundColor(
                Color.parseColor(UiUtils.getColorWithOpacity(setting_obj.eventBg!!,setting_obj.eventBga!!)))
            var title = getSessionTitle(meeting_obj,pos,30)
            Log.d("TAG", "getTemp2Row: s2 $title , $title_name")
            if(!title_name.equals(title)){
                title_name = title
                text_desc2.text = title
            }
        }

        var line_view = TextView(ctx)
        var line_des_lp = LayoutParams(LayoutParams.MATCH_PARENT,1)
        line_view.layoutParams = line_des_lp
        if(session2)  line_view.setBackgroundColor(
            Color.parseColor(UiUtils.getColorWithOpacity(setting_obj.eventBg!!,setting_obj.eventBga!!)))
        else line_view.setBackgroundColor(Color.parseColor(setting_obj.rowBdr))

        desc_layout.addView(text_desc1)
        desc_layout.addView(text_desc2)
        desc_layout.addView(line_view)


        // horizontal view
        layout.addView(vertical_line)
        layout.addView(time_layout)
        layout.addView(vertical_line2)
        layout.addView(desc_layout)

        main_ll?.addView(layout)
    }

    private fun getBgColor(position: Int): Int {
        return if(position==11) Color.parseColor("#008acb") else Color.parseColor("#ffffff")
    }

    private fun getTimeText(position: Int): String {
        return if(12>position) "$position AM" else if(position == 12) "12 PM" else "${position-12} PM"
    }

    fun rotateItems(obj: JSONObject,meeting_obj: CalendarResponseData?) {
        var rotate = DataParsingSetting.getRotate(obj)
        CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(rotate.toLong()))
            withContext(Dispatchers.Main) {
                frame_height = title_height
                main_ll?.removeAllViews()
                position = if(position== meeting_obj?.events?.size!!) 0 else position+1
                getMeetingRow()
            }
        }
    }


    // vertical Divider
    fun getVerticalLine(ctx:Context): View {
        var vertical_line = View(ctx)
        vertical_line.layoutParams = LayoutParams(1,LayoutParams.MATCH_PARENT)
        vertical_line.setBackgroundColor(Color.BLACK)
        return vertical_line
    }


    // Meeting - Room Board
    fun getCalendarRoomBoardWidget(ctx: Context, item: Item, cal_obj: CalendarResponseData):View? {

        var view = (ctx as Activity).layoutInflater.inflate(R.layout.widget_meeting_roomboard,null)
        var room_main_ll = view.findViewById<LinearLayout>(R.id.ll_meeting_roommain)
        var title_tv = view.findViewById<TextView>(R.id.tv_meetingroom_title)
        var time_tv = view.findViewById<TextView>(R.id.tv_meetingroom_time)
        if(cal_obj.event !=null && cal_obj?.event?.starttime !=null ){

            // setting
            var obj = JSONObject(item.settings)
            var bg_color = Color.parseColor(DataParsingSetting.getBgWithOpacity(obj))
            var title_size = DataParsingSetting.getTitleSize(obj).toFloat()
            var time_size = DataParsingSetting.getTimeSize(obj).toFloat()

            var title_font = DataParsingSetting.getFontTitleLabel(obj)
            var time_font = DataParsingSetting.getFontTimeLabel(obj)

            var title_color = DataParsingSetting.getRowTitleTextColor(obj)
            var time_color = DataParsingSetting.getRowTimeTextColor(obj)
            var logo_opt = DataParsingSetting.getLogoOption(obj)

            // main ll size
            var main_ll_lp = LayoutParams(item.frame_w,item.frame_h)
            room_main_ll.layoutParams = main_ll_lp
            // image size
            title_tv.text = cal_obj?.event?.title
            title_tv.visibility = if(cal_obj?.event?.title.equals("")) View.GONE else View.VISIBLE
            time_tv.text = "${cal_obj?.event?.starttime} - ${cal_obj?.event?.endtime}"

            //bg
            room_main_ll.setBackgroundColor(bg_color)
            // size
            title_tv.textSize = title_size
            time_tv.textSize = time_size
            // color
            title_tv.setTextColor(Color.parseColor(title_color))
            time_tv.setTextColor(Color.parseColor(time_color))
            // font
            FontUtil.setFonts(ctx,title_tv,title_font)
            FontUtil.setFonts(ctx,time_tv,time_font)

        }
        return view
    }

}

//
data class SettingOutlookCalendarTemp3 (

    @SerializedName("headerBg"    ) var headerBg    : String?     = null,
    @SerializedName("headerBga"   ) var headerBga   : String?        = null,
    @SerializedName("headerText"  ) var headerText  : String?     = null,
    @SerializedName("headerFont"  ) var headerFont  : HeaderFont? = HeaderFont(),
    @SerializedName("headerSize"  ) var headerSize  : Int?        = null,
    @SerializedName("footerBg"    ) var footerBg    : String?     = null,
    @SerializedName("footerBga"   ) var footerBga   : Int?        = null,
    @SerializedName("footerText"  ) var footerText  : String?     = null,
    @SerializedName("footerFont"  ) var footerFont  : Font? = Font(),
    @SerializedName("footerSize"  ) var footerSize  : Int?        = null,
    @SerializedName("leftBg"      ) var leftBg      : String?     = null,
    @SerializedName("leftBga"     ) var leftBga     : String?     = null,
    @SerializedName("leftText"    ) var leftText    : String?     = null,
    @SerializedName("leftFont"    ) var leftFont    : Font?   = Font(),
    @SerializedName("leftSize"    ) var leftSize    : Int?        = null,
    @SerializedName("rowBg"       ) var rowBg       : String?     = null,
    @SerializedName("rowBga"      ) var rowBga      : String?        = null,
    @SerializedName("altBg"       ) var altBg       : String?     = null,
    @SerializedName("altBga"      ) var altBga      : String?        = null,
    @SerializedName("rowSize"     ) var rowSize     : Int?        = null,
    @SerializedName("rowText"     ) var rowText     : String?     = null,
    @SerializedName("altRowText"  ) var altRowText  : String?     = null,
    @SerializedName("rowFont"     ) var rowFont     : RowFont?    = RowFont(),
    @SerializedName("eventBg"     ) var eventBg     : String?     = null,
    @SerializedName("eventBga"    ) var eventBga    : String?        = null,
    @SerializedName("eventText"   ) var eventText   : String?     = null,
    @SerializedName("isRowBdr"    ) var isRowBdr    : Boolean?    = null,
    @SerializedName("rowBdr"      ) var rowBdr      : String?     = null,
    @SerializedName("logoOpt"     ) var logoOpt     : Boolean?    = null,
    @SerializedName("rotationOpt" ) var rotationOpt : String?     = null,
    @SerializedName("rotate"      ) var rotate      : Int?        = null,
    @SerializedName("sos"         ) var sos         : Sos?        = Sos()
)


data class Sos (

    @SerializedName("sType" ) var sType : String? = null,
    @SerializedName("prior" ) var prior : Int?    = null,
    @SerializedName("eType" ) var eType : String? = null,
    @SerializedName("after" ) var after : Int?    = null

)