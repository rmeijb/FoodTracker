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
import com.example.foodtracker.database.category.Category
import com.example.foodtracker.database.category.CategoryDao
import kotlinx.coroutines.flow.Flow

class CategoryViewModel(private val categoryDao: CategoryDao): ViewModel() {

    //fun fullSchedule(): List<Category> = categoryDao.getAll()
    fun fullSchedule(): Flow<List<Category>> = categoryDao.getAll()

    //fun addCategory(id: Int, category: String, image: ByteArray) = categoryDao.addCategory(id, category, image)

    //fun scheduleForStopName(name: String): Flow<List<Product>> = productDao.getAll()
}

class CategoryViewModelFactory(
    private val categoryDao: CategoryDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(categoryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
