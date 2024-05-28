package com.example.swipe.data.remote.model

data class CreateProductResponse(
    val message: String,
    val product_details: ProductDetails,
    val product_id: Int,
    val success: Boolean
)