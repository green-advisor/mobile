package com.riski.greenadvisor.ui.detail.detailshop

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.response.DataItem
import com.riski.greenadvisor.databinding.ActivityDetailShopBinding

class DetailShopActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailShopBinding
    private val viewModel: DetailShopViewModel by viewModels()
    private lateinit var detailLiveData: MutableLiveData<DataItem>

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailShopBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.detail_shop_name)
            setDisplayHomeAsUpEnabled(true)
        }

        setDetailShop()
    }

    private fun setDetailShop() {
        val detail = intent.getParcelableExtra<DataItem>(EXTRA_SHOP)
        if (detail != null) {
            Glide.with(this)
                .load(detail.foto)
                .into(binding.detailShopImage)
            binding.detailShopName.text = detail.namaBarang
            binding.detailShopPrice.text = detail.harga
            binding.detailShopDeskripsi.text = detail.deskripsi

            binding.detailShopBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(detail.referensi))
                startActivity(intent)
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_SHOP = "DetailShop"
    }
}