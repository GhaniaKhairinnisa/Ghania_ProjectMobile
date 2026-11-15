
package com.example.projek_ghania_mobile.Jelajahi

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.projek_ghania_mobile.Beranda.DetailActivity
import com.example.projek_ghania_mobile.FavoritManager
import com.example.projek_ghania_mobile.R
import com.example.projek_ghania_mobile.databinding.ItemEventListBinding

class JelajahiAdapter(
    context: Context,
    private val events: List<JelajahiModel>
) : ArrayAdapter<JelajahiModel>(context, 0, events) {

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        val binding = ItemEventListBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )

        val data = events[position]

        // Menampilkan informasi event
        binding.ivEventPic.setImageResource(data.gambarEvent)
        binding.tvEventName.text = data.namaEvent
        binding.tvEventLocation.text = data.lokasiEvent

        // Memperbarui warna ikon berdasarkan status favorit
        perbaruiIkonFavorit(binding, data)

        // Aksi ketika ikon hati ditekan
        binding.cardFavorite.setOnClickListener {

            FavoritManager.toggleFavorite(
                context,
                data.id
            )

            // Memperbarui warna ikon setelah ditekan
            perbaruiIkonFavorit(binding, data)
        }

        // Aksi ketika kartu event ditekan
        binding.root.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            context.startActivity(intent)
        }

        return binding.root
    }

    // Mengatur warna ikon hati
    private fun perbaruiIkonFavorit(
        binding: ItemEventListBinding,
        data: JelajahiModel
    ) {

        val sudahFavorit = FavoritManager.isFavorite(
            context,
            data.id
        )

        val warna = if (sudahFavorit) {
            R.color.coral
        } else {
            android.R.color.darker_gray
        }

        binding.ivFavorite.imageTintList =
            android.content.res.ColorStateList.valueOf(
                ContextCompat.getColor(context, warna)
            )
    }
}
