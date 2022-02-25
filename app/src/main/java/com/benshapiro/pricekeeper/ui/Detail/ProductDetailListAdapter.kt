package com.benshapiro.pricekeeper.ui.Detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.benshapiro.pricekeeper.databinding.PriceListItemBinding
import com.benshapiro.pricekeeper.model.Price
import com.benshapiro.pricekeeper.model.getFormattedPrice

class ProductDetailListAdapter(
    private val onPriceClicked: (Price) -> Unit,
    private val listener: onPriceClickListener
) :
    ListAdapter<Price, ProductDetailListAdapter.ItemViewHolder>(DiffCallback) {

    inner class ItemViewHolder(private var binding: PriceListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val price = getItem(position)
                        listener.onLineClicked(price)
                    }
                }
            }
        }

        fun bind(price: Price) {
            binding.apply {
                priceDetListTV.text = price.getFormattedPrice()
                dateDetListTV.text = price.date
            }
        }

    }

    interface onPriceClickListener {
        fun onLineClicked(price: Price)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Price>() {
            override fun areItemsTheSame(oldItem: Price, newItem: Price): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Price, newItem: Price): Boolean {
                return oldItem.id == newItem.id
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            PriceListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onPriceClicked(current)
        }
        holder.bind(current)
    }

}


