package com.example.projek_ghania_mobile.Tiket

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_ghania_mobile.Data.Entity.TiketEntity
import com.example.projek_ghania_mobile.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.EnumMap
import java.util.Locale

class TiketDigitalAdapter :
    RecyclerView.Adapter<TiketDigitalAdapter.TiketViewHolder>() {

    private val daftarTiket = mutableListOf<TiketEntity>()

    fun setData(data: List<TiketEntity>) {
        daftarTiket.clear()
        daftarTiket.addAll(data)
        notifyDataSetChanged()
    }

    inner class TiketViewHolder(itemView: View) :
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

        private val ivQrTiket =
            itemView.findViewById<ImageView>(R.id.ivQrTiket)

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

            // Membuat QR Code dari kode tiket unik
            try {
                val qrBitmap = generateQrCode(tiket.ticketId)
                ivQrTiket.setImageBitmap(qrBitmap)
            } catch (e: Exception) {
                ivQrTiket.setImageDrawable(null)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TiketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tiket_digital, parent, false)

        return TiketViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TiketViewHolder,
        position: Int
    ) {
        holder.bind(daftarTiket[position])
    }

    override fun getItemCount(): Int = daftarTiket.size

    private fun generateQrCode(content: String): Bitmap {
        val width = 512
        val height = 512

        val hints = EnumMap<EncodeHintType, Any>(
            EncodeHintType::class.java
        )

        hints[EncodeHintType.MARGIN] = 1

        val bitMatrix = QRCodeWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            width,
            height,
            hints
        )

        val bitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                )
            }
        }

        return bitmap
    }
}
