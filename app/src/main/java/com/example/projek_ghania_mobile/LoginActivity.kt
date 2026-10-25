package com.example.projek_ghania_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projek_ghania_mobile.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //islogin
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        binding.btnSubmit.setOnClickListener {
            val user = binding.edtUsername.text.toString().trim()
            val pass = binding.edtPassword.text.toString()

            // validasi password: panjang min 8,kapital,angka
            val isPasswordValid = pass.length >= 8 && pass.any { it.isDigit() } && pass.any { it.isUpperCase() }

            if (user.isNotEmpty() && isPasswordValid) {
                val editor = sharedPref.edit()
                editor.putBoolean("isLogin", true)
                editor.putString("username", user)
                editor.apply()

                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, BaseActivity::class.java))
                finish()
            } else {
                val errorMessage = if (user.isEmpty()) {
                    "Username tidak boleh kosong."
                } else {
                    "Password harus minimal 8 karakter, mengandung minimal 1 angka, dan minimal 1 huruf kapital."
                }

                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage(errorMessage)
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }
}