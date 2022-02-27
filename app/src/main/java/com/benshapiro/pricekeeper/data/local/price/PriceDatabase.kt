package com.benshapiro.pricekeeper.data.local.price

import androidx.room.Database
import androidx.room.RoomDatabase
import com.benshapiro.pricekeeper.data.local.product.ProductDao
import com.benshapiro.pricekeeper.model.Price
import com.benshapiro.pricekeeper.model.Product
import dagger.Module

@Database(entities = [Price::class, Product::class], version = 4)
abstract class PriceDatabase : RoomDatabase() {
    abstract fun getPriceDao(): PriceDao
    abstract fun getProductDao(): ProductDao
}