package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.model.widget.*
import com.app.lsquared.model.widget.stock.StockTableItemSetting
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.utils.Constant
import com.app.lsquared.utils.FontUtil
import com.app.lsquared.utils.Utility
import com.app.lsquared.utils.UtilitySetting
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class WidgetStocks {

    companion object : lastItemCalledStock{

        // single stock

        fun getSingleStockWidget(ctx: Context, item: Item, data: String?): View {

            var view : View? = null

            var setting_obj = Gson().fromJson(item.settings,SettingsStocks::class.java)

            if(setting_obj.template.equals(Constant.TEMPLATE_TIME_T1))
                view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_stock_t1, null)
            if(setting_obj.template.equals(Constant.TEMPLATE_TIME_T2))
                view = (ctx as Activity).layoutInflater.inflate(R.layout.temp_stock_t22, null)

            // set template 1
            if(setting_obj.template.equals(Constant.TEMPLATE_TIME_T1))
                setTemplateData1(view, item,data,ctx)
            // set template 2
            if(setting_obj.template.equals(Constant.TEMPLATE_TIME_T2))
                setTemplateData2(view!!, item,data,ctx)
            return view!!
        }

        private fun setTemplateData1(view: View?, item: Item, data: String?, ctx: Context) {
            var tv_symbol = view?.findViewById<TextView>(R.id.tv_stocktemp1_symbol)
            var tv_price = view?.findViewById<TextView>(R.id.tv_stocktemp1_price)
            var tv_name = view?.findViewById<TextView>(R.id.tv_stocktemp1_name)
            var tv_changes = view?.findViewById<TextView>(R.id.tv_stocktemp1_changes)
            var ll_bg = view?.findViewById<LinearLayout>(R.id.ll_stocktemp1_bg)
            var arrow_iv = view?.findViewById<ImageView>(R.id.iv_stocktemp1_arrow)

            Log.d("TAG", "setTemplateData1: $data")

            var data_obj = Gson().fromJson(data, StockDataModel::class.java)

            if(data_obj.data.size==0) return

            tv_symbol?.text = data_obj.data.get(0).symbol
            tv_price?.text = data_obj.data.get(0).price
            tv_name?.text = data_obj.data.get(0).name
            tv_changes?.text = data_obj.data.get(0).dayChange

            tv_symbol?.textSize = data_obj.settings?.sSize!!.toFloat()
            tv_price?.textSize = data_obj.settings?.pSize!!.toFloat()
            tv_name?.textSize = data_obj.settings?.nSize!!.toFloat()
            tv_changes?.textSize = data_obj.settings?.cpnSize!!.toFloat()

            tv_symbol?.setTextColor(UiUtils.getColor(data_obj.settings?.s!!))
            tv_price?.setTextColor(UiUtils.getColor(data_obj.settings?.p!!))
            tv_name?.setTextColor(UiUtils.getColor(data_obj.settings?.n!!))

            if (data_obj.settings?.cpnSize!!*2>100){
                val layoutParams = LinearLayout.LayoutParams(data_obj.settings?.cpnSize!!*2, data_obj.settings?.cpnSize!!*2)
                arrow_iv?.layoutParams = layoutParams
            }

            if(data_obj.data.get(0).dayChange!!.toFloat()<0){
                arrow_iv?.setImageResource(R.drawable.down_arrow)
                tv_changes?.setTextColor(UiUtils.getColor(data_obj.settings?.cn!!))
            } else{
                arrow_iv?.setImageResource(R.drawable.up_arrow)
                tv_changes?.setTextColor(UiUtils.getColor(data_obj.settings?.cp!!))
            }

            ll_bg?.setBackgroundColor(UiUtils.getColor(UiUtils.getColorWithOpacity(data_obj.settings?.bg!!,data_obj.settings?.bga!!,)))

            FontUtil.setFonts(ctx,tv_symbol!!,data_obj.settings?.sFont?.label!!)
            FontUtil.setFonts(ctx,tv_price!!,data_obj.settings?.pFont?.label!!)
            FontUtil.setFonts(ctx,tv_name!!,data_obj.settings?.nFont?.label!!)
            FontUtil.setFonts(ctx,tv_changes!!,data_obj.settings?.cpnFont?.label!!)

        }

        private fun setTemplateData2(view: View, item: Item, data: String?, ctx: Context) {

            var ll_bg = view?.findViewById<LinearLayout>(R.id.ll_stocktemp2_bg)
            var symbol_ll = view?.findViewById<LinearLayout>(R.id.ll_stocktemp2_symbol)
            var hight_ll = view?.findViewById<LinearLayout>(R.id.ll_stocktemp2_high)
            var low_ll = view?.findViewById<LinearLayout>(R.id.ll_stocktemp2_low)
            var tv_symbol = view?.findViewById<TextView>(R.id.tv_stocktemp2_symbol)
            var tv_changes = view?.findViewById<TextView>(R.id.tv_stocktemp2_changes)

            var high_tv = view?.findViewById<TextView>(R.id.tv_stocktemp2_hi)
            var low_tv = view?.findViewById<TextView>(R.id.tv_stocktemp2_low)
            var high_v_tv = view?.findViewById<TextView>(R.id.tv_stocktemp2_hivalue)
            var low_v_tv = view?.findViewById<TextView>(R.id.tv_stocktemp2_lowvalue)
            var price_tv = view?.findViewById<TextView>(R.id.tv_stocktemp2_price)
//            var price2_tv = view?.findViewById<TextView>(R.id.tv_stocktemp2_price2)


            Log.d("TAG", "setTemplateData2: $data")

            var data_obj = Gson().fromJson(data, StockDataModel::class.java)
            var setting = data_obj.settings
            var max_size = setting?.sSize
            max_size = if(setting?.cpnSize!! >max_size!!) setting?.cpnSize!! else max_size
            max_size = if(setting?.hpSize!! >max_size!!) setting?.hpSize!! else max_size
            max_size = if(setting?.lpSize!! >max_size!!) setting?.lpSize!! else max_size
            max_size = if(setting?.pSize!! >max_size!!) setting?.pSize!! else max_size

            val params = symbol_ll!!.layoutParams
            params.width = max_size*8
            symbol_ll.layoutParams = params
            price_tv?.layoutParams = params
            hight_ll?.layoutParams = params
            low_ll?.layoutParams = params


            tv_symbol?.text = data_obj.data.get(0).symbol
            high_v_tv?.text = data_obj.data.get(0).high
            low_v_tv?.text = data_obj.data.get(0).low
            tv_changes?.text = getPrice(data_obj.data.get(0).dayChange)
            price_tv?.text = data_obj.data.get(0).price
//            price2_tv?.text = data_obj.data.get(0).price

            tv_symbol?.textSize = data_obj.settings?.sSize!!.toFloat()
            high_tv?.textSize = data_obj.settings?.hpSize!!.toFloat()
            high_v_tv?.textSize = data_obj.settings?.hpSize!!.toFloat()
            low_tv?.textSize = data_obj.settings?.lpSize!!.toFloat()
            low_v_tv?.textSize = data_obj.settings?.lpSize!!.toFloat()
            tv_changes?.textSize = data_obj.settings?.cpnSize!!.toFloat()
            tv_changes?.textSize = data_obj.settings?.cpnSize!!.toFloat()
            price_tv?.textSize = data_obj.settings?.pSize!!.toFloat()
//            price2_tv?.textSize = data_obj.settings?.pSize!!.toFloat()

            tv_symbol?.setTextColor(UiUtils.getColor(data_obj.settings?.s!!))
            price_tv?.setTextColor(UiUtils.getColor(data_obj.settings?.p!!))
//            price2_tv?.setTextColor(UiUtils.getColor(data_obj.settings?.bg!!))
            tv_changes?.setTextColor(UiUtils.getColor(data_obj.settings?.cp!!))
            high_tv?.setTextColor(UiUtils.getColor(data_obj.settings?.hp!!))
            high_v_tv?.setTextColor(UiUtils.getColor(data_obj.settings?.hpv!!))
            low_tv?.setTextColor(UiUtils.getColor(data_obj.settings?.lp!!))
            low_v_tv?.setTextColor(UiUtils.getColor(data_obj.settings?.lpv!!))

            ll_bg?.setBackgroundColor(UiUtils.getColor(data_obj.settings?.bg!!))

            FontUtil.setFonts(ctx,tv_symbol!!,data_obj.settings?.sFont?.label!!)
            FontUtil.setFonts(ctx,price_tv!!,data_obj.settings?.pFont?.label!!)
//            FontUtil.setFonts(ctx,price2_tv!!,data_obj.settings?.pFont?.label!!)
            FontUtil.setFonts(ctx,high_tv!!,data_obj.settings?.hpFont?.label!!)
            FontUtil.setFonts(ctx,high_v_tv!!,data_obj.settings?.hpFont?.label!!)

            FontUtil.setFonts(ctx,low_tv!!,data_obj.settings?.lpFont?.label!!)
            FontUtil.setFonts(ctx,low_v_tv!!,data_obj.settings?.lpFont?.label!!)

            FontUtil.setFonts(ctx,tv_changes!!,data_obj.settings?.cpnFont?.label!!)
        }

        private fun getPrice(price: String?): String {
            if(price!!.contains("-")) return price!!
            else return "+$price"
        }

        // multiple stock -crawling

        val mHandler: Handler = Handler(Looper.getMainLooper())
        var pixelsToMove = 50
        lateinit var recyclerView: RecyclerView

        val SCROLLING_RUNNABLE: Runnable = object : Runnable {
            override fun run() {
                recyclerView.smoothScrollBy(pixelsToMove, 0)
                mHandler.postDelayed(this, 100)
            }
        }

        fun getWidgetStockCrowling(ctx: Context,item: Item, data: String): View {

            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_rss, null)
            var data_obj = Gson().fromJson(data, StockMultipleDataNSetting::class.java)

            var crowling_text = view.findViewById<TextView>(R.id.tv_rss_fragment_crawl)
            var llMainRss = view.findViewById<LinearLayout>(R.id.ll_main_rss)
            recyclerView = view.findViewById<RecyclerView>(R.id.rv_news_crawling)
            var rl_parent_rv = view.findViewById<RelativeLayout>(R.id.rl_parent_newsrv)


            if(data_obj.settings!!.fontSizeOpt.equals("a")) {
                crowling_text.textSize = (item.frame_h/2.5).toFloat()
            } else crowling_text.textSize = data_obj.settings!!.fontSize!!.toFloat()


            view.findViewById<LinearLayout>(R.id.ll_rv_newsover).setOnClickListener {  }
            llMainRss.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity( data_obj.settings?.bg!!,data_obj.settings?.bga.toString())))

            var scroll_list = mutableListOf<DataMS>()
            for (i in 0..500){
                for (i in 0..data_obj.data.size-1)
                    scroll_list.add(data_obj.data[i])
                if(scroll_list.size>=500) break
            }
            crowling_text.visibility = View.GONE
            rl_parent_rv.visibility = View.VISIBLE
            addViewHorizontal(recyclerView, scroll_list, data_obj,item)
            mHandler.postDelayed(SCROLLING_RUNNABLE, 1000)

            return view
        }

        private fun addViewHorizontal(
            recyclerView: RecyclerView,
            list: MutableList<DataMS>,
            data_obj: StockMultipleDataNSetting,
            item: Item
        ) {
            var layout_manager = LinearLayoutManager(recyclerView.context,
                LinearLayoutManager.HORIZONTAL,false)
            recyclerView.layoutManager = layout_manager
            var adp = StockHorizontalScrollAdapter(list,this,recyclerView,data_obj,item)
            recyclerView.adapter = adp

            pixelsToMove = UtilitySetting.getMovingPixel(data_obj.settings?.speed!!)
            var scrollDuration = UtilitySetting.getSpeed(data_obj.settings?.speed!!)

            val linearSmoothScroller: LinearSmoothScroller =
                object : LinearSmoothScroller(recyclerView.getContext()) {
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                        return scrollDuration / recyclerView.computeVerticalScrollRange()
                    }
                }
            linearSmoothScroller.targetPosition = adp.getItemCount() - 1
            layout_manager.startSmoothScroll(linearSmoothScroller)
        }


        override fun lastItemVisibleStock(
            recyclerView: RecyclerView,
            list: MutableList<DataMS>,
            setting: StockMultipleDataNSetting,
            item: Item
        ) {
            recyclerView.removeAllViews()
            addViewHorizontal(recyclerView, list, setting,item)
        }

    }
}

