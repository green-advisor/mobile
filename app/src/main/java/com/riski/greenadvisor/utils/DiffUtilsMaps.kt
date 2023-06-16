package com.riski.greenadvisor.utils

import androidx.recyclerview.widget.DiffUtil
import com.riski.greenadvisor.data.response.DataItemMaps

class DiffUtilsMaps(private val mOldList: List<DataItemMaps>, private val mNewList: List<DataItemMaps>) : DiffUtil.Callback() {
    override fun getOldListSize() = mOldList.size
    override fun getNewListSize() = mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        mOldList[oldItemPosition].namaTanaman == mNewList[newItemPosition].namaTanaman

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = mOldList[oldItemPosition]
        val newItem = mNewList[newItemPosition]
        return oldItem.namaTanaman == newItem.namaTanaman
    }
}