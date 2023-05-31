package com.riski.greenadvisor.ui.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riski.greenadvisor.data.greetings.ArticlesData
import com.riski.greenadvisor.databinding.ItemArticlesBinding
import com.riski.greenadvisor.ui.detail.detailarticles.DetailArticlesActivity

class ArticlesAdapter(private val articlesList: ArrayList<ArticlesData>) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articlesList[position])
    }

    override fun getItemCount(): Int = articlesList.size

    inner class ViewHolder(private var binding: ItemArticlesBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val articles = articlesList[adapterPosition]
                val intent = Intent(itemView.context, DetailArticlesActivity::class.java)
                intent.putExtra(DetailArticlesActivity.EXTRA_ARTICLES, articles)
                it.context.startActivity(intent)
            }
        }

        fun bind(articles: ArticlesData) {
            with(binding) {
                itemArticlesJudul.text = articles.judul
                itemArticlesDeskripsi.text = articles.deskripsi
                itemArticlesTahun.text = articles.tahun
                itemArticlesCopyright.text = articles.copyRight
                Glide.with(itemArticlesImage)
                    .load(articles.imageId)
                    .centerCrop()
                    .into(itemArticlesImage)
            }
        }
    }

}