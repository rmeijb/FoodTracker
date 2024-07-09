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
import androidx.lifecycle.viewModelScope
import com.example.foodtracker.ConsumeRepository
import com.example.foodtracker.database.complaint.Complaint
import com.example.foodtracker.database.consume.Consume
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ConsumeViewModel(private val repository: ConsumeRepository): ViewModel() {
    fun fullConsume(): Flow<List<Consume>> = repository.consumes

//    suspend fun insertConsume(consume: Consume) = consumeDao.insertConsume(consume)
//
//    suspend fun addConsume(consume: Consume) {
//        insertConsume(consume)
//    }
    fun insertConsume(consume: Consume) = viewModelScope.launch {
    repository.insert(consume)
}
    fun updateConsume(consume: Consume) = viewModelScope.launch {
        repository.update(consume)
    }
    fun deleteConsume(consume: Consume) = viewModelScope.launch {
        repository.delete(consume)
    }
}

class ConsumeViewModelFactory(
    private val consumeRepository: ConsumeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConsumeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConsumeViewModel(consumeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
