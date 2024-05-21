package com.embark.notes.view.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.embark.notes.R
import com.embark.notes.databinding.FragmentNewNoteBinding
import com.embark.notes.model.Note
import com.embark.notes.viewmodel.NoteViewModel
import java.util.UUID

class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    private var binding: FragmentNewNoteBinding? = null
    private val viewModel: NoteViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewNoteBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onPause() {
        super.onPause()

        binding?.apply {
            if (!etNoteTitle.text.isNullOrBlank() && !etNoteContent.text.isNullOrBlank()) {
                viewModel.insert(
                    Note(
                        uuid = UUID.randomUUID().mostSignificantBits,
                        title = this.etNoteTitle.text.toString(),
                        content = this.etNoteContent.text.toString()
                    )
                )
            } else {
                Toast.makeText(requireContext(), "Empty note discarded", Toast.LENGTH_LONG).show()
            }
        }

    }

    companion object {
        fun newInstance() = NewNoteFragment()
    }
}
