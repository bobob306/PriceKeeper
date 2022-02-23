package com.benshapiro.pricekeeper.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.NumberFormat
import java.util.*

@Entity(tableName = "product")
data class Product (
    @PrimaryKey(autoGenerate = true)
    val itemId: Int,
    val name: String,
    val shop: String,
    val currentPrice: Double,
    val priceDate: Int,
    val favourite: Int
) : Serializable

fun Product.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance(Locale.UK).format(currentPrice)