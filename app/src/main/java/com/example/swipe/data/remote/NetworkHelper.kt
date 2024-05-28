package com.example.swipe.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


class NetworkHelper(private val context: Context) {
    fun isNetworkConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}