package com.example.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agenda.config.Constants
import com.example.agenda.viewmodel.MainViewModel
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.ui.NoteActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()

        binding.lifecycleOwner = this
        binding.model = viewModel

        viewModel.initial()

        viewModel = ViewModelProvider(this).get()
        viewModel.initial()

        binding.Recycle.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            binding.Recycle.smoothScrollToPosition(0)
        }

        viewModel.notelList.observe(this, Observer {
            binding.Recycle.adapter = NoteAdapter(it)
        })

        binding.btnNewNote.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            intent.putExtra(Constants.OPERATION_KEY, Constants.OPERATION_INSERT)
            startActivity(intent)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if (s.toString().isNotEmpty()) {
                    viewModel.searchNote()

                } else if (s.toString().isEmpty()) {
                    viewModel.initial()
                }

            }

        })

    }
}

