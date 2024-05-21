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
import com.embark.notes.view.ui.adapter.NotesAdapter
import com.embark.notes.viewmodel.NoteViewModel

class NotesFragment : Fragment(R.layout.fragment_main) {

    companion object {
        private const val TAG = "MainFragment"
        fun newInstance() = NotesFragment()
    }

    private lateinit var binding: FragmentMainBinding
    private val viewModel: NoteViewModel by activityViewModels()

    private val notesAdapter: NotesAdapter by lazy {
        NotesAdapter(viewModel.notes)
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
            rvNotes.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rvNotes.adapter = notesAdapter
        }
    }

    private fun setupObservers() {
        viewModel.getAllNotesLiveData.observe(viewLifecycleOwner, Observer {
            notesAdapter.setData(it)
        })
    }


}