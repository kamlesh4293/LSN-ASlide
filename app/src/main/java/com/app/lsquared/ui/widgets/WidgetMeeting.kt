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
import com.app.lsquared.model.Item
import com.app.lsquared.model.widget.MeetingCalendarResponseData
import com.app.lsquared.model.widget.Ss
import com.app.lsquared.pasring.DataParsingSetting
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.Constant
import com.app.lsquared.utils.FontUtil
import com.app.lsquared.utils.ImageUtil
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class WidgetMeeting {

    var frame_height = 0
    var title_height = 0
    var last_height = 0
    var lastView : View? = null
    var main_ll : LinearLayout? = null
    var title_main_ll : LinearLayout? = null
    var cal_ll : LinearLayout? = null
    var ctx : Context? = null
    var item :Item? = null
    var position = 0
    var meeting_obj :MeetingCalendarResponseData? = null
    var view:View? = null
    var visible_room = true

    // set screen saver
    fun loadScreenSaver(ss_list: ArrayList<Ss>, position: Int,
                        layout: LinearLayout?, ctx: Context) {

        var pos = position
        if(pos>=ss_list.size) pos = 0


        var ss_item = ss_list[pos]
        if(ss_item.type.equals(Constant.CONTENT_IMAGE) ||
            ss_item.type.equals(Constant.CONTENT_VECTOR) ||
            ss_item.type.equals(Constant.CONTENT_POWERPOINT) ||
            ss_item.type.equals(Constant.CONTENT_WORD)){
            layout?.removeAllViews()
            layout?.addView(ImageWidget.getMeetingSsImageWidget(ctx,ss_item.src!!))
        }
        if(ss_item.type.equals(Constant.CONTENT_VIDEO)){
            var videoView = VideoView(ctx)
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

    // Meeting - WALLBOARD
    fun getMeetingWallBoardWidget(ctx: Context, item: Item, meeting_obj: MeetingCalendarResponseData, layout: LinearLayout?):View? {

        var view = (ctx as Activity).layoutInflater.inflate(R.layout.widget_meeting_wallboard,null)
        var main_bg_ll = view?.findViewById<LinearLayout>(R.id.ll_main_meeting_bg)
        main_ll = view?.findViewById<LinearLayout>(R.id.ll_main_meeeting)
        title_main_ll = view?.findViewById<LinearLayout>(R.id.ll_main_meeetingtitle)
        cal_ll = view?.findViewById<LinearLayout>(R.id.ll_cal_meeeting)
        main_ll?.layoutParams = LayoutParams(item.frame_w,item.frame_h)

        this.ctx = ctx
        this.item = item
        this.meeting_obj = meeting_obj

        var sett_obj = JSONObject(item.settings)
        var header_active = DataParsingSetting.getheaderVisibility(JSONObject(item.settings))

        var bg = DataParsingSetting.getBgWithOpacity(sett_obj)
        main_bg_ll?.setBackgroundColor(Color.parseColor(bg))

        title_main_ll?.visibility = if(header_active) View.VISIBLE else View.GONE
        if(header_active) getTitleRow(ctx,item) else getMeetingRow()

        return view
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

        // floor
        var floor_tv = TextView(ctx)
        floor_tv.layoutParams = room_lp
        floor_tv.gravity = Gravity.CENTER

        var image = ImageView(ctx)
        image.layoutParams = LayoutParams(100,LayoutParams.MATCH_PARENT)

        // visibility
        var obj = JSONObject(item.settings)
        var logo_opt = DataParsingSetting.getLogoOption(obj)
        var isRoom = DataParsingSetting.isRoomVisible(obj)
        var isFloor = DataParsingSetting.isFloorVisible(obj)
        var isTime = DataParsingSetting.isTimeVisible(obj)
        var isRF = DataParsingSetting.isRFVisible(obj)


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
        if(isFloor && !isRF){
            layout.addView(getVerticalLine(ctx))
            layout.addView(floor_tv)
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
        floor_tv.textSize = size
        // color
        title_tv.setTextColor(color)
        time_tv.setTextColor(color)
        room_tv.setTextColor(color)
        floor_tv.setTextColor(color)
        // font
        FontUtil.setFonts(ctx,title_tv,font)
        FontUtil.setFonts(ctx,time_tv,font)
        FontUtil.setFonts(ctx,room_tv,font)
        FontUtil.setFonts(ctx,floor_tv,font)
        //text
        title_tv.text = DataParsingSetting.getMeetingTitleText1(obj)
        time_tv.text = DataParsingSetting.getMeetingTitleText2(obj)
        room_tv.text = DataParsingSetting.getMeetingTitleText3(obj)
        floor_tv.text = DataParsingSetting.getMeetingTitleText4(obj)
        title_main_ll?.addView(layout)

        title_main_ll?.post(Runnable() {
            kotlin.run {
                title_height = title_main_ll!!.height
                frame_height = frame_height + title_height
                getMeetingRow()
            }
        })
    }

    // Meeting Row
    fun getMeetingRow(){
        // add previous
        if(lastView!=null){
            Log.d("TAG", "getMeetingRow: no lastview")
            cal_ll?.removeAllViews()
            main_ll?.addView(lastView)
            lastView = null
            frame_height = frame_height + last_height
        }
        var obj = JSONObject(item!!.settings)
        if(meeting_obj?.events?.size!! > position) {

            // new
            var layout = LinearLayout(ctx)
            layout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
            layout.orientation = LinearLayout.HORIZONTAL
            layout.gravity = Gravity.TOP

            val title_lp = LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.6f)
            val title_text_lp = LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            val time_lp = LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.35f)
            val room_lp = LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f)
            val room_text = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

            var meeting = meeting_obj!!.events[position]

            var duration_type = obj.getString("wdmOpt")

            var title_size = DataParsingSetting.getTitleSize(obj).toFloat()
            var title_font = DataParsingSetting.getFontTitleLabel(obj)
            var time_font = DataParsingSetting.getFontTimeLabel(obj)
            var room_font = DataParsingSetting.getFontRoomLabel(obj)
            var floor_font = DataParsingSetting.getFontFloorLabel(obj)

            var layout_title = LinearLayout(ctx)
            layout_title.orientation = LinearLayout.VERTICAL
            layout_title.layoutParams = title_lp

            // title
            var title_tv = TextView(ctx)
            title_tv.text = "${meeting.title}"
            title_tv.setLines(1)
            title_lp.gravity = Gravity.TOP
            title_tv.layoutParams = title_text_lp
            title_tv.setPadding(10,10,0,5)
            title_tv.gravity = Gravity.TOP
            FontUtil.setFonts(ctx!!,title_tv,title_font)

            // title time
            var title_duration_tv = TextView(ctx)
            title_duration_tv.text = "${meeting.startdate} - ${meeting.enddate}"
            title_duration_tv.gravity = Gravity.TOP
            title_duration_tv.layoutParams = title_text_lp
            title_duration_tv.setPadding(10,10,0,5)
            FontUtil.setFonts(ctx!!,title_duration_tv,time_font)


            layout_title.addView(title_tv)
            if(duration_type.equals("w") ||duration_type.equals("m")) layout_title.addView(title_duration_tv)

            var layout_time = LinearLayout(ctx)
            layout_time.orientation = LinearLayout.VERTICAL
            layout_time.layoutParams = time_lp

            var layout_room = LinearLayout(ctx)
            layout_room.orientation = LinearLayout.VERTICAL
            layout_room.layoutParams = room_lp

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
            room_tv.gravity = Gravity.CENTER
            room_tv.layoutParams = room_text
            FontUtil.setFonts(ctx!!,room_tv,room_font)

            // floor
            var floor_tv = TextView(ctx)
            floor_tv.layoutParams = room_lp
            floor_tv.text = "${meeting.floor}"
            floor_tv.setPadding(0,5,0,5)
            FontUtil.setFonts(ctx!!,floor_tv,floor_font)

            // setting
            var bg_color = Color.parseColor("#ffffff")
            var text_color_title = "#000000"
            var text_color_time = "#000000"
            var text_color_room = "#000000"
            var text_color_floor = "#000000"

            var time_size = DataParsingSetting.getTimeSize(obj).toFloat()
            var room_size = DataParsingSetting.getRoomSize(obj).toFloat()
            var floor_size = DataParsingSetting.getFloorSize(obj).toFloat()
            var img_size = getImageSize(time_size,room_size,floor_size)
            Log.d("TAG", "getMeetingRow: $img_size")

            // visibility
            var logo_opt = DataParsingSetting.getLogoOption(obj)
            var isRoom = DataParsingSetting.isRoomVisible(obj)
            var isFloor = DataParsingSetting.isFloorVisible(obj)
            var isTime = DataParsingSetting.isTimeVisible(obj)
            var isRF = DataParsingSetting.isRFVisible(obj)

            if(logo_opt){
                // icon
                var image = ImageView(ctx)
                var image_lp = LayoutParams((img_size*3).toInt(),(img_size*3).toInt())
                image_lp.setMargins(10)
                image.layoutParams = image_lp
                image.setPadding(10,10,10,10)
                if(!meeting.logo.equals("")){
                    image.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity("#000000","0.1")))
//                    image.setBackgroundColor(Color.parseColor("#CBCDCC"))
                    ImageUtil.loadImage(ctx!!,meeting.logo!!,image)
                } else{
//                    image.setBackgroundResource(R.drawable.bg_meeting_icon)
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
                text_color_floor = DataParsingSetting.getRowFloorTextColor(obj)
            }else{
                bg_color = Color.parseColor(DataParsingSetting.getAltRowBgWithOpacity(obj))
                text_color_title = DataParsingSetting.getAltRowTitleTextColor(obj)
                text_color_time = DataParsingSetting.getAltRowTimeTextColor(obj)
                text_color_room = DataParsingSetting.getAltRowRoomTextColor(obj)
                text_color_floor = DataParsingSetting.getAltRowFloorTextColor(obj)
            }
            layout.setBackgroundColor(bg_color)
            title_tv.setTextColor(Color.parseColor(text_color_title))
            title_duration_tv.setTextColor(Color.parseColor(text_color_time))
            time_tv1.setTextColor(Color.parseColor(text_color_time))
            time_tv2.setTextColor(Color.parseColor(text_color_time))
            room_tv.setTextColor(Color.parseColor(text_color_room))
            floor_tv.setTextColor(Color.parseColor(text_color_floor))

            title_tv.textSize = title_size
            title_duration_tv.textSize = (time_size/1.5).toFloat()
            time_tv1.textSize = time_size
            time_tv2.textSize = time_size
            room_tv.textSize = room_size
            floor_tv.textSize = floor_size

            layout.addView(layout_title)

            if(isTime){
                layout.addView(getVerticalLine(ctx!!))
                layout.addView(layout_time)
            }
            if(isRoom){
                layout.addView(getVerticalLine(ctx!!))
                layout_room.addView(room_tv)
                layout.addView(layout_room)
            }
            if(isFloor && !isRF){
                layout.addView(getVerticalLine(ctx!!))
                layout.addView(floor_tv)
            }
            if(isFloor && isRF){
                RotateText().rotateRoomFloor(room_tv,meeting.location,meeting.floor)
            }
            cal_ll?.addView(layout)
            lastView = layout

            layout?.post(Runnable() {
                kotlin.run {
                    last_height = layout.getHeight()
                    frame_height = frame_height + last_height
                    if(frame_height< item?.frame_h!!){
                        Log.d("TAG", "getMeetingRow: added new row - $frame_height- $last_height- ${item?.frame_h}")
                        position = position+1
                        getMeetingRow()
//                        frame_height = frame_height-last_height
                    }else {
                        lastView = null
                        position = position-1
                        rotateItems(obj, meeting_obj)
                    }
                }
            })
        } else {
            rotateItems(obj, meeting_obj)
            visible_room = true
        }
    }

    private fun getImageSize(timeSize: Float, roomSize: Float, floorSize: Float): Float {
        return if(timeSize>roomSize && timeSize>floorSize) timeSize
        else if(roomSize>timeSize && roomSize>floorSize) roomSize
        else floorSize
    }


    fun rotateItems(obj: JSONObject,meeting_obj: MeetingCalendarResponseData?) {
        var rotate = DataParsingSetting.getRotate(obj)
        CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(rotate.toLong()))
            withContext(Dispatchers.Main) {
                if(main_ll!=null) main_ll?.removeAllViews()
                frame_height = title_height
                main_ll?.removeAllViews()
                position = if(position== meeting_obj?.events?.size!!) 0 else position+1
                visible_room = true
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
    fun getMeetingRoomBoardWidget(ctx: Context, item: Item, meeting_obj: MeetingCalendarResponseData, layout: LinearLayout?):View? {

        var view = (ctx as Activity).layoutInflater.inflate(R.layout.widget_meeting_roomboard,null)
        var room_main_ll = view.findViewById<LinearLayout>(R.id.ll_meeting_roommain)
        var image = view.findViewById<ImageView>(R.id.iv_meetingroom_image)
        var title_tv = view.findViewById<TextView>(R.id.tv_meetingroom_title)
        var time_tv = view.findViewById<TextView>(R.id.tv_meetingroom_time)
        if(meeting_obj.event !=null && meeting_obj?.event?.starttime !=null ){

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
            var iv_lp = LayoutParams((title_size*2).toInt(),(title_size*2).toInt())
            image.layoutParams = iv_lp
            image.setPadding(5,5,5,5)
            image.setBackgroundResource(R.drawable.bg_meeting_icon)

            image.visibility = if(logo_opt) View.VISIBLE else View.GONE
            if (logo_opt)ImageUtil.loadImage(ctx,meeting_obj?.event?.logo!!,image)
            title_tv.text = meeting_obj?.event?.title
            time_tv.text = "${meeting_obj?.event?.starttime} - ${meeting_obj?.event?.endtime}"


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

class RotateText{

    fun rotateRoomFloor(room_tv: TextView, location: String?, floor: String?) {
        var meeting = WidgetMeeting()
        var visible = meeting.visible_room
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            meeting.visible_room = !visible
            room_tv.text = if(meeting.visible_room) location else floor
            rotateRoomFloor(room_tv,location,floor)
        },6000)
    }

}
