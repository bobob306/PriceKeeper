package com.benshapiro.pricekeeper.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.benshapiro.pricekeeper.data.PreferencesManager
import com.benshapiro.pricekeeper.data.SortOrder
import com.benshapiro.pricekeeper.di.Repository
import com.benshapiro.pricekeeper.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
@Inject constructor(
    private val repository: Repository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val preferencesFlow = preferencesManager.preferencesFlow

    val productsFlow = combine(
        searchQuery, preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }
        .flatMapLatest { (query, filterPreferences) ->
            repository.getProductsList(query, filterPreferences.sortOrder)
        }
    val products = productsFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onFavClicked(product: Product) {
        val fav = if (product.favourite == 0) 1 else 0
        val currentProduct = Product (
            itemId = product.itemId,
            name = product.name,
            currentPrice = product.currentPrice,
            priceDate = product.priceDate,
            shop = product.shop,
            quantity = product.quantity,
            favourite = fav
        )
        viewModelScope.launch {
            repository.updateProduct(currentProduct)
        }
    }

    fun onLineClicked(product: Product) {
        // TODO
    }

}