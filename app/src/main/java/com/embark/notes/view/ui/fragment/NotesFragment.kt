package com.embark.notes.view.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.embark.notes.R
import com.embark.notes.databinding.FragmentNotesBinding
import com.embark.notes.model.Note
import com.embark.notes.view.ui.adapter.NotesAdapter
import com.embark.notes.view.ui.adapter.NotesAdapter.OnNoteClickedListener
import com.embark.notes.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar

class NotesFragment : Fragment(R.layout.fragment_notes) {

    companion object {
        private const val TAG = "MainFragment"
        fun newInstance() = NotesFragment()
    }

    private var binding: FragmentNotesBinding? = null
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
                findNavController().navigate(R.id.action_mainFragment_to_newNoteFragment)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotesBinding.bind(view)
        setupUI()
        setupObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setupUI() {
        binding?.apply {
            btnAddNote.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_newNoteFragment)
            }

            viewModel.getAllNotes()
            Log.d(TAG, "Notes: viewModel.notes = ${viewModel.notes}")

            searchBar.setNavigationOnClickListener {
                main.open()
            }
            navigationView.setNavigationItemSelectedListener { menuItem ->
                // Handle menu item selected
                menuItem.isChecked = true
                main.close()
                true
            }

            searchBar.inflateMenu(R.menu.search_bar)
            searchBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.layout -> {
                        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                        if (sharedPref.getInt("displayType", 0) == 0) {
                            rvPinnedNotes.layoutManager =
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            rvUnpinnedNotes.layoutManager =
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                            it.setIcon(R.drawable.ic_grid_layout_24)
                            sharedPref.edit().putInt("displayType", 1).apply()
                        } else {
                            rvPinnedNotes.layoutManager =
                                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                            rvUnpinnedNotes.layoutManager =
                                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                            it.setIcon(R.drawable.ic_agenda_layout_24)
                            sharedPref.edit().putInt("displayType", 0).apply()
                        }

                    }
                }
                return@setOnMenuItemClickListener true
            }



            if (viewModel.discardingEmptyNote) {
                Snackbar.make(
                    binding?.root as View,
                    R.string.empty_note_discarded,
                    Snackbar.LENGTH_LONG
                ).setAnchorView(binding?.bottomAppBar)
                    .show()
                viewModel.discardingEmptyNote = false
            }

            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            if (sharedPref.getInt("displayType", 0) == 0) {
                rvPinnedNotes.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                rvUnpinnedNotes.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                searchBar.menu.findItem(R.id.layout).setIcon(R.drawable.ic_agenda_layout_24)
                sharedPref.edit().putInt("displayType", 0).apply()
            } else {
                rvPinnedNotes.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rvUnpinnedNotes.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                searchBar.menu.findItem(R.id.layout).setIcon(R.drawable.ic_grid_layout_24)
                sharedPref.edit().putInt("displayType", 1).apply()
            }
            rvPinnedNotes.adapter = pinnedNotesAdapter
            rvUnpinnedNotes.adapter = unpinnedNotesAdapter
        }
    }

    private fun setupObservers() {
        viewModel.getAllNotesLiveData.observe(viewLifecycleOwner, Observer {
            pinnedNotesAdapter.setData(viewModel.pinnedNotes)
            unpinnedNotesAdapter.setData(viewModel.unpinnedNotes)
            handleNoNotesVisibility()
            handlePinVisibility()
        })
    }

    private fun handleNoNotesVisibility() {
        binding?.apply {
            if (viewModel.notes.isEmpty()) {
                noNotesContainer.visibility = View.VISIBLE
                notesContainer.visibility = View.GONE
            } else {
                noNotesContainer.visibility = View.GONE
                notesContainer.visibility = View.VISIBLE
            }
        }
    }

    private fun handlePinVisibility() {
        binding?.apply {
            if (viewModel.pinnedNotes.isEmpty()) {
                pinnedNotesContainer.visibility = View.GONE
                tvUnpinnedNotesTitle.visibility = View.GONE
            } else {
                pinnedNotesContainer.visibility = View.VISIBLE
                tvUnpinnedNotesTitle.visibility = View.VISIBLE
                if (viewModel.unpinnedNotes.isEmpty()) {
                    unpinnedNotesContainer.visibility = View.GONE
                } else {
                    unpinnedNotesContainer.visibility = View.VISIBLE
                }
            }
        }
    }


}