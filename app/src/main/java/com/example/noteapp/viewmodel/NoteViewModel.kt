package com.example.agenda.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agenda.config.Constants
import com.example.agenda.config.NoteApp.Companion.db
import com.example.noteapp.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModel : ViewModel() {

    var id = MutableLiveData<Long>()
    var tiltle = MutableLiveData<String>()
    var body = MutableLiveData<String>()
    var operation = Constants.OPERATION_INSERT
    var operationsuccessful = MutableLiveData<Boolean>()


    fun saveNote() {

        if (isNotEmpty()) {

            var newNote = Note(
                0,
                tiltle.value!!,
                body.value!!,

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

        }

    }

    private fun isNotEmpty(): Boolean {
        return !(tiltle.value.isNullOrEmpty() ||
                body.value.isNullOrEmpty())
    }

    fun deleteNote() {

        var deleteNote = Note(
            id.value!!,
            "",
            ""
        )

        viewModelScope.launch {
            var result = withContext(Dispatchers.IO) {
                db.noteDao().delete(deleteNote)
            }

            operationsuccessful.value = (result > 0)

        }

    }

}