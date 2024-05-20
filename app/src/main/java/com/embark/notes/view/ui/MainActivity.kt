package com.embark.notes.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Insert
import androidx.room.Room
import com.embark.notes.R
import com.embark.notes.databinding.ActivityMainBinding
import com.embark.notes.db.NoteDatabase
import com.embark.notes.repository.NoteRepository
import com.embark.notes.view.ui.main.MainFragment
import com.embark.notes.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var noteRepository: NoteRepository
    private val viewModel: NoteViewModel by viewModels {
        viewModelFactory { NoteViewModel(noteRepository) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}