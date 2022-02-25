package com.benshapiro.pricekeeper.data.local.product

import androidx.room.*
import com.benshapiro.pricekeeper.data.SortOrder
import com.benshapiro.pricekeeper.model.Product
import com.benshapiro.pricekeeper.model.relations.productWithPrices
import dagger.Provides
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
    fun getProductList(query: String, sortOrder: SortOrder): Flow<List<Product>> =
        when (sortOrder) {
            SortOrder.BY_NAME -> getProductsSortedByName(query)
            SortOrder.BY_PRICE -> getProductsSortedByPrice(query)
            SortOrder.BY_SHOP -> getProductsSortedByShop(query)
        }
    @Query("SELECT * FROM product WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun getProductsSortedByName(searchQuery: String): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE name LIKE '%' || :searchQuery || '%' ORDER BY currentPrice ASC")
    fun getProductsSortedByPrice(searchQuery: String): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE name LIKE '%' || :searchQuery || '%' ORDER BY shop ASC")
    fun getProductsSortedByShop(searchQuery: String): Flow<List<Product>>

    @Query("SELECT * FROM product ORDER BY itemId DESC LIMIT 1")
    suspend fun getMostRecentProduct(): Product

}