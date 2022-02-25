package com.benshapiro.pricekeeper.ui.Add.Product

import androidx.lifecycle.*
import com.benshapiro.pricekeeper.di.Repository
import com.benshapiro.pricekeeper.model.Price
import com.benshapiro.pricekeeper.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel
@Inject constructor(
    private val repository: Repository
) : ViewModel() {

//    val newProduct = repository.getMostRecentProduct().asLiveData()

    fun addNewItem(name: String, price: String, shop: String, date: String) {
        if (isProductValid(name, price, shop, date)) {
            addNewProduct(name, price, shop, date)
        }
    }

    private fun addNewPrice(name: String, price: String, shop: String, date: String) {
        viewModelScope.launch {
            val newPrice = Price(name = name, price = price.toDouble(), date = date, itemId = repository.getMostRecentProduct().itemId)
            repository.insertPrice(newPrice)
        }
    }

    private fun addNewProduct(name: String, price: String, shop: String, date: String) {
        val newProductInput = Product(name = name, currentPrice = price.toDouble(), shop = shop, priceDate = date, favourite = 0)
        viewModelScope.launch {
            repository.insertProduct(newProductInput)
            }
        addNewPrice(name, price, shop, date)
    }


    private fun isProductValid(
        name: String,
        price: String,
        shop: String,
        date: String
    ): Boolean {
        var formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        formatter.isLenient = false

        if (name.isBlank() || price.isBlank() || shop.isBlank() || date.isBlank()) {
            return false
        }
        return true
    }

}