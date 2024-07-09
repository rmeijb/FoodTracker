package com.example.foodtracker.database.consume

import androidx.room.*
import com.example.foodtracker.database.complaint.Complaint
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumeDao {
    @Query("SELECT * FROM consume ORDER BY datetime")
    fun getAll(): Flow<List<Consume>>

    @Query("SELECT * FROM consume WHERE consume.id = :id")
    fun getConsume(id: Int): Consume

    @Insert
    suspend fun insertConsume(consume: Consume)

    @Update
    suspend fun updateConsume(consume: Consume)

    @Delete
    suspend fun deleteConsume(consume: Consume)
}