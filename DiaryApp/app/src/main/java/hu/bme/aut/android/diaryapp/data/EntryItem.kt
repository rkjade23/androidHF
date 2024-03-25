package hu.bme.aut.android.diaryapp.data
import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.*

@Entity(tableName = "entryitem")
data class EntryItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "entry") var entry: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "mood") var mood: Mood,
    @ColumnInfo(name = "image") var image: Bitmap

) {
    enum class Mood {
        GOOD, AVERAGE, BAD;
        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int): Mood? {
                var ret: Mood? = null
                for (md in values()) {
                    if (md.ordinal == ordinal) {
                        ret = md
                        break
                    }
                }
                return ret
            }

            @JvmStatic
            @TypeConverter
            fun toInt(mood: Mood): Int {
                return mood.ordinal
            }
        }
    }
}


