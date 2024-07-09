package com.example.foodtracker

import com.example.foodtracker.database.period.Period
import com.example.foodtracker.database.period.PeriodDao

class PeriodRepository(private val dao: PeriodDao) {

    val periods = dao.getAll()

    suspend fun insert(period: Period) {
        return dao.insertPeriod(period)
    }

    suspend fun update(period: Period) {
        return dao.updatePeriod(period)
    }

    suspend fun delete(period: Period) {
        return dao.deletePeriod(period)
    }
}



