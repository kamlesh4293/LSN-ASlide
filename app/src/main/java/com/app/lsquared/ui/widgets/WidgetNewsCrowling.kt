package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.FontUtil
import com.app.lsquared.utils.UtilitySetting
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


class WidgetNewsCrowling {

    companion object : lastItemCalledNews {

        val mHandler: Handler = Handler(Looper.getMainLooper())
        var pixelsToMove = 50
        lateinit var recyclerView: RecyclerView

        val SCROLLING_RUNNABLE: Runnable = object : Runnable {
            override fun run() {
                recyclerView.smoothScrollBy(pixelsToMove, 0)
                mHandler.postDelayed(this, 100)
            }
        }

        fun getWidgetNewsCrowling(ctx: Context, item: Item, data: String, list: MutableList<String>): View {

            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_rss, null)
            var setting_obj = Gson().fromJson(item.settings, NewsCrawlSettingData::class.java)

            var crowling_text = view.findViewById<TextView>(R.id.tv_rss_fragment_crawl)
            var llMainRss = view.findViewById<LinearLayout>(R.id.ll_main_rss)
            recyclerView = view.findViewById<RecyclerView>(R.id.rv_news_crawling)
            var rl_parent_rv = view.findViewById<RelativeLayout>(R.id.rl_parent_newsrv)

            view.findViewById<LinearLayout>(R.id.ll_rv_newsover).setOnClickListener {  }
            llMainRss.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity( setting_obj.bg!!,setting_obj.bga.toString())))

            var scroll_list = mutableListOf<String>()
            for (i in 0..500){
                for (i in 0..list.size-1)
                    scroll_list.add(list[i])
                if(scroll_list.size>=500) break
            }
            if(setting_obj.dir.equals("bt")){
                crowling_text.visibility = View.GONE
                rl_parent_rv.visibility = View.VISIBLE
                addViewVertical(recyclerView, scroll_list, setting_obj,item,ctx)
            }else{
                crowling_text.visibility = View.GONE
                rl_parent_rv.visibility = View.VISIBLE
                addViewHorizontal(recyclerView, scroll_list, setting_obj,item)
                mHandler.postDelayed(SCROLLING_RUNNABLE, 1000)

//                rl_parent_rv.visibility = View.GONE
//                crowling_text.visibility = View.VISIBLE
//                crowling_text.textSize = setting_obj.fontSize!!.toFloat()
//                crowling_text.setTextColor(Color.parseColor(setting_obj.titleText))
//                crowling_text.text = data
//                crowling_text.isSelected = true
//                setMarqueeSpeed(crowling_text,setting_obj.speed!!.toFloat())
//                FontUtil.setFonts(ctx,crowling_text,setting_obj.font?.label!!)
            }
            return view
        }

        private fun addViewVertical(
            recyclerView: RecyclerView,
            list: MutableList<String>,
            setting: NewsCrawlSettingData,
            item:Item,ctx: Context
        ) {
            var layout_manager = LinearLayoutManager(recyclerView.context)
            recyclerView.layoutManager = layout_manager
            var adp = NewsVerticalScrollAdapter(list,this,recyclerView,setting,item)
            recyclerView.adapter = adp

            var scrollDuration =  UtilitySetting.getSpeed(setting.speed!!)
            val linearSmoothScroller: LinearSmoothScroller =
                object : LinearSmoothScroller(recyclerView.getContext()) {
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                        return scrollDuration / recyclerView.computeVerticalScrollRange()
                    }
                }
            linearSmoothScroller.targetPosition = adp.getItemCount() - 1
            layout_manager.startSmoothScroll(linearSmoothScroller)
        }

        private fun addViewHorizontal(
            recyclerView: RecyclerView,
            list: MutableList<String>,
            setting: NewsCrawlSettingData,
            item:Item
        ) {
            var layout_manager = LinearLayoutManager(recyclerView.context,LinearLayoutManager.HORIZONTAL,false)
            recyclerView.layoutManager = layout_manager
            var adp = NewsHorizontalScrollAdapter(list,this,recyclerView,setting,item)
            recyclerView.adapter = adp

            pixelsToMove = UtilitySetting.getMovingPixel(setting.speed!!)
            var scrollDuration =  UtilitySetting.getSpeed(setting.speed!!)
//            var scrollDuration =  110000f

            val linearSmoothScroller: LinearSmoothScroller =
                object : LinearSmoothScroller(recyclerView.getContext()) {
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                        return scrollDuration / recyclerView.computeVerticalScrollRange()
                    }
                }
            linearSmoothScroller.targetPosition = adp.getItemCount() - 1
            layout_manager.startSmoothScroll(linearSmoothScroller)
        }


        override fun lastItemVisible(
            recyclerView: RecyclerView,
            list: MutableList<String>,
            setting: NewsCrawlSettingData,
            item: Item,
            ctx: Context
        ) {
            recyclerView.removeAllViews()
            if(setting.dir.equals("bt")) addViewVertical(recyclerView, list, setting,item,ctx)
            else addViewHorizontal(recyclerView, list, setting,item)
        }
    }
}

