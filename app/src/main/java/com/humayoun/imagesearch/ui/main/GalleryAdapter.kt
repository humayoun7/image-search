package com.humayoun.imagesearch.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.humayoun.imagesearch.R
import com.humayoun.imagesearch.data.models.GalleryItem


class GalleryAdapter(
    private val context: Context,
    private val onClick: OnClick
    ): PagingDataAdapter<GalleryItem, GalleryAdapter.ViewHolder>(ImageItem_COMPARATOR) {

    class ViewHolder (view: View): RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvImageName)
        val ivImage: ImageView = view.findViewById(R.id.ivImageItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.image_grid_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageItem = getItem(position)

        imageItem?.let {
            holder.tvName.text = imageItem.name
            Glide.with(context)
                .load(imageItem.thumbnailUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivImage)

            holder.itemView.setOnClickListener { onClick.onItemClick(imageItem) }
        }
    }

    interface OnClick {
        fun onItemClick(item: GalleryItem)
    }

    companion object {
        private val ImageItem_COMPARATOR = object : DiffUtil.ItemCallback<GalleryItem>() {
            override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
                return  oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean {
                return  oldItem.id == newItem.id
            }

        }
    }
}