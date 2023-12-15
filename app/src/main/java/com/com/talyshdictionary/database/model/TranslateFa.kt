package com.com.talyshdictionary.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fa")
data class TranslateFa(@PrimaryKey(autoGenerate = true) val id: Int,
                       @ColumnInfo(name = "word") val word: String,
                       @ColumnInfo(name = "translate") val translate: String)