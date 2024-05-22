package com.embark.notes.view.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.embark.notes.R
import com.embark.notes.databinding.FragmentEditNoteBinding
import com.embark.notes.model.Note
import com.embark.notes.viewmodel.NoteViewModel


class EditNoteFragment : Fragment(R.layout.fragment_edit_note) {

    private var binding: FragmentEditNoteBinding? = null
    private val viewModel: NoteViewModel by activityViewModels()

    private var note: Note? = null
    private var toBeDeleted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditNoteBinding.bind(view)
        binding?.apply {
            arguments?.let {
                if (!it.isEmpty) {
                    note = Note(
                        index = it.getLong("index"),
                        title = it.getString("title").toString(),
                        content = it.getString("content").toString(),
                        isPinned = it.getBoolean(("isPinned")),
                        lastModified = it.getLong("lastModified")
                    )
                    etNoteTitle.setText(it.getString("title").toString())
                    etNoteContent.setText(it.getString("content").toString())
                }
            }
            ivBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            if (note == null) {
                ivDelete.visibility = View.GONE
            }
            ivDelete.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Yes") { dialog, id ->
                        viewModel.delete(note!!)
                        toBeDeleted = true
                        dialog.dismiss()
                        findNavController().popBackStack()
                    }.setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                builder.create().show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onPause() {
        super.onPause()

        binding?.apply {
            if (note == null) {
                if (!etNoteTitle.text.isNullOrBlank() || !etNoteContent.text.isNullOrBlank()) {
                    viewModel.insert(
                        Note(
                            title = this.etNoteTitle.text.toString(),
                            content = this.etNoteContent.text.toString(),
                            lastModified = System.currentTimeMillis()
                        )
                    )
                } else {
                    Toast.makeText(requireContext(), "Empty note discarded", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                if (!toBeDeleted) {
                    viewModel.update(
                        Note(
                            index = note!!.index,
                            title = etNoteTitle.text.toString(),
                            content = etNoteContent.text.toString(),
                            isPinned = note!!.isPinned,
                            lastModified = System.currentTimeMillis()
                        )
                    )
                }
            }
        }

    }

    companion object {
        private const val TAG = "EditNoteFragment"
        fun newInstance() = EditNoteFragment()
    }
}
