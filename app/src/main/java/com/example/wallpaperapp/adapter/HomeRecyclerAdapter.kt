package com.example.wallpaperapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.wallpaperapp.data.model.PhotoModel
import com.example.wallpaperapp.databinding.PhotoItemRowBinding

class HomeRecyclerAdapter(
    val onWallpaperClick: (photoUrl: String) -> Unit
) : PagingDataAdapter<PhotoModel, HomeRecyclerAdapter.HomeRecyclerViewHolder>(DIFF_UTIL_CALLBACK()) {

    inner class HomeRecyclerViewHolder(private val binding: PhotoItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = absoluteAdapterPosition
                val item: PhotoModel = getItem(position)!!
                onWallpaperClick(item.src.original)
            }
        }

        fun bind(photo: PhotoModel) {
            binding.apply {
                imageView.load(photo.src.large)
                photoTitle.text = photo.alt
                photographerDetails.text =
                    "Photo by ${photo.photographer} from Unsplash."
            }
        }
    }

    override fun onBindViewHolder(holder: HomeRecyclerViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val photoItemRowBinding =
            PhotoItemRowBinding.inflate(inflater, parent, false)
        return HomeRecyclerViewHolder(photoItemRowBinding)
    }

    class DIFF_UTIL_CALLBACK : DiffUtil.ItemCallback<PhotoModel>() {

        override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
            return oldItem == newItem
        }


    }

}