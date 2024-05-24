package com.embark.notes.view.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.embark.notes.databinding.ItemViewNotesBinding
import com.embark.notes.model.Note

class NotesAdapter(
    private var notes: List<Note>,
    private val listener: OnNoteClickedListener
) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    interface OnNoteClickedListener {
        fun onNoteClicked(note: Note)
    }

    inner class NotesViewHolder(private val binding: ItemViewNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.apply {
                tvNoteTitle.text = note.title
                tvNoteContent.text = note.content

                root.setOnClickListener {
                    listener.onNoteClicked(note)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemViewNotesBinding.inflate(layoutInflater, parent, false)
        return NotesViewHolder(binding)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }
}