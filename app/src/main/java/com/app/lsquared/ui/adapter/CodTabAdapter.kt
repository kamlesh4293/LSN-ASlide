package com.app.lsquared.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lsquared.R
import com.app.lsquared.databinding.ItemCodTabBinding
import com.app.lsquared.model.CodItem
import com.app.lsquared.model.Content
import com.app.lsquared.utils.*


class CodTabAdapter(var list: MutableList<CodItem>, private val listener: (CodItem, Int) -> Unit) : RecyclerView.Adapter<CodTabAdapter.ViewHolder>(){

    lateinit var binding: ItemCodTabBinding
    var selected_pos = 0

    fun setPosition(pos: Int){
        selected_pos = pos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemCodTabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position],position,selected_pos)
        holder.itemView.setOnClickListener {
            listener(list[position],position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(var itemBinding: ItemCodTabBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(item: CodItem, position: Int, selected_pos: Int) {
            var textview = itemBinding.tvCodhoriTabtext
            textview.text = item.name
            if(selected_pos==position)
                textview.setTextColor(textview.context.resources.getColor(R.color.white))
            else
                textview.setTextColor(textview.context.resources.getColor(R.color.tab_inactive_color))
        }
    }

}