package com.riski.greenadvisor.ui.detail.showlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.riski.greenadvisor.R
import com.riski.greenadvisor.databinding.ActivityDetailShowListPlantBinding

class DetailShowListPlantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailShowListPlantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailShowListPlantBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.home_plant_care)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}