package com.example.coordinatebook.presentation.worlds

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coordinatebook.R
import com.example.coordinatebook.databinding.WorldItemBinding
import com.example.coordinatebook.domain.models.WorldInfo


class WorldsRecyclerAdapter(val worldsList: MutableList<WorldInfo>, val listener: WorldClickListener): RecyclerView.Adapter<WorldsRecyclerAdapter.WorldHolder>() {
    class WorldHolder(view: View, val listener: WorldClickListener): RecyclerView.ViewHolder(view) {
        var binding = WorldItemBinding.bind(view)
        fun bind(worldInfo: WorldInfo) {
            binding.name.text = worldInfo.name
            binding.description.text = worldInfo.description
            binding.card.setOnLongClickListener {
                listener.onWorldLongClick(worldInfo)
                true
            }
            binding.card.setOnClickListener {
                listener.onWorldClick(worldInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.world_item, parent, false)
        return WorldHolder(view, listener)
    }

    override fun onBindViewHolder(holder: WorldHolder, position: Int) {
        holder.bind(worldsList[position])
    }

    override fun getItemCount(): Int {
        return worldsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addWorld(worldInfo: WorldInfo) {
        worldsList.add(0, worldInfo)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteWorld(worldInfo: WorldInfo) {
        worldsList.remove(worldInfo)
        notifyDataSetChanged()
    }
}