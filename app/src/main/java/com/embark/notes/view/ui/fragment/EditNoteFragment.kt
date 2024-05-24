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

    private var tempIsPinned = false
    private var toBeDeleted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditNoteBinding.bind(view)
        setupUI()
    }

    private fun setupUI() {
        binding?.apply {
            if (viewModel.selectedNote != null) {
                etNoteTitle.setText(viewModel.selectedNote?.title ?: "")
                etNoteContent.setText(viewModel.selectedNote?.content ?: "")
                tempIsPinned = viewModel.selectedNote?.isPinned == true
            } else {
                ivDelete.visibility = View.GONE
                ivPin.visibility = View.GONE
            }

            if (viewModel.selectedNote?.isPinned == true) {
                ivPin.setImageResource(R.drawable.ic_pinned_24)
            } else {
                ivPin.setImageResource(R.drawable.ic_unpinned_24)
            }

            ivBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            ivDelete.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.delete_note_confirmation))
                    .setPositiveButton(getString(R.string.delete_note_positive_button)) { dialog, _ ->
                        viewModel.selectedNote?.let { note -> viewModel.delete(note) }
                        toBeDeleted = true
                        dialog.dismiss()
                        findNavController().popBackStack()
                    }
                    .setNegativeButton(getString(R.string.delete_note_negative_button)) { dialog, _ ->
                        dialog.dismiss()
                    }
                builder.create().show()
            }

            ivPin.setOnClickListener {
                if (tempIsPinned) {
                    ivPin.setImageResource(R.drawable.ic_unpinned_24)
                } else {
                    ivPin.setImageResource(R.drawable.ic_pinned_24)
                }
                tempIsPinned = !tempIsPinned
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
            if (viewModel.selectedNote == null) {
                if (!etNoteTitle.text.isNullOrBlank() || !etNoteContent.text.isNullOrBlank()) {
                    viewModel.insert(
                        Note(
                            title = this.etNoteTitle.text.toString(),
                            content = this.etNoteContent.text.toString(),
                            lastModified = System.currentTimeMillis()
                        )
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.empty_note_discarded),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            } else {
                if (!toBeDeleted && isModified()) {
                    viewModel.update(
                        Note(
                            index = viewModel.selectedNote!!.index,
                            title = etNoteTitle.text.toString(),
                            content = etNoteContent.text.toString(),
                            isPinned = tempIsPinned,
                            lastModified = System.currentTimeMillis()
                        )
                    )
                }
            }
        }

    }

    private fun isModified(): Boolean {
        return binding!!.etNoteTitle.text.toString() != viewModel.selectedNote?.title ||
                binding!!.etNoteContent.text.toString() != viewModel.selectedNote?.content ||
                viewModel.selectedNote?.isPinned != tempIsPinned
    }

    companion object {
        private const val TAG = "EditNoteFragment"
        fun newInstance() = EditNoteFragment()
    }
}
