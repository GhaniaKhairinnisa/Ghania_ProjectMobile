package com.example.projek_ghania_mobile.Jelajahi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.projek_ghania_mobile.databinding.FragmentJelajahiBinding
import com.google.android.material.tabs.TabLayoutMediator

class JelajahiFragment : Fragment() {

    private var _binding: FragmentJelajahiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJelajahiBinding.inflate(
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

        // Mengatur Toolbar
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(binding.toolbar)

        (requireActivity() as AppCompatActivity)
            .supportActionBar?.title = ""

        // Memasang adapter ke ViewPager2
        val pagerAdapter = JelajahiPagerAdapter(this)
        binding.viewPagerJelajahi.adapter = pagerAdapter

        // Menghubungkan TabLayout dengan ViewPager2
        TabLayoutMediator(
            binding.tabLayoutJelajahi,
            binding.viewPagerJelajahi
        ) { tab, position ->

            when (position) {
                0 -> tab.text = "Semua Event"
                1 -> tab.text = "Favorit Saya"
            }

        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}