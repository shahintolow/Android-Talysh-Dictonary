package com.com.talyshdictionary.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.com.talyshdictionary.R
import com.com.talyshdictionary.database.model.Translate
import com.com.talyshdictionary.database.model.TranslateRu
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.com.talyshdictionary.databinding.ItemTranslateBinding
import java.lang.StringBuilder
import java.util.regex.Pattern

class RecyclerTranslateAdapter : RecyclerView.Adapter<RecyclerTranslateAdapter.ViewHolder>() {
          private val list = ArrayList<Translate>()
          private var _binding : ItemTranslateBinding? = null
          //private val binding get() = _binding!!


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val tvWord: TextView = view.findViewById(R.id.tv_word)
        val tvTranslateContainer: FlexboxLayout = view.findViewById(R.id.tv_translate_container)
        val bCopy: ImageButton = view.findViewById(R.id.b_copy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

         _binding = ItemTranslateBinding.inflate(LayoutInflater.from(parent.context), parent, false) //  from(parent.context) //.inflate(layoutInflate)//  ItemTranslateBinding.inflate()

        val  root = _binding!!.root

        val holder = ViewHolder(root)

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.view.context
        val item = list[position]

        val word = item.word
        holder.tvWord.text = getCurrentWord(word)
        holder.tvTranslateContainer.removeAllViews()

        val arr = item.translate.split(" ")
        val result = mutableListOf<String>()
        var i = 0
        while(i < arr.size) {
            val it = arr[i]
            val isRTL = RTL_CHARACTERS.matcher(it).find()
            val w = if(isRTL) it.reversed() else it

            if(isRTL) {
                val arrRTL = mutableListOf(w)
                var g = i+1

                if(g >= arr.size) break
                while(RTL_CHARACTERS.matcher(arr[g]).find()) {
                    arrRTL.add(0, arr[g].reversed())
                    g++
                    if(g >= arr.size) break
                }
                arrRTL.forEach {
                    result.add(it)
                }
                i = g
            } else {
                result.add(w)
                i++
            }
        }

        result.forEach {
            val s = StringBuilder().append(it).append(" ")
            val tv = LayoutInflater.from(context).inflate(R.layout.item_text,
                holder.tvTranslateContainer, false) as TextView
            tv.text = s
            holder.tvTranslateContainer.addView(tv)
        }

        holder.bCopy.setOnClickListener {
            copyText(context, item.translate)
        }
    }

    private fun copyText(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip)

        Toast.makeText(context, context.getString(R.string.text_copied), Toast.LENGTH_LONG).show()
    }

    private fun setMargins(holder: ViewHolder, position: Int) {
        val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
        if (position == 0)
            params.topMargin = dpToPx(20)
        else
            params.topMargin = dpToPx(12)

        if (position == list.lastIndex)
            params.bottomMargin = dpToPx(50)
        else
            params.bottomMargin = 0
        holder.itemView.layoutParams = params
    }


    override fun getItemCount() = list.size


    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun add(list: List<Translate>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun add(item: Translate) {
        this.list.add(item)
        notifyDataSetChanged()
    }

    fun getCurrentWord(word: String) : String {
        return if(word.contains("i"))
            word.replace("i", "ı")
        else
            word
    }


    private fun dpToPx(px: Int): Int {
        return (px * Resources.getSystem().displayMetrics.density).toInt()
    }

    private val RTL_CHARACTERS = Pattern.compile("[\u0600-\u06FF\u0750-\u077F\u0590-\u05FF\uFE70-\uFEFF]")



}