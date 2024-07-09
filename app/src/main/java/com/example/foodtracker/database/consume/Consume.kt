package com.example.foodtracker.database.consume

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "consume")
data class Consume(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "product_id") var productId: Int,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "datetime") var datetime: String,
    @ColumnInfo(name = "unit", defaultValue = "stuks") var unit: String,
    @ColumnInfo(name = "quantity") var quantity: Double,
    @ColumnInfo(name = "pill") var pill: Int
) : java.io.Serializable
