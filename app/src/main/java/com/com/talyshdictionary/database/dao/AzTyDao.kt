package com.com.talyshdictionary.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.com.talyshdictionary.database.model.Entity
import com.com.talyshdictionary.database.model.TranslateAz
import com.com.talyshdictionary.database.model.TranslateRu

@Dao
interface AzTyDao {
    @Query("SELECT * FROM az")
    fun getAll() : List<TranslateAz>

    @Query("SELECT * FROM az WHERE word = :word LIMIT(1)")
    fun getTranslate(word: String) : TranslateAz?

    @Query("SELECT * FROM az WHERE word LIKE :word LIMIT(15)")
    fun getTranslatePartialMatch(word: String) : List<TranslateAz>

    @Query("SELECT * FROM az WHERE LOWER(translate) LIKE :word LIMIT(20)")
    fun getByTy(word: String) : List<TranslateAz>

    @Insert(entity = TranslateAz::class)
    fun insert(item: Entity)
}