package com.rxnetwork.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item.view.*

/**
 * by y on 2017/2/27
 */

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainAdapterHolder>() {

    private var listModels: MutableList<ListModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MainAdapterHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))

    override fun onBindViewHolder(holder: MainAdapterHolder, position: Int) {
        Glide
                .with(holder.itemView.list_image.context)
                .load(listModels[position].titleImage)
                .into(holder.itemView.list_image)
        holder.itemView.list_tv.text = listModels[position].title
    }

    override fun getItemCount(): Int = listModels.size

    fun addAll(data: List<ListModel>) {
        listModels.addAll(data)
        notifyDataSetChanged()
    }

    fun clear() {
        listModels.clear()
        notifyDataSetChanged()
    }

    class MainAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
