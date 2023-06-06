package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.model.SettingCommon
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.FontUtil
import com.app.lsquared.utils.UtilitySetting
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONObject


class WidgetText {


    companion object : lastItemCalled {

        val mHandler: Handler = Handler(Looper.getMainLooper())
        var pixelsToMove = 50
        lateinit var view: View
        lateinit var recyclerView: RecyclerView

        val SCROLLING_RUNNABLE: Runnable = object : Runnable {
            override fun run() {
                recyclerView.smoothScrollBy(pixelsToMove, 0)
                mHandler.postDelayed(this, 100)
            }
        }


        fun getWidgetTextStaticLayout(ctx: Context, item: Item): View {
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_text, null)

            // setting
            var settings = Gson().fromJson(item.settings, TextStaticWidgetSetting::class.java)

            var layout = view.findViewById<LinearLayout>(R.id.ll_text_main)
            layout.setBackgroundColor(
                Color.parseColor(
                    UiUtils.getColorWithOpacity(
                        settings.bg ?: "#000000", settings.bga.toString()
                    )
                )
            )
            return view

        }

        fun getWidgetTextStatic(ctx: Context, item: Item, text: String?): View {
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_text, null)

            var textview = view.findViewById<TextView>(R.id.tv_text_static)
            var layout = view.findViewById<LinearLayout>(R.id.ll_text_main)
            textview.visibility = View.VISIBLE

            // setting
            var settings = Gson().fromJson(item.settings, TextStaticWidgetSetting::class.java)
            layout.setBackgroundColor(
                Color.parseColor(
                    UiUtils.getColorWithOpacity(
                        settings.bg ?: "#000000", settings.bga.toString()
                    )
                )
            )
            textview.textSize = settings.size!!.toFloat()
            textview.setTextColor(Color.parseColor(settings.titleText))
            FontUtil.setFonts(ctx, textview, settings.font?.label!!)

            var hori_align =
                if (settings.align.equals("l")) Gravity.LEFT else if (settings.align.equals("c")) Gravity.CENTER else Gravity.RIGHT
            var vert_align =
                if (settings.vAlign.equals("t")) Gravity.TOP else if (settings.vAlign.equals("m")) Gravity.CENTER else Gravity.BOTTOM

            textview.gravity = hori_align or vert_align

            textview.text = text
            return view
        }

        fun getWidgetTextCrowling(ctx: Context, item: Item, data: String?): View {
            view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_text_crawling, null)

            var layout = view.findViewById<LinearLayout>(R.id.ll_text_main)
            recyclerView = view.findViewById<RecyclerView>(R.id.rv_text_crawling)
            var rl_parent_rv = view.findViewById<RelativeLayout>(R.id.rl_parent_rv)

            view.findViewById<LinearLayout>(R.id.ll_rv_over).setOnClickListener { }

            // setting
            var setting = Gson().fromJson(item.settings, TextWidgetSetting::class.java)
            layout.setBackgroundColor(Color.parseColor(setting.bg))

            var list = mutableListOf<String>()
            var json_data = JSONObject(data)
            var channel_array = json_data.getJSONArray("channel")
            if(channel_array.length()>0){
                for (i in 0..500) {
                    for (i in 0 until channel_array.length()) {
                        var title_obj = channel_array.getJSONObject(i).getString("title")
                        Log.d("TAG", "setData: listitem $i - $title_obj")
                        list.add(title_obj)
                    }
                    if (list.size >= 500) break
                }
                if (setting.dir.equals("rl")) {
                    rl_parent_rv.visibility = View.VISIBLE
                    addViewHorizontal(recyclerView, list, setting,item)
                    mHandler.postDelayed(SCROLLING_RUNNABLE, 1000);
                } else {
                    rl_parent_rv.visibility = View.VISIBLE
                    addViewVertical(recyclerView, list, setting,item)
                }
            }
            return view
        }

        private fun addViewHorizontal(
            recyclerView: RecyclerView,
            list: MutableList<String>,
            setting: TextWidgetSetting,
            item: Item
        ) {

            var layout_manager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)

            recyclerView.layoutManager = layout_manager
            var adp = TextHorizontalScrollAdapter(list, this, recyclerView, setting,item)
            recyclerView.adapter = adp

            pixelsToMove = UtilitySetting.getMovingPixel(setting.speed!!)
            var scrollDuration = UtilitySetting.getSpeed(setting.speed!!)

            val linearSmoothScroller: LinearSmoothScroller =
                object : LinearSmoothScroller(recyclerView.getContext()) {
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                        return scrollDuration / recyclerView.computeVerticalScrollRange()
                    }
                }
            linearSmoothScroller.targetPosition = adp.getItemCount() - 1
            layout_manager.startSmoothScroll(linearSmoothScroller)
        }

        private fun addViewVertical(
            recyclerView: RecyclerView,
            list: MutableList<String>,
            setting: TextWidgetSetting,
            item: Item
        ) {
            var layout_manager: LinearLayoutManager? = null
            var adp: TextVerticalScrollAdapter? = null

            if (setting.dir.equals("rl")) {
                layout_manager =
                    LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)

                recyclerView.layoutManager = layout_manager
                adp = TextVerticalScrollAdapter(list, this, recyclerView, setting,item)
                recyclerView.adapter = adp
            } else {
                layout_manager = LinearLayoutManager(recyclerView.context)
                recyclerView.layoutManager = layout_manager
                adp = TextVerticalScrollAdapter(list, this, recyclerView, setting, item)
                recyclerView.adapter = adp
            }

            var scrollDuration = UtilitySetting.getSpeed(setting.speed!!)

            val linearSmoothScroller: LinearSmoothScroller =
                object : LinearSmoothScroller(recyclerView.getContext()) {
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                        return scrollDuration / recyclerView.computeVerticalScrollRange()
                    }
                }
            linearSmoothScroller.targetPosition = adp.getItemCount() - 1
            layout_manager.startSmoothScroll(linearSmoothScroller)
        }

        fun getBlankView(ctx: Context): View {
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_text, null)

            var textview = view.findViewById<TextView>(R.id.tv_text_static)
            var layout = view.findViewById<LinearLayout>(R.id.ll_text_main)
            textview.visibility = View.GONE
            layout.setBackgroundColor(ctx.resources.getColor(R.color.black))
            return view
        }

        override fun lastItemVisible(
            recyclerView: RecyclerView,
            list: MutableList<String>,
            setting: TextWidgetSetting,
            item: Item
        ) {
            recyclerView.removeAllViews()
            if (setting.dir.equals("rl")) addViewHorizontal(recyclerView, list, setting, item)
            else addViewVertical(recyclerView, list, setting, item)
        }
    }
}

