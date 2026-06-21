package com.example.data

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import android.app.PendingIntent
import com.example.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val itemId = intent.getIntExtra("item_id", 0)
        val title = intent.getStringExtra("item_title") ?: "Casa em Dia"
        val category = intent.getStringExtra("item_category") ?: "CASA"

        val contentText = when (category) {
            "CARRO" -> "Seu veículo precisa de atenção para: $title."
            "CASA" -> "Sua residência precisa de atenção para: $title."
            else -> "Lembrete: $title está agendado para em breve."
        }

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            itemId,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, "maintenance_alerts")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Lembrete: $title")
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(itemId, builder.build())
    }
}
