package com.com.talyshdictionary.fragments

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.com.talyshdictionary.R
import com.com.talyshdictionary.activities.MainActivity
import com.com.talyshdictionary.adapters.RecyclerTranslateAdapter
import com.com.talyshdictionary.database.DBRoomManager
import com.com.talyshdictionary.managers.BillingManager
import com.com.talyshdictionary.models.TranslateType
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.common.AccountPicker
import com.com.talyshdictionary.databinding.FragmentTranslateBinding
import com.com.talyshdictionary.databinding.DialogActiveForUseBinding

import java.text.Normalizer
import java.util.regex.Pattern


class FragmentTranslate : BaseFragment() {
    private lateinit var adapter: RecyclerTranslateAdapter
    private lateinit var et_text : AppCompatEditText
    private lateinit var tv_select : TextView
    private lateinit var pb : ProgressBar
    private lateinit var b_replace_language : ImageButton
    private lateinit var rv_translate : RecyclerView
    private lateinit var spinner_to_language : AppCompatSpinner
    private lateinit var spinner_from_language : AppCompatSpinner

    private var from = TranslateType.RU
    private var to = TranslateType.TY

       private var _binding : FragmentTranslateBinding? = null
       private val binding get() = _binding!!

       private var bindAlert : DialogActiveForUseBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflater.inflate(R.layout.fragment_translate, container, false)

        _binding = FragmentTranslateBinding.inflate(inflater, container, false)

        val root = _binding!!.root

        et_text = root.findViewById(R.id.et_text)
        //tv_select = root.findViewById(R.id.tv_select)
        pb = root.findViewById(R.id.pb)
        b_replace_language = root.findViewById(R.id.b_replace_language)
        rv_translate = root.findViewById(R.id.rv_translate)
        spinner_to_language = root.findViewById(R.id.spinner_to_language)
        spinner_from_language = root.findViewById(R.id.spinner_from_language)

        setListeners()
        fillInfo()

