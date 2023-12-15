package com.com.talyshdictionary.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.com.talyshdictionary.R
import com.com.talyshdictionary.databinding.FragmentInfoAboutAppBinding

class FragmentInfoAboutApp : BaseFragment() {

      private var _binding : FragmentInfoAboutAppBinding? = null
      private val binding get() = _binding!!
      lateinit var t : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View?{

          _binding = FragmentInfoAboutAppBinding.inflate(inflater, container, false)
          val root: View = binding.root

          t = root.findViewById<TextView>(R.id.tv_decs_app)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            t.text = Html.fromHtml(getString(R.string.desc), Html.FROM_HTML_MODE_COMPACT)
        } else {
            t.text = Html.fromHtml(getString(R.string.desc))
        }

        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            t.text = Html.fromHtml(getString(R.string.desc), Html.FROM_HTML_MODE_COMPACT)
        } else {
            t.text = Html.fromHtml(getString(R.string.desc))
        }
    }
}