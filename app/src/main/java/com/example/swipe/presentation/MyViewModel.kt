package com.example.swipe.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipe.data.remote.model.ProductDetails
import com.example.swipe.data.remote.model.ProductResponse
import com.example.swipe.domain.repository.ProductRepository
import com.example.swipe.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<Resource<List<ProductResponse>>>(Resource.Loading())
    val products: StateFlow<Resource<List<ProductResponse>>> = _products

    private val _productCreationStatus = MutableStateFlow<Resource<Unit>>(Resource.Idle())
    val productCreationStatus: StateFlow<Resource<Unit>> get() = _productCreationStatus

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            _products.value = Resource.Loading()
            try {
                val response = repository.getApi()
                if (response.isNotEmpty()) {
                    _products.value = Resource.Success(response)
                } else {
                    _products.value = Resource.Error("No cached data available")
                }
            } catch (e: Exception) {
                _products.value = Resource.Error("Failed to fetch products: ${e.localizedMessage}")
            }
        }
    }

    fun createProduct(productDetails: ProductDetails, imageUri: Uri?) {
        viewModelScope.launch {
            try {
                _productCreationStatus.value = Resource.Loading()
                repository.createProduct(productDetails, imageUri)
                _productCreationStatus.value = Resource.Success(Unit)
            } catch (e: Exception) {
                _productCreationStatus.value =
                    Resource.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }
}
