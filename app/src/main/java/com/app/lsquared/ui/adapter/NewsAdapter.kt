package com.app.lsquared.ui.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.model.news_setting.News
import com.app.lsquared.model.widget.RssItem
import com.app.lsquared.ui.widgets.NewsListSettingData
import com.app.lsquared.utils.FontUtil
import org.json.JSONObject

class NewsAdapter(var list: List<RssItem>,var item: Item,var ctx: Context,var setting : NewsListSettingData) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    var last_index = 0;


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
        Log.d("TAG", "onBindViewHolder: position- $position")

        var json_setting = JSONObject(item.settings)

        var title_size = json_setting.getInt("titleSize")
        var title_text = json_setting.getString("titleText")
        var desc_size = json_setting.getInt("descSize")
        var desc_color = json_setting.getString("descText")
        var bg_color = json_setting.getString("bg")
        var bg_alt_color = json_setting.getString("altBg")

        holder.title_tv.setText(list[position].title)
        holder.desc_tv.setText(list[position].desc)

        holder.title_tv.textSize = title_size.toFloat()
        holder.desc_tv.textSize = desc_size.toFloat()

        holder.title_tv.setTextColor(Color.parseColor(title_text))
        holder.desc_tv.setTextColor(Color.parseColor(desc_color))

        if(position%2 ==0){
            holder.title_tv.setBackgroundColor(Color.parseColor(bg_color.toString()))
            holder.desc_tv.setBackgroundColor(Color.parseColor(bg_color.toString()))
            FontUtil.setFonts(ctx,holder.title_tv,setting?.rowFont?.label!!)
            FontUtil.setFonts(ctx,holder.desc_tv,setting?.rowFont?.label!!)
        }else{
            holder.title_tv.setBackgroundColor(Color.parseColor(bg_alt_color.toString()))
            holder.desc_tv.setBackgroundColor(Color.parseColor(bg_alt_color.toString()))
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

        val title_tv:TextView = itemView.findViewById(R.id.tv_itemnews_title)
        val desc_tv:TextView = itemView.findViewById(R.id.tv_itemnews_descrip)
    }

}