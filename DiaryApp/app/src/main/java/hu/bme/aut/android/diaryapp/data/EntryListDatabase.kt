package hu.bme.aut.android.diaryapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [EntryItem::class], version = 1)

@TypeConverters(Converters::class)
abstract class EntryListDatabase : RoomDatabase() {
    abstract fun entryItemDao(): EntryItemDao

    companion object {
        fun getDatabase(applicationContext: Context):EntryListDatabase {
            return Room.databaseBuilder(
                applicationContext,
                EntryListDatabase::class.java,
                "entry-list"
            ).build();
        }
    }
}