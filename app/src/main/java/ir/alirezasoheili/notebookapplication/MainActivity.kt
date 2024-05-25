package ir.alirezasoheili.notebookapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ir.alirezasoheili.notebookapplication.database.NoteDatabase
import ir.alirezasoheili.notebookapplication.databinding.ActivityMainBinding
import ir.alirezasoheili.notebookapplication.repository.NoteRepository
import ir.alirezasoheili.notebookapplication.viewmodel.NoteViewModel
import ir.alirezasoheili.notebookapplication.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
    }

    private fun setUpViewModel() {
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelFactory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]
    }
}