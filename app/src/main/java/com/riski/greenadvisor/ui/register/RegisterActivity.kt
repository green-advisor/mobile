package com.riski.greenadvisor.ui.register

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
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

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)
        supportActionBar?.hide()

        regisViewModel()
        editText()
        buttonEnabled()
        btnRegister()
        progressBar()
        readyAccount()
        changeLanguage()
    }

    private fun regisViewModel() {
        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory(Preference.getInstance(dataStore), this))[RegisterViewModel::class.java]
    }


    private fun readyAccount() {
        binding.registerReady.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun buttonEnabled() {
        binding.registerBtn.isEnabled = binding.registerEmailInput.text.toString().isNotEmpty() && binding.registerPasswordInput.text.toString().isNotEmpty() && binding.registerConfirmPassword.text.toString().isNotEmpty()
                && binding.registerPasswordInput.text.toString().length >= 8 && binding.registerConfirmPassword.text.toString().length >=8 && mailValid(binding.registerEmailInput.text.toString())
    }

    private fun changeLanguage() {
        binding.registerLanguageFab.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
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
            blur()

            registerViewModel.register(name, email, password, cPassword)
            registerViewModel.response.observe(this@RegisterActivity) { register->
                if (register != null) {
                    if (register.success) {
                        progressBar()
                        binding.registerSuccess.visibility = View.VISIBLE
                        binding.registerSuccess.playAnimation()
                        Handler(Looper.getMainLooper()).postDelayed({
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        },2000L)
                        Toast.makeText(this@RegisterActivity, getString(R.string.regis_success), Toast.LENGTH_SHORT).show()
                    } else {
                        noBlur()
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
                    registerBtn.visibility = View.GONE
                } else {
                    registerLoading.visibility = View.GONE
                    registerBtn.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun blur() {
        binding.registerImage.alpha = 0.3f
        binding.registerLogin.alpha = 0.3f
        binding.registerLogin1.alpha = 0.3f
        binding.registerName.alpha = 0.3f
        binding.registerNameInput.alpha = 0.3f
        binding.registerEmail.alpha = 0.3f
        binding.registerEmailInput.alpha = 0.3f
        binding.registerPassword.alpha = 0.3f
        binding.registerPasswordInput.alpha = 0.3f
        binding.registerConfirmPasswordText.alpha = 0.3f
        binding.registerConfirmPassword.alpha = 0.3f
        binding.registerReady.alpha = 0.3f
        binding.registerBtn.alpha = 0.3f
    }

    private fun noBlur() {
            binding.registerImage.alpha = 1f
            binding.registerLogin.alpha = 1f
            binding.registerLogin1.alpha = 1f
            binding.registerName.alpha = 1f
            binding.registerNameInput.alpha = 1f
            binding.registerEmail.alpha = 1f
            binding.registerEmailInput.alpha = 1f
            binding.registerPassword.alpha = 1f
            binding.registerPasswordInput.alpha = 1f
            binding.registerConfirmPasswordText.alpha = 1f
            binding.registerConfirmPassword.alpha = 1f
            binding.registerReady.alpha = 1f
            binding.registerBtn.alpha = 1f
    }
}