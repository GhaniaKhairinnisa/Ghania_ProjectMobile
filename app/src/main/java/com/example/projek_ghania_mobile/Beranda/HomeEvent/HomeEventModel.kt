
package com.example.projek_ghania_mobile.Beranda.HomeEvent

data class HomeEventModel(
    val id: Int,
    val gambarEvent: Int,
    val kategori: List<String>,
    val lokasi: String,
    val namaEvent: String,
    val tanggalWaktu: String,
    val hitungMundur: String,
    val jumlahTenant: String,
    val hargaTiket: String,
    val posterUri: String? = null
)