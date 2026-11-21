package com.example.projek_ghania_mobile.Beranda

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.projek_ghania_mobile.Data.AppDatabase
import com.example.projek_ghania_mobile.Data.Entity.EventEntity
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.databinding.ActivityTambahEventBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TambahEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahEventBinding

    private lateinit var imgPosterEvent: ImageView
    private lateinit var btnPilihPoster: Button

    // URI poster yang sudah disimpan ke penyimpanan internal aplikasi
    private var posterUri: String? = null

    private var tanggalMulaiMillis: Long? = null
    private var tanggalSelesaiMillis: Long? = null

    private lateinit var btnTanggalMulai: Button
    private lateinit var btnTanggalSelesai: Button

    private lateinit var edtNamaEvent: EditText
    private lateinit var edtKategoriEvent: EditText
    private lateinit var edtLokasiEvent: EditText
    private lateinit var edtJumlahTenant: EditText
    private lateinit var edtHargaMinimum: EditText
    private lateinit var edtHargaRegular: EditText
    private lateinit var edtHargaVip: EditText
    private lateinit var edtHargaBundling: EditText

    private lateinit var btnSimpanEvent: Button

    private val pilihPosterLauncher =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            if (uri != null) {
                simpanPosterKeInternal(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityTambahEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Toolbar PopKraf
        setSupportActionBar(binding.toolbarTambahEvent)

        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        imgPosterEvent = findViewById(R.id.imgPosterEvent)
        btnPilihPoster = findViewById(R.id.btnPilihPoster)

        btnPilihPoster.setOnClickListener {
            pilihPosterLauncher.launch("image/*")
        }

        btnTanggalMulai = findViewById(R.id.btnTanggalMulai)
        btnTanggalSelesai = findViewById(R.id.btnTanggalSelesai)

        btnTanggalMulai.setOnClickListener {
            pilihTanggalDanWaktu(isMulai = true)
        }

        btnTanggalSelesai.setOnClickListener {
            pilihTanggalDanWaktu(isMulai = false)
        }

        edtNamaEvent = findViewById(R.id.edtNamaEvent)
        edtKategoriEvent = findViewById(R.id.edtKategoriEvent)
        edtLokasiEvent = findViewById(R.id.edtLokasiEvent)
        edtJumlahTenant = findViewById(R.id.edtJumlahTenant)
        edtHargaMinimum = findViewById(R.id.edtHargaMinimum)
        edtHargaRegular = findViewById(R.id.edtHargaRegular)
        edtHargaVip = findViewById(R.id.edtHargaVip)
        edtHargaBundling = findViewById(R.id.edtHargaBundling)

        btnSimpanEvent = findViewById(R.id.btnSimpanEvent)

        btnSimpanEvent.setOnClickListener {
            simpanEvent()
        }
    }

    // Fungsi tombol kembali pada toolbar
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun simpanPosterKeInternal(uri: Uri) {
        try {
            val folderPoster = File(filesDir, "event_posters")

            if (!folderPoster.exists()) {
                folderPoster.mkdirs()
            }

            val filePoster = File(
                folderPoster,
                "poster_${System.currentTimeMillis()}.jpg"
            )

            contentResolver.openInputStream(uri)?.use { inputStream ->
                filePoster.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: run {
                Toast.makeText(
                    this,
                    "Gagal membaca gambar",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            // Simpan URI file lokal agar dapat digunakan kembali
            posterUri = Uri.fromFile(filePoster).toString()

            // Tampilkan poster yang dipilih
            imgPosterEvent.setImageURI(Uri.parse(posterUri))

            Toast.makeText(
                this,
                "Poster berhasil dipilih",
                Toast.LENGTH_SHORT
            ).show()

        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Gagal menyimpan poster: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun pilihTanggalDanWaktu(isMulai: Boolean) {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->

                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)

                        val waktuMillis = calendar.timeInMillis

                        val formatTanggal = SimpleDateFormat(
                            "dd MMM yyyy, HH:mm",
                            Locale("id", "ID")
                        )

                        val teksTanggal = formatTanggal.format(calendar.time)

                        if (isMulai) {
                            tanggalMulaiMillis = waktuMillis
                            btnTanggalMulai.text = teksTanggal
                        } else {
                            tanggalSelesaiMillis = waktuMillis
                            btnTanggalSelesai.text = teksTanggal
                        }

                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()
    }

    private fun simpanEvent() {

        val namaEvent = edtNamaEvent.text.toString().trim()
        val kategoriEvent = edtKategoriEvent.text.toString().trim()
        val lokasiEvent = edtLokasiEvent.text.toString().trim()

        val jumlahTenant = edtJumlahTenant.text.toString().toIntOrNull()
        val hargaMinimum = edtHargaMinimum.text.toString().toIntOrNull()
        val hargaRegular = edtHargaRegular.text.toString().toIntOrNull()
        val hargaVip = edtHargaVip.text.toString().toIntOrNull()
        val hargaBundling = edtHargaBundling.text.toString().toIntOrNull()

        // Validasi input teks
        if (namaEvent.isEmpty() ||
            kategoriEvent.isEmpty() ||
            lokasiEvent.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Nama, kategori, dan lokasi event wajib diisi",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Validasi jumlah tenant dan harga
        if (jumlahTenant == null ||
            hargaMinimum == null ||
            hargaRegular == null ||
            hargaVip == null ||
            hargaBundling == null
        ) {
            Toast.makeText(
                this,
                "Jumlah tenant dan semua harga harus berupa angka",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (jumlahTenant <= 0 ||
            hargaMinimum < 0 ||
            hargaRegular < 0 ||
            hargaVip < 0 ||
            hargaBundling < 0
        ) {
            Toast.makeText(
                this,
                "Jumlah tenant harus lebih dari 0 dan harga tidak boleh negatif",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Validasi tanggal
        val mulai = tanggalMulaiMillis
        val selesai = tanggalSelesaiMillis

        if (mulai == null || selesai == null) {
            Toast.makeText(
                this,
                "Pilih tanggal dan waktu mulai serta selesai",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (selesai <= mulai) {
            Toast.makeText(
                this,
                "Waktu selesai harus setelah waktu mulai",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Membentuk data event
        val eventBaru = EventEntity(
            eventName = namaEvent,
            imageResId = R.drawable.event_pku,
            categories = kategoriEvent,
            location = lokasiEvent,
            startDateTimeMillis = mulai,
            endDateTimeMillis = selesai,
            tenantCount = jumlahTenant,
            minTicketPrice = hargaMinimum,
            regularPrice = hargaRegular,
            vipPrice = hargaVip,
            bundlingPrice = hargaBundling,
            posterUri = posterUri
        )

        // Menyimpan ke Room
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    AppDatabase.getDatabase(this@TambahEventActivity)
                        .eventDao()
                        .insertEvent(eventBaru)
                }

                Toast.makeText(
                    this@TambahEventActivity,
                    "Event berhasil disimpan",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            } catch (e: Exception) {
                Toast.makeText(
                    this@TambahEventActivity,
                    "Gagal menyimpan event: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
