package com.riski.greenadvisor.ui.user

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.riski.greenadvisor.BetaActivity
import com.riski.greenadvisor.databinding.FragmentUserBinding

@Suppress("DEPRECATION")
class UserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        binding=  FragmentUserBinding.inflate(inflater, container,false)
        (activity as AppCompatActivity).supportActionBar?.show()
        val rootView = binding.root
        settingProfile()
        settingPrivacy()
        termsCondition()
        setPolicy()
        changeLanguage()

        return rootView
    }

    private fun settingProfile() {
        binding.userSettingProfil.setOnClickListener {
            startActivity(Intent(requireContext(), BetaActivity::class.java))
        }
    }

    private fun settingPrivacy() {
        binding.userSettingSafety.setOnClickListener {
            startActivity(Intent(requireContext(), BetaActivity::class.java))
        }
    }

    private fun changeLanguage() {
        binding.userLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun termsCondition() {
        binding.userKebijakan.setOnClickListener {
            startActivity(Intent(requireContext(), BetaActivity::class.java))
        }
    }

    private fun setPolicy() {
        binding.userSyarat.setOnClickListener {
            startActivity(Intent(requireContext(), BetaActivity::class.java))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }
}