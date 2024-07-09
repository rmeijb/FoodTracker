package com.example.foodtracker

import com.example.foodtracker.database.consume.Consume
import com.example.foodtracker.database.consume.ConsumeDao
import com.example.foodtracker.database.product.Product
import com.example.foodtracker.database.product.ProductDao
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val dao: ProductDao) {

    var products: Flow<List<Product>>? = null

    fun getProducts(categoryId: Int): Flow<List<Product>>? {
        products = dao.getAll(categoryId)
        return products
    }
    suspend fun insert(product: Product) {
        return dao.insertProduct(product)
    }

    suspend fun update(product: Product) {
        return dao.updateProduct(product)
    }
}
