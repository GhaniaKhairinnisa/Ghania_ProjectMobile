package com.example.projek_ghania_mobile.Data.Entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(

    @PrimaryKey(autoGenerate = true)
    val eventId: Int = 0,

    val eventName: String,

    val imageResId: Int,

    val categories: String,

    val location: String,

    val startDateTimeMillis: Long,

    val endDateTimeMillis: Long,

    val tenantCount: Int,

    val minTicketPrice: Int,

    val regularPrice: Int,

    val vipPrice: Int,

    val bundlingPrice: Int,

    val posterUri: String? = null


)