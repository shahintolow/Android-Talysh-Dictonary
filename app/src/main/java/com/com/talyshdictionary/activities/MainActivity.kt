package com.com.talyshdictionary.activities

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
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
import com.com.talyshdictionary.managers.NotificationReceiver
import java.util.Calendar
import java.util.Date

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
        myAlarm()
        FirebaseManager().getNewWord(TranslateType.EN, this)
        FirebaseManager().getNewWord(TranslateType.FA, this)
        FirebaseManager().getNewWord(TranslateType.AZ, this)
        FirebaseManager().getNewWord(TranslateType.RU, this)
    }

    private fun myAlarm() {

    val calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 3)
    calendar.set(Calendar.SECOND, 0)

    if (calendar.time < Date())
        calendar.add(Calendar.DAY_OF_MONTH, 1)

    val intent =  Intent(applicationContext, NotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(applicationContext , 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

    val alarmManager =  getSystemService(ALARM_SERVICE) as AlarmManager

    if (alarmManager != null) {
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
  }
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
