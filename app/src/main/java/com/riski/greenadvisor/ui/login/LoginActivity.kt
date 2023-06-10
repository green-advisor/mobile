package com.riski.greenadvisor.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.riski.greenadvisor.MainActivity
import com.riski.greenadvisor.data.Preference
import com.riski.greenadvisor.R
import com.riski.greenadvisor.databinding.ActivityLoginBinding
import com.riski.greenadvisor.ui.register.RegisterActivity
import com.riski.greenadvisor.utils.mailValid

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("GreetingsPrefs", Context.MODE_PRIVATE)

        loginUserViewModel()
        toRegister()
        btnLogin()
        btnEnabled()
        editText()
        progressBar()

    }


    private fun btnEnabled() {
        val resultMail = binding.loginEmail.text
        val resultPass = binding.loginPassword.text

        binding.loginButton.isEnabled = resultPass != null && resultMail != null && mailValid(binding.loginEmail.text.toString()) && binding.loginPassword.text.toString().length >= 8
    }

    private fun loginUserViewModel() {
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(Preference.getInstance(dataStore), this))[LoginViewModel::class.java]
    }

    private fun editText() {
        binding.loginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnEnabled()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.loginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnEnabled()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun btnLogin() {
        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            loginViewModel.login(email, password)
            loginViewModel.response.observe(this@LoginActivity) { login->
                if (login != null) {
                    if (login.success) {
                       progressBar()
                       Toast.makeText(this@LoginActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        progressBar()
                        Toast.makeText(this@LoginActivity, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun toRegister() {
        binding.loginHave.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun progressBar() {
        loginViewModel.loading.observe(this) {
            binding.apply {
                if (it) {
                    loginLoading.visibility = View.VISIBLE
                    loginButton.visibility = View.GONE
                } else {
                    loginLoading.visibility = View.GONE
                    loginButton.visibility = View.VISIBLE
                }
            }
        }
    }
}