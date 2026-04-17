package dev.inteiintel.teduhserviceapp.utils

sealed class NetworkStatus {
    object Available: NetworkStatus()
    object NoConnection: NetworkStatus()
    object NoInternet: NetworkStatus()

}