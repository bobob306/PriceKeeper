package com.benshapiro.pricekeeper.ui.Detail

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
class ProductDetailViewModel @Inject constructor(private val repository: Repository, private val state: SavedStateHandle) : ViewModel() {

    private val stateId = state.get<Product>("Product")!!.itemId
    val product = repository.getProductById(stateId).asLiveData()
    val priceList = repository.getPriceHistory(stateId).asLiveData()

    fun onLineClicked(price: Price) {
        // do something
    }
    fun onFavClicked() {
        val fav = if (product.value!!.favourite == 0) 1 else 0
        val currentProduct = Product (
            itemId = product.value!!.itemId,
            name = product.value!!.name,
            currentPrice = product.value!!.currentPrice,
            priceDate = product.value!!.priceDate,
            shop = product.value!!.shop,
            favourite = fav
                )
        viewModelScope.launch {
            repository.updateProduct(currentProduct)
        }
    }
}