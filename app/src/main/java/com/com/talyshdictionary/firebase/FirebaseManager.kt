package com.com.talyshdictionary.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.com.talyshdictionary.R
import com.com.talyshdictionary.database.DBRoomManager
import com.com.talyshdictionary.models.TranslateType
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseManager {
    private val tag = "firebase"

    fun getNewWord(locale: TranslateType, context: Context) {
        Firebase.firestore
            .collection("Words")
            .document(locale.name)
            .get()
            .addOnSuccessListener { document ->
                document.data?.forEach {
                    DBRoomManager.addFromServer(it.key, it.value.toString(), locale)
                }
                Toast.makeText(context, context.getString(R.string.s4), Toast.LENGTH_LONG).show()
                Log.d(tag, "firebase data get success")
            }
            .addOnFailureListener {
                Log.d(tag, "firebase data get error: ${it.message}")
            }
    }
}