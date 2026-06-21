package com.example.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.data.MaintenanceItem
import java.time.LocalDate
import java.time.ZoneId

object NotificationScheduler {
    fun scheduleNotification(context: Context, item: MaintenanceItem) {
        cancelNotification(context, item.id)

        val targetDate = LocalDate.now().plusDays(item.daysLeft.toLong())
        val alarmDate = targetDate.minusDays(item.alertDaysBefore.toLong())

        val triggerTime = alarmDate.atTime(9, 0)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val now = System.currentTimeMillis()
        if (triggerTime <= now) {
            Log.d("NotificationScheduler", "Alarm time is in the past, scheduling was skipped. ID: ${item.id}")
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("item_id", item.id)
            putExtra("item_title", item.title)
            putExtra("item_category", item.category)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
            Log.d("NotificationScheduler", "Scheduled alarm for item: ${item.title} at $alarmDate")
            
            // Show Toast visual feedback on Main Thread safely
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                val label = RecurrenceHelper.getAlertLabel(item.alertDaysBefore)
                android.widget.Toast.makeText(
                    context,
                    "Notificação agendada: $label!",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Log.e("NotificationScheduler", "Failed to schedule alarm", e)
        }
    }

    fun cancelNotification(context: Context, itemId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            itemId,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d("NotificationScheduler", "Canceled alarm for item ID: $itemId")
        }
    }
}
