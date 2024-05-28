package com.example.swipe.di

import android.content.Context
import com.example.swipe.data.remote.ApiService
import com.example.swipe.data.remote.NetworkHelper
import com.example.swipe.data.repository.ProductRepositoryImpl
import com.example.swipe.domain.repository.ProductRepository
import com.example.swipe.presentation.MyViewModel
import com.example.swipe.utils.Constants
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val module = module {
    single {
        val context: Context = androidContext()
        val cacheSize = 30 * 1024 * 1024L // 30 MB cache
        val cache = Cache(context.cacheDir, cacheSize)

        val logging = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val offlineInterceptor = Interceptor { chain ->
            var request = chain.request()
            if (!NetworkHelper(context).isNetworkConnected()) {
                val maxStale = 60 * 60 * 24 * 7 // 1 week stale
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
            }
            chain.proceed(request)
        }

        val networkInterceptor = Interceptor { chain ->
            val request = chain.request()
            if (NetworkHelper(context).isNetworkConnected()) {
                // Force network response by adding a no-cache directive
                request.newBuilder()
                    .header("Cache-Control", "public, max-age=0")
                    .build()
            }
            chain.proceed(request)
        }

        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(logging)
            .addInterceptor(offlineInterceptor) // First check if network is unavailable
            .addNetworkInterceptor(networkInterceptor) // Apply no-cache policy for network requests
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>())
            .build()
            .create(ApiService::class.java)
    }

    single<ProductRepository> {
        ProductRepositoryImpl(get(), androidContext())
    }

    viewModel {
        MyViewModel(get())
    }
}
