package ir.alirezasoheili.notebookapplication.fragments.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import ir.alirezasoheili.notebookapplication.MainActivity
import ir.alirezasoheili.notebookapplication.R
import ir.alirezasoheili.notebookapplication.adapter.NoteAdapter
import ir.alirezasoheili.notebookapplication.databinding.FragmentHomeBinding
import ir.alirezasoheili.notebookapplication.model.Note
import ir.alirezasoheili.notebookapplication.viewmodel.NoteViewModel
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var noteAdapter: NoteAdapter

    private var isGridView = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel = (activity as MainActivity).noteViewModel
        binding.lifecycleOwner = this
        setUpRecyclerView()

        // add click listener to floating action button
        binding.fabAddNote.setOnClickListener {
            goToNewNoteFragment(it)
        }

        setUpMenu()
    }

    private fun goToNewNoteFragment(view: View) {
        view.findNavController().navigate(
            R.id.action_homeFragment_to_newNoteFragment
        )
    }

    private fun setUpMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(getMenuProvider(), viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun getMenuProvider(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu)
                setUpSearchView(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }

                    R.id.menu_delete_all -> {
                        deleteAllNotes()
                    }

                    R.id.view_grid_or_list -> {
                        isGridView = !isGridView
                        menuItem.title = if (isGridView) {
                            getString(R.string.view_as_list)
                        } else {
                            getString(R.string.view_as_grid)
                        }
                        setLayoutManager()
                    }
                }
                return false
            }
        }
    }

    private fun deleteAllNotes() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete All Notes")
            setMessage("Are you sure want to delete All notes?")
            setPositiveButton("Delete") { _, _ ->
                noteViewModel.deleteAll()
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    private fun setUpSearchView(menu: Menu) {
        val menuItem: MenuItem = menu.findItem(R.id.menu_search)
        val menuSearch = menuItem.actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(getQueryTextListener())
    }

    private fun getQueryTextListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchNote(newText)
                }
                return true
            }
        }
    }

    private fun searchNote(query: String?) {
        noteViewModel.searchNote(query).observe(
            this
        ) { list ->
            noteAdapter.differ.submitList(list)
        }
    }

    private fun setUpRecyclerView() {
        noteAdapter = NoteAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.apply {
            setLayoutManager()
            setHasFixedSize(true)
            adapter = noteAdapter
            // Animation for recycler view
            recyclerView.itemAnimator = FadeInUpAnimator().apply {
                addDuration = 300
            }
            // swipe to delete
            swipeToDelete(binding.recyclerView)
        }
        activity?.let {
            noteViewModel.getAllNotes().observe(viewLifecycleOwner) { note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
                binding.recyclerView.scheduleLayoutAnimation()
            }
        }
    }

    private fun setLayoutManager() {
        val spanCount = if (isGridView) {
            2
        } else {
            1
        }
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(
            spanCount, StaggeredGridLayoutManager.VERTICAL
        )
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedNote = noteAdapter.differ.currentList[viewHolder.adapterPosition]
                // Delete Note
                noteViewModel.deleteNote(deletedNote)
                // Restore Deleted Note
                restoreDeletedNote(viewHolder.itemView, deletedNote, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedNote(view: View, deletedNote: Note, position: Int) {
        val snackBar = Snackbar.make(view, "Deleted ${deletedNote.title}", Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo") {
            noteViewModel.addNote(deletedNote)
            noteAdapter.notifyItemChanged(position)
        }
        snackBar.show()
    }

    private fun updateUI(note: List<Note>?) {
        if (note != null) {
            if (note.isNotEmpty()) {
                binding.cardView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            } else {
                binding.cardView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}