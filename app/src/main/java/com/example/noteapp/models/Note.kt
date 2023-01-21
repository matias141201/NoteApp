package com.example.noteapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    var ID: Long,
    var title: String,
    var body: String
)