package com.example.coordinatebook.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coordinatebook.R
import com.example.coordinatebook.databinding.WorldBinding
import com.example.coordinatebook.domain.models.WorldInfo


class WorldsRecyclerAdapter(val worlds: MutableList<WorldInfo>, val listener: WorldClickListener): RecyclerView.Adapter<WorldsRecyclerAdapter.WorldHolder>() {
    class WorldHolder(view: View, val listener: WorldClickListener): RecyclerView.ViewHolder(view) {
        var binding = WorldBinding.bind(view)
        fun bind(worldInfo: WorldInfo) {
            binding.name.text = worldInfo.name
            binding.description.text = worldInfo.description
            binding.card.setOnLongClickListener {
                listener.onWorldClick(worldInfo)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.world, parent, false)
        return WorldHolder(view, listener)
    }

    override fun onBindViewHolder(holder: WorldHolder, position: Int) {
        holder.bind(worlds[position])
    }

    override fun getItemCount(): Int {
        return worlds.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addWorld(worldInfo: WorldInfo) {
        worlds.add(0, worldInfo)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteWorld(worldInfo: WorldInfo) {
        worlds.remove(worldInfo)
        notifyDataSetChanged()
    }
}