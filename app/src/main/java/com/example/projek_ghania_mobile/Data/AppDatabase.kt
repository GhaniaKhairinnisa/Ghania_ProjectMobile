
package com.example.projek_ghania_mobile.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projek_ghania_mobile.Dao.TenantDao
import com.example.projek_ghania_mobile.Data.Dao.EventDao
import com.example.projek_ghania_mobile.Data.Dao.FavoriteDao
import com.example.projek_ghania_mobile.Data.Dao.TenantRegistrationDao
import com.example.projek_ghania_mobile.Data.Entity.EventEntity
import com.example.projek_ghania_mobile.Data.Entity.FavoriteEntity
import com.example.projek_ghania_mobile.Data.Entity.TenantEntity
import com.example.projek_ghania_mobile.Data.Entity.TenantRegistrationEntity

@Database(
    entities = [
        EventEntity::class,
        FavoriteEntity::class,
        TenantEntity::class,
        TenantRegistrationEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    abstract fun favoriteDao(): FavoriteDao

    abstract fun tenantDao(): TenantDao

    abstract fun tenantRegistrationDao(): TenantRegistrationDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "popkraf_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
