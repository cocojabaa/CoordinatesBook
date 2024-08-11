package com.example.coordinatebook.presentation.coordinates

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coordinatebook.R
import com.example.coordinatebook.databinding.CoordinatesItemBinding
import com.example.coordinatebook.domain.models.CoordinatesInfo
import com.example.coordinatebook.domain.models.Dimensions

class CoordinatesRecyclerAdapter(
    var coordinatesList: MutableList<CoordinatesInfo>,
    val listener: CoordinatesClickListener
): RecyclerView.Adapter<CoordinatesRecyclerAdapter.ViewHolder>() {
    class ViewHolder(view: View, val listener: CoordinatesClickListener): RecyclerView.ViewHolder(view) {
        val binding = CoordinatesItemBinding.bind(view)
        fun bind(coordinatesInfo: CoordinatesInfo) {
            binding.xText.text = coordinatesInfo.x.toString()
            if (coordinatesInfo.y == null) binding.yText.text = ""
            else binding.yText.text = coordinatesInfo.y.toString()
            binding.zText.text = coordinatesInfo.z.toString()
            binding.descriptionText.text = coordinatesInfo.description

            when (coordinatesInfo.dimension) {
                Dimensions.UpperWorld -> {binding.dimensionImage.setBackgroundResource(R.drawable.upper_world_sign) }
                Dimensions.Nether -> {binding.dimensionImage.setBackgroundResource(R.drawable.nether_sign) }
                Dimensions.End -> {binding.dimensionImage.setBackgroundResource(R.drawable.end_sign) }
            }

            binding.card.setOnLongClickListener {
                listener.onCoordinatesLongClick(coordinatesInfo)
                true
            }

            binding.card.setOnClickListener {
                listener.onCoordinatesClick(coordinatesInfo)
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

    @SuppressLint("NotifyDataSetChanged")
    fun addCoordinates(coordinatesInfo: CoordinatesInfo) {
        coordinatesList.add(coordinatesInfo)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteCoordinates(coordinatesInfo: CoordinatesInfo) {
        coordinatesList.remove(coordinatesInfo)
        notifyDataSetChanged()
    }
}