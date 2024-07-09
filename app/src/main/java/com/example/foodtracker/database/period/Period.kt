package com.example.foodtracker.database.period

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "period")
data class Period(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "start_datetime") var startdatetime: String,
    @ColumnInfo(name = "end_datetime") var enddatetime: String
): java.io.Serializable