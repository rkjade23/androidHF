package hu.bme.aut.android.diaryapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.diaryapp.R
import hu.bme.aut.android.diaryapp.data.EntryItem
import hu.bme.aut.android.diaryapp.databinding.ItemEntryListBinding

class EntryAdapter (private val listener: EntryItemClickListener) :
    RecyclerView.Adapter<EntryAdapter.EntryViewHolder>()
{
    private val items = mutableListOf<EntryItem>()

    interface EntryItemClickListener {
        fun onEntryRemoved(entry: EntryItem)
        fun onItemChanged(entry: EntryItem)
        fun onEntryEditing(entry: EntryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EntryViewHolder(
        ItemEntryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entryItem = items[position]

        holder.binding.ivIcon.setImageResource(getImageResource(entryItem.mood))
        holder.binding.tvTitle.text = entryItem.title
        holder.binding.tvDate.text = entryItem.date

        holder.binding.ibRemove.setOnClickListener{
            listener.onEntryRemoved(entryItem)
        }

        holder.binding.ibEdit.setOnClickListener{
            listener.onEntryEditing(entryItem)
        }
    }

    @DrawableRes()
    private fun getImageResource(mood: EntryItem.Mood): Int {
        return when (mood) {
            EntryItem.Mood.GOOD -> R.drawable.ic_green
            EntryItem.Mood.AVERAGE -> R.drawable.ic_yellow
            EntryItem.Mood.BAD -> R.drawable.ic_red
        }
    }

    fun addItem(item: EntryItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(entryItems: List<EntryItem>) {
        items.clear()
        items.addAll(entryItems)
        notifyDataSetChanged()
    }

    fun deleteEntry(entry: EntryItem){
        val id = items.indexOf(entry)
        items.remove(entry)
        notifyItemRemoved(id)
    }

    override fun getItemCount(): Int = items.size

    inner class EntryViewHolder(val binding: ItemEntryListBinding) : RecyclerView.ViewHolder(binding.root){

    }
}