package com.benshapiro.pricekeeper.data.local.price

import androidx.room.*
import com.benshapiro.pricekeeper.model.Price
import kotlinx.coroutines.flow.Flow

@Dao
interface PriceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(price: Price)

    @Update
    suspend fun update(price: Price)

    @Query("SELECT * FROM table_prices WHERE itemId =:itemId ORDER BY date DESC")
    fun getPriceHistory(itemId: Int): Flow<List<Price>>

    // This simply deletes all values
    // Again this must be a suspend fun, signifying it will utilise coroutines
    @Query("DELETE FROM table_prices")
    suspend fun deleteAll()

    @Query("SELECT * FROM table_prices WHERE id =:id")
    fun getPriceById(id: Int): Flow<Price>
}