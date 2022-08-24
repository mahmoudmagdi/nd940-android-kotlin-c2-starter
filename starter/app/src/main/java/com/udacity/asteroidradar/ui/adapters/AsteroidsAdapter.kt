package com.udacity.asteroidradar.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ItemViewBinding
import com.udacity.asteroidradar.model.Asteroid

class AsteroidsAdapter(private val onItemClickListener: AsteroidClickListener) :
    RecyclerView.Adapter<AsteroidsAdapter.AsteroidViewHolder>() {

    var asteroidsList = listOf<Asteroid>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: AsteroidViewHolder,
        position: Int
    ) {
        holder.bind(asteroidsList[position], onItemClickListener)
    }

    override fun getItemCount() = asteroidsList.size

    class AsteroidViewHolder(private val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid, onItemClickListener: AsteroidClickListener) {
            binding.asteroid = asteroid
            binding.clickListener = onItemClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemViewBinding.inflate(layoutInflater, parent, false)
                return AsteroidViewHolder(binding)
            }
        }
    }
}

class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}