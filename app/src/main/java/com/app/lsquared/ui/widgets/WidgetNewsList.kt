package com.app.lsquared.ui.widgets

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.lsquared.R
import com.app.lsquared.model.Item
import com.app.lsquared.model.news_setting.News
import com.app.lsquared.model.news_setting.NewsListSettingData
import com.app.lsquared.model.widget.RssItem
import com.app.lsquared.ui.adapter.BeingNewsAdapter
import com.app.lsquared.ui.adapter.NewsAdapter
import com.google.gson.Gson

class WidgetNewsList {

    companion object{

        fun getWidgetNewsListAll(
            ctx: Context,
            item: Item,
            list: List<RssItem>,
            adapter: NewsAdapter
        ): View {

            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_rss,null)

            var title_tv = view.findViewById<TextView>(R.id.tv_rss_title)
            var list_rv = view.findViewById<RecyclerView>(R.id.rv_rss_fragment)

            list_rv.visibility = View.VISIBLE
            val myLinearLayoutManager = object : LinearLayoutManager(ctx) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            var adapter = NewsAdapter(list,item,ctx)
            list_rv.layoutManager = myLinearLayoutManager
            list_rv.adapter = adapter
            list_rv.isNestedScrollingEnabled = false

            // title text
            title_tv.text = ""

            // setting
            var setting_obj = Gson().fromJson(item.settings,
                NewsListSettingData::class.java)
            title_tv.visibility = View.VISIBLE
            title_tv.textSize = setting_obj.headerSize!!.toFloat()

            return view
        }

        fun getWidgetNewsListBeing(
            ctx: Context,
            item: Item,
            list: ArrayList<News>,
            adapter: BeingNewsAdapter
        ): View {
            var view = (ctx as Activity).layoutInflater.inflate(R.layout.fragment_rss,null)

            var list_rv = view.findViewById<RecyclerView>(R.id.rv_rss_fragment)
            list_rv.visibility = View.VISIBLE
            val myLinearLayoutManager = object : LinearLayoutManager(ctx) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
//            var adapter = BeingNewsAdapter(list,item,ctx)
            list_rv.layoutManager = myLinearLayoutManager
            list_rv.adapter = adapter
            list_rv.isNestedScrollingEnabled = false

            return view
        }
    }
}