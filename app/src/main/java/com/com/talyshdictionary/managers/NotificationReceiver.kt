package com.com.talyshdictionary.managers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {



    override fun onReceive(p0: Context?, p1: Intent?) {
        val notifiHelp = NotificationHelper(p0!!)
        notifiHelp.aCreateNotify()
    }

}