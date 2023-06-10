package com.riski.greenadvisor.ui.user

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.riski.greenadvisor.BetaActivity
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.databinding.FragmentUserBinding
import com.riski.greenadvisor.ui.login.LoginActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
@Suppress("DEPRECATION")
class UserFragment : Fragment() {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: FragmentUserBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        binding=  FragmentUserBinding.inflate(inflater, container,false)
        (activity as AppCompatActivity).supportActionBar?.show()
        val rootView = binding.root
        userViewModel()
        settingProfile()
        settingPrivacy()
        termsCondition()
        setPolicy()
        changeLanguage()
        setLogout()

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataStore = requireContext().dataStore
    }

    private fun userViewModel() {
        userViewModel = ViewModelProvider(this, UserViewModelFactory(Preference.getInstance(dataStore),requireContext()))[UserViewModel::class.java]
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

    private fun setLogout() {
        binding.userLogout.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setMessage(getString(R.string.user_logout_ask))
                setPositiveButton(getString(R.string.user_logout_yes)) { _, _->
                    userViewModel.logout()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finishAffinity()
                    requireActivity().finish()
                }
                setNegativeButton(getString(R.string.user_logout_no)) { _, _-> }
                create()
                show()
            }
        }
    }
}