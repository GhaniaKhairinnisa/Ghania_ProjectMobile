package com.example.projek_ghania_mobile.Tiket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_ghania_mobile.Data.AppDatabase
import com.example.projek_ghania_mobile.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RiwayatTiketFragment : Fragment() {

    private lateinit var rvRiwayatTiket: RecyclerView
    private lateinit var tvRiwayatKosong: View
    private lateinit var riwayatTiketAdapter: RiwayatTiketAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_riwayat_tiket,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        rvRiwayatTiket =
            view.findViewById(R.id.rvRiwayatTiket)

        tvRiwayatKosong =
            view.findViewById(R.id.tvRiwayatKosong)

        riwayatTiketAdapter = RiwayatTiketAdapter()

        rvRiwayatTiket.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = riwayatTiketAdapter
        }

        muatRiwayatTiket()
    }

    override fun onResume() {
        super.onResume()

        if (::riwayatTiketAdapter.isInitialized) {
            muatRiwayatTiket()
        }
    }

    private fun muatRiwayatTiket() {
        viewLifecycleOwner.lifecycleScope.launch {
            val daftarTiket = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(requireContext())
                    .tiketDao()
                    .getRiwayatTiket()
            }

            riwayatTiketAdapter.setData(daftarTiket)

            tvRiwayatKosong.visibility =
                if (daftarTiket.isEmpty()) View.VISIBLE else View.GONE

            rvRiwayatTiket.visibility =
                if (daftarTiket.isEmpty()) View.GONE else View.VISIBLE
        }
    }
}
