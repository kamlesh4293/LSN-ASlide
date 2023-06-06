package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.model.news_setting.News
import com.app.lsquared.model.widget.RssItem
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.ui.adapter.BeingNewsAdapter
import com.app.lsquared.ui.adapter.NewsAdapter
import com.app.lsquared.utils.DataParsing
import com.app.lsquared.utils.FontUtil
import com.app.lsquared.utils.UtilitySetting
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlin.math.log

class WidgetNewsList {

    companion object{


        // for news

        var layout: LinearLayout? = null
        var cal_layout: LinearLayout? = null
        var lastView: View? = null
        var total_hight = 0
        var position = 0
        var list : List<RssItem>? = null
        var setting_obj : NewsListSettingData? = null
        var item: Item? = null

        fun getWidgetNewsListAllFixContent(
            ctx: Context,
            item: Item,
            mylist: List<RssItem>,
            title: String?,
            start_pos: Int
        ): View {

            total_hight = 0
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.news_list_view,null)

            cal_layout = view.findViewById<LinearLayout>(R.id.ll_main_listview_cal)
            layout = view.findViewById<LinearLayout>(R.id.ll_main_listview)
            var title_tv = view.findViewById<TextView>(R.id.tv_listview_title)

            // setting
            Log.d("TAG", "getWidgetNewsListAllFixContent: setting - ${item.settings}")
            setting_obj = Gson().fromJson(item.settings, NewsListSettingData::class.java)

            // title
            title_tv.setText(title)
            title_tv.textSize = setting_obj?.headerSize!!.toFloat()
            title_tv.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(setting_obj?.headerBg!!, setting_obj?.headerBga!!)))
            title_tv.setTextColor(Color.parseColor(setting_obj?.headerText))
            FontUtil.setFonts(ctx,title_tv, setting_obj?.headerFont?.label!!)

            if(setting_obj?.hTextOpt.equals("n"))   // header set none
                title_tv.visibility = View.GONE
            else if(setting_obj?.hTextOpt.equals("a")){ // header text auto
                title_tv.visibility = View.VISIBLE
                title_tv.text = title
            }else{                                          // header custom text
                title_tv.visibility = View.VISIBLE
                title_tv.text = setting_obj?.hText
            }

            title_tv.post(Runnable() {
                kotlin.run {
                    var height = title_tv.getHeight()
                    total_hight = total_hight +height
                }
            })

            this.item = item
            position = start_pos

            layout?.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(setting_obj?.bg!!,setting_obj?.bga!!)))
            //
            list = mylist
            addView(ctx,list!!)
            mHandler.postDelayed(SCROLLING_RUNNABLE, 300);
            return view
        }

        private val mHandler: Handler = Handler(Looper.getMainLooper())

        private val SCROLLING_RUNNABLE: Runnable = object : Runnable {
            override fun run() {
                if(total_hight< item?.frame_h!!){
                    cal_layout?.removeAllViews()
                    if(lastView!=null){
                        position = position+1
                        layout?.addView(lastView)
                        lastView = null
                    }
                    addView(layout?.context!!, list!!)
                    mHandler.postDelayed(this, 10)
                }else{
                    lastView = null
                }
            }
        }

        private fun addView(ctx: Context, list: List<RssItem>) {
            if(list.size > position) {
                cal_layout?.removeAllViews()

                var param = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                var ch_layout = LinearLayout(ctx)
                ch_layout.layoutParams = param
                ch_layout.orientation = LinearLayout.VERTICAL
                ch_layout.setPadding(10,10,10,20)


                var textview = TextView(ctx)
                textview.layoutParams = param
                textview.text = "${list[position].title} "
                textview.setTypeface(Typeface.DEFAULT_BOLD)

                var desc_textview = TextView(ctx)
                desc_textview.layoutParams = param
                desc_textview.text = getFormattedString(list[position].desc)
                if(getFormattedString(list[position].desc).equals(""))desc_textview.visibility = View.GONE


                Log.d("TAG", "addView: title = ${list[position].title} ,  desc - ${list[position].desc}")

                if(position%2 ==0){
                    ch_layout.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(setting_obj?.rowBg!!,setting_obj?.rowBga!!)))
                    textview.setTextColor(Color.parseColor(setting_obj!!.titleText))
                    desc_textview.setTextColor(Color.parseColor(setting_obj!!.descText))
                    FontUtil.setFonts(ctx,textview, setting_obj?.rowFont?.label!!)
                    FontUtil.setFonts(ctx,desc_textview,setting_obj?.rowFont?.label!!)
                }else{
                    ch_layout.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(setting_obj?.altBg!!,setting_obj?.altBga!!)))
                    textview.setTextColor(Color.parseColor(setting_obj?.altTitleText))
                    desc_textview.setTextColor(Color.parseColor(setting_obj?.altDescText))
                    FontUtil.setFonts(ctx,textview,setting_obj?.altRowFont?.label!!)
                    FontUtil.setFonts(ctx,desc_textview,setting_obj?.altRowFont?.label!!)
                }
                textview.textSize = setting_obj?.titleSize!!.toFloat()
                desc_textview.textSize = setting_obj?.descSize!!.toFloat()

                ch_layout?.addView(textview)
                ch_layout?.addView(desc_textview)

                cal_layout?.addView(ch_layout)

                lastView = ch_layout

                ch_layout.post(Runnable() {
                    kotlin.run {
                        var height = ch_layout.getHeight()
                        total_hight = total_hight+height
                        Log.d("TAG", "addView: view heigh - $height , total heigh - $total_hight")
                    }
                })
            }
        }

        private fun getFormattedString(desc: String): String {
            var description = ""
            if(!desc.contains("<p>")) return desc
            else if(desc.contains("<p>")){
                var desc_p = desc.split("<p>")
                var new_desc =  desc_p[1].split("</p>")
                return new_desc[0]
            }
            return description
        }

        fun getLastPosition(): Int {
            return position
        }

        // for being news

        var being_layout: LinearLayout? = null
        var being_cal_layout: LinearLayout? = null
        var being_lastView: View? = null
        var being_total_hight = 0
        var being_position = 0
        var being_list : List<News>? = null
        var being_setting_obj : NewsListSettingData? = null
        var being_item: Item? = null

        fun getWidgetNewsListAllFixContentBeing(
            ctx: Context,
            item: Item,
            mylist: List<News>,
            start_pos: Int,
            title: String
        ): View {

            being_total_hight = 0
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.news_list_view,null)

            being_cal_layout = view.findViewById<LinearLayout>(R.id.ll_main_listview_cal)
            being_layout = view.findViewById<LinearLayout>(R.id.ll_main_listview)
            var title_tv = view.findViewById<TextView>(R.id.tv_listview_title)

            being_item = item
            being_position = start_pos
            // setting
            Log.d("TAG", "getWidgetNewsListAllFixContentBeing: ${item.settings}")
            being_setting_obj = Gson().fromJson(item.settings, NewsListSettingData::class.java)

            if(UtilitySetting.getHeaderTextOpt(item.settings).equals("n"))   // header set none
                title_tv.visibility = View.GONE
            else if(UtilitySetting.getHeaderTextOpt(item.settings).equals("a")){ // header text auto
                title_tv.visibility = View.VISIBLE
                title_tv.text = title
            }else{                                          // header custom text
                title_tv.visibility = View.VISIBLE
                title_tv.text = UtilitySetting.getHeaderText(item.settings)
            }
            title_tv.setBackgroundColor(Color.parseColor(
                    UiUtils.getColorWithOpacity(UtilitySetting.getHeaderBg(item.settings),
                    UtilitySetting.getHeaderBga(item.settings)))
            )
            title_tv.setTextColor(Color.parseColor(being_setting_obj!!.headerText))
            title_tv.textSize = UtilitySetting.getHeaderFontSize(item.settings)
            FontUtil.setFonts(ctx,title_tv, UtilitySetting.getFontLable(item.settings))

            title_tv.post(Runnable() {
                kotlin.run {
                    var height = title_tv.getHeight()
                    total_hight = total_hight +height
                }
            })


            being_layout?.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(being_setting_obj?.bg!!,being_setting_obj?.bga!!)))
            //
            being_list = mylist
            addViewBeing(ctx,being_list!!)
            mHandlerBeing.postDelayed(SCROLLING_RUNNABLEBeing, 300);
            return view
        }

        private val mHandlerBeing: Handler = Handler(Looper.getMainLooper())

        private val SCROLLING_RUNNABLEBeing: Runnable = object : Runnable {
            override fun run() {
                if(being_total_hight< being_item?.frame_h!!){
                    being_cal_layout?.removeAllViews()
                    if(being_lastView!=null){
                        being_layout?.addView(being_lastView)
                        being_lastView = null
                    }
                    addViewBeing(being_layout?.context!!, being_list!!)
                    mHandlerBeing.postDelayed(this, 10)
                }else{
                    being_lastView = null
                }
            }
        }

        private fun addViewBeing(ctx: Context, list: List<News>) {
            if(list.size > being_position) {
                being_cal_layout?.removeAllViews()

                var param = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                var ch_layout = LinearLayout(ctx)
                ch_layout.layoutParams = param
                ch_layout.orientation = LinearLayout.VERTICAL
                ch_layout.setPadding(10,10,10,20)


                var textview = TextView(ctx)
                textview.layoutParams = param
                textview.text = "${list[being_position].title} "
                textview.setTypeface(Typeface.DEFAULT_BOLD)

                var desc_textview = TextView(ctx)
                desc_textview.layoutParams = param
                var desc = list[being_position].desc?.replace(" ","")
                desc_textview.text = desc

                if(list[being_position].desc.equals("")) desc_textview.visibility = View.GONE

                Log.d("TAG", "addView: title = ${list[being_position].title} ,  desc - ${list[being_position].desc}")

                if(being_position%2 ==0){
                    ch_layout.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(being_setting_obj?.rowBg!!,being_setting_obj?.rowBga!!)))
                    textview.setTextColor(Color.parseColor(being_setting_obj!!.titleText))
                    desc_textview.setTextColor(Color.parseColor(being_setting_obj!!.descText))
                    FontUtil.setFonts(ctx,textview, being_setting_obj?.rowFont?.label!!)
                    FontUtil.setFonts(ctx,desc_textview,being_setting_obj?.rowFont?.label!!)
                }else{
                    ch_layout.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(being_setting_obj?.altBg!!,being_setting_obj?.altBga!!)))
                    textview.setTextColor(Color.parseColor(being_setting_obj?.altTitleText))
                    desc_textview.setTextColor(Color.parseColor(being_setting_obj?.altDescText))
                    FontUtil.setFonts(ctx,textview,being_setting_obj?.altRowFont?.label!!)
                    FontUtil.setFonts(ctx,desc_textview,being_setting_obj?.altRowFont?.label!!)
                }
                if(being_setting_obj?.titleSize!=null) textview.textSize = being_setting_obj?.titleSize!!.toFloat()
                if (being_setting_obj?.descSize!=null)desc_textview.textSize = being_setting_obj?.descSize!!.toFloat()

                ch_layout?.addView(textview)
                ch_layout?.addView(desc_textview)

                being_cal_layout?.addView(ch_layout)

                being_lastView = ch_layout
                being_position = being_position+1

                ch_layout.post(Runnable() {
                    kotlin.run {
                        var height = ch_layout.getHeight()
                        being_total_hight = being_total_hight+height
                        Log.d("TAG", "addView: view heigh - $height , total heigh - $being_total_hight")
                    }
                })
            }
        }

        fun getLastPositionBeing(): Int {
            return being_position
        }

    }
}

