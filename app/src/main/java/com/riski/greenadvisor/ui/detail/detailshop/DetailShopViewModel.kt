package com.riski.greenadvisor.ui.detail.detailshop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riski.greenadvisor.data.response.DataItem

class DetailShopViewModel: ViewModel() {
    private val _detailShop: MutableLiveData<DataItem> = MutableLiveData()
    val detailShop: LiveData<DataItem> = _detailShop

    fun setDetail(shop: DataItem) {
        _detailShop.value = shop
    }
}