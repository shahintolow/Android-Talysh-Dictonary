package com.com.talyshdictionary.activities

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDexApplication
import com.com.talyshdictionary.managers.LocaleManager
import com.com.talyshdictionary.managers.LocaleManager.setLocale


class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setLocale(this)
    }
}