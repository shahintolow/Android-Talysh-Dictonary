package com.com.talyshdictionary.activities

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.com.talyshdictionary.R
import com.com.talyshdictionary.database.DBRoomManager
import com.com.talyshdictionary.firebase.FirebaseManager
import com.com.talyshdictionary.fragments.*
import com.com.talyshdictionary.managers.BillingManager
import com.com.talyshdictionary.models.TranslateType
import com.google.android.material.navigation.NavigationView
import com.com.talyshdictionary.databinding.ToolbarBinding
import com.com.talyshdictionary.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

          private var _binding : ToolbarBinding? = null
         // private val bindingToolbar get() = _binding!!
          lateinit var toolbar : Toolbar
          lateinit var drawer_layout : DrawerLayout

          private var _bindingActivityMain : ActivityMainBinding? = null
          //private var bindingMain get() = _bindingActivityMain!!
          lateinit var myMenu : NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AppCompat_NoActionBar)

        super.onCreate(savedInstanceState)

        _bindingActivityMain = ActivityMainBinding.inflate(layoutInflater)
        drawer_layout = _bindingActivityMain!!.root

        setContentView(drawer_layout)

        myMenu = drawer_layout.findViewById(R.id.my_menu)

        _binding = ToolbarBinding.inflate(layoutInflater)
         val rootToolbar = _binding!!.root

         toolbar = drawer_layout.findViewById<Toolbar>(R.id.toolbar)

        //toolbar = rootToolbar.findViewById(R.id.toolbar)


        DBRoomManager.init(this)
        BillingManager.init(this)

        loadFragment(FragmentTranslate())
        setupDrawerContent()
        setToolbar()
        FirebaseManager().getNewWord(TranslateType.EN, this)
        FirebaseManager().getNewWord(TranslateType.FA, this)
        FirebaseManager().getNewWord(TranslateType.AZ, this)
        FirebaseManager().getNewWord(TranslateType.RU, this)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            println(" toolbar press ")
            drawer_layout.openDrawer(GravityCompat.START, true)
        }

    }

    private val listener = NavigationView.OnNavigationItemSelectedListener { menuItem ->
        hideKeyboardFrom()
        when (menuItem.itemId) {
            R.id.translator -> loadFragment(FragmentTranslate())
            R.id.about_app -> loadFragment(FragmentInfoAboutApp())
            R.id.setting -> loadFragment(FragmentSetting())
            R.id.subs -> loadFragment(FragmentHelpProject())
            R.id.contacts -> loadFragment(FragmentContact())
            R.id.alphabet -> loadFragment(FragmentAlphabet())
        }
        drawer_layout.closeDrawer(GravityCompat.START, true)
        true
    }
    private fun setupDrawerContent() {
        myMenu.setNavigationItemSelectedListener(listener)
    }

    fun goSub() {
        myMenu.setCheckedItem(R.id.subs)
        listener.onNavigationItemSelected(myMenu.checkedItem!!)

    }

    override fun onResume() {
        super.onResume()
        BillingManager.queryPurchases()
    }

    private fun hideKeyboardFrom() {
        val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(drawer_layout.windowToken, 0)
    }
}
