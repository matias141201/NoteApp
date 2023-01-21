package com.example.noteapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.example.noteapp.databinding.ActivityNoteBinding

class NoteActivity : AppCompatActivity(), DeleteListener {

    lateinit var binding: ActivityNoteBinding
    lateinit var viewModel: NoteViewModel
    lateinit var DeleteDialog: DeleteDialog

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
            binding.LinearEdit.visibility = View.VISIBLE

        } else {

            binding.LinearEdit.visibility = View.GONE

        }

        binding.btnDelete.setOnClickListener {
            DeleteDialog()
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

}