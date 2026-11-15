
package com.example.projek_ghania_mobile.Beranda.HomeEvent

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_ghania_mobile.FavoritManager
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.databinding.ItemHomeEventBinding
import com.google.android.material.card.MaterialCardView

class HomeEventAdapter(
    private val context: Context,
    private val onEventClick: (HomeEventModel) -> Unit
) : RecyclerView.Adapter<HomeEventAdapter.EventViewHolder>() {

    // Menyimpan seluruh data event
    private val semuaEvent = mutableListOf<HomeEventModel>()

    // Menyimpan event yang sesuai dengan filter dan pencarian
    private val eventDitampilkan = mutableListOf<HomeEventModel>()

    // Menyimpan kategori yang sedang dipilih
    private var kategoriTerpilih = emptyList<String>()

    // Menyimpan kata kunci pencarian
    private var kataKunciPencarian = ""

    // ViewHolder untuk setiap kartu event
    inner class EventViewHolder(
        val binding: ItemHomeEventBinding
    ) : RecyclerView.ViewHolder(binding.root)

    // Membuat tampilan kartu event
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {

        val binding = ItemHomeEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return EventViewHolder(binding)
    }

    // Menghubungkan data event dengan komponen XML
    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int
    ) {

        val event = eventDitampilkan[position]
        val binding = holder.binding

        // Menampilkan informasi event
        binding.ivEventImage.setImageResource(event.gambarEvent)
        binding.tvLocationEvent.text = event.lokasi
        binding.tvEventTitle.text = event.namaEvent
        binding.tvEventDateTime.text = event.tanggalWaktu
        binding.tvEventCountdown.text = event.hitungMundur
        binding.tvEventTenants.text = event.jumlahTenant
        binding.tvEventPrice.text = event.hargaTiket

        // Menghapus tag kategori sebelumnya
        binding.layoutEventTags.removeAllViews()

        // Membuat tag kategori secara dinamis
        event.kategori.forEach { kategoriEvent ->

            val kartuTag = MaterialCardView(context).apply {
                radius = dpToPx(12).toFloat()
                cardElevation = 0f
                setCardBackgroundColor(Color.WHITE)
                strokeWidth = 0
            }

            val teksTag = TextView(context).apply {
                text = kategoriEvent.uppercase()
                setTextColor(Color.parseColor("#20263A"))
                textSize = 10f
                typeface = context.resources.getFont(R.font.nunito_bold)

                setPadding(
                    dpToPx(10),
                    dpToPx(4),
                    dpToPx(10),
                    dpToPx(4)
                )
            }

            kartuTag.addView(teksTag)

            val parameterTag = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = dpToPx(6)
            }

            binding.layoutEventTags.addView(
                kartuTag,
                parameterTag
            )
        }

        // Memeriksa status favorit event
        perbaruiIkonFavorit(event, holder)

        // Aksi tombol favorit
        binding.cardFavorite.setOnClickListener {

            val posisiSekarang = holder.bindingAdapterPosition

            if (posisiSekarang != RecyclerView.NO_POSITION) {

                FavoritManager.toggleFavorite(
                    context,
                    event.id
                )

                // Memperbarui warna ikon hati
                perbaruiIkonFavorit(event, holder)
            }
        }

        // Aksi tombol Lihat Event
        binding.btnLihatEvent.setOnClickListener {
            onEventClick(event)
        }
    }

    // Mengatur warna ikon hati berdasarkan status favorit
    private fun perbaruiIkonFavorit(
        event: HomeEventModel,
        holder: EventViewHolder
    ) {

        val sudahFavorit = FavoritManager.isFavorite(
            context,
            event.id
        )

        val warnaIkon = if (sudahFavorit) {
            R.color.coral
        } else {
            android.R.color.darker_gray
        }

        holder.binding.ivFavorite.imageTintList =
            android.content.res.ColorStateList.valueOf(
                context.getColor(warnaIkon)
            )
    }

    // Menghitung jumlah event yang ditampilkan
    override fun getItemCount(): Int {
        return eventDitampilkan.size
    }

    // Memasukkan seluruh data event
    fun setData(daftarEvent: List<HomeEventModel>) {

        semuaEvent.clear()
        semuaEvent.addAll(daftarEvent)

        terapkanFilter()
    }

    // Memperbarui kategori yang dipilih
    fun filterKategori(kategori: List<String>) {

        kategoriTerpilih = kategori
        terapkanFilter()
    }

    // Memperbarui kata kunci pencarian
    fun filterPencarian(kataKunci: String) {

        kataKunciPencarian = kataKunci.trim()
        terapkanFilter()
    }

    // Memperbarui tampilan status favorit
    fun perbaruiSemuaIkonFavorit() {
        notifyDataSetChanged()
    }

    // Menggabungkan filter kategori dan pencarian
    private fun terapkanFilter() {

        eventDitampilkan.clear()

        val hasilFilter = semuaEvent.filter { event ->

            // Pemeriksaan kategori menggunakan logika OR
            val sesuaiKategori = kategoriTerpilih.isEmpty() ||
                    event.kategori.any { kategoriEvent ->

                        kategoriTerpilih.any { kategoriDipilih ->

                            kategoriEvent.equals(
                                kategoriDipilih,
                                ignoreCase = true
                            )
                        }
                    }

            // Pemeriksaan kata kunci pencarian
            val sesuaiPencarian = kataKunciPencarian.isBlank() ||
                    event.namaEvent.contains(
                        kataKunciPencarian,
                        ignoreCase = true
                    ) ||
                    event.lokasi.contains(
                        kataKunciPencarian,
                        ignoreCase = true
                    ) ||
                    event.kategori.any { kategoriEvent ->

                        kategoriEvent.contains(
                            kataKunciPencarian,
                            ignoreCase = true
                        )
                    }

            // Kategori DAN pencarian harus sama-sama sesuai
            sesuaiKategori && sesuaiPencarian
        }

        eventDitampilkan.addAll(hasilFilter)

        // Memperbarui tampilan RecyclerView
        notifyDataSetChanged()
    }

    // Mengubah satuan dp menjadi pixel
    private fun dpToPx(dp: Int): Int {
        return (
                dp * context.resources.displayMetrics.density
                ).toInt()
    }
}
