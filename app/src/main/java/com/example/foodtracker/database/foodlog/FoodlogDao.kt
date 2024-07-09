package com.example.foodtracker.database.foodlog

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodlogDao {
    @Query("SELECT id, product_name as item, 'Consume' as type, datetime as startdate, datetime as enddate, pill FROM consume UNION SELECT id, symptom as item, 'Complaint' as type, start_datetime as startdate, end_datetime as enddate, 0 as pill FROM complaint UNION SELECT id, 'Menstruatie' as item, 'Period' as type, start_datetime as startdate, end_datetime as enddate, 0 as pill FROM period UNION SELECT id, type as item, 'Ontlasting' as type, start_datetime as startdate, start_datetime as enddate, 0 as pill FROM ontlasting ORDER BY startdate")
    fun getAll(): Flow<List<Foodlog>>

    @Query("select 'Complaint' as type, replace(substr(start_datetime,instr( start_datetime, '/')+1,2),'/','') as month, count() as count from complaint group by 1, 2")
    fun getCountPerMonth(): Flow<List<FoodlogMonth>>
}