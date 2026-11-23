package com.example.projek_ghania_mobile.Tiket

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.projek_ghania_mobile.BaseActivity
import com.example.projek_ghania_mobile.Data.AppDatabase
import com.example.projek_ghania_mobile.Data.Entity.EventEntity
import com.example.projek_ghania_mobile.Data.Entity.TiketEntity
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.databinding.ActivityTiketBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class TiketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTiketBinding

    private var event: EventEntity? = null

    // Jumlah tiket yang dipilih
    private var jumlahRegular = 0
    private var jumlahVip = 0
    private var jumlahBundling = 0

    // Metode pembayaran yang dipilih
    private var metodePembayaran = "QRIS"

    // Harga masing-masing kategori tiket
    private var hargaRegular = 0
    private var hargaVip = 0
    private var hargaBundling = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTiketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur inset layar
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

        // Mengatur toolbar
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        // Mengatur jumlah tiket
        setupTicketSelection()

        // Mengatur metode pembayaran
        setupPaymentMethod()

        // Mengatur tombol Bayar Sekarang
        setupPaymentButton()

        // Mengambil event dari DetailActivity
        val eventId = intent.getIntExtra("eventId", -1)

        if (eventId != -1) {
            ambilDataEvent(eventId)
        }
    }

    // ==============================
    // TOMBOL KEMBALI TOOLBAR
    // ==============================

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    // ==============================
    // MENGAMBIL DATA EVENT DARI ROOM
    // ==============================

    private fun ambilDataEvent(eventId: Int) {

        lifecycleScope.launch {

            val dataEvent = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(applicationContext)
                    .eventDao()
                    .getEventById(eventId)
            }

            event = dataEvent

            dataEvent?.let {

                // Mengambil harga tiket dari database
                hargaRegular = it.regularPrice
                hargaVip = it.vipPrice
                hargaBundling = it.bundlingPrice

                // Menampilkan informasi event
                tampilkanInfoEvent(it)

                // Memperbarui harga dan total tiket
                updateTicketSummary()
            }
        }
    }

    // ==============================
    // MENAMPILKAN INFORMASI EVENT
    // ==============================

    private fun tampilkanInfoEvent(event: EventEntity) {

        val localeIndonesia = Locale("id", "ID")

        val formatTanggal = SimpleDateFormat(
            "EEEE, dd MMMM yyyy",
            localeIndonesia
        ).apply {
            timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        }

        val formatJam = SimpleDateFormat(
            "HH:mm",
            localeIndonesia
        ).apply {
            timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        }

        val tanggal = formatTanggal.format(
            Date(event.startDateTimeMillis)
        )

        val jamMulai = formatJam.format(
            Date(event.startDateTimeMillis)
        )

        val jamSelesai = formatJam.format(
            Date(event.endDateTimeMillis)
        )

        val harga = if (event.minTicketPrice == 0) {
            "Gratis"
        } else {
            NumberFormat.getCurrencyInstance(localeIndonesia)
                .apply {
                    maximumFractionDigits = 0
                }
                .format(event.minTicketPrice)
        }

        binding.tvNamaEventTiket.text =
            event.eventName

        binding.tvTanggalEventTiket.text =
            "$tanggal\n$jamMulai - $jamSelesai WIB"

        binding.tvLokasiEventTiket.text =
            event.location

        binding.tvHargaEventTiket.text =
            "Mulai dari $harga"
    }

    // ==============================
    // MENGATUR PILIHAN JUMLAH TIKET
    // ==============================

    private fun setupTicketSelection() {

        // REGULAR PASS
        binding.btnPlusRegular.setOnClickListener {
            jumlahRegular++
            updateTicketSummary()
        }

        binding.btnMinusRegular.setOnClickListener {
            if (jumlahRegular > 0) {
                jumlahRegular--
                updateTicketSummary()
            }
        }

        // VIP + MERCHANDISE
        binding.btnPlusVip.setOnClickListener {
            jumlahVip++
            updateTicketSummary()
        }

        binding.btnMinusVip.setOnClickListener {
            if (jumlahVip > 0) {
                jumlahVip--
                updateTicketSummary()
            }
        }

        // BUNDLING VOUCHER UMKM
        binding.btnPlusBundling.setOnClickListener {
            jumlahBundling++
            updateTicketSummary()
        }

        binding.btnMinusBundling.setOnClickListener {
            if (jumlahBundling > 0) {
                jumlahBundling--
                updateTicketSummary()
            }
        }

        // Menampilkan nilai awal
        updateTicketSummary()
    }

    // ==============================
    // MENGATUR METODE PEMBAYARAN
    // ==============================

    private fun setupPaymentMethod() {

        fun pilihMetode(metode: String) {

            metodePembayaran = metode

            // Semua metode kembali ke background tidak terpilih
            binding.tvMetodeQris.setBackgroundResource(
                R.drawable.bg_payment_unselected
            )

            binding.tvMetodeBank.setBackgroundResource(
                R.drawable.bg_payment_unselected
            )

            binding.tvMetodeEwallet.setBackgroundResource(
                R.drawable.bg_payment_unselected
            )

            // Memberikan tanda pada metode terpilih
            when (metode) {

                "QRIS" -> {
                    binding.tvMetodeQris.setBackgroundResource(
                        R.drawable.bg_qris_selected
                    )
                }

                "Bank Transfer" -> {
                    binding.tvMetodeBank.setBackgroundResource(
                        R.drawable.bg_qris_selected
                    )
                }

                "E-Wallet" -> {
                    binding.tvMetodeEwallet.setBackgroundResource(
                        R.drawable.bg_qris_selected
                    )
                }
            }
        }

        // Pilihan QRIS
        binding.tvMetodeQris.setOnClickListener {
            pilihMetode("QRIS")
        }

        // Pilihan Bank Transfer
        binding.tvMetodeBank.setOnClickListener {
            pilihMetode("Bank Transfer")
        }

        // Pilihan E-Wallet
        binding.tvMetodeEwallet.setOnClickListener {
            pilihMetode("E-Wallet")
        }

        // Metode pembayaran awal
        pilihMetode("QRIS")
    }

    // ==============================
    // MENGATUR TOMBOL BAYAR SEKARANG
    // ==============================

    private fun setupPaymentButton() {

        binding.btnBayarSekarang.setOnClickListener {

            // Memastikan event sudah tersedia
            if (event == null) {

                AlertDialog.Builder(this)
                    .setTitle("Data Event Belum Tersedia")
                    .setMessage(
                        "Informasi event belum berhasil dimuat. " +
                                "Silakan coba kembali."
                    )
                    .setPositiveButton("Mengerti", null)
                    .show()

                return@setOnClickListener
            }

            // Menghitung jumlah tiket
            val totalJumlahTiket =
                jumlahRegular + jumlahVip + jumlahBundling

            // Validasi jumlah tiket
            if (totalJumlahTiket == 0) {

                AlertDialog.Builder(this)
                    .setTitle("Tiket Belum Dipilih")
                    .setMessage(
                        "Silakan pilih minimal satu tiket " +
                                "sebelum melanjutkan pembayaran."
                    )
                    .setPositiveButton("Mengerti", null)
                    .show()

                return@setOnClickListener
            }

            // Menghitung subtotal
            val subtotalRegular =
                jumlahRegular * hargaRegular

            val subtotalVip =
                jumlahVip * hargaVip

            val subtotalBundling =
                jumlahBundling * hargaBundling

            // Menghitung total pembayaran
            val totalPembayaran =
                subtotalRegular +
                        subtotalVip +
                        subtotalBundling

            // Membuat ringkasan pesanan
            val ringkasan = buildString {

                appendLine("Event: ${event?.eventName ?: "-"}")
                appendLine()

                appendLine(
                    "Regular Pass: $jumlahRegular tiket"
                )

                appendLine(
                    "VIP + Merchandise: $jumlahVip tiket"
                )

                appendLine(
                    "Bundling Voucher UMKM: $jumlahBundling tiket"
                )

                appendLine()
                appendLine(
                    "Metode Pembayaran: $metodePembayaran"
                )

                appendLine()
                append(
                    "Total Pembayaran: " +
                            formatRupiah(totalPembayaran)
                )
            }

            // Dialog konfirmasi pesanan
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Pesanan")
                .setMessage(ringkasan)
                .setNegativeButton("Kembali", null)
                .setPositiveButton("Konfirmasi") { _, _ ->

                    // Simulasi konfirmasi pesanan
                    AlertDialog.Builder(this)
                        .setTitle("Pesanan Dikonfirmasi")
                        .setMessage(
                            "Pesanan tiket berhasil " +
                                    "dikonfirmasi dalam simulasi.\n\n" +
                                    "Metode pembayaran: " +
                                    "$metodePembayaran\n" +
                                    "Total: " +
                                    formatRupiah(totalPembayaran) +
                                    "\n\n" +
                                    "Pembayaran belum dilakukan " +
                                    "secara nyata."
                        )
                        .setPositiveButton("Selesai") { _, _ ->
                            simpanTiketDanKembali()
                        }
                        .show()
                }
                .show()
        }
    }

    // ==============================
    // MENYIMPAN TIKET KE ROOM
    // ==============================

    private fun simpanTiketDanKembali() {

        val dataEvent = event

        if (dataEvent == null) {
            AlertDialog.Builder(this)
                .setTitle("Gagal Menyimpan Tiket")
                .setMessage("Data event tidak tersedia.")
                .setPositiveButton("Mengerti", null)
                .show()

            return
        }

        val daftarTiket = mutableListOf<TiketEntity>()
        val waktuPembelian = System.currentTimeMillis()

        // Menambahkan tiket Regular Pass
        repeat(jumlahRegular) {
            daftarTiket.add(
                TiketEntity(
                    ticketId = "PKF-${UUID.randomUUID()}",
                    eventId = dataEvent.eventId,
                    eventName = dataEvent.eventName,
                    startDateTimeMillis = dataEvent.startDateTimeMillis,
                    location = dataEvent.location,
                    ticketType = "Regular Pass",
                    ticketPrice = hargaRegular,
                    paymentMethod = metodePembayaran,
                    purchasedAtMillis = waktuPembelian
                )
            )
        }

        // Menambahkan tiket VIP + Merchandise
        repeat(jumlahVip) {
            daftarTiket.add(
                TiketEntity(
                    ticketId = "PKF-${UUID.randomUUID()}",
                    eventId = dataEvent.eventId,
                    eventName = dataEvent.eventName,
                    startDateTimeMillis = dataEvent.startDateTimeMillis,
                    location = dataEvent.location,
                    ticketType = "VIP + Merchandise",
                    ticketPrice = hargaVip,
                    paymentMethod = metodePembayaran,
                    purchasedAtMillis = waktuPembelian
                )
            )
        }

        // Menambahkan tiket Bundling Voucher UMKM
        repeat(jumlahBundling) {
            daftarTiket.add(
                TiketEntity(
                    ticketId = "PKF-${UUID.randomUUID()}",
                    eventId = dataEvent.eventId,
                    eventName = dataEvent.eventName,
                    startDateTimeMillis = dataEvent.startDateTimeMillis,
                    location = dataEvent.location,
                    ticketType = "Bundling Voucher UMKM",
                    ticketPrice = hargaBundling,
                    paymentMethod = metodePembayaran,
                    purchasedAtMillis = waktuPembelian
                )
            )
        }

        lifecycleScope.launch {

            try {

                // Menyimpan tiket ke database Room
                withContext(Dispatchers.IO) {
                    AppDatabase.getDatabase(applicationContext)
                        .tiketDao()
                        .insertDaftarTiket(daftarTiket)
                }

                // Dialog setelah tiket berhasil disimpan
                AlertDialog.Builder(this@TiketActivity)
                    .setTitle("Tiket Berhasil Disimpan")
                    .setMessage(
                        "Tiket berhasil disimpan ke Dompet Tiket."
                    )
                    .setPositiveButton("Lihat Tiket") { _, _ ->

                        // Membuka BaseActivity dan menu Dompet Tiket
                        val intent = Intent(
                            this@TiketActivity,
                            BaseActivity::class.java
                        ).apply {

                            putExtra(
                                BaseActivity.EXTRA_OPEN_TICKET_WALLET,
                                true
                            )

                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }

                        startActivity(intent)
                    }
                    .show()

            } catch (e: Exception) {

                AlertDialog.Builder(this@TiketActivity)
                    .setTitle("Gagal Menyimpan Tiket")
                    .setMessage(
                        "Tiket belum berhasil disimpan. " +
                                "Silakan coba kembali.\n\n" +
                                "Detail: ${e.message}"
                    )
                    .setPositiveButton("Mengerti", null)
                    .show()
            }
        }
    }

    // ==============================
    // MENGHITUNG SUBTOTAL DAN TOTAL
    // ==============================

    private fun updateTicketSummary() {

        val subtotalRegular =
            jumlahRegular * hargaRegular

        val subtotalVip =
            jumlahVip * hargaVip

        val subtotalBundling =
            jumlahBundling * hargaBundling

        val totalPembayaran =
            subtotalRegular +
                    subtotalVip +
                    subtotalBundling

        // Menampilkan jumlah tiket
        binding.tvJumlahRegular.text =
            jumlahRegular.toString()

        binding.tvJumlahVip.text =
            jumlahVip.toString()

        binding.tvJumlahBundling.text =
            jumlahBundling.toString()

        // Menampilkan harga kategori
        binding.tvHargaRegular.text =
            formatRupiah(hargaRegular)

        binding.tvHargaVip.text =
            formatRupiah(hargaVip)

        binding.tvHargaBundling.text =
            formatRupiah(hargaBundling)

        // Menampilkan subtotal
        binding.tvSubtotalRegular.text =
            formatRupiah(subtotalRegular)

        binding.tvSubtotalVip.text =
            formatRupiah(subtotalVip)

        binding.tvSubtotalBundling.text =
            formatRupiah(subtotalBundling)

        // Menampilkan total pembayaran
        binding.tvTotalPembayaran.text =
            formatRupiah(totalPembayaran)
    }

    // ==============================
    // FORMAT HARGA RUPIAH
    // ==============================

    private fun formatRupiah(nominal: Int): String {

        val localeIndonesia = Locale("id", "ID")

        return NumberFormat.getCurrencyInstance(localeIndonesia)
            .apply {
                maximumFractionDigits = 0
                minimumFractionDigits = 0
            }
            .format(nominal)
    }
}
