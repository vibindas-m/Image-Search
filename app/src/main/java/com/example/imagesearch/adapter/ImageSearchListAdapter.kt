package com.example.imagesearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearch.R
import com.example.imagesearch.domain.model.ImageSearchModel
import com.example.imagesearch.ui.extension.loadImageUrl

class ImageSearchListAdapter : RecyclerView.Adapter<ImageSearchListAdapter.ImageListHolder>() {

    lateinit var listener: OnItemClickListener
    private lateinit var items: List<ImageSearchModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.image_search_list_item, parent, false)
        return ImageListHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ImageListHolder, position: Int) {
        val searchImageItem = items[position]

        searchImageItem.thumbnail?.let {
            holder.imageView.loadImageUrl(it)
        }
        holder.imageView.setOnClickListener {
            listener.onClick(it, searchImageItem)
        }
    }

    fun setData(list: List<ImageSearchModel>) {
        this.items = list
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(view: View, data: ImageSearchModel)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class ImageListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
    }
}




