package dev.inteiintel.teduhserviceapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import dev.inteiintel.teduhserviceapp.MainActivity
import dev.inteiintel.teduhserviceapp.R

object NotificationHelper {

    const val CHANNEL_ID_ANTREAN = "teduh_antrean_channel"
    const val CHANNEL_NAME_ANTREAN = "Pemberitahuan Antrean Servis"

    const val CHANNEL_ID_PROMO = "teduh_promo_channel"
    const val CHANNEL_NAME_PROMO = "Promo & Pengumuman Bengkel"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // 1. Channel Antrean (High Importance + Vibrate)
            val antreanChannel = NotificationChannel(
                CHANNEL_ID_ANTREAN,
                CHANNEL_NAME_ANTREAN,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi status panggilan dan update antrean servis motor"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }

            // 2. Channel Promo & Broadcast (Default Importance + Vibrate)
            val promoChannel = NotificationChannel(
                CHANNEL_ID_PROMO,
                CHANNEL_NAME_PROMO,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Informasi diskon, voucher dan pengumuman dari bengkel"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 200, 300)
            }

            notificationManager.createNotificationChannel(antreanChannel)
            notificationManager.createNotificationChannel(promoChannel)
        }
    }

    fun showAntreanNotification(
        context: Context,
        notificationId: Int = System.currentTimeMillis().toInt(),
        title: String,
        message: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_ANTREAN)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    fun showPromoNotification(
        context: Context,
        notificationId: Int = System.currentTimeMillis().toInt(),
        title: String,
        message: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_PROMO)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 300, 200, 300))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }
}
