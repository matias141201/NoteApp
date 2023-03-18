package com.example.noteapp.viewmodel

import android.app.Notification
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.config.Constants
import com.example.noteapp.config.NoteApp.Companion.db
import com.example.noteapp.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NoteViewModel : ViewModel() {

    var id = MutableLiveData<Long>()
    var tiltle = MutableLiveData<String>()
    var body = MutableLiveData<String>()
    var time = MutableLiveData<String>()
    var operation = Constants.OPERATION_INSERT
    var operationsuccessful = MutableLiveData<Boolean>()


    fun saveNote() {

        if (isNotEmpty()) {

            val newNote = Note(
                0,
                tiltle.value!!,
                body.value!!,
                time.value

                )

            when (operation) {
                Constants.OPERATION_INSERT -> {
                    viewModelScope.launch {
                        val result = withContext(Dispatchers.IO) {
                            db.noteDao().insert(
                                arrayListOf<Note>(
                                    newNote
                                )
                            )
                        }
                        operationsuccessful.value = result.isNotEmpty()

                    }

                }

                Constants.OPERATION_EDIT -> {

                    newNote.ID = id.value!!
                    viewModelScope.launch {
                        val result = withContext(Dispatchers.IO) {
                            db.noteDao().update(newNote)
                        }

                        operationsuccessful.value = (result > 0)
                    }

                }
            }
        } else {

            operationsuccessful.value = false

        }


    }

    fun editNote() {

        viewModelScope.launch {
            var note = withContext(Dispatchers.IO) {
                db.noteDao().getById(id.value!!)
            }

            tiltle.value = note.title
            body.value = note.body
            time.value = note.time

        }

    }

    private fun isNotEmpty(): Boolean {
        return !(tiltle.value.isNullOrEmpty() ||
                body.value.isNullOrEmpty())
    }

    fun deleteNote() {

        val deleteNote = Note(
            id.value!!,
            "",
            "",
            time.value
        )

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                db.noteDao().delete(deleteNote)
            }

            operationsuccessful.value = (result > 0)

        }

    }

}