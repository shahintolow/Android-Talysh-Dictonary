package com.com.talyshdictionary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import com.com.talyshdictionary.R
import com.com.talyshdictionary.managers.LocaleManager
import com.com.talyshdictionary.managers.LocaleManager.setLocale
import com.com.talyshdictionary.models.TranslateType
import com.com.talyshdictionary.databinding.FragmentSettingBinding

class FragmentSetting : BaseFragment() {

      private var _binding : FragmentSettingBinding? = null
      private val binding get() = _binding!!
      private lateinit var spinnerselectLanguage : AppCompatSpinner


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View?{

          _binding = FragmentSettingBinding.inflate(inflater, container, false)
          val root: View = binding.root

        spinnerselectLanguage = root.findViewById<AppCompatSpinner>(R.id.spinner_select_language)


        fillInfo()

        return root
    }


    private fun fillInfo() {
        context?.let {
            val adapter = ArrayAdapter.createFromResource(
                it, R.array.language_app,
                R.layout.item_dropdown_language
            )
            adapter.setDropDownViewResource(R.layout.item_dropdown_language)
            spinnerselectLanguage.adapter = adapter

            spinnerselectLanguage.setSelection(getPositionByType(LocaleManager.getLanguage(it)))
            spinnerselectLanguage.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?,
                        position: Int, id: Long
                    ) {
                        val code = when(position) {
                            0 -> TranslateType.EN
                            1 -> TranslateType.FA
                            2 -> TranslateType.AZ
                            else -> TranslateType.RU
                        }
                        if(LocaleManager.getLanguage(it) != code.name) {
                            LocaleManager.saveLanguage(it, code)
                            setLocale(context)
                            activity?.recreate()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
        }
    }

    private fun getPositionByType(code: String) = when(code) {
        TranslateType.EN.name -> 0
        TranslateType.FA.name -> 1
        TranslateType.AZ.name -> 2
        else -> 3
    }


}