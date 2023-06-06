package com.app.lsquared.ui.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.model.widget.RssItem
import com.app.lsquared.ui.UiUtils
import com.app.lsquared.ui.widgets.NewsListSettingData
import com.app.lsquared.utils.FontUtil
import com.google.gson.Gson

class NewsAdapter(var list: List<RssItem>,var item: Item,var ctx: Context,var setting : NewsListSettingData) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    var last_index = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
        return NewsViewHolder(view)
    }

    fun updateAdapter(newlist: List<RssItem>){
        list = newlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        last_index = position
        Log.d("TAG", "onBindViewHolder: position- $last_index")

        // setting
        var setting_obj = Gson().fromJson(item.settings, NewsListSettingData::class.java)

        holder.title_tv.setText(list[position].title)
        holder.desc_tv.setText(list[position].desc)

        holder.title_tv.textSize = setting_obj.titleSize!!.toFloat()
        holder.desc_tv.textSize = setting_obj.descSize!!.toFloat()

        if(position%2 ==0){
            holder.ll_row.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(setting_obj.rowBg!!,setting_obj.rowBga!!)))
            holder.title_tv.setTextColor(Color.parseColor(setting_obj.titleText))
            holder.desc_tv.setTextColor(Color.parseColor(setting_obj.descText))
            FontUtil.setFonts(ctx,holder.title_tv,setting?.rowFont?.label!!)
            FontUtil.setFonts(ctx,holder.desc_tv,setting?.rowFont?.label!!)
        }else{
            Log.d("TAG", "onBindViewHolder: text ${list[position].desc} - alt desc color - ${setting_obj.altDescText}")
            holder.ll_row.setBackgroundColor(Color.parseColor(UiUtils.getColorWithOpacity(setting_obj.altBg!!,setting_obj.altBga!!)))
            holder.title_tv.setTextColor(Color.parseColor(setting_obj.altTitleText))
            holder.desc_tv.setTextColor(Color.parseColor(setting_obj.altDescText))
            FontUtil.setFonts(ctx,holder.title_tv,setting?.altRowFont?.label!!)
            FontUtil.setFonts(ctx,holder.desc_tv,setting?.altRowFont?.label!!)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getlastIndex() :Int{
        return last_index
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ll_row:LinearLayout = itemView.findViewById(R.id.ll_listview_row)
        val title_tv:TextView = itemView.findViewById(R.id.tv_itemnews_title)
        val desc_tv:TextView = itemView.findViewById(R.id.tv_itemnews_descrip)
    }

}