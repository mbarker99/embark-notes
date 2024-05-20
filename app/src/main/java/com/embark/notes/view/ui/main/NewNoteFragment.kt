package com.embark.notes.view.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.embark.notes.R
import com.embark.notes.databinding.FragmentNewNoteBinding
import com.embark.notes.model.Note
import com.embark.notes.viewmodel.NoteViewModel
import java.util.UUID

class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    private lateinit var binding: FragmentNewNoteBinding
    private val viewModel: NoteViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewNoteBinding.bind(view)
        setupUI()
    }

    private fun setupUI() {
        binding.btnSaveNote.setOnClickListener {
            viewModel.insert(
                Note(
                    uuid = UUID.randomUUID().mostSignificantBits,
                    title = binding.etNoteTitle.text.toString(),
                    content = binding.etNoteContent.text.toString()

                )
            )
            requireActivity().supportFragmentManager.popBackStack("MainFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

    }

    companion object {
        fun newInstance() = NewNoteFragment()
    }
}
