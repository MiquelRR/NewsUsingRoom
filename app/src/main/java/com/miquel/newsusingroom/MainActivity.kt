package com.miquel.newsusingroom

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.miquel.newsusingroom.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.miquel.newsusingroom.entities.User
import com.miquel.newsusingroom.repository.NewsApplication
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var user: User? =  null
    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = Intent(this, NewsListActivity::class.java)
        user =  null
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val loggedUserMail = preferences.getString("logged_user_mail", "")
        if (loggedUserMail != "") {
            lifecycleScope.launch {
                user = NewsApplication.database.userDao().getUserByEmail(loggedUserMail!!)
            }
            if (user != null) {
                Toast.makeText(this, "logeado como $loggedUserMail", Toast.LENGTH_SHORT).show()
                intent.putExtra("user", user)
                startActivity(intent)
                finish()
            }
        }
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this).load("https://emtstatic.com/2016/01/aniversario-20011.jpg").into(binding.background)
        binding.background.alpha = 0.3f
        binding.registerButton.setOnClickListener {
            val (email, password) = pair(binding)
            if (isValidEmail(email) && isValidPassword(password)) {
                lifecycleScope.launch {
                    user= NewsApplication.database.userDao().getUserByEmail(email)
                    if (user == null) {
                        user= User(email=email, password=password)
                        lifecycleScope.launch {
                            NewsApplication.database.userDao().addUser(user!!)
                        }
                    } else {
                        binding.name.error = getString(R.string.mail_exist_error)
                    }
                }
                if (user != null) {
                    val userMailToStore = if (binding.rememberCheck.isChecked) email else ""
                    preferences.edit().putString("logged_user_mail", userMailToStore).apply()
                }
            }
        }
        binding.loginButton.setOnClickListener {
            val (email, password) = pair(binding)
            if (isValidEmail(email) && isValidPassword(password)) {
                lifecycleScope.launch {
                    user = NewsApplication.database.userDao().getUserByEmail(email)
                    if (user != null && user!!.password == password) {
                        val userMailToStore = if (binding.rememberCheck.isChecked) email else ""
                        preferences.edit().putString("logged_user_mail", userMailToStore).apply()
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun pair(binding: ActivityMainBinding): Pair<String, String> {
        val email = binding.name.text.toString()
        val password = binding.pass.text.toString()
        if (!isValidEmail(email)) {
            binding.name.error = getString(R.string.mail_error)
        }
        if (!isValidPassword(password)) {
            binding.pass.error = getString(R.string.pass_error)
        }
        return Pair(email, password)
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }
    fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^[^\\s]{3,}$"
        return password.matches(passwordRegex.toRegex())
    }
}