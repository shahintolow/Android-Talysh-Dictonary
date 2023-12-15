package com.com.talyshdictionary.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.com.talyshdictionary.database.dao.AzTyDao
import com.com.talyshdictionary.database.dao.EnTyDao
import com.com.talyshdictionary.database.dao.FaTyDao
import com.com.talyshdictionary.database.dao.RuTyDao
import com.com.talyshdictionary.database.model.TranslateAz
import com.com.talyshdictionary.database.model.TranslateEn
import com.com.talyshdictionary.database.model.TranslateFa
import com.com.talyshdictionary.database.model.TranslateRu

@Database(entities = [TranslateRu::class, TranslateAz::class, TranslateEn::class, TranslateFa::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun getRuTyDao() : RuTyDao
    abstract fun getAzTyDao() : AzTyDao
    abstract fun getEnTyDao() : EnTyDao
    abstract fun getFaTyDao() : FaTyDao
}