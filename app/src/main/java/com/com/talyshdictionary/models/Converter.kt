package com.com.talyshdictionary.models

import com.com.talyshdictionary.database.model.*

object Converter {

    fun toTranslate(items:  List<Any>, type: TranslateType) : List<Translate> {
        val arr = ArrayList<Translate>()
        items.forEach {
            when(type) {
                TranslateType.RU -> {
                    it as TranslateRu
                    arr.add(Translate(it.id, it.word, it.translate))
                }
                TranslateType.EN -> {
                    it as TranslateEn
                    arr.add(Translate(it.id, it.word, it.translate))
                }
                TranslateType.AZ -> {
                    it as TranslateAz
                    arr.add(Translate(it.id, it.word, it.translate))
                }
                TranslateType.FA -> {
                    it as TranslateFa
                    arr.add(Translate(it.id, it.word, it.translate))
                }

                else -> {

                }
            }

        }
        return arr
    }

}