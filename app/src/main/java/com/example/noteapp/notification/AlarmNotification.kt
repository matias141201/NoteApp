package com.example.noteapp.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.test.runner.intent.IntentMonitorRegistry.getInstance
import com.example.noteapp.MainActivity
import com.example.noteapp.R
import com.example.noteapp.config.Constants.BODY_EXTRA
import com.example.noteapp.config.Constants.NOTIFICATION_ID
import com.example.noteapp.config.Constants.TITLE_EXTRA
import com.example.noteapp.config.NoteApp.Companion.db
import com.example.noteapp.models.Note
import com.example.noteapp.ui.NoteActivity
import com.example.noteapp.viewmodel.NoteViewModel
import java.text.NumberFormat.getInstance
import java.util.Calendar.getInstance
import java.util.Currency.getInstance


class AlarmNotification : BroadcastReceiver() {

    override fun onReceive(context: Context, p1: Intent?) {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        val notification = NotificationCompat.Builder(context, NoteActivity.MY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_sticky_note)
            .setContentTitle(p1?.getStringExtra(TITLE_EXTRA))
            .setContentText(p1?.getStringExtra(BODY_EXTRA))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(p1?.getStringExtra(BODY_EXTRA))
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)

    }


}