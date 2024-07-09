package com.example.foodtracker.viewmodels
/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodtracker.FoodlogRepository
import com.example.foodtracker.database.foodlog.Foodlog
import com.example.foodtracker.database.foodlog.FoodlogMonth
import kotlinx.coroutines.flow.Flow

class FoodlogViewModel(private val repository: FoodlogRepository): ViewModel() {
    fun fullConsume(): Flow<List<Foodlog>> = repository.foodtrack
    fun summary(): Flow<List<FoodlogMonth>> = repository.foodSummary
}

class FoodlogViewModelFactory(
    private val consumeRepository: FoodlogRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodlogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodlogViewModel(consumeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
