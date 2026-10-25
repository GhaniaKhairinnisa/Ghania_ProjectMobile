package com.example.projek_ghania_mobile.Beranda

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projek_ghania_mobile.BaseActivity
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.Tiket.TiketActivity
import com.example.projek_ghania_mobile.WebViewActivity
import com.example.projek_ghania_mobile.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)

            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        // Aksi ketika tombol Peta diklik
        binding.btnPeta.setOnClickListener {
            val intent = Intent(this@DetailActivity, WebViewActivity::class.java)
            startActivity(intent)
        }

        binding.btnBeliTiket.setOnClickListener {
            Snackbar.make(binding.root, "Pilih Jenis Tiket",
                Snackbar.LENGTH_LONG)
                .setAction("Lihat Detail") {
                    val intent = Intent(this@DetailActivity, TiketActivity::class.java)
                    startActivity(intent)
                }
                .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}