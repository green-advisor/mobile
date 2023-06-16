package com.riski.greenadvisor.utils

import androidx.recyclerview.widget.DiffUtil
import com.riski.greenadvisor.data.response.DataItem

class DiffUtilsShop(private val oldList: List<DataItem>, private val NewList: List<DataItem>): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = NewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].id == NewList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItemStory = oldList[oldItemPosition]
        val newItemStory = NewList[newItemPosition]
        return oldItemStory.id == newItemStory.id
    }
}