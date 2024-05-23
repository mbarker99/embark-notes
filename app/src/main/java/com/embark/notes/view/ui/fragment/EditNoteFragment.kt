package com.embark.notes.view.ui.fragment

import android.os.Bundle
import android.util.Log
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

    private var selectedNote: Note? = null
    private var toBePinned = false
    private var toBeDeleted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditNoteBinding.bind(view)
        binding?.apply {
            arguments?.let {
                if (!it.isEmpty) {
                    selectedNote = Note(
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

            if (selectedNote == null) {
                ivDelete.visibility = View.GONE
                ivPin.visibility = View.GONE
            }

            ivBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            ivDelete.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Yes") { dialog, id ->
                        viewModel.delete(selectedNote!!)
                        toBeDeleted = true
                        dialog.dismiss()
                        findNavController().popBackStack()
                    }.setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                builder.create().show()
            }

            if (selectedNote?.isPinned == true) {
                ivPin.setImageResource(R.drawable.ic_pinned_24)
            } else {
                ivPin.setImageResource(R.drawable.ic_unpinned_24)
            }

            ivPin.setOnClickListener {
                toBePinned = true
                if (selectedNote?.isPinned == true) {
                    ivPin.setImageResource(R.drawable.ic_unpinned_24)
                } else {
                    ivPin.setImageResource(R.drawable.ic_pinned_24)
                }
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
            if (selectedNote == null) {
                if (!etNoteTitle.text.isNullOrBlank() || !etNoteContent.text.isNullOrBlank()) {
                    viewModel.insert(
                        Note(
                            title = this.etNoteTitle.text.toString(),
                            content = this.etNoteContent.text.toString(),
                            isPinned = false,
                            lastModified = System.currentTimeMillis()
                        )
                    )
                } else {
                    Toast.makeText(requireContext(), "Empty note discarded", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                if (!toBeDeleted && isModified()) {
                    Log.i(TAG, "Note updated -- Note=${selectedNote}")
                    var newPinState = selectedNote?.isPinned
                    if (toBePinned)
                        newPinState = !newPinState!!
                    viewModel.update(
                        Note(
                            index = selectedNote!!.index,
                            title = etNoteTitle.text.toString(),
                            content = etNoteContent.text.toString(),
                            isPinned = newPinState,
                            lastModified = System.currentTimeMillis()
                        )
                    )
                }
            }
        }

    }

    private fun isModified(): Boolean {
        return binding!!.etNoteTitle.text.toString() != selectedNote?.title ||
                binding!!.etNoteContent.text.toString() != selectedNote?.content ||
                toBePinned
    }

    companion object {
        private const val TAG = "EditNoteFragment"
        fun newInstance() = EditNoteFragment()
    }
}
