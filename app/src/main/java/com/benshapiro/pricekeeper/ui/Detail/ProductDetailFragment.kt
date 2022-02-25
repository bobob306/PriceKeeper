package com.benshapiro.pricekeeper.ui.Detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.benshapiro.pricekeeper.R
import com.benshapiro.pricekeeper.databinding.ProductDetailFragmentBinding
import com.benshapiro.pricekeeper.model.Price
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : Fragment(), ProductDetailListAdapter.onPriceClickListener {

    private var _binding : ProductDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ProductDetailListAdapter(onPriceClicked = {
            val action = ProductDetailFragmentDirections.actionProductDetailFragmentToEditProductFragment()
            this.findNavController().navigate(action)
        }, this)
    }

    override fun onLineClicked(price: Price) {
        viewModel.onLineClicked(price)
    }

}