package com.georgcantor.wallpaperapp.ui.fragment.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.georgcantor.wallpaperapp.R
import com.georgcantor.wallpaperapp.model.local.Favorite
import com.georgcantor.wallpaperapp.util.loadImage

class FavoritesAdapter(
    private val favorites: List<Favorite>,
    private val clickListener: (Favorite) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FavoritesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_picture, parent, false)
    )

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val favorite = favorites[position]
        with(holder) {
            itemView.context.loadImage(favorite.url, image, null, R.color.gray)
            itemView.setOnClickListener { clickListener(favorite) }
        }
    }

    override fun getItemCount() = favorites.size

    class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)
    }
}