package com.riski.greenadvisor.ui.shop.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.response.DataItem
import com.riski.greenadvisor.databinding.ItemShopBinding
import com.riski.greenadvisor.ui.detail.detailshop.DetailShopActivity
import com.riski.greenadvisor.utils.DiffUtilsShop

class ShopAdapter: RecyclerView.Adapter<ShopAdapter.ShopView>() {

    private val list = ArrayList<DataItem>()

    fun setShopData(shopResponseList: List<DataItem>) {
        val diff = DiffUtilsShop(this.list, shopResponseList)
        val diffResult = DiffUtil.calculateDiff(diff)

        this.list.clear()
        this.list.addAll(shopResponseList)
        diffResult.dispatchUpdatesTo(this)
        Log.d("ShopAdapter", "Data size: ${shopResponseList.size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopView {
        val view = ItemShopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopView(view)
    }

    override fun onBindViewHolder(holder: ShopView, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ShopView(private var binding: ItemShopBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val shopPlant = list[adapterPosition]
                val intent = Intent(itemView.context, DetailShopActivity::class.java)
                intent.putExtra(DetailShopActivity.EXTRA_SHOP, shopPlant)
                it.context.startActivity(intent)
            }
        }
        @SuppressLint("SetTextI18n")
        fun bind(shop: DataItem) {
            with(binding){
            binding.itemShopName.text = shop.namaBarang
            binding.itemShopHarga.text = binding.root.context.getString(R.string.price) + " " + shop.harga
            Glide.with(itemShopImg)
                .load(shop.foto)
                .circleCrop()
                .into(binding.itemShopImg)

        }}
    }
}