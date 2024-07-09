package com.example.foodtracker

import com.example.foodtracker.database.complaint.Complaint
import com.example.foodtracker.database.consume.Consume
import com.example.foodtracker.database.consume.ConsumeDao

class ConsumeRepository(private val dao: ConsumeDao) {

    val consumes = dao.getAll()

    suspend fun insert(consume: Consume) {
        return dao.insertConsume(consume)
    }

    suspend fun update(consume: Consume) {
        return dao.updateConsume(consume)
    }

    suspend fun delete(consume: Consume) {
        return dao.deleteConsume(consume)
    }
}
