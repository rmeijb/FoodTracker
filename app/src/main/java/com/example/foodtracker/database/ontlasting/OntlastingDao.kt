package com.example.foodtracker.database.ontlasting

import androidx.room.*
import com.example.foodtracker.database.complaint.Complaint
import kotlinx.coroutines.flow.Flow

@Dao
interface OntlastingDao {
    @Query("SELECT * FROM ontlasting ORDER BY id")
    fun getAll(): Flow<List<Ontlasting>>

    @Query("SELECT * FROM ontlasting WHERE ontlasting.id = :id")
    fun getOntlasting(id: Int): Ontlasting

    @Insert
    suspend fun insertOntlasting(ontlasting: Ontlasting)

    @Update
    suspend fun updateOntlasting(ontlasting: Ontlasting)

    @Delete
    suspend fun deleteOntlasting(ontlasting: Ontlasting)
}