class TextVerticalScrollAdapter(
    var list: MutableList<String>,
    var lister: lastItemCalled,
    var recyclerView: RecyclerView,
    var setting: TextWidgetSetting,
    var item: Item
) : RecyclerView.Adapter<TextVerticalScrollAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scroll_vertical, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = list[position]
        holder.textView.setTextColor(Color.parseColor(setting.titleText))
        FontUtil.setFonts(holder.textView.context, holder.textView, setting.font?.label!!)

        if (setting.align.equals("l"))
            holder.textView.setGravity(Gravity.BOTTOM or Gravity.LEFT)
        if (setting.align.equals("r"))
            holder.textView.setGravity(Gravity.BOTTOM or Gravity.RIGHT)
        if (setting.align.equals("c"))
            holder.textView.setGravity(Gravity.BOTTOM or Gravity.CENTER)
        if (position == list.size - 1) {
            lister.lastItemVisible(recyclerView, list, setting, item)
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

class TextHorizontalScrollAdapter(
    var list: MutableList<String>,
    var lister: lastItemCalled,
    var recyclerView: RecyclerView,
    var setting: TextWidgetSetting,
    var item: Item
) : RecyclerView.Adapter<TextHorizontalScrollAdapter.ViewHolder>() {

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

        holder.bullet_tv.setTextColor(Color.parseColor(setting.bullet))
        holder.bullet_tv.setBackgroundColor(Color.parseColor(setting.bullet))

        FontUtil.setFonts(holder.textView.context, holder.textView, setting.font?.label!!)

        if (position == list.size - 1) {
            lister.lastItemVisible(recyclerView, list, setting,item)
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

interface lastItemCalled {

    fun lastItemVisible(
        recyclerView: RecyclerView,
        list: MutableList<String>,
        setting: TextWidgetSetting,
        item: Item
    )
}

data class TextWidgetSetting(

    @SerializedName("speed") var speed: Int? = null,
    @SerializedName("bg") var bg: String? = null,
    @SerializedName("bga") var bga: String? = null,
    @SerializedName("bullet") var bullet: String? = null,
    @SerializedName("titleText") var titleText: String? = null,
    @SerializedName("font") var font: Font? = Font(),
    @SerializedName("dir") var dir: String? = null,
    @SerializedName("align") var align: String? = null,
    @SerializedName("fontSize") var fontSize: Int? = null,
    @SerializedName("fontSizeOpt") var fontSizeOpt: String? = null

)

data class Font(

    @SerializedName("label") var label: String? = null,
    @SerializedName("value") var value: String? = null

)

data class TextStaticWidgetSetting(

    @SerializedName("rotationOpt") var rotationOpt: String? = null,
    @SerializedName("size") var size: Int? = null,
    @SerializedName("rotate") var rotate: Int? = null,
//    @SerializedName("bg"          ) var bg          : String? = null,
//    @SerializedName("bga"         ) var bga         : String?    = null,
    @SerializedName("titleText") var titleText: String? = null,
    @SerializedName("font") var font: Font? = Font(),
    @SerializedName("vAlign") var vAlign: String? = null,
    @SerializedName("align") var align: String? = null,
    override var bg: String,
    override var bga: String

) : SettingCommon()