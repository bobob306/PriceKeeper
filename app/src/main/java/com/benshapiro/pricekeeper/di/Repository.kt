package com.benshapiro.pricekeeper.di

import com.benshapiro.pricekeeper.data.local.price.PriceDao
import com.benshapiro.pricekeeper.data.local.product.ProductDao
import com.benshapiro.pricekeeper.model.Price
import com.benshapiro.pricekeeper.model.Product
import javax.inject.Inject

class Repository
@Inject constructor(
    private val priceDao: PriceDao,
    private val productDao: ProductDao
) {
    fun getProductsList() {
        productDao.getProductList()
    }
    suspend fun insertProduct(product: Product) {
        productDao.insert(product)
    }
    suspend fun updateProduct(product: Product) {
        productDao.update(product)
    }

    fun getPriceHistory(itemId: Int) {
        priceDao.searchDatabase(itemId)
    }
    suspend fun updatePrice(price: Price) {
        priceDao.update(price)
    }
    suspend fun insertPrice(price: Price) {
        priceDao.insert(price)
    }
}