package com.com.talyshdictionary.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.com.talyshdictionary.database.model.Entity
import com.com.talyshdictionary.database.model.TranslateRu

@Dao
interface RuTyDao {
    @Query("SELECT * FROM ru")
    fun getAll() : List<TranslateRu>

    @Query("SELECT * FROM ru WHERE LOWER(word) = :word LIMIT(1)")
    fun getTranslate(word: String) : TranslateRu?

    @Query("SELECT * FROM ru WHERE LOWER(word) LIKE :word LIMIT(15)")
    fun getTranslatePartialMatch(word: String) : List<TranslateRu>

    @Query("SELECT * FROM ru WHERE LOWER(translate) LIKE :word LIMIT(20)")
    fun getByTy(word: String) : List<TranslateRu>

    @Insert(entity = TranslateRu::class)
    fun insert(item: Entity)
}