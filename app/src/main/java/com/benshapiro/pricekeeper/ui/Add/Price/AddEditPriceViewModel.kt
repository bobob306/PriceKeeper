package com.benshapiro.pricekeeper.ui.Add.Price

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.benshapiro.pricekeeper.di.Repository
import com.benshapiro.pricekeeper.model.Price
import com.benshapiro.pricekeeper.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditPriceViewModel @Inject constructor(private val repository: Repository, private val state: SavedStateHandle) : ViewModel() {

    private val stateProductId = state.get<Product>("Product")!!.itemId
    val currentProduct = repository.getProductById(stateProductId).asLiveData()

    private val statePriceId = state.get<Price>("Price")?.id ?: -1
    val currentPrice = if (statePriceId != -1) {
        repository.getPriceById(statePriceId).asLiveData()
    } else {null}

    fun addNewPrice(newPrice: String, newQuantity: String, newDate: String) {
        if (isPriceValid(newPrice, newQuantity, newDate)) {
            comparePriceDates(newPrice, newQuantity, newDate)
            insertNewPrice(newPrice, newQuantity, newDate)
        }
    }

    private fun comparePriceDates(newPrice: String, newQuantity: String, newDate: String) {
        val currentDate = currentProduct.value!!.priceDate
        val formattedCurrentDate = SimpleDateFormat("dd-MM-yyyy").parse(currentDate)
        val formattedNewDate = SimpleDateFormat("dd-MM-yyyy").parse(newDate)
        if (formattedNewDate >= formattedCurrentDate) {
            updateProductPrice(newPrice, newQuantity, newDate)
        }

    }

    private fun updateProductPrice(newPrice: String, newQuantity: String, newDate: String) {
        viewModelScope.launch {
            val updatedProduct = Product(
                itemId = currentProduct.value!!.itemId,
                favourite = currentProduct.value!!.favourite,
                priceDate = newDate,
                currentPrice = newPrice.toDouble(),
                quantity = newQuantity.toDouble(),
                name = currentProduct.value!!.name,
                shop = currentProduct.value!!.shop
            )
            repository.updateProduct(updatedProduct)
        }
    }

    private fun insertNewPrice(newPricePoint: String, newQuantity: String, newDate: String) {
        viewModelScope.launch {
            if (statePriceId != -1) {
                val updatedPrice = Price(
                    id = currentPrice?.value!!.id,
                    name = currentProduct.value!!.name,
                    price = newPricePoint.toDouble(),
                    quantity = newQuantity.toDouble(),
                    date = newDate,
                    itemId = currentProduct.value!!.itemId
                )
                Log.d("price", "$newPricePoint")
                repository.updatePrice(updatedPrice)
            } else {
                val price = Price(
                    name = currentProduct.value!!.name,
                    price = newPricePoint.toDouble(),
                    quantity = newQuantity.toDouble(),
                    date = newDate,
                    itemId = currentProduct.value!!.itemId
                )
                repository.insertPrice(price)
            }
        }
    }

    private fun isPriceValid(newPricePoint: String, newQuantity: String, newDate: String): Boolean {
        if (newPricePoint.isBlank() || newQuantity.isBlank()|| newDate.isBlank()) {
            Log.d("blank", "true")
            return false
        }
        Log.d("blank", "false")
        return true
    }

}