package com.haris.expensetracker.room

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromTimeStamp(value: Long?): java.util.Date? = value?.let { java.util.Date(it) }

    @TypeConverter
    fun dateToTimeStamp(date: java.util.Date?): Long? = date?.time
}