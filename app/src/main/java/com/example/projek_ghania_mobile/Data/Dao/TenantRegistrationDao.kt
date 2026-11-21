package com.example.projek_ghania_mobile.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.projek_ghania_mobile.Data.Entity.TenantRegistrationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TenantRegistrationDao {

    @Query("SELECT * FROM tenant_registrations ORDER BY registrationId DESC")
    fun getAllRegistrations(): Flow<List<TenantRegistrationEntity>>

    @Query("SELECT * FROM tenant_registrations WHERE tenantId = :tenantId")
    fun getRegistrationsByTenant(tenantId: Int): Flow<List<TenantRegistrationEntity>>

    @Insert
    suspend fun insertRegistration(
        registration: TenantRegistrationEntity
    )

    @Query("DELETE FROM tenant_registrations WHERE registrationId = :registrationId")
    suspend fun deleteRegistration(registrationId: Int)
}
