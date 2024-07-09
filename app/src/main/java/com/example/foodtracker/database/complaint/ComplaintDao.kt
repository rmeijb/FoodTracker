package com.example.foodtracker.database.complaint

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintDao {
    @Query("SELECT * FROM complaint ORDER BY id")
    fun getAll(): Flow<List<Complaint>>

    @Query("SELECT * FROM complaint WHERE complaint.id = :id")
    fun getComplaint(id: Int): Complaint

    @Insert
    suspend fun insertComplaint(complaint: Complaint)

    @Update
    suspend fun updateComplaint(complaint: Complaint)

    @Delete
    suspend fun deleteComplaint(complaint: Complaint)

}