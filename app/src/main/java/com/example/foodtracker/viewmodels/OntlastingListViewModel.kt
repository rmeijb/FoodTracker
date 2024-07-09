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
import com.example.foodtracker.ComplaintRepository
import com.example.foodtracker.ConsumeRepository
import com.example.foodtracker.OntlastingRepository
import com.example.foodtracker.database.complaint.Complaint
import com.example.foodtracker.database.consume.Consume
import com.example.foodtracker.database.ontlasting.Ontlasting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class OntlastingViewModel(private val repository: OntlastingRepository): ViewModel() {
    fun fullConsume(): Flow<List<Ontlasting>> = repository.ontlasting

//    suspend fun insertConsume(consume: Consume) = consumeDao.insertConsume(consume)
//
//    suspend fun addConsume(consume: Consume) {
//        insertConsume(consume)
//    }

    fun insertOntlasting(ontlasting: Ontlasting) = viewModelScope.launch {
        repository.insert(ontlasting)
    }
    fun updateOntlasting(ontlasting: Ontlasting) = viewModelScope.launch {
        repository.update(ontlasting)
    }
    fun deleteOntlasting(ontlasting: Ontlasting) = viewModelScope.launch {
        repository.delete(ontlasting)
    }
}

class OntlastingViewModelFactory(
    private val ontlastingRepository: OntlastingRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OntlastingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OntlastingViewModel(ontlastingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
