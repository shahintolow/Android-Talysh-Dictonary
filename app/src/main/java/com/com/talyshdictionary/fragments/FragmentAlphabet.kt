package com.com.talyshdictionary.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.com.talyshdictionary.R
import com.com.talyshdictionary.databinding.FragmentAlphabet2Binding
import com.com.talyshdictionary.databinding.ItemAlphabetBinding
import com.google.android.flexbox.FlexboxLayout


class FragmentAlphabet : BaseFragment() {

      private var _binding : FragmentAlphabet2Binding? = null
      private val binding get() = _binding!!
      private var bindItem : ItemAlphabetBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View?  {

          _binding = FragmentAlphabet2Binding.inflate(inflater, container, false)
          val root: View = binding.root

          val cont =   root.findViewById<FlexboxLayout>(R.id.container)

        cont!!.removeAllViews()
        context?.resources?.getStringArray(R.array.alphabet_fa)?.forEach {
            Log.i("string", " sdf $it ")

             bindItem = ItemAlphabetBinding.inflate(LayoutInflater.from(context), container, false)

//            val view = LayoutInflater.from(context).inflate(
//                R.layout.item_alphabet,
//                container, false
//            )
            val arr = it.split("-")

            bindItem!!.root.findViewById<TextView>(R.id.tv_1).text = arr[0]
            bindItem!!.root.findViewById<TextView>(R.id.tv_2).text = arr[1]
            bindItem!!.root.findViewById<TextView>(R.id.tv_3).text = arr[2]

           // root.rootView.addView(bindItem)
            cont.addView(bindItem!!.root)
        }

       return root
    }
}