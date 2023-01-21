package com.example.noteapp.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noteapp.dao.NoteDao
import com.example.noteapp.models.Note


@Database(
    entities = [ Note::class ],
    version = 1
)
abstract class NoteDb:RoomDatabase() {

    abstract fun noteDao(): NoteDao

}