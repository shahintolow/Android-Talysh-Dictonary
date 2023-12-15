package com.com.talyshdictionary.database

import android.content.Context
import android.util.ArrayMap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.com.talyshdictionary.database.model.Entity
import com.com.talyshdictionary.database.model.Translate
import com.com.talyshdictionary.database.model.TranslateRu
import com.com.talyshdictionary.models.Converter
import com.com.talyshdictionary.models.TranslateType
import java.util.concurrent.Executors

object DBRoomManager {
    private const val tagDB = "db_words"
    private lateinit var db: Database

    fun init(context: Context) {
        db = Room.databaseBuilder(context, Database::class.java, "db_data.db")
            .createFromAsset("db/db_data.db")
            .build()
    }

    fun addFromServer(word: String, translate: String, locale: TranslateType) {
        Thread {
            when (locale) {
                TranslateType.RU -> {
                    val item = db.getRuTyDao().getTranslate(word)
                    if (item == null)
                        db.getRuTyDao().insert(Entity(word, translate))
                }
                TranslateType.EN -> {
                    val item = db.getEnTyDao().getTranslate(word)
                    if (item == null)
                        db.getEnTyDao().insert(Entity(word, translate))
                }
                TranslateType.FA -> {
                    val item = db.getFaTyDao().getTranslate(word)
                    if (item == null)
                        db.getFaTyDao().insert(Entity(word, translate))
                }
                TranslateType.AZ -> {
                    val item = db.getAzTyDao().getTranslate(word)
                    if (item == null)
                        db.getAzTyDao().insert(Entity(word, translate))
                }

                else -> {

                }
            }
        }.start()
    }

    fun getTranslateByWord(word: String): LiveData<TranslateRu?> {
        val data = MutableLiveData<TranslateRu?>()
        Thread {
            Log.i(tagDB, "word is $word")
            val result = db.getRuTyDao().getTranslate(word)
            Log.i(tagDB, "result is ${result?.word}")
            data.postValue(result)
        }.start()
        return data
    }

    fun getAll(): MutableLiveData<List<TranslateRu>> {
        val data = MutableLiveData<List<TranslateRu>>()
        Thread {
            val result = db.getRuTyDao().getAll()
            Log.i(tagDB, result.size.toString())
            Log.i(tagDB, result.get(3).word)
            data.postValue(result)
        }.start()
        return data
    }

    private var excecutor = Executors.newSingleThreadExecutor()
    fun getTranslatePartialMatch(
        words: List<String>, isFindingByPartActive: Boolean,
        from: TranslateType, to: TranslateType
    ): LiveData<ArrayMap<String, List<Translate>>> {
        try {
            if (excecutor.isShutdown) {
                excecutor = Executors.newSingleThreadExecutor()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val data = MutableLiveData<ArrayMap<String, List<Translate>>>()
        val runnable = Runnable {
            try {
                val arr = ArrayMap<String, List<Translate>>()

                words.forEach { word ->
                    val res = getBy(word, from, to).toMutableList()

                    if (from == TranslateType.FA)
                        res.addAll(getBy("$word%", from, to))
                    else if (isFindingByPartActive)
                        res.addAll(getBy("%$word%", from, to))

                    arr[word] = res
                }
                data.postValue(arr)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        try {
            excecutor.submit(runnable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    fun stopAllFinding() {
        try {
            excecutor.shutdownNow()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getBy(word: String, from: TranslateType, to: TranslateType): List<Translate> {
        return when (from) {
            TranslateType.TY -> {
                when (to) {
                    TranslateType.AZ -> Converter.toTranslate(
                        db.getAzTyDao()
                            .getByTy(word),
                        to
                    )
                    TranslateType.RU -> Converter.toTranslate(
                        db.getRuTyDao()
                            .getByTy(word),
                        to
                    )
                    TranslateType.EN -> Converter.toTranslate(
                        db.getEnTyDao()
                            .getByTy(word),
                        to
                    )
                    else -> Converter.toTranslate(
                        db.getFaTyDao()
                            .getByTy(word),
                        to
                    )
                }
            }
            TranslateType.AZ -> {
                val db = db.getAzTyDao()
                val arr = db.getTranslatePartialMatch(word)
                Converter.toTranslate(arr, from)
            }
            TranslateType.RU -> {
                val db = db.getRuTyDao()
                val arr = db.getTranslatePartialMatch(word)
                Converter.toTranslate(arr, from)
            }
            TranslateType.EN -> {
                val db = db.getEnTyDao()
                val arr = db.getTranslatePartialMatch(word)
                Converter.toTranslate(arr, from)
            }
            else -> {
                val db = db.getFaTyDao()
                val arr = db.getTranslatePartialMatch(word)
                Converter.toTranslate(arr, from)
            }
        }
    }


}