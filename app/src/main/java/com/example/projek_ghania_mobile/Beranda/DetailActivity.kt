
package com.example.projek_ghania_mobile.Beranda

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.projek_ghania_mobile.Data.AppDatabase
import com.example.projek_ghania_mobile.Data.Entity.EventEntity
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.Tiket.TiketActivity
import com.example.projek_ghania_mobile.WebViewActivity
import com.example.projek_ghania_mobile.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        // Mengambil data event dari Room berdasarkan eventId
        val eventId = intent.getIntExtra("eventId", -1)

        if (eventId != -1) {
            tampilkanDetailEvent(eventId)
        }

        // Aksi ketika tombol Peta diklik
        binding.btnPeta.setOnClickListener {
            val intent = Intent(
                this@DetailActivity,
                WebViewActivity::class.java
            )
            startActivity(intent)
        }

        binding.btnBeliTiket.setOnClickListener {
            Snackbar.make(
                binding.root,
                "Pilih Jenis Tiket",
                Snackbar.LENGTH_LONG
            )
                .setAction("Lihat Detail") {
                    val intent = Intent(
                        this@DetailActivity,
                        TiketActivity::class.java
                    )

                    intent.putExtra("eventId", eventId)

                    startActivity(intent)
                }
                .show()
        }
    }

    private fun tampilkanDetailEvent(eventId: Int) {
        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(applicationContext)

            val event = withContext(Dispatchers.IO) {
                database.eventDao().getEventById(eventId)
            }

            if (event != null) {
                isiDetailEvent(event)
            } else {
                Snackbar.make(
                    binding.root,
                    "Data event tidak ditemukan",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isiDetailEvent(event: EventEntity) {
        // Gambar dan nama event
        binding.ivBanner.setImageResource(event.imageResId)
        binding.tvDetailTitle.text = event.eventName

        // Lokasi event
        binding.tvLocationName.text = event.location

        // Kategori event
        val daftarKategori = event.categories
            .split("|")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val daftarTag = listOf(
            binding.tvTag1,
            binding.tvTag2,
            binding.tvTag3
        )

        daftarTag.forEachIndexed { index, textView ->
            if (index < daftarKategori.size) {
                textView.text = daftarKategori[index].uppercase()
                textView.visibility = View.VISIBLE
            } else {
                textView.visibility = View.GONE
            }
        }

        // Tanggal dan waktu event
        val zonaWaktu = ZoneId.of("Asia/Jakarta")

        val tanggalFormatter = DateTimeFormatter
            .ofPattern("d MMM yyyy", Locale("id", "ID"))

        val waktuFormatter = DateTimeFormatter
            .ofPattern("HH.mm", Locale("id", "ID"))

        val tanggalMulai = Instant
            .ofEpochMilli(event.startDateTimeMillis)
            .atZone(zonaWaktu)

        val tanggalSelesai = Instant
            .ofEpochMilli(event.endDateTimeMillis)
            .atZone(zonaWaktu)

        val tanggalText = if (
            tanggalMulai.toLocalDate() == tanggalSelesai.toLocalDate()
        ) {
            tanggalMulai.format(tanggalFormatter)
        } else {
            "${tanggalMulai.format(tanggalFormatter)} – " +
                    tanggalSelesai.format(tanggalFormatter)
        }

        val waktuText =
            "${tanggalMulai.format(waktuFormatter)} – " +
                    "${tanggalSelesai.format(waktuFormatter)} WIB"

        binding.tvDetailDate.text = tanggalText
        binding.tvDetailTime.text = waktuText

        // Harga tiket
        val hargaText = if (event.minTicketPrice <= 0) {
            "Gratis"
        } else {
            val formatHarga = NumberFormat.getCurrencyInstance(
                Locale("id", "ID")
            )
            "Mulai dari ${formatHarga.format(event.minTicketPrice)}/orang"
        }

        binding.tvDetailPrice.text = hargaText

        // Jumlah tenant
        binding.tvDetailTenant.text =
            "${event.tenantCount} tenant berpartisipasi"
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
