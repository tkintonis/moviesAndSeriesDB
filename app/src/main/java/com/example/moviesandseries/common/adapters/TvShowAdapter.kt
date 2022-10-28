package com.example.moviesandseries.common.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesandseries.common.models.TvShow
import com.example.moviesandseries.databinding.TvshowListItemLayoutBinding
import com.example.moviesandseries.common.viewholders.TvShowViewHolder

class TvShowAdapter(
    private val resources: Resources,
    private val openDetails: (TvShow) -> Unit
) : RecyclerView.Adapter<TvShowViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<TvShow>() {
        override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        val binding = TvshowListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowViewHolder(resources, binding, openDetails)
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        holder.bindData(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size
}