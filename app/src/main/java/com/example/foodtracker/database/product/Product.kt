package com.example.foodtracker.database.product

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "category_id", defaultValue = "1") val categoryId: Int,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "unit", defaultValue = "stuks") val unit: String
)