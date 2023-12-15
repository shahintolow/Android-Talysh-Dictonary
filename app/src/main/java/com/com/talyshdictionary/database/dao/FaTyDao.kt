package com.com.talyshdictionary.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.com.talyshdictionary.database.model.Entity
import com.com.talyshdictionary.database.model.TranslateFa
import com.com.talyshdictionary.database.model.TranslateRu

@Dao
interface FaTyDao {
    @Query("SELECT * FROM fa")
    fun getAll() : List<TranslateFa>

    @Query("SELECT * FROM fa WHERE LOWER(word) = :word LIMIT(1)")
    fun getTranslate(word: String) : TranslateFa?

    @Query("SELECT * FROM fa WHERE LOWER(word) LIKE :word LIMIT(15)")
    fun getTranslatePartialMatch(word: String) : List<TranslateFa>

    @Query("SELECT * FROM fa WHERE LOWER(translate) LIKE :word LIMIT(20)")
    fun getByTy(word: String) : List<TranslateFa>

    @Insert(entity = TranslateFa::class)
    fun insert(item: Entity)
}