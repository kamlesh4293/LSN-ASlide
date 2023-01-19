package com.app.lsquared.ui.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lsquared.R
import com.app.lsquared.databinding.ItemCodHorizontalBinding
import com.app.lsquared.model.Content
import com.app.lsquared.utils.*
import java.io.File


class CODHoriContentAdapter(var list: MutableList<Content>,
                            private val listener: (Content, Int) -> Unit) : RecyclerView.Adapter<CODHoriContentAdapter.ViewHolder>(){

    lateinit var binding: ItemCodHorizontalBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemCodHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position])
        holder.itemView.setOnClickListener {
            listener(list[position], position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(var itemBinding: ItemCodHorizontalBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(item: Content) {
            itemBinding.tvCodhoriLabel.text = item.label
            if(item.type.equals(Constant.CONTENT_IMAGE)|| item.type.equals(Constant.CONTENT_VIDEO)){
                val path1 = DataManager.getDirectory()+ File.separator+item.fileName?.substring(0, item.fileName!!.lastIndexOf('.'))+"_thumb.jpg"
                val path2 = DataManager.getDirectory()+ File.separator+item.fileName?.substring(0, item.fileName!!.lastIndexOf('.'))+"_thumb.png"
                if(File(path1).exists()) itemBinding.ivCodItem.setImageBitmap(BitmapFactory.decodeFile(path1,ImageUtil.getImageOption()))
                else if(File(path2).exists()) itemBinding.ivCodItem.setImageBitmap(BitmapFactory.decodeFile(path2,ImageUtil.getImageOption()))

                if(item.type.equals(Constant.CONTENT_VIDEO)){
                    itemBinding.tvCodhoriTime.visibility = View.VISIBLE
                    itemBinding.ivCodhoriVidType.visibility = View.VISIBLE
                    itemBinding.tvCodhoriTime.text = DateTimeUtil.secondsToMinutes(item.duration!!)
                }
            }
            if(item.type.equals(Constant.CONTENT_WIDGET_YOUTUBE)|| item.type.equals(Constant.CONTENT_WIDGET_VIMEO)){
                ImageUtil.loadImage(itemBinding.ivCodItem.context,item.thumb!!,itemBinding.ivCodItem)
                itemBinding.tvCodhoriTime.visibility = View.VISIBLE
                itemBinding.ivCodhoriVidType.visibility = View.VISIBLE
                itemBinding.tvCodhoriTime.text = DateTimeUtil.secondsToMinutes(item.duration!!)
                if(item.type.equals(Constant.CONTENT_WIDGET_YOUTUBE)) itemBinding.ivCodhoriVidType.setImageResource(R.drawable.youtube)
                if(item.type.equals(Constant.CONTENT_WIDGET_VIMEO)) itemBinding.ivCodhoriVidType.setImageResource(R.drawable.vimeo)
            }
            if(item.type.equals(Constant.CONTENT_WEB)||item.type.equals(Constant.CONTENT_WIDGET_GOOGLE)){
                itemBinding.ivCodItem.setImageResource(R.drawable.thumb_web)
            }
            if(item.type.equals(Constant.CONTENT_WIDGET_POWER)){
                itemBinding.ivCodItem.setImageResource(R.drawable.powerbi)
            }
        }
    }

}