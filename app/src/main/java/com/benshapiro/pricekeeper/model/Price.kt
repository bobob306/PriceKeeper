package com.benshapiro.pricekeeper.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.io.Serializable
import java.text.NumberFormat
import java.util.*

@Entity(tableName = "table_prices")
data class Price(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val price: Double,
    val date: Int,
    val name: String,
    val itemId: Int
) : Serializable

fun Price.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance(Locale.UK).format(price)