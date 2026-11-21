
package com.example.projek_ghania_mobile.Data.Seed

import com.example.projek_ghania_mobile.Data.Dao.EventDao
import com.example.projek_ghania_mobile.Data.Entity.EventEntity
import com.example.projek_ghania_mobile.R
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.ZoneId

object EventSeeder {

    suspend fun seedIfEmpty(eventDao: EventDao) {

        // Periksa apakah tabel events sudah memiliki data
        val dataEvent = eventDao.getAllEvents().first()

        if (dataEvent.isNotEmpty()) {
            return
        }

        val zonaWaktu = ZoneId.of("Asia/Jakarta")

        fun konversiWaktu(waktu: String): Long {
            return LocalDateTime.parse(waktu)
                .atZone(zonaWaktu)
                .toInstant()
                .toEpochMilli()
        }

        val daftarEvent = listOf(

            EventEntity(
                eventId = 1,
                eventName = "Riau Creative Fest 2026",
                imageResId = R.drawable.event_pku,
                categories = "Bazar|UMKM|Musik",
                location = "📍 Bandar Seni Raja Ali Haji",
                startDateTimeMillis = konversiWaktu("2026-12-12T09:00:00"),
                endDateTimeMillis = konversiWaktu("2026-12-12T20:00:00"),
                tenantCount = 80,
                minTicketPrice = 25000,
                regularPrice = 25000,
                vipPrice = 75000,
                bundlingPrice = 50000
            ),

            EventEntity(
                eventId = 2,
                eventName = "Pekanbaru Art Exhibition",
                imageResId = R.drawable.event_pku,
                categories = "Pameran|Komunitas",
                location = "📍 Taman Budaya Pekanbaru",
                startDateTimeMillis = konversiWaktu("2026-12-18T10:00:00"),
                endDateTimeMillis = konversiWaktu("2026-12-18T18:00:00"),
                tenantCount = 25,
                minTicketPrice = 15000,
                regularPrice = 15000,
                vipPrice = 50000,
                bundlingPrice = 35000
            ),

            EventEntity(
                eventId = 3,
                eventName = "Festival Kuliner Riau",
                imageResId = R.drawable.event_pku,
                categories = "Bazar|Kuliner|Festival",
                location = "📍 Lapangan Purna MTQ Pekanbaru",
                startDateTimeMillis = konversiWaktu("2026-12-20T11:00:00"),
                endDateTimeMillis = konversiWaktu("2026-12-20T21:00:00"),
                tenantCount = 50,
                minTicketPrice = 0,
                regularPrice = 0,
                vipPrice = 40000,
                bundlingPrice = 25000
            ),

            EventEntity(
                eventId = 4,
                eventName = "Pekanbaru Indie Music Day",
                imageResId = R.drawable.event_pku,
                categories = "Musik|Festival",
                location = "📍 Lapangan Purna MTQ Pekanbaru",
                startDateTimeMillis = konversiWaktu("2026-12-26T15:00:00"),
                endDateTimeMillis = konversiWaktu("2026-12-26T22:00:00"),
                tenantCount = 12,
                minTicketPrice = 35000,
                regularPrice = 35000,
                vipPrice = 100000,
                bundlingPrice = 65000
            ),

            EventEntity(
                eventId = 5,
                eventName = "UMKM Creative Market",
                imageResId = R.drawable.event_pku,
                categories = "UMKM|Bazar",
                location = "📍 Mall Pekanbaru",
                startDateTimeMillis = konversiWaktu("2027-01-03T10:00:00"),
                endDateTimeMillis = konversiWaktu("2027-01-03T21:00:00"),
                tenantCount = 40,
                minTicketPrice = 0,
                regularPrice = 0,
                vipPrice = 30000,
                bundlingPrice = 20000
            ),

            EventEntity(
                eventId = 6,
                eventName = "Creative Workshop Day",
                imageResId = R.drawable.event_pku,
                categories = "Workshop|Komunitas",
                location = "📍 Gedung Kreatif Pekanbaru",
                startDateTimeMillis = konversiWaktu("2027-01-09T09:00:00"),
                endDateTimeMillis = konversiWaktu("2027-01-09T16:00:00"),
                tenantCount = 8,
                minTicketPrice = 20000,
                regularPrice = 20000,
                vipPrice = 60000,
                bundlingPrice = 45000
            ),

            EventEntity(
                eventId = 7,
                eventName = "Riau Craft & Handmade Expo",
                imageResId = R.drawable.event_pku,
                categories = "Pameran|UMKM|Workshop",
                location = "📍 Taman Budaya Pekanbaru",
                startDateTimeMillis = konversiWaktu("2027-01-16T10:00:00"),
                endDateTimeMillis = konversiWaktu("2027-01-16T19:00:00"),
                tenantCount = 35,
                minTicketPrice = 10000,
                regularPrice = 10000,
                vipPrice = 45000,
                bundlingPrice = 30000
            ),

            EventEntity(
                eventId = 8,
                eventName = "Pekanbaru Creative Community Day",
                imageResId = R.drawable.event_pku,
                categories = "Komunitas|Musik|Festival",
                location = "📍 Bandar Seni Raja Ali Haji",
                startDateTimeMillis = konversiWaktu("2027-01-23T13:00:00"),
                endDateTimeMillis = konversiWaktu("2027-01-23T21:00:00"),
                tenantCount = 20,
                minTicketPrice = 0,
                regularPrice = 0,
                vipPrice = 50000,
                bundlingPrice = 30000
            )
        )

        // Simpan seluruh event ke database
        eventDao.insertEvents(daftarEvent)
    }
}
