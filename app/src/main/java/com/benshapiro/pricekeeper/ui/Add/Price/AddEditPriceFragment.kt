package com.benshapiro.pricekeeper.ui.Add.Price

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.benshapiro.pricekeeper.databinding.FragmentAddEditPriceBinding
import com.benshapiro.pricekeeper.model.getFormattedPrice
import com.benshapiro.pricekeeper.utils.DatePickerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditPriceFragment : Fragment() {

    private var _binding: FragmentAddEditPriceBinding? = null
    private val binding get() = _binding!!

    private val viewModelEdit: AddEditPriceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEditPriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemDate.isFocusable = false

        viewModelEdit.currentProduct.observe(this.viewLifecycleOwner) { product ->
            product.let {
                binding.apply {
                    nameTV.text = product.name
                    priceTV.text = product.getFormattedPrice()
                    shopTV.text = product.shop
                    dateTV.text = product.priceDate
                }
            }
        }

        viewModelEdit.currentPrice?.observe(this.viewLifecycleOwner) { price ->
            price.let {
                if (price != null) {
                    binding.apply {
                        itemPrice.setText(price.price.toString())
                        itemDate.setText(price.date)
                        itemQauntity.setText(price.quantity.toString())
                    }
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
                viewModelEdit.addNewPrice(
                    itemPrice.text.toString(),
                    itemQauntity.text.toString(),
                    itemDate.text.toString(),
                )
                findNavController().navigateUp()
            }
        }
    }
}