package com.benshapiro.pricekeeper.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.NumberFormat
import java.util.*

@Entity(tableName = "table_prices")
data class Price(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val price: Double,
    val quantity: Double,
    val date: String,
    val itemId: Int
) : Serializable

fun Price.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance(Locale.UK).format(price)
fun Price.getValue(): String =
    NumberFormat.getCurrencyInstance(Locale.UK).format(price/quantity) + " per unit"