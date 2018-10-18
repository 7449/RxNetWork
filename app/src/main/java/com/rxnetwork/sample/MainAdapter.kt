package com.rxnetwork.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * by y on 2017/2/27
 */

internal class MainAdapter(listModels: MutableList<ListModel>) : RecyclerView.Adapter<MainAdapter.MainAdapterHolder>() {

    private var listModels: MutableList<ListModel>? = null

    init {
        this.listModels = listModels
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapterHolder =
            MainAdapterHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))

    override fun onBindViewHolder(holder: MainAdapterHolder, position: Int) {
        if (listModels == null) {
            return
        }
        Glide
                .with(holder.imageView.context)
                .load(listModels!![position].titleImage)
                .into(holder.imageView)
        holder.textView.text = listModels!![position].title
    }

    override fun getItemCount(): Int = listModels?.size ?: 0

    fun addAll(data: List<ListModel>) {
        if (listModels != null) {
            listModels!!.addAll(data)
            notifyDataSetChanged()
        }
    }

    fun clear() {
        if (listModels != null) {
            listModels!!.clear()
            notifyDataSetChanged()
        }
    }

    internal inner class MainAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: AppCompatImageView = itemView.findViewById(R.id.list_image) as AppCompatImageView
        val textView: AppCompatTextView = itemView.findViewById(R.id.list_tv) as AppCompatTextView

    }
}
