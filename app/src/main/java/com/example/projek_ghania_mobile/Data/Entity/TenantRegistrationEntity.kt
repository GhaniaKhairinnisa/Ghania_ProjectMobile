package com.example.projek_ghania_mobile.Data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tenant_registrations")
data class TenantRegistrationEntity(
    @PrimaryKey(autoGenerate = true)
    val registrationId: Int = 0,

    val tenantId: Int,
    val eventId: Int,
    val status: String = "Menunggu"
)
