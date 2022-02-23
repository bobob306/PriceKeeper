package com.benshapiro.pricekeeper.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.benshapiro.pricekeeper.data.PreferencesManager
import com.benshapiro.pricekeeper.di.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
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

}