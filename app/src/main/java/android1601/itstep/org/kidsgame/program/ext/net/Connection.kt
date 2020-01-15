package android1601.itstep.org.kidsgame.program.ext.net

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android1601.itstep.org.kidsgame.program.di.ContextProvider


fun isOffline() = !isOnline()

fun isOnline(): Boolean {
    val context by ContextProvider()
    val manager = context.getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager
    val networkInfo = manager?.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}



