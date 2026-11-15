
package com.example.projek_ghania_mobile

import android.content.Context

object FavoritManager {

    private const val NAMA_PENYIMPANAN = "penyimpanan_favorit"
    private const val KUNCI_FAVORIT = "daftar_id_favorit"

    // Mengambil penyimpanan favorit
    private fun getPreferences(context: Context) =
        context.applicationContext.getSharedPreferences(
            NAMA_PENYIMPANAN,
            Context.MODE_PRIVATE
        )

    // Mengambil seluruh ID event yang sudah disukai
    fun getFavoriteIds(context: Context): Set<Int> {

        val dataTersimpan = getPreferences(context)
            .getStringSet(KUNCI_FAVORIT, emptySet())
            ?: emptySet()

        return dataTersimpan.mapNotNull { it.toIntOrNull() }.toSet()
    }

    // Memeriksa apakah sebuah event sudah menjadi favorit
    fun isFavorite(
        context: Context,
        eventId: Int
    ): Boolean {

        return getFavoriteIds(context).contains(eventId)
    }

    // Menambahkan atau menghapus event dari favorit
    // Mengembalikan true jika ditambahkan, false jika dihapus
    fun toggleFavorite(
        context: Context,
        eventId: Int
    ): Boolean {

        val daftarFavorit = getFavoriteIds(context).toMutableSet()

        val berhasilDitambahkan = if (eventId in daftarFavorit) {
            daftarFavorit.remove(eventId)
            false
        } else {
            daftarFavorit.add(eventId)
            true
        }

        getPreferences(context)
            .edit()
            .putStringSet(
                KUNCI_FAVORIT,
                daftarFavorit.map { it.toString() }.toSet()
            )
            .apply()

        return berhasilDitambahkan
    }
}
