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
import com.example.foodtracker.ProductRepository
import com.example.foodtracker.database.product.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository): ViewModel() {
    fun fullSchedule(categoryId: Int): Flow<List<Product>>? = repository.getProducts(categoryId)
        //.getAll(categoryId)

    //fun addProduct(id: Int, categoryId: Int, productName: String, unit: String) = productDao.addProduct(id, categoryId, productName, unit)

    //fun scheduleForStopName(name: String): Flow<List<Product>> = productDao.getAll()
    fun insertProduct(product: Product) = viewModelScope.launch {
        repository.insert(product)
    }
    fun updateProduct(product: Product) = viewModelScope.launch {
        repository.update(product)
    }

}

class ProductViewModelFactory(
    private val productRepository: ProductRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
