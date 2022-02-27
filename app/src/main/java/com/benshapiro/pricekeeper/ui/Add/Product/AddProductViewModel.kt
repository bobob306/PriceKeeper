package com.benshapiro.pricekeeper.ui.Add.Product

import android.util.Log
import androidx.lifecycle.*
import com.benshapiro.pricekeeper.di.Repository
import com.benshapiro.pricekeeper.model.Price
import com.benshapiro.pricekeeper.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel
@Inject constructor(
    private val repository: Repository, private val state: SavedStateHandle
) : ViewModel() {

    var dateStatus = MutableLiveData<Boolean?>()

    val productId = state.get<Product>("Product")?.itemId ?: -1
    val currentProduct = if (productId != -1) {
        repository.getProductById(productId).asLiveData()} else { null }

    fun addNewItem(name: String, price: String, shop: String, quantity: String, date: String) {
        if (productId != -1) {
            // update product
            if (isProductValid(name, price, shop, quantity, date)) {
                updateProduct(name, price, shop, quantity, date)
            }
        } else //insert new product
            {
            if (isProductValid(name, price, shop, quantity, date)) {
                addNewProduct(name, price, shop, quantity, date)
            }
        }
    }

    private fun updateProduct(name: String, price: String, shop: String, quantity: String, date: String) {
        val updatedProduct = Product(itemId = productId , name = name, currentPrice = price.toDouble(), shop = shop, quantity = quantity.toDouble(), priceDate = date, favourite = 0)
        viewModelScope.launch {
            repository.updateProduct(updatedProduct)
        }
        triggerSuccessUpdateEvent(name)
    }

    private fun addNewPrice(name: String, price: String, shop: String, quantity: String, date: String) {
        viewModelScope.launch {
            val newItemId : Int = repository.getMostRecentProduct().itemId
            val newPrice = Price(name = name, price = price.toDouble(), date = date, quantity = quantity.toDouble(), itemId = newItemId)
            repository.insertPrice(newPrice)
        }
        triggerSuccessEvent(name)
    }


    private fun addNewProduct(name: String, price: String, shop: String, quantity: String, date: String) {
        val newProductInput = Product(name = name, currentPrice = price.toDouble(), shop = shop, quantity = quantity.toDouble(), priceDate = date, favourite = 0)
        viewModelScope.launch {
            repository.insertProduct(newProductInput)
            val newItemId : Int = repository.getMostRecentProduct().itemId
            Log.d("new item id", "$newItemId")
        }
        addNewPrice(name, price, shop, quantity, date)
    }


    private fun isProductValid(
        name: String,
        price: String,
        shop: String,
        quantity: String,
        date: String
    ): Boolean {
        var formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        formatter.isLenient = false

        return if (name.isBlank() || price.isBlank() || shop.isBlank() || quantity.isBlank() || date.isBlank()) {
            triggerErrorEvent(name, price, shop, quantity, date)
            false
        } else {
            true
        }
    }

    sealed class Event {
        data class ProductCreatedEvent(val message: String): Event()
        data class ProductCreationError(val message: String): Event()
    }

    private val eventChannel = Channel<Event>()
    val eventFlow = eventChannel.receiveAsFlow()

    private fun triggerSuccessEvent(name: String)  = viewModelScope.launch {
        eventChannel.send(Event.ProductCreatedEvent("Product $name was created"))
    }
    private fun triggerSuccessUpdateEvent(name: String) = viewModelScope.launch {
        eventChannel.send(Event.ProductCreatedEvent("Product $name was updated"))
    }

    private fun triggerErrorEvent(
        name: String, price: String, shop: String, quantity: String, date: String
    ) = viewModelScope.launch {
        val errorField = if (name.isBlank()) {
            "Product name" } else {
                if (price.isBlank()) {
                    "Price" } else {
                        if (shop.isBlank()) {
                            "Shop name" } else {
                                if (quantity.isBlank()) {"Quantity"} else {
                                    "Date"
                                }
                        }
                }
        }
        eventChannel.send(Event.ProductCreationError("Product was not created, problem with $errorField field"))
    }
}