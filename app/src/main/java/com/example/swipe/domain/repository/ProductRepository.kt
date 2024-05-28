package com.example.swipe.domain.repository

import android.net.Uri
import com.example.swipe.data.remote.model.CreateProductResponse
import com.example.swipe.data.remote.model.ProductDetails
import com.example.swipe.data.remote.model.ProductResponse

interface ProductRepository {
    suspend fun getApi(): List<ProductResponse>
    suspend fun createProduct(
        productDetails: ProductDetails,
        imageUri: Uri?
    ): CreateProductResponse
}
