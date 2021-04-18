package com.georgcantor.wallpaperapp.ui.fragment.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.georgcantor.wallpaperapp.R
import com.georgcantor.wallpaperapp.databinding.ItemPictureBinding
import com.georgcantor.wallpaperapp.model.remote.response.CommonPic
import com.georgcantor.wallpaperapp.util.loadImage

class GalleryAdapter(
    private val clickListener: (CommonPic?) -> Unit
) : PagingDataAdapter<CommonPic, RecyclerView.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GalleryViewHolder(
        ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val picture = getItem(position)
        (holder as GalleryViewHolder).apply {
            picture?.url?.let { itemView.context.loadImage(it, binding.image, null, R.color.gray) }
            itemView.setOnClickListener { clickListener(picture) }
        }
    }

    class GalleryViewHolder(val binding: ItemPictureBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        object DiffCallback : DiffUtil.ItemCallback<CommonPic>() {
            override fun areItemsTheSame(oldItem: CommonPic, newItem: CommonPic) = oldItem == newItem

            override fun areContentsTheSame(oldItem: CommonPic, newItem: CommonPic) = oldItem.id == newItem.id
        }
    }
}