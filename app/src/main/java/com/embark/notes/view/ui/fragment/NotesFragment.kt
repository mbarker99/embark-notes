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
import com.google.android.material.snackbar.Snackbar

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
                viewModel.selectedNote = note
                findNavController().navigate(R.id.action_mainFragment_to_newNoteFragment)
            }
        })
    }

    private val unpinnedNotesAdapter: NotesAdapter by lazy {
        NotesAdapter(viewModel.unpinnedNotes, object : OnNoteClickedListener {
            override fun onNoteClicked(note: Note) {
                viewModel.selectedNote = note
                findNavController().navigate(
                    R.id.action_mainFragment_to_newNoteFragment
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

            searchBar.inflateMenu(R.menu.search_bar)
            searchBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.layout -> {
                        // TODO : Change layout of notes from StaggeredGrid to Linear
                    }
                }
                return@setOnMenuItemClickListener true
            }

            if (viewModel.discardingEmptyNote) {
                Snackbar.make(
                    binding.root as View,
                    R.string.empty_note_discarded,
                    Snackbar.LENGTH_LONG
                ).setAnchorView(binding.bottomAppBar)
                    .show()
                viewModel.discardingEmptyNote = false
            }

        }
    }

    private fun setupObservers() {
        viewModel.getAllNotesLiveData.observe(viewLifecycleOwner, Observer {

            /*if (viewModel.notes.isEmpty()) {
                binding.noNotesContainer.visibility = View.VISIBLE
                binding.notesContainer.visibility = View.GONE
            } else {
                binding.noNotesContainer.visibility = View.GONE
                binding.notesContainer.visibility = View.VISIBLE
            }*/

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