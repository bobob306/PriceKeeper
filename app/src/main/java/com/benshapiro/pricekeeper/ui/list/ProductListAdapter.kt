package com.benb.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.benshapiro.pricekeeper.databinding.ProductRowLayoutBinding
import com.benshapiro.pricekeeper.model.Product
import com.benshapiro.pricekeeper.model.getFormattedPrice

class ProductListAdapter(private val onProductClicked: (Product) -> Unit, private val listener: onProductClickListener) :
    ListAdapter<Product, ProductListAdapter.ItemViewHolder>(DiffCallback) {

    inner class ItemViewHolder(private var binding: ProductRowLayoutBinding):RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                favBtn.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val product = getItem(position)
                        listener.onFavClicked(product)
                    }
                }
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val product = getItem(position)
                        listener.onLineClicked(product)
                    }
                }
            }
        }

        fun bind (product: Product) {
            binding.apply{
                tvName.text = product.name
                priceTV.text = product.getFormattedPrice()
                shopTV.text = product.shop
            }
        }
    }

    interface onProductClickListener {
        fun onFavClicked(product: Product)
        fun onLineClicked(product: Product)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.name ==  newItem.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ProductRowLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{
            onProductClicked(current)
        }
        holder.bind(current)
    }

}