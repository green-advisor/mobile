package com.riski.greenadvisor.ui.detail.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riski.greenadvisor.data.response.DataItemMaps
import com.riski.greenadvisor.databinding.ItemListMapBinding
import com.riski.greenadvisor.utils.DiffUtilsMaps

class DetailListMapPlantAdaptor: RecyclerView.Adapter<DetailListMapPlantAdaptor.MapsView>() {

    private val listMaps = ArrayList<DataItemMaps>()

    fun setMapsData(mapsResponseList: List<DataItemMaps>) {
        val diff = DiffUtilsMaps(this.listMaps, mapsResponseList)
        val diffResult = DiffUtil.calculateDiff(diff)

        this.listMaps.clear()
        this.listMaps.addAll(mapsResponseList)
        diffResult.dispatchUpdatesTo(this)
        Log.e("ShopAdapter", "onFailure: ${mapsResponseList.size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapsView {
        val view = ItemListMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MapsView(view)
    }

    override fun onBindViewHolder(holder: MapsView, position: Int) {
        holder.bind(listMaps[position])
    }

    override fun getItemCount() = listMaps.size

    inner class MapsView(private val binding: ItemListMapBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(maps: DataItemMaps) {
            with(binding) {
                Glide.with(itemMapsImage)
                    .load(maps.foto)
                    .circleCrop()
                    .into(itemMapsImage)
                itemMapName.text = maps.namaTanaman
                itemMapsIklim.text = maps.iklim
            }

        }
    }
}