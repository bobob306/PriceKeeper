package com.benshapiro.pricekeeper.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.benshapiro.pricekeeper.model.Price
import com.benshapiro.pricekeeper.model.Product

data class productWithPrices(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "itemId",
        entityColumn = "itemId"
    )
    val priceHistory: List<Price>
)
