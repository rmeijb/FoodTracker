package com.example.foodtracker.database.period

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PeriodDao {
    @Query("SELECT * FROM period ORDER BY id")
    fun getAll(): Flow<List<Period>>

    @Query("SELECT * FROM period WHERE period.id = :id")
    fun getPeriod(id: Int): Period

    @Insert
    suspend fun insertPeriod(period: Period)

    @Update
    suspend fun updatePeriod(period: Period)

    @Delete
    suspend fun deletePeriod(period: Period)
}