package com.example.projek_ghania_mobile.Jelajahi

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class JelajahiPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DaftarEventFragment.newInstance("semua")
            1 -> DaftarEventFragment.newInstance("favorit")
            else -> throw IllegalStateException(
                "Posisi tab tidak valid"
            )
        }
    }
}
