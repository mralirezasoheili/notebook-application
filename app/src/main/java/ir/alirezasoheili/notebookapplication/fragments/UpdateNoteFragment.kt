package ir.alirezasoheili.notebookapplication.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import ir.alirezasoheili.notebookapplication.MainActivity
import ir.alirezasoheili.notebookapplication.R
import ir.alirezasoheili.notebookapplication.databinding.FragmentUpdateNoteBinding
import ir.alirezasoheili.notebookapplication.model.Note
import ir.alirezasoheili.notebookapplication.viewmodel.NoteViewModel

class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {
    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var currentNote: Note

    private val args: UpdateNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMenu()
        noteViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

        initialEditTexts()

        binding.fabUpdateNote.setOnClickListener {
            if (updateNote()) {
                Toast.makeText(
                    it.context, "Note Updated Successfully.", Toast.LENGTH_SHORT
                ).show()
                backToHomeFragment(it)
            } else {
                Toast.makeText(
                    it.context, "Please Enter Note Title First!", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setUpMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(getMenuProvider(), viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun getMenuProvider(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_update_note, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                        return true
                    }

                    R.id.menu_delete -> {
                        deleteNote()
                    }
                }
                return false
            }
        }
    }

    private fun backToHomeFragment(view: View) {
        view.findNavController().navigate(
            R.id.action_updateNoteFragment_to_homeFragment
        )
    }

    private fun initialEditTexts() {
        binding.etNoteTitle.setText(currentNote.title)
        binding.etNoteBody.setText(currentNote.body)
    }

    private fun updateNote(): Boolean {
        val noteTitle = binding.etNoteTitle.text.toString().trim()
        val noteBody = binding.etNoteBody.text.toString().trim()
        return if (noteTitle.isNotEmpty()) {
            noteViewModel.updateNote(Note(currentNote.id, noteTitle, noteBody))
            true
        } else {
            false
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you sure want to delete this note?")
            setPositiveButton("Delete") { _, _ ->
                noteViewModel.deleteNote(currentNote)
                backToHomeFragment(requireView())
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}