package com.riski.greenadvisor.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.riski.greenadvisor.R

class PlantCareAdapter(private val plantCareList: List<Int>) : RecyclerView.Adapter<PlantCareAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plant_care, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plantCareImage = plantCareList[position]
        holder.bind(plantCareImage)
    }

    override fun getItemCount(): Int {
        return plantCareList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val plantCareImageView: ImageView = itemView.findViewById(R.id.item_plant_care_image)

        fun bind(imageRes: Int) {
            plantCareImageView.setImageResource(imageRes)
        }
    }
}