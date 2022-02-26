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

    fun addNewPrice(newPrice: String, newDate: String) {
        if (isPriceValid(newPrice, newDate)) {
            insertNewPrice(newPrice, newDate)
        }
    }

    private fun insertNewPrice(newPricePoint: String, newDate: String) {
        viewModelScope.launch {
            val price = Price(
                name = product.value!!.name,
                price = newPricePoint.toDouble(),
                date = newDate,
                itemId = product.value!!.itemId
            )
            Log.d("id", "$statePriceId")
            if (statePriceId != -1) {
                Log.d("price", "$newPricePoint")
                repository.updatePrice(price)
            } else {
                repository.insertPrice(price)
            }
        }
    }

    private fun isPriceValid(newPricePoint: String, newDate: String): Boolean {
        if (newPricePoint.isBlank() || newDate.isBlank()) {
            Log.d("blank", "true")
            return false
        }
        Log.d("blank", "false")
        return true
    }

}