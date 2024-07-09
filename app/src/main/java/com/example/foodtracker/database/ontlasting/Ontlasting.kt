package com.example.foodtracker.database.ontlasting

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ontlasting")
data class Ontlasting(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "start_datetime") var startdatetime: String,
    @ColumnInfo(name = "type") var type: String
): java.io.Serializable