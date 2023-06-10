package com.riski.greenadvisor.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riski.greenadvisor.data.Preference

class LoginViewModelFactory(private val preferences: Preference, private val context: Context): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}