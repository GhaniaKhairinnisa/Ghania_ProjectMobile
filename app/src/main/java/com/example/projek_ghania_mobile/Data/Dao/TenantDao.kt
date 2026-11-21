
package com.example.projek_ghania_mobile.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.projek_ghania_mobile.Data.Entity.TenantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TenantDao {

    @Query("SELECT * FROM tenants ORDER BY tenantId DESC")
    fun getAllTenants(): Flow<List<TenantEntity>>

    @Query("SELECT * FROM tenants WHERE tenantId = :tenantId")
    suspend fun getTenantById(tenantId: Int): TenantEntity?

    @Insert
    suspend fun insertTenant(tenant: TenantEntity): Long

    @Query("DELETE FROM tenants WHERE tenantId = :tenantId")
    suspend fun deleteTenant(tenantId: Int)
}
