@file:Suppress("DEPRECATION")

package com.riski.greenadvisor.ui.greetings

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.greetings.GreetingsData
import com.riski.greenadvisor.ui.login.LoginActivity

class GreetingsViewModel(application: Application) : AndroidViewModel(application) {
    private val _imageIndex = MutableLiveData<List<GreetingsData>>()
    val imageIndex: LiveData<List<GreetingsData>>
    get() = _imageIndex

    init {
        val greetings = listOf(
            GreetingsData(R.drawable.greetings1, R.string.greetings1, false, R.dimen.text_size_image1),
            GreetingsData(R.drawable.greetings2, R.string.greetings2, true, R.dimen.text_size_image2)
        )
        _imageIndex.value = greetings
    }

    fun onJoinButtonClick(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val joinStatus = sharedPreferences.getBoolean("JOIN_STATUS", false)

        if (!joinStatus) {
            setJoinStatus(context, true)
        } else {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            (context as Activity).finish()
        }
    }

    private fun setJoinStatus(context: Context, status: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putBoolean("JOIN_STATUS", status)
        editor.apply()
    }
}