data class NewsListSettingData(

    @SerializedName("rotationOpt"  ) var rotationOpt  : String?     = null,
    @SerializedName("rotate"       ) var rotate       : Int?        = null,
    @SerializedName("hTextOpt"     ) var hTextOpt     : String?     = null,
    @SerializedName("hText"        ) var hText        : String?     = null,
    @SerializedName("headerBg"     ) var headerBg     : String?     = null,
    @SerializedName("headerBga"    ) var headerBga    : String?     = null,
    @SerializedName("headerText"   ) var headerText   : String?     = null,
    @SerializedName("headerFont"   ) var headerFont   : HeaderFont? = HeaderFont(),
    @SerializedName("isbg"         ) var isbg         : Boolean?    = null,
    @SerializedName("bg"           ) var bg           : String?     = null,
    @SerializedName("bga"          ) var bga          : String?     = null,
    @SerializedName("rowBg"        ) var rowBg        : String?     = null,
    @SerializedName("rowBga"       ) var rowBga       : String?     = null,
    @SerializedName("altBg"        ) var altBg        : String?     = null,
    @SerializedName("altBga"       ) var altBga       : String?     = null,
    @SerializedName("titleText"    ) var titleText    : String?     = null,
    @SerializedName("descText"     ) var descText     : String?     = null,
    @SerializedName("rowFont"      ) var rowFont      : RowFont?    = RowFont(),
    @SerializedName("altTitleText" ) var altTitleText : String?     = null,
    @SerializedName("altDescText"  ) var altDescText  : String?     = null,
    @SerializedName("altRowFont"   ) var altRowFont   : AltRowFont? = AltRowFont(),
    @SerializedName("headerSize"   ) var headerSize   : Int?        = null,
    @SerializedName("titleSize"    ) var titleSize    : Int?        = null,
    @SerializedName("descSize"     ) var descSize     : Int?        = null
)

data class HeaderFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class RowFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class AltRowFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)