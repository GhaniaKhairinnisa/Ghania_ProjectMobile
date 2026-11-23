package com.example.projek_ghania_mobile.Beranda

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projek_ghania_mobile.Beranda.HomeEvent.HomeEventAdapter
import com.example.projek_ghania_mobile.Beranda.HomeEvent.HomeEventModel
import com.example.projek_ghania_mobile.Data.AppDatabase
import com.example.projek_ghania_mobile.Data.Entity.EventEntity
import com.example.projek_ghania_mobile.Data.Entity.FavoriteEntity
import com.example.projek_ghania_mobile.FavoritManager.toggleFavorite
import com.example.projek_ghania_mobile.LoginActivity
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.databinding.ActivityHomeFragmentBinding
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: ActivityHomeFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeEventAdapter: HomeEventAdapter

    private var favoriteEventIds: Set<Int> = emptySet()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ActivityHomeFragmentBinding.inflate(
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

        setupToolbar()
        setupSapaan()
        setupRecyclerView()
        setupFilterKategori()
        setupPencarian()

        binding.btnDaftarTenant.setOnClickListener {
            val intent = Intent(requireContext(), TambahEventActivity::class.java)
            startActivity(intent)
        }
    }


    // Mengatur menu toolbar
    private fun setupToolbar() {

        binding.toolbar.inflateMenu(R.menu.menu_home)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {

                R.id.action_logout -> {

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Konfirmasi Logout")
                        .setMessage(
                            "Apakah Anda yakin ingin keluar dan kembali ke halaman Login?"
                        )
                        .setNegativeButton("Tidak") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Ya") { _, _ ->

                            val sharedPref = requireContext()
                                .getSharedPreferences(
                                    "user_pref",
                                    Context.MODE_PRIVATE
                                )

                            sharedPref.edit().clear().apply()

                            startActivity(
                                Intent(
                                    requireContext(),
                                    LoginActivity::class.java
                                )
                            )

                            requireActivity().finish()
                        }
                        .setCancelable(false)
                        .show()

                    true
                }

                R.id.action_notifikasi -> {

                    Snackbar.make(
                        binding.root,
                        "Menu Notifikasi diklik!",
                        Snackbar.LENGTH_SHORT
                    ).show()

                    true
                }

                R.id.action_profile -> {

                    Snackbar.make(
                        binding.root,
                        "Menu Profil diklik!",
                        Snackbar.LENGTH_SHORT
                    ).show()

                    true
                }

                else -> false
            }
        }
    }

    // Mengatur sapaan pengguna
    private fun setupSapaan() {

        val sharedPref = requireContext()
            .getSharedPreferences(
                "user_pref",
                Context.MODE_PRIVATE
            )

        val username = sharedPref.getString(
            "username",
            "Kreator"
        )

        binding.tvSapaan.text = "Halo, $username! \uD83D\uDC4B"
    }

    // Mengatur RecyclerView dan Adapter event
    private fun setupRecyclerView() {

        homeEventAdapter = HomeEventAdapter(
            context = requireContext(),

            onEventClick = { event ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("eventId", event.id)
                startActivity(intent)
            },

            onFavoriteClick = { eventId ->
                toggleFavorite(eventId)
            }
        )


        binding.rvHomeEvent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = homeEventAdapter
            isNestedScrollingEnabled = false
            setHasFixedSize(false)
        }

        // Mengambil data event dari Room Database
        observeEvents()
        observeFavorites()
    }

    // Mengamati data event dari Room Database
    private fun observeEvents() {

        val eventDao = AppDatabase
            .getDatabase(requireContext())
            .eventDao()

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                eventDao.getAllEvents().collect { dataEvent ->

                    val daftarEvent = dataEvent.map { event ->

                        konversiEventKeModel(event)
                    }

                    // Memasukkan data event ke Adapter
                    homeEventAdapter.setData(daftarEvent)
                }
            }
        }
    }

    private fun konversiEventKeModel(
        event: EventEntity
    ): HomeEventModel {

        val zonaWaktu = ZoneId.of("Asia/Jakarta")

        val waktuMulai = Instant
            .ofEpochMilli(event.startDateTimeMillis)
            .atZone(zonaWaktu)

        val waktuSelesai = Instant
            .ofEpochMilli(event.endDateTimeMillis)
            .atZone(zonaWaktu)

        val formatterTanggal = DateTimeFormatter
            .ofPattern("dd MMM yyyy", Locale("id", "ID"))

        val formatterWaktu = DateTimeFormatter
            .ofPattern("HH.mm", Locale("id", "ID"))

        val tanggalWaktu =
            "📅 ${waktuMulai.format(formatterTanggal)}   " +
                    "⏰ ${waktuMulai.format(formatterWaktu)}–" +
                    "${waktuSelesai.format(formatterWaktu)} WIB"

        val waktuSekarang = System.currentTimeMillis()

        val statusEvent = when {
            waktuSekarang < event.startDateTimeMillis ->
                "🔥 SEGERA HADIR"

            waktuSekarang <= event.endDateTimeMillis ->
                "🔥 SEDANG BERLANGSUNG"

            else ->
                "🔥 EVENT SELESAI"
        }

        val hargaTiket = if (event.minTicketPrice == 0) {
            "Gratis Masuk"
        } else {
            val formatHarga = NumberFormat
                .getNumberInstance(Locale("id", "ID"))

            "Mulai Rp ${formatHarga.format(event.minTicketPrice)}"
        }

        return HomeEventModel(
            id = event.eventId,
            gambarEvent = event.imageResId,
            kategori = event.categories
                .split(",", "|")
                .map { it.trim() }
                .filter { it.isNotEmpty() },
            lokasi = "📍 ${event.location}",
            namaEvent = event.eventName,
            tanggalWaktu = tanggalWaktu,
            hitungMundur = statusEvent,
            jumlahTenant = "🏪 ${event.tenantCount}+ Tenants",
            hargaTiket = hargaTiket,
            posterUri = event.posterUri
        )
    }

    // Mengatur filter kategori event
    private fun setupFilterKategori() {

        binding.chipGroupKategori
            .setOnCheckedStateChangeListener { group, checkedIds ->

                val kategoriTerpilih = checkedIds.mapNotNull { id ->

                    group.findViewById<Chip>(id)
                        ?.text
                        ?.toString()
                }

                homeEventAdapter.filterKategori(kategoriTerpilih)
            }
    }

    // Mengatur pencarian event secara real-time
    private fun setupPencarian() {

        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Tidak ada proses sebelum teks berubah
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                val kataKunci = s?.toString().orEmpty()

                homeEventAdapter.filterPencarian(kataKunci)
            }

            override fun afterTextChanged(s: Editable?) {
                // Tidak ada proses setelah teks berubah
            }
        })
    }

    private fun toggleFavorite(eventId: Int) {
        val favoriteDao = AppDatabase
            .getDatabase(requireContext())
            .favoriteDao()

        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                if (favoriteEventIds.contains(eventId)) {
                    favoriteDao.deleteFavorite(eventId)
                } else {
                    favoriteDao.insertFavorite(
                        FavoriteEntity(eventId = eventId)
                    )
                }
            }
        }
    }

    private fun observeFavorites() {
        val favoriteDao = AppDatabase
            .getDatabase(requireContext())
            .favoriteDao()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteDao.getFavoriteEventIds().collect { ids ->
                    favoriteEventIds = ids.toSet()

                    homeEventAdapter.setFavoriteEventIds(ids)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (::homeEventAdapter.isInitialized) {
            homeEventAdapter.perbaruiSemuaIkonFavorit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
