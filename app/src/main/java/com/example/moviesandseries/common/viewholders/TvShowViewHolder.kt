package com.example.moviesandseries.common.viewholders

import android.content.res.Resources
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesandseries.R
import com.example.moviesandseries.common.constants.Constants
import com.example.moviesandseries.common.enums.ShowType
import com.example.moviesandseries.common.hide
import com.example.moviesandseries.common.models.TvShow
import com.example.moviesandseries.common.show
import com.example.moviesandseries.databinding.TvshowListItemLayoutBinding

class TvShowViewHolder(
    private val resources: Resources,
    private val binding: TvshowListItemLayoutBinding,
    private val openDetails: (TvShow) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(tvShow: TvShow) {
        with(binding) {
            binding.container.setOnClickListener { openDetails.invoke(tvShow) }
            setupType(tvShow.mediaType)
            title.text = tvShow.title
            loadImage(tvShow)
        }
    }

    private fun TvshowListItemLayoutBinding.loadImage(tvShow: TvShow) {
        poster.scaleType = if (tvShow.posterImage.isEmpty()) {
            ImageView.ScaleType.FIT_CENTER
        } else {
            ImageView.ScaleType.CENTER_CROP
        }
        Glide.with(itemView.context).load(
            if (tvShow.posterImage.isEmpty()) ResourcesCompat.getDrawable(resources, R.drawable.ic_no_photo, null)
            else Constants.BASE_IMAGE_URL + tvShow.posterImage
        ).into(poster)
    }

    private fun TvshowListItemLayoutBinding.setupType(type: ShowType) {
        if (type == ShowType.UNDEFINED) {
            typeContainer.hide()
            return
        }

        if (type == ShowType.SERIES) {
            typeText.setTextColor(ResourcesCompat.getColor(resources, R.color.marsDark, null))
            typeText.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.white, null))
        }
        typeText.text = resources.getString(type.typeText)
        typeContainer.show()
    }
}