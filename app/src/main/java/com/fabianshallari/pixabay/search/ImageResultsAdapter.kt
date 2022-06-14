package com.fabianshallari.pixabay.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fabianshallari.pixabay.R
import com.fabianshallari.pixabay.api.PixabayImage
import com.fabianshallari.pixabay.databinding.ImageCardBinding

class ImageResultsAdapter(private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<ImageCardViewHolder>() {
    private val items = mutableListOf<PixabayImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageCardViewHolder =
        ImageCardViewHolder.create(parent)

    override fun onBindViewHolder(holder: ImageCardViewHolder, position: Int) =
        holder.bind(items[position], itemClickListener)

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun update(newItems: List<PixabayImage>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long = items[position].id
}

class ImageCardViewHolder(private val binding: ImageCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(pixabayImage: PixabayImage, itemClickListener: ItemClickListener) = with(binding) {
        root.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClicked(position)
            }
        }
        image.load(pixabayImage.previewURL)
        userName.text = root.context.getString(R.string.uploaded_by, pixabayImage.user)
        tags.text = root.context.getString(R.string.tags, pixabayImage.tags)
    }

    companion object {
        fun create(parent: ViewGroup): ImageCardViewHolder {
            val binding =
                ImageCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ImageCardViewHolder(binding)
        }
    }

}

