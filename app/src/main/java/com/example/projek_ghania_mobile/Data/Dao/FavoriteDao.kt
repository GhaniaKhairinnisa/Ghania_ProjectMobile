package com.example.projek_ghania_mobile.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projek_ghania_mobile.Data.Entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT eventId FROM favorite_events")
    fun getFavoriteEventIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorite_events WHERE eventId = :eventId")
    suspend fun deleteFavorite(eventId: Int)

    @Query("DELETE FROM favorite_events")
    suspend fun deleteAllFavorites()
}
