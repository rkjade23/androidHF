package hu.bme.aut.android.diaryapp.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import hu.bme.aut.android.diaryapp.data.EntryItem
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray) : Bitmap{
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    @TypeConverter
    fun getByOrdinal(ordinal: Int): EntryItem.Mood? {
        var ret: EntryItem.Mood? = null
        for (md in EntryItem.Mood.values()) {
            if (md.ordinal == ordinal) {
                ret = md
                break
            }
        }
        return ret
    }

    @TypeConverter
    fun toInt(mood: EntryItem.Mood): Int {
        return mood.ordinal
    }
}