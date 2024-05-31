package com.embark.notes.view.ui.fragment

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.embark.notes.R
import com.embark.notes.databinding.FragmentEditNoteBinding
import com.embark.notes.model.Note
import com.embark.notes.viewmodel.NoteViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


class EditNoteFragment : Fragment(R.layout.fragment_edit_note) {

    private var binding: FragmentEditNoteBinding? = null
    private val viewModel: NoteViewModel by activityViewModels()

    private var tempIsPinned = false
    private var toBeDeleted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditNoteBinding.bind(view)
        setupUI()
        Log.d(TAG, "checklist::${viewModel.selectedNote?.checklist}")
    }

    private fun setupUI() {
        binding?.apply {

            topAppBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(getString(R.string.delete_note_dialog_title))
                            .setMessage(getString(R.string.delete_note_dialog_message))
                            .setPositiveButton(getString(R.string.delete_note_positive_button)) { dialog, _ ->
                                viewModel.selectedNote?.let { note -> viewModel.delete(note) }
                                toBeDeleted = true
                                dialog.dismiss()
                                findNavController().popBackStack()
                            }
                            .setNegativeButton(getString(R.string.delete_note_negative_button)) { dialog, _ ->
                                dialog.dismiss()
                            }.create().show()
                        true
                    }

                    R.id.pin -> {
                        if (tempIsPinned) {
                            Log.d(TAG, "topAppBar Unpinned")
                            menuItem.setIcon(R.drawable.ic_unpinned_24)
                        } else {
                            Log.d(TAG, "topAppBar Pinned")
                            menuItem.setIcon(R.drawable.ic_pinned_24)
                        }
                        tempIsPinned = !tempIsPinned
                        true
                    }

                    else -> false
                }
            }

            bottomAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.add -> {
                        // Handle accelerator icon press
                        true
                    }

                    R.id.change_color -> {
                        // Handle rotation icon press
                        true
                    }

                    R.id.edit_text -> {
                        true
                    }

                    else -> false
                }
            }
            if (viewModel.selectedNote != null) {
                etNoteTitle.setText(viewModel.selectedNote?.title ?: "")
                etNoteContent.setText(viewModel.selectedNote?.content ?: "")
                tempIsPinned = viewModel.selectedNote?.isPinned == true

                val pinItem = topAppBar.menu.findItem(R.id.pin)
                if (viewModel.selectedNote?.isPinned == true) {
                    pinItem.setIcon(R.drawable.ic_pinned_24)
                } else {
                    pinItem.setIcon(R.drawable.ic_unpinned_24)
                }

                viewModel.selectedNote?.lastModified?.let {
                    if (DateUtils.isToday(it)) {
                        val dateTimeFormatter =
                            DateTimeFormatter.ofPattern("h:mm a")
                                .withLocale(Locale.getDefault())
                                .withZone(ZoneId.systemDefault())

                        val date = dateTimeFormatter.format(Instant.ofEpochMilli(it))
                        topAppBar.subtitle = "Edited $date"
                    } else {
                        // TODO : Add date time formatter for Month / Day
                    }
                }
            } else {
                topAppBar.menu.clear()
                topAppBar.subtitle = ""
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
                            title = etNoteTitle.text.toString(),
                            content = etNoteContent.text.toString(),
                            lastModified = System.currentTimeMillis(),
                            createdAt = System.currentTimeMillis(),
                            isChecklist = true,
                            checklist = listOf("bread", "eggs", "milk")
                        )
                    )
                } else {
                    viewModel.discardingEmptyNote = true
                }
            } else {
                if (!toBeDeleted && isModified()) {
                    viewModel.update(
                        Note(
                            index = viewModel.selectedNote!!.index,
                            title = etNoteTitle.text.toString(),
                            content = etNoteContent.text.toString(),
                            isPinned = tempIsPinned,
                            lastModified = System.currentTimeMillis(),
                            createdAt = viewModel.selectedNote!!.createdAt
                        )
                    )
                }
            }
        }
        viewModel.selectedNote = null
    }

    private fun isModified(): Boolean {
        return binding?.etNoteTitle?.text.toString() != viewModel.selectedNote?.title ||
                binding?.etNoteContent?.text.toString() != viewModel.selectedNote?.content ||
                viewModel.selectedNote?.isPinned != tempIsPinned
    }

    companion object {
        private const val TAG = "EditNoteFragment"
        fun newInstance() = EditNoteFragment()
    }
}
