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

class TiketDigitalFragment : Fragment() {

    private lateinit var rvTiketDigital: RecyclerView
    private lateinit var tvTiketKosong: View
    private lateinit var tiketDigitalAdapter: TiketDigitalAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_tiket_digital,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        rvTiketDigital = view.findViewById(R.id.rvTiketDigital)
        tvTiketKosong = view.findViewById(R.id.tvTiketKosong)

        tiketDigitalAdapter = TiketDigitalAdapter()

        rvTiketDigital.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tiketDigitalAdapter
        }

        muatTiketAktif()
    }

    override fun onResume() {
        super.onResume()

        if (::tiketDigitalAdapter.isInitialized) {
            muatTiketAktif()
        }
    }

    private fun muatTiketAktif() {
        viewLifecycleOwner.lifecycleScope.launch {
            val daftarTiket = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(requireContext())
                    .tiketDao()
                    .getTiketAktif()
            }

            tiketDigitalAdapter.setData(daftarTiket)

            tvTiketKosong.visibility =
                if (daftarTiket.isEmpty()) View.VISIBLE else View.GONE

            rvTiketDigital.visibility =
                if (daftarTiket.isEmpty()) View.GONE else View.VISIBLE
        }
    }
}
