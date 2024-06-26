package com.tencent.qcloud.tuikit.tuicallkit.extensions

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.tencent.qcloud.tuikit.tuicallkit.data.Constants
import com.tencent.qcloud.tuikit.tuicallkit.utils.DeviceUtils

/**
 * TUICallKit are kept alive to ensure that applications are not killed by the system in the background.
 * You can delete them if they are not needed.
 */
class TUICallService : Service() {

    companion object {
        private const val NOTIFICATION_ID = 1001
        fun start(context: Context) {
            if (DeviceUtils.isServiceRunning(context, TUICallService::class.java.name)) {
                return
            }
            val starter = Intent(context, TUICallService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(starter)
            } else {
                context.startService(starter)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, TUICallService::class.java)
            context.stopService(intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        val notification = createForegroundNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createForegroundNotification(): Notification {
        val builder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
        return builder.build()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }
}