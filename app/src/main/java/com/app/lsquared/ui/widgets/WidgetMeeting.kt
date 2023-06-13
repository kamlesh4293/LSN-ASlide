package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.Uri
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
import com.app.lsquared.model.widget.MeetingRoomBoardResponseData
import com.app.lsquared.model.widget.MeetingWallBoardResponseData
import com.app.lsquared.model.widget.Ss
import com.app.lsquared.pasring.DataParsingSetting
import com.app.lsquared.utils.Constant
import com.app.lsquared.utils.FontUtil
import com.app.lsquared.utils.ImageUtil
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class WidgetMeeting {

    var frame_height = 0
    var title_height = 0
    var lastView : View? = null
    var main_ll : LinearLayout? = null
    var title_main_ll : LinearLayout? = null
    var cal_ll : LinearLayout? = null
    var ctx : Context? = null
    var item :Item? = null
    var position = 0
    var meeting_obj :MeetingWallBoardResponseData? = null
    var view:View? = null

    // Meeting - WALLBOARD
    fun getMeetingWallBoardWidget(ctx: Context, item: Item, meeting_obj: MeetingWallBoardResponseData, layout: LinearLayout?):View? {

        var view = (ctx as Activity).layoutInflater.inflate(R.layout.widget_meeting_wallboard,null)
        main_ll = view?.findViewById<LinearLayout>(R.id.ll_main_meeeting)
        title_main_ll = view?.findViewById<LinearLayout>(R.id.ll_main_meeetingtitle)
        cal_ll = view?.findViewById<LinearLayout>(R.id.ll_cal_meeeting)
        main_ll?.layoutParams = LayoutParams(item.frame_w,item.frame_h)

        this.ctx = ctx
        this.item = item
        this.meeting_obj = meeting_obj

        if(meeting_obj.events.size>0){
            getTitleRow(ctx,item)
        }else if (meeting_obj.ss.size>0)
            loadScreenSaver(meeting_obj.ss,0,layout!!,ctx)
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

        var image = ImageView(ctx)
        image.layoutParams = LayoutParams(100,LayoutParams.MATCH_PARENT)


        layout.addView(title_tv)
        layout.addView(image)
        layout.addView(getVerticalLine(ctx))
        layout.addView(time_tv)
        layout.addView(getVerticalLine(ctx))
        layout.addView(room_tv)

        // setting
        var obj = JSONObject(item.settings)
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

        Log.d("TAG", "getMeetingRow: frameHi - $frame_height , pos - $position")

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

            val title_lp = LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.6f)
            val time_lp = LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.35f)
            val room_lp = LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.4f)

            var meeting = meeting_obj!!.events[position]

            var title_size = DataParsingSetting.getTitleSize(obj).toFloat()

            // icon
            var image = ImageView(ctx)
            var image_lp = LayoutParams((title_size*3).toInt(),(title_size*3).toInt())
            image_lp.setMargins(10)
            image.layoutParams = image_lp
            image.setPadding(10,10,10,10)
            if(!meeting.logo.equals("")){
                image.setBackgroundColor(Color.parseColor("#CBCDCC"))
                ImageUtil.loadImage(ctx!!,meeting.logo!!,image)
            } else{
                image.setBackgroundResource(R.drawable.bg_meeting_icon)
                image.setImageResource(R.drawable.ic_calendar)
            }

            // title
            var title_tv = TextView(ctx)
            title_tv.text = "${meeting.title}"
            title_tv.layoutParams = title_lp
            title_tv.setPadding(0,10,0,5)

            var layout_time = LinearLayout(ctx)
            layout_time.orientation = LinearLayout.VERTICAL
            layout_time.layoutParams = time_lp

            // time  1
            var time_tv1 = TextView(ctx)
            time_tv1.text = "${meeting.starttime}"
            time_tv1.gravity = Gravity.CENTER
            time_tv1.setPadding(0,10,0,5)

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

            layout_time.addView(time_tv1)
            layout_time.addView(time_sep_tv)
            layout_time.addView(time_tv2)

            // room
            var room_tv = TextView(ctx)
            room_tv.text = "${meeting.location}"
            room_tv.layoutParams = room_lp
            room_tv.gravity = Gravity.CENTER
            room_tv.setPadding(0,5,0,5)


            // setting
            var bg_color = Color.parseColor("#ffffff")
            var text_color_title = "#000000"
            var text_color_time = "#000000"
            var text_color_room = "#000000"
            var text_color_floor = "#000000"

            var time_size = DataParsingSetting.getTimeSize(obj).toFloat()
            var room_size = DataParsingSetting.getRoomSize(obj).toFloat()
            var floor_size = DataParsingSetting.getFloorSize(obj).toFloat()

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
            time_tv1.setTextColor(Color.parseColor(text_color_time))
            time_tv2.setTextColor(Color.parseColor(text_color_time))
            room_tv.setTextColor(Color.parseColor(text_color_room))

            title_tv.textSize = title_size
            time_tv1.textSize = time_size
            time_tv2.textSize = time_size
            room_tv.textSize = room_size

            layout.addView(image)
            layout.addView(title_tv)
            layout.addView(getVerticalLine(ctx!!))
            layout.addView(layout_time)
            layout.addView(getVerticalLine(ctx!!))
            layout.addView(room_tv)

            cal_ll?.addView(layout)
            lastView = layout

            layout?.post(Runnable() {
                kotlin.run {
                    frame_height = frame_height + layout.getHeight()
                    if(frame_height< item?.frame_h!!){
                        position = position+1
                        getMeetingRow()
                    }else rotateItems(obj, meeting_obj)
                }
            })
        } else rotateItems(obj, meeting_obj)
    }

    fun rotateItems(obj: JSONObject,meeting_obj: MeetingWallBoardResponseData?) {
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

    // set screen saver
    private fun loadScreenSaver(ss_list: ArrayList<Ss>, position: Int,
                                layout: LinearLayout?, ctx: Activity) {

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

    // Meeting - Room Board
    fun getMeetingRoomBoardWidget(ctx: Context, item: Item, meeting_obj: MeetingRoomBoardResponseData, layout: LinearLayout?):View? {

        var view = (ctx as Activity).layoutInflater.inflate(R.layout.widget_meeting_roomboard,null)
        var room_main_ll = view.findViewById<LinearLayout>(R.id.ll_meeting_roommain)
        var image = view.findViewById<ImageView>(R.id.iv_meetingroom_image)
        var title_tv = view.findViewById<TextView>(R.id.tv_meetingroom_title)
        var time_tv = view.findViewById<TextView>(R.id.tv_meetingroom_time)
        if(meeting_obj.event !=null ){

            ImageUtil.loadImage(ctx,meeting_obj?.event?.logo!!,image)
            title_tv.text = meeting_obj?.event?.title
            time_tv.text = "${meeting_obj?.event?.starttime} - ${meeting_obj?.event?.endtime}"

            // setting
            var obj = JSONObject(item.settings)
            var bg_color = Color.parseColor(DataParsingSetting.getBgWithOpacity(obj))
            var title_size = DataParsingSetting.getTitleSize(obj).toFloat()
            var time_size = DataParsingSetting.getTimeSize(obj).toFloat()
            var title_font = DataParsingSetting.getFontTitleLabel(obj)
            var time_font = DataParsingSetting.getFontTimeLabel(obj)
            var title_color = DataParsingSetting.getRowTitleTextColor(obj)
            var time_color = DataParsingSetting.getRowTimeTextColor(obj)

            // main ll size
            var main_ll_lp = LayoutParams(item.frame_w,item.frame_h)
            room_main_ll.layoutParams = main_ll_lp
            // image size
            var iv_lp = LayoutParams((title_size*2).toInt(),(title_size*2).toInt())
            image.layoutParams = iv_lp
            image.setPadding(5,5,5,5)
            image.setBackgroundResource(R.drawable.bg_meeting_icon)

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

        }else if (meeting_obj.ss.size>0)
            loadScreenSaver(meeting_obj.ss,0,layout!!,ctx)
        return view
    }

}