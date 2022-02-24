package com.benshapiro.pricekeeper.ui.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ListAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.benb.inventory.adapter.ProductListAdapter
import com.benshapiro.pricekeeper.R
import com.benshapiro.pricekeeper.data.SortOrder
import com.benshapiro.pricekeeper.databinding.ProductListFragmentBinding
import com.benshapiro.pricekeeper.model.Product
import com.benshapiro.pricekeeper.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment(), ProductListAdapter.onProductClickListener {

    private var _binding : ProductListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ProductListViewModel  by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ProductListAdapter(onProductClicked = {
            val action = ProductListFragmentDirections.actionProductListFragmentToProductDetailFragment()
            action.product = it
            this.findNavController().navigate(action)
        }, this)
        binding.recyclerView.adapter = adapter

        viewModel.products.observe(this.viewLifecycleOwner) {products ->
            products.let {
                adapter.submitList(it)
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val action = ProductListFragmentDirections.actionProductListFragmentToAddProductFragment()
            this.findNavController().navigate(action)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_list, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_price -> {
                viewModel.onSortOrderSelected(SortOrder.BY_PRICE)
                true
            }
            R.id.action_sort_by_shop -> {
                viewModel.onSortOrderSelected(SortOrder.BY_SHOP)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onFavClicked(product: Product) {
        viewModel.onFavClicked(product)
    }

    override fun onLineClicked(product: Product) {
        viewModel.onLineClicked(product)
    }

}