package com.embark.notes.view.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.embark.notes.R
import com.embark.notes.databinding.FragmentMainBinding
import com.embark.notes.model.Note
import com.embark.notes.view.ui.adapter.NotesAdapter
import com.embark.notes.view.ui.adapter.NotesAdapter.OnNoteClickedListener
import com.embark.notes.viewmodel.NoteViewModel

class NotesFragment : Fragment(R.layout.fragment_main) {

    companion object {
        private const val TAG = "MainFragment"
        fun newInstance() = NotesFragment()
    }

    private lateinit var binding: FragmentMainBinding
    private val viewModel: NoteViewModel by activityViewModels()

    private val pinnedNotesAdapter: NotesAdapter by lazy {
        NotesAdapter(viewModel.pinnedNotes, object : OnNoteClickedListener {
            override fun onNoteClicked(note: Note) {
                val noteBundle = Bundle()
                noteBundle.putLong("index", note.index)
                noteBundle.putString("title", note.title)
                noteBundle.putString("content", note.content)
                noteBundle.putBoolean("isPinned", note.isPinned == true)
                noteBundle.putLong("lastModified", note.lastModified)
                findNavController().navigate(
                    R.id.action_mainFragment_to_newNoteFragment,
                    noteBundle
                )
            }
        })
    }

    private val unpinnedNotesAdapter: NotesAdapter by lazy {
        NotesAdapter(viewModel.unpinnedNotes, object : OnNoteClickedListener {
            override fun onNoteClicked(note: Note) {
                val noteBundle = Bundle()
                noteBundle.putLong("index", note.index)
                noteBundle.putString("title", note.title)
                noteBundle.putString("content", note.content)
                noteBundle.putBoolean("isPinned", note.isPinned == true)
                noteBundle.putLong("lastModified", note.lastModified)
                findNavController().navigate(
                    R.id.action_mainFragment_to_newNoteFragment,
                    noteBundle
                )
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        binding.apply {
            btnAddNote.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_newNoteFragment)
            }

            viewModel.getAllNotes()
            Log.d(TAG, "Notes: viewModel.notes = ${viewModel.notes}")

            rvPinnedNotes.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rvPinnedNotes.adapter = pinnedNotesAdapter

            rvUnpinnedNotes.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rvUnpinnedNotes.adapter = unpinnedNotesAdapter
        }
    }

    private fun setupObservers() {
        viewModel.getAllNotesLiveData.observe(viewLifecycleOwner, Observer {

            if (viewModel.notes.isEmpty()) {
                binding.noNotesContainer.visibility = View.VISIBLE
                binding.notesContainer.visibility = View.GONE
            } else {
                binding.noNotesContainer.visibility = View.GONE
                binding.notesContainer.visibility = View.VISIBLE
            }

            pinnedNotesAdapter.setData(viewModel.pinnedNotes)
            unpinnedNotesAdapter.setData(viewModel.unpinnedNotes)

            if (viewModel.pinnedNotes.isEmpty()) {
                binding.pinnedNotesContainer.visibility = View.GONE
                binding.tvUnpinnedNotesTitle.visibility = View.GONE
            } else {
                binding.pinnedNotesContainer.visibility = View.VISIBLE
                binding.tvUnpinnedNotesTitle.visibility = View.VISIBLE
            }

        })
    }


}