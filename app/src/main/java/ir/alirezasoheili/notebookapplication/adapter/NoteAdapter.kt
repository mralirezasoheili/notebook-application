package ir.alirezasoheili.notebookapplication.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.alirezasoheili.notebookapplication.databinding.NoteItemBinding
import ir.alirezasoheili.notebookapplication.fragments.home.HomeFragmentDirections
import ir.alirezasoheili.notebookapplication.model.Note
import kotlin.random.Random


class NoteAdapter : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    class ViewHolder(val noteItemBinding: NoteItemBinding) :
        RecyclerView.ViewHolder(noteItemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.body == newItem.body
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NoteItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNote = differ.currentList[position]
        holder.noteItemBinding.tvNoteTitle.text = currentNote.title
        holder.noteItemBinding.tvNoteBody.text = currentNote.body
        holder.noteItemBinding.ibColor.setBackgroundColor(getRandomColor())
        holder.itemView.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToUpdateNoteFragment(currentNote)
            it.findNavController().navigate(direction)
        }
    }

    private fun getRandomColor(): Int {
        return Color.argb(
            255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)
        )
    }
}