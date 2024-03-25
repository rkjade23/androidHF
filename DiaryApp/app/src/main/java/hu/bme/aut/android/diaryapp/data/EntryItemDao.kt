package hu.bme.aut.android.diaryapp.data

import androidx.room.*

@Dao
interface EntryItemDao {
    @Query("SELECT * FROM entryitem")
    fun getAll(): List<EntryItem>


    @Insert
    fun insert(entryItems: EntryItem): Long

    @Update
    fun update(entryItem: EntryItem)

    @Delete
    fun deleteItem(entryItem: EntryItem)

}