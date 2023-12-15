package com.com.talyshdictionary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.com.talyshdictionary.databinding.FragmentSubsBinding

class FragmentHelpProject : BaseFragment() {

      private var _binding : FragmentSubsBinding? = null
      private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {

          _binding = FragmentSubsBinding.inflate(inflater, container, false)
          val root: View = binding.root


        return root
    }
}