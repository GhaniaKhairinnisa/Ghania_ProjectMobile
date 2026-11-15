
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projek_ghania_mobile.Beranda.HomeEvent.HomeEventAdapter
import com.example.projek_ghania_mobile.Beranda.HomeEvent.HomeEventModel
import com.example.projek_ghania_mobile.LoginActivity
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.databinding.ActivityHomeFragmentBinding
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: ActivityHomeFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeEventAdapter: HomeEventAdapter

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

        homeEventAdapter = HomeEventAdapter(requireContext()) {

            val intent = Intent(
                requireContext(),
                DetailActivity::class.java
            )

            startActivity(intent)
        }

        binding.rvHomeEvent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = homeEventAdapter
            isNestedScrollingEnabled = false
            setHasFixedSize(false)
        }

        // Daftar data event PopKraf
        val daftarEvent = listOf(

            HomeEventModel(
                id = 1,
                gambarEvent = R.drawable.event_pku,
                kategori = listOf("Bazar", "UMKM", "Musik"),
                lokasi = "📍 Bandar Seni Raja Ali Haji",
                namaEvent = "Riau Creative Fest 2026",
                tanggalWaktu = "📅 12 Des 2026   ⏰ 09.00–20.00 WIB",
                hitungMundur = "🔥 MULAI 02:14:36",
                jumlahTenant = "🏪 80+ Tenants",
                hargaTiket = "Mulai Rp 25.000"
            ),

            HomeEventModel(
                id = 2,
                gambarEvent = R.drawable.event_pku,
                kategori = listOf("Pameran", "Komunitas"),
                lokasi = "📍 Taman Budaya Pekanbaru",
                namaEvent = "Pekanbaru Art Exhibition",
                tanggalWaktu = "📅 18 Des 2026   ⏰ 10.00–18.00 WIB",
                hitungMundur = "🔥 SEGERA HADIR",
                jumlahTenant = "🎨 25+ Kreator",
                hargaTiket = "Mulai Rp 15.000"
            ),

            HomeEventModel(
                id = 3,
                gambarEvent = R.drawable.event_pku,
                kategori = listOf("Bazar", "Kuliner", "Festival"),
                lokasi = "📍 Lapangan Purna MTQ Pekanbaru",
                namaEvent = "Festival Kuliner Riau",
                tanggalWaktu = "📅 20 Des 2026   ⏰ 11.00–21.00 WIB",
                hitungMundur = "🔥 SEGERA HADIR",
                jumlahTenant = "🍜 50+ Tenant",
                hargaTiket = "Gratis Masuk"
            ),

            HomeEventModel(
                id = 4,
                gambarEvent = R.drawable.event_pku,
                kategori = listOf("Musik", "Festival"),
                lokasi = "📍 Lapangan Purna MTQ Pekanbaru",
                namaEvent = "Pekanbaru Indie Music Day",
                tanggalWaktu = "📅 26 Des 2026   ⏰ 15.00–22.00 WIB",
                hitungMundur = "🔥 SEGERA HADIR",
                jumlahTenant = "🎵 12+ Musisi",
                hargaTiket = "Mulai Rp 35.000"
            ),

            HomeEventModel(
                id = 5,
                gambarEvent = R.drawable.event_pku,
                kategori = listOf("UMKM", "Bazar"),
                lokasi = "📍 Mall Pekanbaru",
                namaEvent = "UMKM Creative Market",
                tanggalWaktu = "📅 03 Jan 2027   ⏰ 10.00–21.00 WIB",
                hitungMundur = "🔥 SEGERA HADIR",
                jumlahTenant = "🏪 40+ UMKM",
                hargaTiket = "Gratis Masuk"
            ),

            HomeEventModel(
                id = 6,
                gambarEvent = R.drawable.event_pku,
                kategori = listOf("Workshop", "Komunitas"),
                lokasi = "📍 Gedung Kreatif Pekanbaru",
                namaEvent = "Creative Workshop Day",
                tanggalWaktu = "📅 09 Jan 2027   ⏰ 09.00–16.00 WIB",
                hitungMundur = "🔥 SEGERA HADIR",
                jumlahTenant = "👥 8+ Kelas",
                hargaTiket = "Mulai Rp 20.000"
            ),

            HomeEventModel(
                id = 7,
                gambarEvent = R.drawable.event_pku,
                kategori = listOf("Pameran", "UMKM", "Workshop"),
                lokasi = "📍 Taman Budaya Pekanbaru",
                namaEvent = "Riau Craft & Handmade Expo",
                tanggalWaktu = "📅 16 Jan 2027   ⏰ 10.00–19.00 WIB",
                hitungMundur = "🔥 SEGERA HADIR",
                jumlahTenant = "🎨 35+ Kreator",
                hargaTiket = "Mulai Rp 10.000"
            ),

            HomeEventModel(
                id = 8,
                gambarEvent = R.drawable.event_pku,
                kategori = listOf("Komunitas", "Musik", "Festival"),
                lokasi = "📍 Bandar Seni Raja Ali Haji",
                namaEvent = "Pekanbaru Creative Community Day",
                tanggalWaktu = "📅 23 Jan 2027   ⏰ 13.00–21.00 WIB",
                hitungMundur = "🔥 SEGERA HADIR",
                jumlahTenant = "👥 20+ Komunitas",
                hargaTiket = "Gratis Masuk"
            )

        )

        // Memasukkan data event ke Adapter
        homeEventAdapter.setData(daftarEvent)
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
