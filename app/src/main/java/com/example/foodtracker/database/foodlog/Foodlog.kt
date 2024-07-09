package com.example.foodtracker.database.foodlog

enum class Type {
    Consume, Complaint, Ontlasting, Period
}
class Foodlog(val id: Int = 0, val item: String, val type: Type, val startdate: String, val enddate: String, val pill:Int = 0 )