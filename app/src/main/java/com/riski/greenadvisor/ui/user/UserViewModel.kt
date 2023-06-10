package com.riski.greenadvisor.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riski.greenadvisor.data.Preference
import kotlinx.coroutines.launch

class UserViewModel(private val preference: Preference) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            preference.userLogout()
        }
    }
}