package com.humayoun.imagesearch.ui.main

import android.content.Context
import android.media.Image
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
import com.humayoun.imagesearch.data.models.ImageItem
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.main_fragment.*


class ImageAdapter(
    private val context: Context,
    private val onClick: OnClick
    ): PagingDataAdapter<ImageItem, ImageAdapter.ViewHolder>(ImageItem_COMPARATOR) {

    class ViewHolder (view: View): RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvImageName)
        val ivImage = view.findViewById<ImageView>(R.id.ivImageItem)
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

//    override fun getItemCount(): Int {
//        return imageList.size
//    }

    interface OnClick {
        fun onItemClick(item: ImageItem)
    }

    companion object {
        private val ImageItem_COMPARATOR = object : DiffUtil.ItemCallback<ImageItem>() {
            override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                return  oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                return  oldItem.id == newItem.id
            }

        }
    }
}