        return root
    }



    override fun onDestroy() {
        super.onDestroy()
        DBRoomManager.stopAllFinding()
    }

    private fun requestEmail() {
        val account = AccountPicker.AccountChooserOptions
            .Builder()
            .setAllowableAccountsTypes(mutableListOf(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE))
            .setAlwaysShowAccountPicker(true)
            .build()

        val googlePicker = AccountPicker.newChooseAccountIntent(account)
        startActivityForResult(googlePicker, 1)
    }

    private fun setListeners() {
        et_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            @SuppressLint("FragmentLiveDataObserve")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.clear()
                val string = s.toString()
                if (string.isEmpty()) return

                pb.visibility = View.VISIBLE

                val arr = getStringArr(string)
                val clearArr = getListClearString(arr)
                DBRoomManager.getTranslatePartialMatch(
                    clearArr,
                    isFindingByPartActive(clearArr, string),
                    from,
                    to
                ).observe(this@FragmentTranslate,
                    Observer { result ->
                        adapter.clear()
                        pb.visibility = View.GONE

                        result.keys.forEach { key ->
                            val list = result[key]!!
                            val clear = list.distinctBy { it.id }
                                .sortedBy { it.word.length - key.length }

                            adapter.add(clear)
                        }
                    })
            }
        })
    }

    private fun fillInfo() {
        context?.let {
            rv_translate.layoutManager = LinearLayoutManager(it)
            adapter = RecyclerTranslateAdapter()
            rv_translate.adapter = adapter
        }

        inflateLanguage()
    }

    private fun inflateLanguage() {
        context?.let {
            val adapter = ArrayAdapter.createFromResource(
                it, R.array.language,
                R.layout.item_dropdown_language
            )
            adapter.setDropDownViewResource(R.layout.item_dropdown_language)
            spinner_from_language.adapter = adapter
            spinner_to_language.adapter = adapter

            b_replace_language.setOnClickListener {
                replace()
            }

            spinner_from_language.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?,
                        position: Int, id: Long
                    ) {
                        clear()
                        from = getTypeByPosition(position)
                        checkErrorSelected(true)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

            spinner_to_language.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?, view: View?,
                        position: Int, id: Long
                    ) {
                        clear()
                        to = getTypeByPosition(position)
                        checkErrorSelected(false)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
        }
    }

    private fun getTypeByPosition(pos: Int) = when (pos) {
        0 -> TranslateType.RU
        1 -> TranslateType.TY
        2 -> TranslateType.AZ
        3 -> TranslateType.EN
        else -> TranslateType.FA
    }

    private fun getPositionByType(type: TranslateType) = when (type) {
        TranslateType.RU -> 0
        TranslateType.TY -> 1
        TranslateType.AZ -> 2
        TranslateType.EN -> 3
        else -> 4
    }

    private fun replace() {
        val a = to
        val b = from

        to = b
        from = a

        notifyLanguageChanged()
    }

    private fun checkErrorSelected(isErrorInFrom: Boolean) {
        if (from == TranslateType.TY && to == TranslateType.TY) {
            if (isErrorInFrom)
                to = TranslateType.RU
            else
                from = TranslateType.RU
        }
        if (from != TranslateType.TY && to != TranslateType.TY) {
            if (isErrorInFrom)
                to = TranslateType.TY
            else
                from = TranslateType.TY
        }

        notifyLanguageChanged()
    }

    private fun notifyLanguageChanged() {
        spinner_to_language.setSelection(getPositionByType(to), true)
        spinner_from_language.setSelection(getPositionByType(from), true)
    }


    private fun clear() {
        adapter.clear()
        et_text.setText("")
    }


    private fun getListClearString(arr: List<String>): List<String> {
        val clear = mutableListOf<String>()
        for (it in arr)
            if (it.isEmpty())
                continue
            else {
                if ((RTL_CHARACTERS.matcher(it).find())) {
                    clear.add(unaccent(it))
                    if (it.contains("ه"))
                        clear.add(it.replace("ه", "ھ"))
                }

                if (it.contains("i")) {
                    clear.add(it)
                    clear.add((it.replace("i", "i̇")))
                } else if (it.contains("i̇")) {
                    clear.add(it)
                    clear.add(it.replace("i̇", "i"))
                } else if (it.contains("I")) {
                    clear.add(it.replace("I", "i"))
                    clear.add(it.replace("I", "i̇"))
                } else if (it.contains("ı")) {
                    clear.add(it.replace("ı", "i"))
                    clear.add(it.replace("ı", "i̇"))
                } else {
                    clear.add(it)
                }
            }

        for (it in arr) Log.i("test", "$it/  ${toHex(it)}")
        for (it in clear) Log.i("test", "$it// ${toHex(it)}")
        return clear
    }

    fun toHex(bin: String): String {
        var res = ""
        bin.toCharArray().forEach {
            var st = ""
            it.toString().toByteArray().forEach {
                st += String.format("%02X", it)
            }
            res += "$st "
        }
        return res
    }


    private fun getStringArr(s: String) = s.replace(".", " ")
        .replace(",", " ")
        .replace(";", " ")
        .replace("-", " ")
        .replace("  ", " ")
        .split(" ")

    private fun isFindingByPartActive(arr: List<String>, s: String) =
        !(arr.size == 1 && s.contains(" "))

    private var dialog: AlertDialog? = null
    private fun showDialog() {
        context?.let {
            dialog?.dismiss()
            //val view = LayoutInflater.from(it).inflate(R.layout.dialog_active_for_use, null)

            bindAlert = DialogActiveForUseBinding.inflate(LayoutInflater.from(it), null, false)

            dialog = AlertDialog.Builder(context)
                .setView(view)
                .create()
            bindAlert!!.root.findViewById<Button>(R.id.b_positive).setOnClickListener {
                (activity as MainActivity).goSub()
                dialog?.dismiss()
                dialog = null
            }
            bindAlert!!.root.findViewById<Button>(R.id.b_negative).setOnClickListener {
                dialog?.dismiss()
                dialog = null
            }
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()
        }
    }

    private val RTL_CHARACTERS =
        Pattern.compile("[\u0600-\u06FF\u0750-\u077F\u0590-\u05FF\uFE70-\uFEFF]")
    private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
    fun unaccent(it: String): String {
        val temp = Normalizer.normalize(it, Normalizer.Form.NFKC)
        return REGEX_UNACCENT.replace(temp, "")
    }
}