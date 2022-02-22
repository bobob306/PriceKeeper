package com.benshapiro.pricekeeper.data.local.product

import androidx.room.*
import com.benshapiro.pricekeeper.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM product ORDER BY name asc")
    fun getProductList(): Flow<List<Product>>
}