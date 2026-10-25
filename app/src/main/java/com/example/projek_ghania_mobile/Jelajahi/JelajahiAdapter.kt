package com.example.projek_ghania_mobile.Jelajahi

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.projek_ghania_mobile.Beranda.DetailActivity
import com.example.projek_ghania_mobile.databinding.ItemEventListBinding

class JelajahiAdapter(
    context: Context,
    private val events: List<JelajahiModel>
) : ArrayAdapter<JelajahiModel>(context, 0, events) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemEventListBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root

        val data = events[position]

        binding.ivEventPic.setImageResource(data.gambarEvent)
        binding.tvEventName.text = data.namaEvent
        binding.tvEventLocation.text = data.lokasiEvent

        view.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            context.startActivity(intent)
        }

        return view
    }
}