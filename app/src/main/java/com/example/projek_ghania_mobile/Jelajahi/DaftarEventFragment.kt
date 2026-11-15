
package com.example.projek_ghania_mobile.Jelajahi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projek_ghania_mobile.FavoritManager
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.databinding.FragmentDaftarEventBinding

class DaftarEventFragment : Fragment() {

    private var _binding: FragmentDaftarEventBinding? = null
    private val binding get() = _binding!!

    private var jelajahiAdapter: JelajahiAdapter? = null

    companion object {
        private const val ARG_EVENT_TYPE = "event_type"

        fun newInstance(eventType: String): DaftarEventFragment {
            return DaftarEventFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_EVENT_TYPE, eventType)
                }
            }
        }
    }

    private val isFavoriteTab: Boolean
        get() = arguments?.getString(ARG_EVENT_TYPE) == "favorit"

    private fun getAllEvents(): List<JelajahiModel> {
        return listOf(
            JelajahiModel("Riau Creative Fest 2026", "Bandar Seni Raja Ali Haji", R.drawable.event_pku, 1),
            JelajahiModel("Pekanbaru Art Exhibition", "Taman Budaya Pekanbaru", R.drawable.event_pku, 2),
            JelajahiModel("Festival Kuliner Riau", "Lapangan Purna MTQ Pekanbaru", R.drawable.event_pku, 3),
            JelajahiModel("Pekanbaru Indie Music Day", "Lapangan Purna MTQ Pekanbaru", R.drawable.event_pku, 4),
            JelajahiModel("UMKM Creative Market", "Mall Pekanbaru", R.drawable.event_pku, 5),
            JelajahiModel("Creative Workshop Day", "Gedung Kreatif Pekanbaru", R.drawable.event_pku, 6),
            JelajahiModel("Riau Craft & Handmade Expo", "Taman Budaya Pekanbaru", R.drawable.event_pku, 7),
            JelajahiModel("Pekanbaru Creative Community Day", "Bandar Seni Raja Ali Haji", R.drawable.event_pku, 8),
            JelajahiModel("Pameran Seni PopKraf", "Pustaka Wilayah Soeman HS", R.drawable.event_pku, 9),
            JelajahiModel("Bazar UMKM Mahasiswa", "Kampus Politeknik Caltex Riau", R.drawable.event_pku, 10),
            JelajahiModel("Pekanbaru Culinary Night", "Jalan Arifin Ahmad", R.drawable.event_pku, 11),
            JelajahiModel("Festival Musik Indie", "Riau Garden", R.drawable.event_pku, 12),
            JelajahiModel("Digital Creative Festival", "Bandar Seni Raja Ali Haji", R.drawable.event_pku, 13),
            JelajahiModel("Local Brand Pop-Up Market", "Mall SKA Pekanbaru", R.drawable.event_pku, 14),
            JelajahiModel("Creative Photography Class", "Gedung Kreatif Pekanbaru", R.drawable.event_pku, 15)
        )
    }

    private fun getDisplayedEvents(): List<JelajahiModel> {
        val allEvents = getAllEvents()

        if (!isFavoriteTab) {
            return allEvents
        }

        val favoriteIds = FavoritManager.getFavoriteIds(requireContext())

        return allEvents.filter { event ->
            event.id in favoriteIds
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDaftarEventBinding.inflate(
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

        val header = binding.layoutEksplorasi

        if (isFavoriteTab) {
            header.visibility = View.GONE
        } else {
            (header.parent as? ViewGroup)?.removeView(header)

            binding.lvEvent.addHeaderView(
                header,
                null,
                false
            )
        }

        jelajahiAdapter = JelajahiAdapter(
            requireContext(),
            getDisplayedEvents()
        )

        binding.lvEvent.adapter = jelajahiAdapter
    }

    override fun onResume() {
        super.onResume()

        if (_binding != null) {
            val header = binding.layoutEksplorasi

            if (isFavoriteTab) {
                jelajahiAdapter = JelajahiAdapter(
                    requireContext(),
                    getDisplayedEvents()
                )

                binding.lvEvent.adapter = jelajahiAdapter
            } else {
                jelajahiAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        jelajahiAdapter = null
        _binding = null
    }
}
