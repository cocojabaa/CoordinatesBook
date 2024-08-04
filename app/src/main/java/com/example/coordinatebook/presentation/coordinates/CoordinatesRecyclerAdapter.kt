package com.example.coordinatebook.presentation.coordinates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coordinatebook.R
import com.example.coordinatebook.databinding.CoordinatesItemBinding
import com.example.coordinatebook.domain.models.CoordinatesInfo

class CoordinatesRecyclerAdapter(
    val coordinatesList: MutableList<CoordinatesInfo>,
    val listener: CoordinatesClickListener
): RecyclerView.Adapter<CoordinatesRecyclerAdapter.ViewHolder>() {
    class ViewHolder(view: View, val listener: CoordinatesClickListener): RecyclerView.ViewHolder(view) {
        val binding = CoordinatesItemBinding.bind(view)
        fun bind(coordinatesInfo: CoordinatesInfo) {
            binding.xText.text = coordinatesInfo.x.toString()
            binding.yText.text = coordinatesInfo.y.toString()
            binding.zText.text = coordinatesInfo.z.toString()
            binding.descriptionText.text = coordinatesInfo.description

            binding.card.setOnLongClickListener {
                listener.onCoordinatesLongClick(coordinatesInfo)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coordinates_item, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(coordinatesList[position])
    }

    override fun getItemCount(): Int {
        return coordinatesList.size
    }
}