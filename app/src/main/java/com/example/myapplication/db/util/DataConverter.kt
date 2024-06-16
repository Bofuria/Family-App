package com.example.myapplication.db.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date
import java.lang.reflect.Type
import javax.inject.Singleton

@Singleton
class DataConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String): ArrayDeque<String> {
        val listType: Type = object : TypeToken<ArrayDeque<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayDeque(list: ArrayDeque<String>): String {
        return Gson().toJson(list)
    }
}
