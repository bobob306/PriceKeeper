package com.benshapiro.pricekeeper.data.local.price

import androidx.room.Database
import androidx.room.RoomDatabase
import com.benshapiro.pricekeeper.model.Price
import com.benshapiro.pricekeeper.model.Product

@Database(entities = [Price::class, Product::class], version = 1)
abstract class PriceDatabase : RoomDatabase() {
    abstract fun getPriceDao(): PriceDao
}