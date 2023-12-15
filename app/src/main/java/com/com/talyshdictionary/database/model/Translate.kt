package com.com.talyshdictionary.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

open class Translate(@PrimaryKey val id: Int,
                       @ColumnInfo(name = "word") var word: String,
                       @ColumnInfo(name = "translate") val translate: String)