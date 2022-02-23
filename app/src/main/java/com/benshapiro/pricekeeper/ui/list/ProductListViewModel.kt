package com.benshapiro.pricekeeper.ui.list

import androidx.lifecycle.ViewModel
import com.benshapiro.pricekeeper.di.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
    @Inject constructor(
        private val repository: Repository
    )
    :ViewModel() {

}