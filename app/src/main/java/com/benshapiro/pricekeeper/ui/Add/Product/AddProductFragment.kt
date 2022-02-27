package com.benshapiro.pricekeeper.ui.Add.Product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.benshapiro.pricekeeper.databinding.AddProductFragmentBinding
import com.benshapiro.pricekeeper.utils.DatePickerFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddProductFragment : Fragment() {

    private var _binding: AddProductFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddProductFragmentBinding.inflate(inflater, container, false)

        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is AddProductViewModel.Event.ProductCreatedEvent -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                        findNavController().navigateUp()
                    }
                    is AddProductViewModel.Event.ProductCreationError -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
                    }
                }

            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply { itemDate.isFocusable = false }

        viewModel.currentProduct?.observe(this.viewLifecycleOwner) { product ->
            if (viewModel.productId != -1) {
                binding.apply {
                    itemName.setText(product.name)
                    itemPrice.setText(product.currentPrice.toString())
                    itemDate.setText(product.priceDate)
                    itemQauntity.setText(product.quantity.toString())
                    itemShop.setText(product.shop)
                    itemPrice.isFocusable = false
                    itemDate.isFocusable = false
                    itemQauntity.isFocusable = false
                }
            }

        }

        binding.apply {
            selectDateBtn.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY", viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        Log.d("date", "$date")
                        itemDate.setText(date)
                    }
                }
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
            saveAction.setOnClickListener {
                viewModel.addNewItem(
                    itemName.text.toString(),
                    itemPrice.text.toString(),
                    itemShop.text.toString(),
                    itemQauntity.text.toString(),
                    itemDate.text.toString()
                )
            }
        }
    }
}