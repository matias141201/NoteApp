package com.example.agenda.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.agenda.config.NoteApp.Companion.db
import com.example.noteapp.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    val notelList = MutableLiveData<List<Note>>()
    var searchNote = MutableLiveData<String>()


    fun initial() {
        viewModelScope.launch {
            notelList.value = withContext(Dispatchers.IO) {
                db.noteDao().getAll()

            }

        }
    }

    fun searchNote() {
        viewModelScope.launch {
            notelList.value = withContext(Dispatchers.IO) {
                db.noteDao().getByTitle(searchNote.value!!)

            }

        }

    }

}