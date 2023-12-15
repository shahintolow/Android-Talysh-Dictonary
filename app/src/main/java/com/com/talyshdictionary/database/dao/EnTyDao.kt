package com.com.talyshdictionary.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.com.talyshdictionary.database.model.Entity
import com.com.talyshdictionary.database.model.TranslateEn
import com.com.talyshdictionary.database.model.TranslateRu

@Dao
interface EnTyDao {
    @Query("SELECT * FROM en")
    fun getAll() : List<TranslateEn>

    @Query("SELECT * FROM en WHERE word = :word LIMIT(1)")
    fun getTranslate(word: String) : TranslateEn?

    @Query("SELECT * FROM en WHERE word LIKE :word LIMIT(15)")
    fun getTranslatePartialMatch(word: String) : List<TranslateEn>

    @Query("SELECT * FROM en WHERE LOWER(translate) LIKE :word LIMIT(20)")
    fun getByTy(word: String) : List<TranslateEn>

    @Insert(entity = TranslateEn::class)
    fun insert(item: Entity)
}