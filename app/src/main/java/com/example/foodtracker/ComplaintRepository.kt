package com.example.foodtracker

import androidx.recyclerview.widget.ListAdapter
import com.example.foodtracker.database.complaint.Complaint
import com.example.foodtracker.database.complaint.ComplaintDao
import com.example.foodtracker.database.consume.Consume
import com.example.foodtracker.database.consume.ConsumeDao

class ComplaintRepository(private val dao: ComplaintDao) {

    val complaints = dao.getAll()

    suspend fun insert(complaint: Complaint) {
        return dao.insertComplaint(complaint)
    }

    suspend fun update(complaint: Complaint) {
        return dao.updateComplaint(complaint)
    }

    suspend fun delete(complaint: Complaint) {
        return dao.deleteComplaint(complaint)
    }
}



