
package com.example.projek_ghania_mobile.Jelajahi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.projek_ghania_mobile.Data.AppDatabase
import com.example.projek_ghania_mobile.Data.Entity.EventEntity
import com.example.projek_ghania_mobile.databinding.FragmentDaftarEventBinding
import kotlinx.coroutines.launch

class DaftarEventFragment : Fragment() {

    private var _binding: FragmentDaftarEventBinding? = null
    private val binding get() = _binding!!

    private var jelajahiAdapter: JelajahiAdapter? = null

    private var allEvents: List<JelajahiModel> = emptyList()
    private var favoriteIds: Set<Int> = emptySet()

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

        // Mengatur header pada tab
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

        // Menghubungkan daftar event dengan Room
        observeEvents()
    }

    private fun observeEvents() {
        val database = AppDatabase.getDatabase(requireContext())
        val eventDao = database.eventDao()
        val favoriteDao = database.favoriteDao()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                // Mengambil seluruh event dari Room
                launch {
                    eventDao.getAllEvents().collect { events ->
                        allEvents = events.map { event ->
                            konversiEventKeModel(event)
                        }

                        tampilkanEvents()
                    }
                }

                // Mengambil ID event favorit dari Room
                launch {
                    favoriteDao.getFavoriteEventIds().collect { ids ->
                        favoriteIds = ids.toSet()

                        tampilkanEvents()
                    }
                }
            }
        }
    }

    private fun konversiEventKeModel(
        event: EventEntity
    ): JelajahiModel {
        return JelajahiModel(
            namaEvent = event.eventName,
            lokasiEvent = event.location,
            gambarEvent = event.imageResId,
            id = event.eventId,
            posterUri = event.posterUri
        )
    }

    private fun tampilkanEvents() {
        if (_binding == null) return

        val displayedEvents = if (isFavoriteTab) {
            allEvents.filter { event ->
                event.id in favoriteIds
            }
        } else {
            allEvents
        }

        jelajahiAdapter = JelajahiAdapter(
            requireContext(),
            displayedEvents,
            favoriteIds
        ) { eventId ->

            viewLifecycleOwner.lifecycleScope.launch {
                val favoriteDao = AppDatabase
                    .getDatabase(requireContext())
                    .favoriteDao()

                if (eventId in favoriteIds) {
                    favoriteDao.deleteFavorite(eventId)
                } else {
                    favoriteDao.insertFavorite(
                        com.example.projek_ghania_mobile.Data.Entity.FavoriteEntity(
                            eventId = eventId
                        )
                    )
                }
            }
        }

        binding.lvEvent.adapter = jelajahiAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()

        jelajahiAdapter = null
        _binding = null
    }
}
