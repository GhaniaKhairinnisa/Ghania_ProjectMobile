package com.example.projek_ghania_mobile.Tiket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_ghania_mobile.Data.Entity.TiketEntity
import com.example.projek_ghania_mobile.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RiwayatTiketAdapter :
    RecyclerView.Adapter<RiwayatTiketAdapter.RiwayatViewHolder>() {

    private val daftarTiket = mutableListOf<TiketEntity>()

    fun setData(data: List<TiketEntity>) {
        daftarTiket.clear()
        daftarTiket.addAll(data)
        notifyDataSetChanged()
    }

    inner class RiwayatViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvNamaEvent =
            itemView.findViewById<TextView>(R.id.tvNamaEventTiket)

        private val tvTanggalEvent =
            itemView.findViewById<TextView>(R.id.tvTanggalEventTiket)

        private val tvLokasiEvent =
            itemView.findViewById<TextView>(R.id.tvLokasiEventTiket)

        private val tvJenisTiket =
            itemView.findViewById<TextView>(R.id.tvJenisTiket)

        private val tvKodeTiket =
            itemView.findViewById<TextView>(R.id.tvKodeTiket)

        private val tvStatusTiket =
            itemView.findViewById<TextView>(R.id.tvStatusTiket)

        fun bind(tiket: TiketEntity) {
            tvNamaEvent.text = tiket.eventName
            tvLokasiEvent.text = tiket.location
            tvJenisTiket.text = tiket.ticketType
            tvKodeTiket.text = "Kode: ${tiket.ticketId}"
            tvStatusTiket.text = tiket.status

            val formatTanggal = SimpleDateFormat(
                "dd MMMM yyyy, HH:mm",
                Locale("id", "ID")
            )

            tvTanggalEvent.text = formatTanggal.format(
                Date(tiket.startDateTimeMillis)
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tiket_digital, parent, false)

        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RiwayatViewHolder,
        position: Int
    ) {
        holder.bind(daftarTiket[position])
    }

    override fun getItemCount(): Int = daftarTiket.size
}
