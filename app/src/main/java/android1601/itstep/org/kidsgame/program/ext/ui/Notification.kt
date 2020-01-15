package studio.medved.mobile.nika.ext.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android1601.itstep.org.kidsgame.R
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

fun Context.showNotification(message: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) registerChannel()
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val title = getString(R.string.app_name)

    NotificationCompat.Builder(this, "chanel").apply {
        setContentTitle(title)
        setContentText(message)
        setStyle(NotificationCompat.BigTextStyle().bigText(message))
        setSmallIcon(R.mipmap.ic_launcher)
        setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
        setTicker(message)
        setAutoCancel(true)
        setDefaults(Notification.DEFAULT_VIBRATE and Notification.DEFAULT_LIGHTS)
        notificationManager.notify(System.currentTimeMillis().toInt(), build())
    }
}

@RequiresApi(api = Build.VERSION_CODES.O)
private fun Context.registerChannel() {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channel = notificationManager.getNotificationChannel("chanel")
    if (channel != null) return
    NotificationChannel("chanel", "chanel", NotificationManager.IMPORTANCE_DEFAULT).apply {
        enableLights(true)
        lightColor = Color.RED
        enableVibration(true)
        vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(this)
    }
}
