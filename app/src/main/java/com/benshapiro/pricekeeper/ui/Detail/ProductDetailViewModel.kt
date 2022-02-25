package com.benshapiro.pricekeeper.ui.Detail

import androidx.lifecycle.ViewModel
import com.benshapiro.pricekeeper.model.Price
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ProductDetailViewModel : ViewModel() {

    fun onLineClicked(price: Price) {
        // do sometihng
    }
}