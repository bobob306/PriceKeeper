package com.benshapiro.pricekeeper.ui.Add.Product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.benshapiro.pricekeeper.databinding.AddProductFragmentBinding
import com.benshapiro.pricekeeper.utils.DatePickerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductFragment : Fragment() {

    private var _binding: AddProductFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddProductFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        viewModel.newProduct.observe(this.viewLifecycleOwner){ product ->
            product.let {
                val currentId = viewModel.newProduct.value?.itemId ?: 0
                if (currentId != viewModel.newProduct.value?.itemId ?: 0) {
                    Log.d("old id", "$currentId")
                    Log.d("id", "${viewModel.newProduct.value?.itemId ?: 1}")
                }
            }
        }
        */

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
                    itemDate.text.toString()
                )
            }
        }
    }


}