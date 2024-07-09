package com.example.foodtracker.database.complaint

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaint")
data class Complaint(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "symptom") var symptom: String,
    @ColumnInfo(name = "start_datetime") var startdatetime: String,
    @ColumnInfo(name = "end_datetime") var enddatetime: String,
    @ColumnInfo(name = "severeness") var severeness: Int,
    @ColumnInfo(name = "description") var description: String
): java.io.Serializable