class NewsVerticalScrollAdapter(
    var list: MutableList<String>,
    var lister: lastItemCalledNews,
    var recyclerView: RecyclerView,
    var setting: NewsCrawlSettingData,
    var item:Item
)
    : RecyclerView.Adapter<NewsVerticalScrollAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_scroll_vertical, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = list[position]
        holder.textView.textSize = setting.fontSize!!.toFloat()
        holder.textView.setTextColor(Color.parseColor(setting.titleText))
        FontUtil.setFonts(holder.textView.context,holder.textView,setting.font?.label!!)

        if(setting.align.equals("l"))
            holder.textView.setGravity(Gravity.BOTTOM or Gravity.LEFT)
        if(setting.align.equals("r"))
            holder.textView.setGravity(Gravity.BOTTOM or Gravity.RIGHT)
        if(setting.align.equals("c"))
            holder.textView.setGravity(Gravity.BOTTOM or Gravity.CENTER)
        if(position==list.size-1){
            lister.lastItemVisible(recyclerView,list,setting,item,holder.textView.context)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_scrollitem_text)
        val ll_main: LinearLayout = itemView.findViewById(R.id.ll_scrollitem_main)
    }
}

class NewsHorizontalScrollAdapter(
    var list: MutableList<String>,
    var lister: lastItemCalledNews,
    var recyclerView: RecyclerView,
    var setting: NewsCrawlSettingData,
    var item:Item
) : RecyclerView.Adapter<NewsHorizontalScrollAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scroll_horizontal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = list[position]
        holder.textView.setTextColor(Color.parseColor(setting.titleText))

        if(setting.fontSizeOpt.equals("a")) holder.textView.textSize = item.frame_h/2.5.toFloat()
        else holder.textView.textSize = setting.fontSize!!.toFloat()

        if(setting.fontSizeOpt.equals("a")) {
            val bullet_hiwi = LinearLayout.LayoutParams(item.frame_h/2.5.toInt(), item.frame_h/2.5.toInt())
            holder.bullet_tv.layoutParams = bullet_hiwi
        } else {
            val bullet_hiwi = LinearLayout.LayoutParams(setting.fontSize!!, setting.fontSize!!)
            holder.bullet_tv.layoutParams = bullet_hiwi
        }


        holder.bullet_tv.setBackgroundColor(Color.parseColor(setting.bullet))

        FontUtil.setFonts(holder.textView.context, holder.textView, setting.font?.label!!)

        if (position == list.size - 1) {
            lister.lastItemVisible(recyclerView, list, setting,item,holder.textView.context)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_horizontalscroll_text)
        val bullet_tv: TextView = itemView.findViewById(R.id.tv_horizontalscroll_bullet)
    }
}


interface lastItemCalledNews{

    fun lastItemVisible(
        recyclerView: RecyclerView,
        list: MutableList<String>,
        setting: NewsCrawlSettingData,
        item: Item,
        ctx: Context
    )
}


data class NewsCrawlSettingData(
    @SerializedName("speed"       ) var speed       : Int?    = null,
    @SerializedName("bg"          ) var bg          : String? = null,
    @SerializedName("bga"         ) var bga         : String?    = null,
    @SerializedName("bullet"      ) var bullet      : String? = null,
    @SerializedName("titleText"   ) var titleText   : String? = null,
    @SerializedName("font"        ) var font        : Font?   = Font(),
    @SerializedName("dir"         ) var dir         : String? = null,
    @SerializedName("align"       ) var align       : String? = null,
    @SerializedName("fontSizeOpt" ) var fontSizeOpt : String? = null,
    @SerializedName("fontSize"    ) var fontSize    : Int?    = null
)