class StockHorizontalScrollAdapter(
    var list: MutableList<DataMS>,
    var lister: lastItemCalledStock,
    var recyclerView: RecyclerView,
    var data_obj: StockMultipleDataNSetting,
    var item: Item
) : RecyclerView.Adapter<StockHorizontalScrollAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stock_crawling, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var setting = data_obj.settings

        holder.symbol_textView.text = list[position].symbol
        holder.name_textView.text = list[position].name+","
        holder.price_textView.text = list[position].price+","

        holder.symbol_textView.setTextColor(Color.parseColor(setting?.s))
        holder.name_textView.setTextColor(Color.parseColor(setting?.n))
        holder.price_textView.setTextColor(Color.parseColor(setting?.p))
        if(list[position].dayChange!!.toFloat()<0){
            holder.changes_textView.text = list[position].dayChange
            holder.changes_textView.setTextColor(Color.parseColor(setting?.cn))
        } else{
            holder.changes_textView.text = "+${list[position].dayChange}"
            holder.changes_textView.setTextColor(Color.parseColor(setting?.cp))
        }

        if (setting?.fontSizeOpt.equals("a")){
            var size = item.frame_h/2.5.toFloat()
            holder.symbol_textView.textSize = size
            holder.name_textView.textSize = size
            holder.price_textView.textSize = size
            holder.changes_textView.textSize = size
            val bullet_hiwi = LinearLayout.LayoutParams(size.toInt(), size.toInt())
            holder.bullet_tv.layoutParams = bullet_hiwi
        }else{
            holder.symbol_textView.textSize = setting?.fontSize!!.toFloat()
            holder.name_textView.textSize = setting?.fontSize!!.toFloat()
            holder.price_textView.textSize = setting?.fontSize!!.toFloat()
            holder.changes_textView.textSize = setting?.fontSize!!.toFloat()
            val bullet_hiwi = LinearLayout.LayoutParams(setting?.fontSize!!, setting.fontSize!!)
            holder.bullet_tv.layoutParams = bullet_hiwi
        }

        holder.bullet_tv.setBackgroundColor(Color.parseColor(setting?.b))


        FontUtil.setFonts(holder.symbol_textView.context, holder.symbol_textView, setting?.font?.label!!)
        FontUtil.setFonts(holder.symbol_textView.context, holder.name_textView, setting?.font?.label!!)
        FontUtil.setFonts(holder.symbol_textView.context, holder.price_textView, setting?.font?.label!!)
        FontUtil.setFonts(holder.symbol_textView.context, holder.changes_textView, setting?.font?.label!!)

        if (position == list.size - 1) {
            lister.lastItemVisibleStock(recyclerView, list, data_obj,item)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val bullet_tv: TextView = itemView.findViewById(R.id.tv_stockcrawling_bullet)
        val symbol_textView: TextView = itemView.findViewById(R.id.tv_stockcrawling_code)
        val name_textView: TextView = itemView.findViewById(R.id.tv_stockcrawling_name)
        val price_textView: TextView = itemView.findViewById(R.id.tv_stockcrawling_price)
        val changes_textView: TextView = itemView.findViewById(R.id.tv_stockcrawling_changes)
    }
}


