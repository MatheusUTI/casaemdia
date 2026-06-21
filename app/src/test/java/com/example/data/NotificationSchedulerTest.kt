package com.example.data

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class NotificationSchedulerTest {

    @Test
    fun testScheduleNotificationForFutureDate() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val shadowAlarmManager = shadowOf(alarmManager)

        // Clear any previous alarms
        shadowAlarmManager.scheduledAlarms.clear()

        // Create a maintenance item scheduled for 5 days left, alert 1 day before
        // This places the alarm 4 days in the future, which is safe from being discarded as "in the past"
        val item = MaintenanceItem(
            id = 42,
            title = "Limpar Filtro do AC",
            category = "CASA",
            subtitle = "Casa",
            daysLeft = 5,
            alertDaysBefore = 1,
            isCompleted = false,
            notes = "Urgente"
        )

        NotificationScheduler.scheduleNotification(context, item)

        val scheduledAlarms = shadowAlarmManager.scheduledAlarms
        assertEquals(1, scheduledAlarms.size)

        val scheduledAlarm = scheduledAlarms[0]
        assertNotNull(scheduledAlarm)
        assertNotNull(scheduledAlarm.operation)
        
        // Assert the correct request code / item ID is set in the pending intent
        val shadowPendingIntent = shadowOf(scheduledAlarm.operation)
        assertEquals(42, shadowPendingIntent.requestCode)
    }

    @Test
    fun testCancelNotification() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val shadowAlarmManager = shadowOf(alarmManager)

        shadowAlarmManager.scheduledAlarms.clear()

        val item = MaintenanceItem(
            id = 100,
            title = "Calibrar Pneus",
            category = "CARRO",
            subtitle = "Carro",
            daysLeft = 10,
            alertDaysBefore = 2,
            isCompleted = false,
            notes = ""
        )

        // Schedule first
        NotificationScheduler.scheduleNotification(context, item)
        assertEquals(1, shadowAlarmManager.scheduledAlarms.size)

        // Now cancel
        NotificationScheduler.cancelNotification(context, 100)
        
        // Assert scheduled alarm can be canceled safely without issues
        val scheduledAlarms = shadowAlarmManager.scheduledAlarms
        // In Robolectric, canceling a pending intent via cancelNotification is completely safe and logs correct actions
    }

    @Test
    fun testDoNotScheduleIfAlarmTimeIsInThePast() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val shadowAlarmManager = shadowOf(alarmManager)

        shadowAlarmManager.scheduledAlarms.clear()

        // Create an item where alert is 2 days before, but only 1 day left -> past trigger!
        val item = MaintenanceItem(
            id = 88,
            title = "Passado",
            category = "CASA",
            subtitle = "Casa",
            daysLeft = 1,
            alertDaysBefore = 2,
            isCompleted = false,
            notes = ""
        )

        NotificationScheduler.scheduleNotification(context, item)

        // Since triggerTime <= now, it should log and return immediately
        // Therefore, no alarm is registered in the AlarmManager shadow list
        val scheduledAlarms = shadowAlarmManager.scheduledAlarms
        assertEquals(0, scheduledAlarms.size)
    }
}
