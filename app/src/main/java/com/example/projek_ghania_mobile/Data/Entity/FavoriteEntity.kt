package com.example.projek_ghania_mobile.Data.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEntity(
    @PrimaryKey
    val eventId: Int
)
