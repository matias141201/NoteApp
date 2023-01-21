package com.example.agenda.dao

import androidx.room.*
import com.example.noteapp.models.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM Note ORDER BY ID DESC")
    suspend fun getAll(): List<Note>

    @Query("SELECT * FROM Note WHERE ID = :id")
    suspend fun getById(id: Long): Note

    @Query("SELECT * FROM Note WHERE title LIKE '%' || :title || '%' ")
    suspend fun getByTitle(title: String): List<Note>

    @Insert
    suspend fun insert(note: List<Note>): List<Long>

    @Update
    suspend fun update(note: Note): Int

    @Delete
    suspend fun delete(note: Note): Int

}