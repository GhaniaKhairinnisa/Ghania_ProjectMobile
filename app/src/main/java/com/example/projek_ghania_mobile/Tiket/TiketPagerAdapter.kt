
package com.example.projek_ghania_mobile.Tiket

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TiketPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TiketDigitalFragment()
            1 -> RiwayatTiketFragment()
            else -> TiketDigitalFragment()
        }
    }
}
