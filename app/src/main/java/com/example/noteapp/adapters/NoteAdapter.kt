package com.example.noteapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.config.Constants
import com.example.noteapp.models.Note
import com.example.noteapp.R
import com.example.noteapp.databinding.NoteListBinding
import com.example.noteapp.ui.NoteActivity


class NoteAdapter(private val dataSet: List<Note>?) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.note_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val item = dataSet?.get(position)
        viewHolder.bind(item!!)

    }

    override fun getItemCount() = dataSet!!.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var binding = NoteListBinding.bind(view)
        var context = view.context

        fun bind(notes: Note) {

            binding.tvTitle.text = notes.title
            binding.tvBody.text = notes.body

            binding.root.setOnClickListener {
                val intent = Intent(context, NoteActivity::class.java)
                intent.putExtra(Constants.OPERATION_KEY, Constants.OPERATION_EDIT)
                intent.putExtra(Constants.ID_KEY, notes.ID)
                context.startActivity(intent)
            }


        }


    }

}

