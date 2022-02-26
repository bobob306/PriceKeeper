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
import javax.inject.Inject

@HiltViewModel
class AddEditPriceViewModel @Inject constructor(private val repository: Repository, private val state: SavedStateHandle) : ViewModel() {

    private val stateProductId = state.get<Product>("Product")!!.itemId
    val product = repository.getProductById(stateProductId).asLiveData()

    private val statePriceId = state.get<Price>("Price")?.id ?: -1
    val price = if (statePriceId != -1) {
        repository.getPriceById(statePriceId).asLiveData()
    } else {null}

    fun addNewPrice(price: String, date: String) {
        if (isPriceValid(price, date)) {
            insertNewPrice(price, date)
        }
    }

    private fun insertNewPrice(newPrice: String, newDate: String) {
        viewModelScope.launch {
            val newPrice = Price(
                name = product.value!!.name,
                price = newPrice.toDouble(),
                date = newDate,
                itemId = product.value!!.itemId
            )
            Log.d("id", "$statePriceId")
            if (statePriceId != -1) {
                repository.updatePrice(newPrice)
            } else {
                repository.insertPrice(newPrice)
            }
        }
    }

    private fun isPriceValid(price: String, date: String): Boolean {
        if (price.isBlank() || date.isBlank()) {
            Log.d("blank", "true")
            return false
        }
        Log.d("blank", "false")
        return true
    }

}