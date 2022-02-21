package com.benshapiro.pricekeeper.data.local

import androidx.room.*
import com.benshapiro.pricekeeper.model.Price
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(price: List<Price>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewPrice(price: Price)

    @Update
    suspend fun update(price: Price)

    @Query("SELECT * FROM table_prices WHERE name LIKE :price")
    fun searchDatabase(price: String): Flow<List<Price>>

    // This simply deletes all values
    // Again this must be a suspend fun, signifying it will utilise coroutines
    @Query("DELETE FROM table_prices")
    suspend fun deleteAll()
}