package com.example.projek_ghania_mobile.Data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tenants")
data class TenantEntity(
    @PrimaryKey(autoGenerate = true)
    val tenantId: Int = 0,

    val namaTenant: String,
    val namaPemilik: String,
    val kategori: String,
    val deskripsi: String,
    val nomorTelepon: String,
    val imageUri: String
)