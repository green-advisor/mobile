package com.riski.greenadvisor.ui.detail.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riski.greenadvisor.data.greetings.ArticlesData
import com.riski.greenadvisor.databinding.ItemArticlesBinding
import com.riski.greenadvisor.ui.detail.detailarticles.DetailArticlesActivity
import com.riski.greenadvisor.utils.DiffUtilsArticles

class DetailShowListArticlesAdaptor(private val articlesList: ArrayList<ArticlesData>): RecyclerView.Adapter<DetailShowListArticlesAdaptor.ViewHolder>() {

    fun setListDetailArticles(itemList: List<ArticlesData>) {
        val diffUtils = DiffUtilsArticles(this.articlesList, itemList)
        val diffResult = DiffUtil.calculateDiff(diffUtils)

        this.articlesList.clear()
        this.articlesList.addAll(itemList)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemArticlesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int = articlesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articlesList[position])
        }

    @SuppressLint("SuspiciousIndentation")
    inner class ViewHolder(private var binding: ItemArticlesBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val articles = articlesList[adapterPosition]
                val intent = Intent(itemView.context, DetailArticlesActivity::class.java)
                intent.putExtra(DetailArticlesActivity.EXTRA_ARTICLES, articles)
                val itemArticlesImage: ImageView = binding.itemArticlesImage
                val itemArticlesJudul: TextView = binding.itemArticlesJudul
                val itemArticlesDeskripsi: TextView = binding.itemArticlesDeskripsi

                val compat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        it.context as Activity,
                        Pair(itemArticlesImage, "articles"),
                        Pair(itemArticlesJudul, "judul"),
                        Pair(itemArticlesDeskripsi, "deskripsi")
                    )
                    it.context.startActivity(intent, compat.toBundle())
                }
        }
        fun bind(articles: ArticlesData) {
            with(binding) {
                itemArticlesJudul.text = articles.judul
                itemArticlesDeskripsi.text = articles.deskripsi
                itemArticlesCopyright.text = articles.copyRight
                Glide.with(itemView)
                    .load(articles.imageId)
                    .centerCrop()
                    .into(itemArticlesImage)
            }
        }
    }
}