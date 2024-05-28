package com.example.swipe.data.remote

import com.example.swipe.data.remote.model.CreateProductResponse
import com.example.swipe.data.remote.model.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("get")
    @Headers("Cache-Control: max-age=640000")
    suspend fun getaApi(): List<ProductResponse>

    @Multipart
    @POST("add")
    suspend fun createProduct(
        @Part files: List<MultipartBody.Part>,
        @Part("price") price: RequestBody,
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("tax") tax: RequestBody
    ): CreateProductResponse

    @Multipart
    @POST("add")
    suspend fun createProductWithoutImage(
        @Part("price") price: RequestBody,
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("tax") tax: RequestBody
    ): CreateProductResponse


}