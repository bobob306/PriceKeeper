package com.benshapiro.pricekeeper.ui.Detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.benshapiro.pricekeeper.R
import com.benshapiro.pricekeeper.databinding.ProductDetailFragmentBinding
import com.benshapiro.pricekeeper.model.Price
import com.benshapiro.pricekeeper.model.getFormattedPrice
import com.benshapiro.pricekeeper.model.getValue
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ProductDetailFragment : Fragment(), ProductDetailListAdapter.onPriceClickListener {

    private var _binding: ProductDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ProductDetailListAdapter(onPriceClicked = {
            val action =
                ProductDetailFragmentDirections.actionProductDetailFragmentToAddEditPriceFragment()
            action.price = it
            action.product = viewModel.product.value
            this.findNavController().navigate(action)
        }, this)
        binding.priceHistRV.adapter = adapter

        viewModel.priceList.observe(this.viewLifecycleOwner) { prices ->
            prices.let {
                adapter.submitList(prices.sortedByDescending {
                    LocalDate.parse(
                        it.date,
                        DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    )
                })
//                Log.d("first entry", "${prices[0].itemId} ${prices[0].price}" )
            }
        }

        viewModel.product.observe(this.viewLifecycleOwner) { product ->
            product.let {
                binding.apply {
                    nameDetailTV.text = product.name
                    shopDetailTV.text = product.shop
                    priceDetailTV.text = product.getFormattedPrice()
                    dateTV.text = product.priceDate
                    qauntityTV.text = "Quantity: " + product.quantity.toString()
                    valueTV.text = product.getValue()
                    favDetailBtn.setImageResource(
                        if (viewModel.product.value!!.favourite == 0) {
                            R.drawable.ic_baseline_star_outline_24
                        } else {
                            R.drawable.ic_baseline_star_rate_24
                        }
                    )
                }
            }
        }

        binding.favDetailBtn.setOnClickListener {
            viewModel.onFavClicked()
        }

        binding.editBtn.setOnClickListener {
            val action =
                ProductDetailFragmentDirections.actionProductDetailFragmentToAddProductFragment()
            action.product = viewModel.product.value
            this.findNavController().navigate(action)
        }

        binding.fab2.setOnClickListener {
            val action =
                ProductDetailFragmentDirections.actionProductDetailFragmentToAddEditPriceFragment()
            action.product = viewModel.product.value
            Navigation.findNavController(it).navigate(action)
        }

        binding.priceHistRV.layoutManager = LinearLayoutManager(this.context)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onLineClicked(price: Price) {
        viewModel.onLineClicked(price)
    }

}