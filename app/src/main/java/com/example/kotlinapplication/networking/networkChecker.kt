package com.example.kotlinapplication.networking

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

class NetworkChecker (val connectivityManager: ConnectivityManager) {

    @RequiresApi(Build.VERSION_CODES.M)
    fun performAction(action: () -> Unit) {
        if(hasValidInternetConnection()) {
            action()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasValidInternetConnection() : Boolean {
        val network  = connectivityManager.activeNetwork
        val  capability = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                return capability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        return capability.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
}