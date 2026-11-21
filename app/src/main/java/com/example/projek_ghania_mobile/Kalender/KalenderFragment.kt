package com.example.projek_ghania_mobile.Kalender

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.projek_ghania_mobile.Beranda.DetailActivity
import com.example.projek_ghania_mobile.Data.AppDatabase
import com.example.projek_ghania_mobile.Data.Entity.EventEntity
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.databinding.FragmentKalenderBinding
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class KalenderFragment : Fragment() {

    private var _binding: FragmentKalenderBinding? = null
    private val binding get() = _binding!!

    private val zonaWaktu = ZoneId.of("Asia/Jakarta")
    private val localeIndonesia = Locale("id", "ID")

    private var bulanSaatIni: YearMonth = YearMonth.now(zonaWaktu)
    private var tanggalDipilih: LocalDate = LocalDate.now(zonaWaktu)

    private var daftarEvent: List<EventEntity> = emptyList()

    private val formatterBulan = DateTimeFormatter
        .ofPattern("MMMM yyyy", localeIndonesia)

    private val formatterTanggal = DateTimeFormatter
        .ofPattern("d MMM yyyy", localeIndonesia)

    private val formatterWaktu = DateTimeFormatter
        .ofPattern("HH:mm", localeIndonesia)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKalenderBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Tombol bulan sebelumnya
        binding.btnPreviousMonth.setOnClickListener {
            bulanSaatIni = bulanSaatIni.minusMonths(1)
            tanggalDipilih = tanggalDipilih.withYear(bulanSaatIni.year)
                .withMonth(bulanSaatIni.monthValue)
                .coerceAtMost(bulanSaatIni.atEndOfMonth())

            tampilkanKalender()
        }

        // Tombol bulan berikutnya
        binding.btnNextMonth.setOnClickListener {
            bulanSaatIni = bulanSaatIni.plusMonths(1)
            tanggalDipilih = tanggalDipilih.withYear(bulanSaatIni.year)
                .withMonth(bulanSaatIni.monthValue)
                .coerceAtMost(bulanSaatIni.atEndOfMonth())

            tampilkanKalender()
        }

        tampilkanKalender()
        amatiDataEvent()
    }

    private fun amatiDataEvent() {
        val eventDao = AppDatabase
            .getDatabase(requireContext())
            .eventDao()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                eventDao.getAllEvents().collect { events ->
                    daftarEvent = events

                    tampilkanKalender()
                    tampilkanEventTanggalDipilih()
                }
            }
        }
    }

    private fun tampilkanKalender() {
        if (_binding == null) return

        binding.tvMonthYear.text = bulanSaatIni
            .format(formatterBulan)
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(localeIndonesia)
                else it.toString()
            }

        binding.calendarGrid.removeAllViews()

        val tanggalPertama = bulanSaatIni.atDay(1)

        // Kalender dimulai dari Senin
        val offsetHari = tanggalPertama.dayOfWeek.value - 1

        val tanggalMulai = tanggalPertama.minusDays(
            offsetHari.toLong()
        )

        // Menampilkan 6 baris x 7 kolom
        for (index in 0 until 42) {
            val tanggal = tanggalMulai.plusDays(index.toLong())

            buatTanggalKalender(
                tanggal = tanggal,
                baris = index / 7,
                kolom = index % 7
            )
        }

        tampilkanEventTanggalDipilih()
    }

    private fun buatTanggalKalender(
        tanggal: LocalDate,
        baris: Int,
        kolom: Int
    ) {
        val context = requireContext()

        val tanggalMemilikiEvent = daftarEvent.any { event ->
            tanggalDalamRentangEvent(tanggal, event)
        }

        val tanggalTerpilih = tanggal == tanggalDipilih
        val tanggalBulanIni = tanggal.month == bulanSaatIni.month &&
                tanggal.year == bulanSaatIni.year

        val cell = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            background = buatBackgroundTanggal(tanggalTerpilih)
            isClickable = true
            isFocusable = true
        }

        val nomorTanggal = TextView(context).apply {
            text = tanggal.dayOfMonth.toString()
            gravity = Gravity.CENTER
            textSize = 10f

            setTypeface(
                Typeface.create(
                    "sans-serif",
                    if (tanggalTerpilih) Typeface.BOLD
                    else Typeface.NORMAL
                )
            )

            setTextColor(
                when {
                    tanggalTerpilih -> Color.WHITE
                    !tanggalBulanIni -> Color.LTGRAY
                    tanggal.dayOfWeek.value == 7 -> Color.parseColor("#FF6B6B")
                    else -> Color.parseColor("#20263A")
                }
            )
        }

        cell.addView(
            nomorTanggal,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(22)
            )
        )

        // Titik penanda event
        if (tanggalMemilikiEvent) {
            val warnaTitik = if (tanggalTerpilih) {
                Color.WHITE
            } else {
                warnaKategoriEvent(tanggal)
            }

            val titik = View(context).apply {
                background = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(warnaTitik)
                }
            }

            val titikParams = LinearLayout.LayoutParams(
                dp(4),
                dp(4)
            )

            titikParams.topMargin = dp(2)

            cell.addView(titik, titikParams)
        } else {
            // Ruang kosong agar posisi angka konsisten
            val ruangKosong = View(context)

            cell.addView(
                ruangKosong,
                LinearLayout.LayoutParams(
                    dp(4),
                    dp(4)
                ).apply {
                    topMargin = dp(2)
                }
            )
        }

        cell.setOnClickListener {
            tanggalDipilih = tanggal
            bulanSaatIni = YearMonth.from(tanggal)

            tampilkanKalender()
        }

        val params = GridLayout.LayoutParams().apply {
            width = 0
            height = dp(42)

            columnSpec = GridLayout.spec(kolom, 1f)
            rowSpec = GridLayout.spec(baris)

            setMargins(
                dp(1),
                dp(1),
                dp(1),
                dp(1)
            )
        }

        binding.calendarGrid.addView(cell, params)
    }

    private fun buatBackgroundTanggal(
        terpilih: Boolean
    ): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dp(24).toFloat()

            setColor(
                if (terpilih) {
                    Color.parseColor("#FF6B6B")
                } else {
                    Color.TRANSPARENT
                }
            )
        }
    }

    private fun tanggalDalamRentangEvent(
        tanggal: LocalDate,
        event: EventEntity
    ): Boolean {
        val tanggalMulai = Instant
            .ofEpochMilli(event.startDateTimeMillis)
            .atZone(zonaWaktu)
            .toLocalDate()

        val tanggalSelesai = Instant
            .ofEpochMilli(event.endDateTimeMillis)
            .atZone(zonaWaktu)
            .toLocalDate()

        return !tanggal.isBefore(tanggalMulai) &&
                !tanggal.isAfter(tanggalSelesai)
    }

    private fun warnaKategoriEvent(
        tanggal: LocalDate
    ): Int {
        val eventPadaTanggal = daftarEvent.firstOrNull { event ->
            tanggalDalamRentangEvent(tanggal, event)
        }

        val kategori = eventPadaTanggal?.categories.orEmpty()

        return when {
            kategori.contains("Musik", ignoreCase = true) ->
                Color.parseColor("#3D6FE8")

            kategori.contains("Pameran", ignoreCase = true) ->
                Color.parseColor("#B88AE8")

            kategori.contains("Workshop", ignoreCase = true) ->
                Color.parseColor("#B8F0D4")

            kategori.contains("Bazar", ignoreCase = true) ||
                    kategori.contains("UMKM", ignoreCase = true) ->
                Color.parseColor("#FFD95A")

            else -> Color.parseColor("#FF6B6B")
        }
    }

    private fun tampilkanEventTanggalDipilih() {
        if (_binding == null) return

        binding.tvSelectedDate.text =
            "Jadwal pada ${tanggalDipilih.format(formatterTanggal)}"

        binding.layoutEventDate.removeAllViews()

        val eventPadaTanggal = daftarEvent.filter { event ->
            tanggalDalamRentangEvent(
                tanggalDipilih,
                event
            )
        }

        if (eventPadaTanggal.isEmpty()) {
            binding.tvNoEvents.visibility = View.VISIBLE
            return
        }

        binding.tvNoEvents.visibility = View.GONE

        eventPadaTanggal.forEach { event ->
            binding.layoutEventDate.addView(
                buatCardEvent(event)
            )
        }
    }


    private fun buatCardEvent(
        event: EventEntity
    ): View {
        val context = requireContext()

        val card = CardView(context).apply {
            radius = dp(12).toFloat()
            cardElevation = dp(2).toFloat()
            setCardBackgroundColor(Color.WHITE)
            useCompatPadding = false
            isClickable = true
            isFocusable = true
        }

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(
                dp(10),
                dp(10),
                dp(10),
                dp(10)
            )
        }

        // Gambar Event
        val gambar = ImageView(context).apply {
            setImageResource(event.imageResId)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        container.addView(
            gambar,
            LinearLayout.LayoutParams(
                dp(52),
                dp(52)
            )
        )

        // Informasi Event
        val informasi = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(
                dp(10),
                0,
                dp(6),
                0
            )
        }

        val waktuMulai = Instant
            .ofEpochMilli(event.startDateTimeMillis)
            .atZone(zonaWaktu)
            .toLocalDateTime()

        val waktuSelesai = Instant
            .ofEpochMilli(event.endDateTimeMillis)
            .atZone(zonaWaktu)
            .toLocalDateTime()

        // Waktu Event
        val teksWaktu = TextView(context).apply {
            text = "${waktuMulai.format(formatterWaktu)} WIB"
            textSize = 9f
            setTextColor(Color.parseColor("#FF6B6B"))
            typeface = Typeface.create(
                "sans-serif",
                Typeface.BOLD
            )
        }

        // Nama Event
        val teksNama = TextView(context).apply {
            text = event.eventName
            textSize = 11f
            setTextColor(Color.parseColor("#20263A"))
            typeface = Typeface.create(
                "sans-serif",
                Typeface.BOLD
            )
            maxLines = 2
        }

        // Lokasi Event
        val teksLokasi = TextView(context).apply {
            text = event.location
            textSize = 9f
            setTextColor(Color.parseColor("#705D59"))
            maxLines = 1
        }

        // Kategori Event
        val teksKategori = TextView(context).apply {
            text = event.categories
                .split("|")
                .take(2)
                .joinToString(" • ")

            textSize = 8f
            setTextColor(Color.parseColor("#705D59"))
            maxLines = 1
        }

        informasi.addView(teksWaktu)
        informasi.addView(teksNama)
        informasi.addView(teksLokasi)
        informasi.addView(teksKategori)

        container.addView(
            informasi,
            LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
        )

        // Panah Detail
        val panah = TextView(context).apply {
            text = "›"
            textSize = 24f
            setTextColor(Color.parseColor("#20263A"))
            gravity = Gravity.CENTER
        }

        container.addView(
            panah,
            LinearLayout.LayoutParams(
                dp(20),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        card.addView(container)

        // Jarak antar card
        val cardParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        cardParams.bottomMargin = dp(8)
        card.layoutParams = cardParams

        // Aksi ketika card event ditekan
        card.setOnClickListener {
            val intent = Intent(
                context,
                DetailActivity::class.java
            )

            intent.putExtra(
                "eventId",
                event.eventId
            )

            startActivity(intent)
        }

        return card
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
