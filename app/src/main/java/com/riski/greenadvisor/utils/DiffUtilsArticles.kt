package com.riski.greenadvisor.utils

import androidx.recyclerview.widget.DiffUtil
import com.riski.greenadvisor.data.greetings.ArticlesData

class DiffUtilsArticles(private val mOldList: List<ArticlesData>, private val mNewList: List<ArticlesData>) : DiffUtil.Callback() {
    override fun getOldListSize() = mOldList.size
    override fun getNewListSize() = mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        mOldList[oldItemPosition].judul == mNewList[newItemPosition].judul

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = mOldList[oldItemPosition]
        val newItem = mNewList[newItemPosition]
        return oldItem.judul == newItem.judul
    }
}