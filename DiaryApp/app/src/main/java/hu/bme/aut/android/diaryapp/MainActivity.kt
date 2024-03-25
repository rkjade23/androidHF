package hu.bme.aut.android.diaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.diaryapp.adapter.EntryAdapter
import hu.bme.aut.android.diaryapp.data.EntryItem
import hu.bme.aut.android.diaryapp.data.EntryListDatabase
import hu.bme.aut.android.diaryapp.databinding.ActivityMainBinding
import hu.bme.aut.android.diaryapp.databinding.ItemEntryListBinding
import hu.bme.aut.android.diaryapp.fragments.ChartDialogFragment
import hu.bme.aut.android.diaryapp.fragments.NewEntryItemDialogFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), EntryAdapter.EntryItemClickListener,
    NewEntryItemDialogFragment.NewEntryItemDialogListener
     {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: EntryListDatabase
    private lateinit var adapter: EntryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = EntryListDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener{
            NewEntryItemDialogFragment().show(
                supportFragmentManager,
                NewEntryItemDialogFragment.TAG
            )
            NewEntryItemDialogFragment().createNew()
        }
        initRecyclerView()
    }

         private fun initRecyclerView() {
             adapter = EntryAdapter(this)
             binding.rvMain.layoutManager = LinearLayoutManager(this)
             binding.rvMain.adapter = adapter
             loadItemsInBackground()
         }

         private fun loadItemsInBackground() {
             thread {
                 val items = database.entryItemDao().getAll()
                 runOnUiThread {
                     adapter.update(items)
                 }
             }
         }

         override fun onEntryRemoved(entry: EntryItem){
             thread{
                 database.entryItemDao().deleteItem(entry)
                 runOnUiThread {
                     adapter.deleteEntry(entry)
                 }
                 Log.d("EntryActivity", "Entry delete was successful")
             }
         }

         override fun onItemChanged(item: EntryItem) {
             thread {
                 database.entryItemDao().update(item)
                 Log.d("MainActivity", "Entry update was successful")
             }
         }

         override fun onEntryEditing(item: EntryItem) {
             val fragment = NewEntryItemDialogFragment()

             val args = Bundle()
             args.putLong("id", item.id ?: -1)
             args.putString("title", item.title)
             args.putString("entry", item.entry)
             args.putInt("mood", EntryItem.Mood.toInt(item.mood))
             args.putParcelable("image", item.image)

             fragment.arguments = args
             fragment.show(
                 supportFragmentManager,
                 NewEntryItemDialogFragment.TAG
             )
         }

         override fun onEntryEdited(item: EntryItem) {
             thread {
                 database.entryItemDao().update(item)
                 val items = database.entryItemDao().getAll()
                 runOnUiThread {
                     adapter.update(items)
                 }
             }
         }

         override fun onEntryItemCreated(newItem: EntryItem) {
             thread {
                 val insertId = database.entryItemDao().insert(newItem)
                 newItem.id = insertId

                 runOnUiThread {
                     adapter.addItem(newItem)
                 }
             }
         }









         override fun onCreateOptionsMenu(menu: Menu) : Boolean {
             menuInflater.inflate(R.menu.toolbar_menu, menu)
             return super.onCreateOptionsMenu(menu)
         }

         override fun onOptionsItemSelected(item: MenuItem): Boolean{
             return when (item.itemId){
                 R.id.chartButton ->{
                     ChartDialogFragment().show(
                         supportFragmentManager,
                         NewEntryItemDialogFragment.TAG)
                     true

                 }
                 else -> super.onOptionsItemSelected(item)
             }
         }





}