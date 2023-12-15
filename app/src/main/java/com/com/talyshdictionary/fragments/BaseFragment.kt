package com.com.talyshdictionary.fragments

import android.app.AlertDialog
import android.content.res.Resources
import androidx.fragment.app.Fragment
import com.com.talyshdictionary.R
import com.com.talyshdictionary.activities.BaseActivity

open class BaseFragment : Fragment() {

    protected fun loadFragment(fragment: Fragment, containerId: Int = R.id.fragments_container) {
        activity?.let {
            (it as BaseActivity).loadFragment(fragment, containerId)
        }
    }

    protected fun addFragment(fragment: Fragment, isAddToBackStack: Boolean) {
        activity?.let {
            (it as BaseActivity).addFragment(fragment, isAddToBackStack)
        }
    }

    protected fun undoFragment() {
        activity?.let {
            (it as BaseActivity).undoFragment()
        }
    }

    protected fun clearBackStack() {
        activity?.let {
            (it as BaseActivity).clearBackStack()
        }
    }


    protected fun showError(error: String) {
        context?.let {
            AlertDialog.Builder(context)
                .setTitle(error)
                .setPositiveButton("ok", null)
                .create()
                .show()
        }
    }

    protected fun dpToPx(px: Int): Int {
        return (px * Resources.getSystem().displayMetrics.density).toInt()
    }



}