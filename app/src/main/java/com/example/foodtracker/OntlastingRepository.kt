package com.example.foodtracker

import androidx.recyclerview.widget.ListAdapter
import com.example.foodtracker.database.complaint.Complaint
import com.example.foodtracker.database.complaint.ComplaintDao
import com.example.foodtracker.database.consume.Consume
import com.example.foodtracker.database.consume.ConsumeDao
import com.example.foodtracker.database.ontlasting.Ontlasting
import com.example.foodtracker.database.ontlasting.OntlastingDao

class OntlastingRepository(private val dao: OntlastingDao) {

    val ontlasting = dao.getAll()

    suspend fun insert(ontlasting: Ontlasting) {
        return dao.insertOntlasting(ontlasting)
    }

    suspend fun update(ontlasting: Ontlasting) {
        return dao.updateOntlasting(ontlasting)
    }

    suspend fun delete(ontlasting: Ontlasting) {
        return dao.deleteOntlasting(ontlasting)
    }

}



