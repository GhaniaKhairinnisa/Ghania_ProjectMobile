
package com.example.projek_ghania_mobile.Data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class TiketEntity(

    @PrimaryKey
    val ticketId: String,

    val eventId: Int,
    val eventName: String,
    val startDateTimeMillis: Long,
    val location: String,

    val ticketType: String,
    val ticketPrice: Int,

    val paymentMethod: String,
    val purchasedAtMillis: Long,

    val status: String = "ACTIVE"
)
