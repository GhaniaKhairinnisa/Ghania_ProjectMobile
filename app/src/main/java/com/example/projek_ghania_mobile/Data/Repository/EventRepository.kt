
package com.example.projek_ghania_mobile.Data.Repository

import com.example.projek_ghania_mobile.Data.Dao.EventDao
import com.example.projek_ghania_mobile.Data.Entity.EventEntity
import kotlinx.coroutines.flow.Flow

class EventRepository(
    private val eventDao: EventDao
) {

    fun getAllEvents(): Flow<List<EventEntity>> {
        return eventDao.getAllEvents()
    }

    suspend fun getEventById(eventId: Int): EventEntity? {
        return eventDao.getEventById(eventId)
    }

    suspend fun insertEvent(event: EventEntity) {
        eventDao.insertEvent(event)
    }

    suspend fun insertEvents(events: List<EventEntity>) {
        eventDao.insertEvents(events)
    }

    suspend fun updateEvent(event: EventEntity) {
        eventDao.updateEvent(event)
    }

    suspend fun deleteEventById(eventId: Int) {
        eventDao.deleteEventById(eventId)
    }

    suspend fun deleteAllEvents() {
        eventDao.deleteAllEvents()
    }
}
