package com.example.swipe.presentation.view


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.swipe.R
import com.example.swipe.data.remote.model.ProductResponse
import com.example.swipe.databinding.ItemProductBinding

class ProductAdapter :
    ListAdapter<ProductResponse, ProductAdapter.ProductViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductResponse) {
            binding.textProductName.text = product.product_name
            binding.textProductType.text = product.product_type ?: ""
            binding.textProductPrice.text = product.price?.toString() ?: ""
            binding.textProductTax.text = product.tax?.toString() ?: ""


            binding.productImage.load(product.image) {
                placeholder(R.drawable.loading) // Placeholder image while loading
                error(R.drawable.defaultimage) // Error image if loading fails
                fallback(R.drawable.defaultimage) // Default image if URL is null
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ProductResponse>() {
        override fun areItemsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
            return oldItem.product_name == newItem.product_name
        }

        override fun areContentsTheSame(
            oldItem: ProductResponse,
            newItem: ProductResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
}

