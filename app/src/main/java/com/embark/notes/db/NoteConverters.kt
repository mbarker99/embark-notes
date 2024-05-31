package com.embark.notes.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class NoteConverters {
    @TypeConverter
    fun fromListString(intList: List<String>?): String? {
        return if (!intList.isNullOrEmpty())
            intList.toString()
        else null
    }

    @TypeConverter
    fun toListString(data: String?): List<String>? {
        return if (!data.isNullOrEmpty())
            listOf(*data.split(",").map { it }.toTypedArray())
        else null
    }
}