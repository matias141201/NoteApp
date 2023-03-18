package com.example.noteapp.ui

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.noteapp.config.Constants
import com.example.noteapp.dialog.DeleteDialog
import com.example.noteapp.dialog.DeleteListener
import com.example.noteapp.viewmodel.NoteViewModel
import com.example.noteapp.MainActivity
import com.example.noteapp.R
import com.example.noteapp.config.Constants.BODY_EXTRA
import com.example.noteapp.config.Constants.NOTIFICATION_ID
import com.example.noteapp.config.Constants.TITLE_EXTRA
import com.example.noteapp.databinding.ActivityNoteBinding
import com.example.noteapp.notification.AlarmNotification
import java.util.*

class NoteActivity : AppCompatActivity(), DeleteListener {

    companion object {
        const val MY_CHANNEL_ID = "myChannel"
    }


    lateinit var binding: ActivityNoteBinding
    lateinit var viewModel: NoteViewModel
    lateinit var DeleteDialog: DeleteDialog

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DeleteDialog = DeleteDialog(this)
        viewModel = ViewModelProvider(this).get()
        viewModel.operation = intent.getStringExtra(Constants.OPERATION_KEY)!!


        binding.model = viewModel
        binding.lifecycleOwner = this

        viewModel.operationsuccessful.observe(this, Observer {

            if (it) {
                goHome()

            } else {
                showError()
            }

        })

        if (viewModel.operation.equals(Constants.OPERATION_EDIT)) {

            viewModel.id.value = intent.getLongExtra(Constants.ID_KEY, 0)
            viewModel.editNote()
            binding.btnDelete.visibility = View.VISIBLE

        } else {

            binding.btnDelete.visibility = View.GONE

        }

        binding.btnDelete.setOnClickListener {
            DeleteDialog()
        }

        createChannel()

        binding.btnCreateNotification.setOnClickListener {
            scheduleNotification()
            viewModel.saveNote()
            binding.cvSelect.visibility = View.GONE
        }

        binding.btnHour.setOnClickListener {
            binding.datePicker.visibility = View.GONE
            binding.timePicker.visibility = View.VISIBLE
            binding.btnCreateNotification.visibility = View.VISIBLE
            binding.btnHour.visibility = View.GONE
        }

        binding.btnBack.setOnClickListener {
            if (binding.timePicker.visibility == View.GONE) {
                binding.cvSelect.visibility = View.GONE
            } else {
                binding.timePicker.visibility = View.GONE
                binding.datePicker.visibility = View.VISIBLE
                binding.btnCreateNotification.visibility = View.GONE
                binding.btnHour.visibility = View.VISIBLE

            }
        }


    }

    override fun onBackPressed() {
        viewModel.saveNote()
        super.onBackPressed()
    }

    private fun DeleteDialog() {
        DeleteDialog.show(supportFragmentManager, "Delete Dialog")

    }

    private fun showError() {
        Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_LONG).show()

    }

    private fun goHome() {

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }

    override fun deleteNote() {

        viewModel.deleteNote()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification() {
        val intent = Intent(applicationContext, AlarmNotification::class.java)
        val title = binding.etTitle.text.toString()
        val body = binding.etBody.text.toString()
        intent.putExtra(TITLE_EXTRA, title)
        intent.putExtra(BODY_EXTRA, body)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            getTime(),
            pendingIntent
        )

        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month

        if (minute < 10) {
            binding.tvNotification.text = "$day/${month + 1} - $hour:0$minute"
        }
        else {
            binding.tvNotification.text = "$day/${month + 1} - $hour:$minute"
        }

    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MY_CHANNEL_ID,
                "NotificationChannel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notification"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()

        calendar.set(year, month, day, hour, minute)

        return calendar.timeInMillis
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.notification -> {
                binding.cvSelect.visibility = View.VISIBLE
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
