package com.riski.greenadvisor.ui.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.riski.greenadvisor.R
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.databinding.ActivityRegisterBinding
import com.riski.greenadvisor.ui.login.LoginActivity
import com.riski.greenadvisor.utils.mailValid

class RegisterActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    private lateinit var binding : ActivityRegisterBinding

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        regisViewModel()
        editText()
        buttonEnabled()
        btnRegister()
        progressBar()
        readyAccount()
    }

    private fun regisViewModel() {
        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory(Preference.getInstance(dataStore), this))[RegisterViewModel::class.java]
    }


    private fun readyAccount() {
        binding.registerReady.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun buttonEnabled() {
        binding.registerBtn.isEnabled = binding.registerEmailInput.text.toString().isNotEmpty() && binding.registerPasswordInput.text.toString().isNotEmpty() && binding.registerConfirmPassword.text.toString().isNotEmpty()
                && binding.registerPasswordInput.text.toString().length >= 8 && binding.registerConfirmPassword.text.toString().length >=8 && mailValid(binding.registerEmailInput.text.toString())
    }

    private fun editText() {
        binding.registerEmailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonEnabled()
            }
        })

        binding.registerPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonEnabled()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.registerConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonEnabled()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun btnRegister() {
        binding.registerBtn.setOnClickListener {
            val name = binding.registerNameInput.text.toString()
            val email = binding.registerEmailInput.text.toString()
            val password = binding.registerPasswordInput.text.toString()
            val cPassword = binding.registerConfirmPassword.text.toString()

            registerViewModel.register(name, email, password, cPassword)
            registerViewModel.response.observe(this@RegisterActivity) { register->
                if (register != null) {
                    if (register.success) {
                        progressBar()
                        Toast.makeText(this@RegisterActivity, getString(R.string.regis_success), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        progressBar()
                        Toast.makeText(this@RegisterActivity, getString(R.string.regis_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun progressBar() {
        registerViewModel.loading.observe(this) {
            binding.apply {
                if (it) {
                    registerLoading.visibility = View.VISIBLE
                } else {
                    registerLoading.visibility = View.GONE
                }
            }
        }
    }
}