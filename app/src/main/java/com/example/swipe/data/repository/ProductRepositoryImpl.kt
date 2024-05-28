package com.example.swipe.data.repository

import android.content.Context
import android.net.Uri
import com.example.swipe.data.remote.ApiService
import com.example.swipe.data.remote.model.CreateProductResponse
import com.example.swipe.data.remote.model.ProductDetails
import com.example.swipe.data.remote.model.ProductResponse
import com.example.swipe.domain.repository.ProductRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class ProductRepositoryImpl(
    private val api: ApiService,
    private val context: Context
) : ProductRepository {


    override suspend fun getApi(): List<ProductResponse> {
        return api.getaApi()
    }


    override suspend fun createProduct(
        productDetails: ProductDetails,
        imageUri: Uri?
    ): CreateProductResponse {
        // Convert strings to RequestBody
        val priceBody = RequestBody.create("text/plain".toMediaTypeOrNull(), productDetails.price)
        val productNameBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), productDetails.product_name)
        val productTypeBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), productDetails.product_type)
        val taxBody = RequestBody.create("text/plain".toMediaTypeOrNull(), productDetails.tax)

        return if (imageUri != null) {
            // Convert image URI to MultipartBody.Part
            val imageFile = createFileFromUri(imageUri)
            val requestFile =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imageFile)
            val imagePart =
                MultipartBody.Part.createFormData("files[]", imageFile.name, requestFile)

            // Make the API call with image
            api.createProduct(
                listOf(imagePart),
                priceBody,
                productNameBody,
                productTypeBody,
                taxBody
            )
        } else {
            // Make the API call without image
            api.createProductWithoutImage(priceBody, productNameBody, productTypeBody, taxBody)
        }
    }

    private fun createFileFromUri(uri: Uri): File {
        val fileExtension = context.contentResolver.getType(uri)?.split("/")?.last()
        val imageFile = File(context.cacheDir, "upload_image.$fileExtension")
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(imageFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return imageFile
    }
}
