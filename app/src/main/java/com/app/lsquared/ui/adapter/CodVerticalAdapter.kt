package com.app.lsquared.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.lsquared.databinding.ItemCodVerticalBinding
import com.app.lsquared.model.Cat
import com.app.lsquared.model.Content


class CodVerticalAdapter(var cat_list: ArrayList<Cat>,
                         private val listener: (Content, Int) -> Unit) : RecyclerView.Adapter<CodVerticalAdapter.ViewHolder>(){

    lateinit var binding: ItemCodVerticalBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemCodVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(cat_list[position])
    }

    override fun getItemCount(): Int {
        return cat_list.size
    }

    class ViewHolder(var itemBinding: ItemCodVerticalBinding,var listener: (Content, Int) -> Unit) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(item: Cat) {
            itemBinding.tvCodvertTitle.text = item.label
            itemBinding.rvCodHorizontal.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = CODHoriContentAdapter(item.content) { item, position ->
                    listener(item, position)
                }
            }

        }
    }


}