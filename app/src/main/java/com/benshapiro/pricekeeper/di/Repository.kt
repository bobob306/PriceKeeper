package com.benshapiro.pricekeeper.di

import com.benshapiro.pricekeeper.data.SortOrder
import com.benshapiro.pricekeeper.data.local.price.PriceDao
import com.benshapiro.pricekeeper.data.local.price.PriceDatabase
import com.benshapiro.pricekeeper.data.local.product.ProductDao
import com.benshapiro.pricekeeper.model.Price
import com.benshapiro.pricekeeper.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository
@Inject constructor(
    private val priceDao: PriceDao,
    private val productDao: ProductDao,
    private val priceDatabase: PriceDatabase
) {
    fun getProductsList(query: String, sortOrder: SortOrder) : Flow<List<Product>> {
        return productDao.getProductList(query, sortOrder)
    }
    suspend fun insertProduct(product: Product) {
        productDao.insert(product)
    }
    suspend fun updateProduct(product: Product) {
        productDao.update(product)
    }
    suspend fun getMostRecentProduct() : Product {
        return productDao.getMostRecentProduct()
    }
    fun getProductById(itemId: Int) : Flow<Product> {
        return productDao.getProductById(itemId)
    }

    fun getPriceHistory(itemId: Int) : Flow<List<Price>> {
        return priceDao.getPriceHistory(itemId)
    }
    suspend fun updatePrice(newPriceEntry: Price) {
        priceDao.update(newPriceEntry)
    }
    suspend fun insertPrice(price: Price) {
        priceDao.insert(price)
    }
    fun getPriceById(id: Int) : Flow<Price> {
        return priceDao.getPriceById(id)
    }
}