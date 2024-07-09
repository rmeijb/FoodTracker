package com.example.foodtracker

import com.example.foodtracker.database.foodlog.Foodlog
import com.example.foodtracker.database.foodlog.FoodlogDao
import kotlinx.coroutines.flow.*

class FoodlogRepository(foodlogDao: FoodlogDao, ) {

    //val consumes = consumeDao.getAll()
    //val complaints = complaintDao.getAll()
    val foodtrack = foodlogDao.getAll()
    val foodSummary = foodlogDao.getCountPerMonth()

//    fun getAll(): Flow<List<Foodlog>> {
//        val foodlogs:Flow<List<Foodlog>> = flow {
//            foodtrack.collect() {
//                val consumeList = ArrayList<Foodlog>()
//                it.forEach() {
//                    val foodlog = Foodlog(0, it.productName, Type.Consume, it.datetime)
//                    consumeList.add(foodlog)
//                }
//                emit(consumeList)
//            }
//        }
//        val foodlogs2:Flow<List<Foodlog>> = flow {
//            complaints.collect() {
//                val complaintList = ArrayList<Foodlog>()
//                it.forEach() {
//                   val foodlog = Foodlog(0,it.symptom, Type.Complaint, it.startdatetime )
//                   complaintList.add(foodlog)
//                }
//                emit(complaintList)
//            }
//        }
//        return merge(foodlogs, foodlogs2)
//        return foodlogs
//    }


}
