
package com.example.projek_ghania_mobile.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.projek_ghania_mobile.Data.Entity.TiketEntity

@Dao
interface TiketDao {

    @Insert
    suspend fun insertTiket(tiket: TiketEntity)

    @Insert
    suspend fun insertDaftarTiket(
        daftarTiket: List<TiketEntity>
    )

    @Query("SELECT * FROM tickets ORDER BY purchasedAtMillis DESC")
    suspend fun getSemuaTiket(): List<TiketEntity>

    @Query("SELECT * FROM tickets WHERE status = 'ACTIVE' ORDER BY purchasedAtMillis DESC")
    suspend fun getTiketAktif(): List<TiketEntity>

    @Query("SELECT * FROM tickets WHERE status != 'ACTIVE' ORDER BY purchasedAtMillis DESC")
    suspend fun getRiwayatTiket(): List<TiketEntity>
}
