package com.example.agenda.config

import android.app.Application
import androidx.room.Room

class NoteApp : Application() {

    companion object {
        lateinit var db: NoteDb
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            NoteDb::class.java,
            "note"
        ).build()
    }

}