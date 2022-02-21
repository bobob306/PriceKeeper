package com.benshapiro.pricekeeper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.benshapiro.pricekeeper.model.Price

@Database(entities = [Price::class], version = 1)
abstract class PriceDatabase : RoomDatabase() {
    abstract fun getPriceDao(): PriceDao
}