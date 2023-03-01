package com.example.airpollutionmonitor.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.navigation.NavController
import androidx.navigation.NavDirections


fun Context.hasNetworkToDownload(): Boolean {
    var hasInternet = false
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    hasInternet = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return hasInternet
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}