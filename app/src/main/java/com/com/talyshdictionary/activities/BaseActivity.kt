package com.com.talyshdictionary.activities

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.com.talyshdictionary.R
import com.com.talyshdictionary.managers.LocaleManager

open class BaseActivity : AppCompatActivity() {

    fun loadFragment(fragment: Fragment, containerId: Int = R.id.fragments_container) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.setCustomAnimations(
            R.anim.fragment_in_anim,
            R.anim.fragment_out_anim, R.anim.fragment_in_anim,
            R.anim.fragment_out_anim)
        transaction.replace(containerId, fragment)

        transaction.addToBackStack(null)

        transaction.commit()
    }

    fun addFragment(fragment: Fragment, isAddToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.setCustomAnimations(
            R.anim.fragment_in_anim,
            R.anim.fragment_out_anim, R.anim.fragment_in_anim,
            R.anim.fragment_out_anim)
        transaction.add(R.id.fragments_container, fragment)

        if(isAddToBackStack)
            transaction.addToBackStack(fragment.toString())

        transaction.setCustomAnimations(android.R.animator.fade_in,
            android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out)

        transaction.commit()

        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun undoFragment() {
        if(supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
    }

    fun clearBackStack() {
        for(i in 0 until supportFragmentManager.backStackEntryCount)
            supportFragmentManager.popBackStackImmediate()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
        else
            closeApp()
    }

    private fun closeApp() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.close_app))
            .setPositiveButton(getString(R.string.close)) { _, _ ->
                this.finishAffinity()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->

            }
            .create()
            .show()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(newBase))
    }
}
