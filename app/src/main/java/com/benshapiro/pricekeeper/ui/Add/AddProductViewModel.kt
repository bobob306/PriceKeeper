package com.benshapiro.pricekeeper.ui.Add

import androidx.lifecycle.ViewModel
import com.benshapiro.pricekeeper.di.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel
    @Inject constructor(
        private val repository: Repository
    )
    : ViewModel() {

}