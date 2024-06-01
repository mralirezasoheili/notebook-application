package ir.alirezasoheili.notebookapplication.fragments

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
import ir.alirezasoheili.notebookapplication.MainActivity
import ir.alirezasoheili.notebookapplication.R
import ir.alirezasoheili.notebookapplication.databinding.FragmentNewNoteBinding
import ir.alirezasoheili.notebookapplication.model.Note
import ir.alirezasoheili.notebookapplication.viewmodel.NoteViewModel

class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        setUpMenu()
    }

    private fun setUpMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(getMenuProvider(), viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun getMenuProvider(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_add_note, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }

                    R.id.menu_add -> {
                        addNote()
                    }
                }
                return true
            }
        }
    }

    private fun addNote() {
        if (saveNote()) {
            Toast.makeText(
                activity, "Note Saved Successfully.", Toast.LENGTH_SHORT
            ).show()
            backToHomeFragment(requireView())
        } else {
            Toast.makeText(
                activity, "Please Enter Note Title First!", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun backToHomeFragment(view: View) {
        view.findNavController().navigate(
            R.id.action_newNoteFragment_to_homeFragment
        )
    }

    private fun saveNote(): Boolean {
        val noteTitle = binding.etNoteTitle.text.toString().trim()
        val noteBody = binding.etNoteBody.text.toString().trim()

        return if (noteTitle.isNotEmpty()) {
            noteViewModel.addNote(Note(0, noteTitle, noteBody))
            true
        } else {
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}