interface lastItemCalledStock{

    fun lastItemVisibleStock(
        recyclerView: RecyclerView,
        list: MutableList<DataMS>,
        setting: StockMultipleDataNSetting,
        item: Item
    )
}


// setting

data class SettingsStocks (

    @SerializedName("bg"       ) var bg       : String?  = null,
    @SerializedName("bga"      ) var bga      : String?     = null,
    @SerializedName("s"        ) var s        : String?  = null,
    @SerializedName("sSize"    ) var sSize    : Int?     = null,
    @SerializedName("sFont"    ) var sFont    : SFont?   = SFont(),
    @SerializedName("n"        ) var n        : String?  = null,
    @SerializedName("nSize"    ) var nSize    : Int?     = null,
    @SerializedName("nFont"    ) var nFont    : NFont?   = NFont(),
    @SerializedName("p"        ) var p        : String?  = null,
    @SerializedName("pSize"    ) var pSize    : Int?     = null,
    @SerializedName("pFont"    ) var pFont    : PFont?   = PFont(),
    @SerializedName("cp"       ) var cp       : String?  = null,
    @SerializedName("cn"       ) var cn       : String?  = null,
    @SerializedName("cpnSize"  ) var cpnSize  : Int?     = null,
    @SerializedName("cpnFont"  ) var cpnFont  : CpnFont? = CpnFont(),
    @SerializedName("e"        ) var e        : String?  = null,
    @SerializedName("eSize"    ) var eSize    : Int?     = null,
    @SerializedName("eFont"    ) var eFont    : EFont?   = EFont(),
    @SerializedName("template" ) var template : String?  = null

)

data class SFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class NFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class PFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class CpnFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

data class EFont (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("value" ) var value : String? = null

)

