package com.example.projek_ghania_mobile.Jelajahi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.databinding.FragmentJelajahiBinding

class JelajahiFragment : Fragment() {

    private var _binding: FragmentJelajahiBinding? = null
    private val binding get() = _binding!!
    private val eventList = listOf(
        JelajahiModel("Riau Creative Fest 2026", "Bandar Seni Raja Ali Haji", R.drawable.event_pku),
        JelajahiModel("Pameran Seni PopKraf", "Pustaka Wilayah Soeman HS", R.drawable.event_pku),
        JelajahiModel("Bazar UMKM Mahasiswa", "Kampus Politeknik Caltex Riau", R.drawable.event_pku),
        JelajahiModel("Pekanbaru Culinary Night", "Jalan Arifin Ahmad", R.drawable.event_pku),
        JelajahiModel("Festival Musik Indie", "Riau Garden", R.drawable.event_pku)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJelajahiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        val adapter = JelajahiAdapter(requireContext(), eventList)
        binding.lvEvent.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}