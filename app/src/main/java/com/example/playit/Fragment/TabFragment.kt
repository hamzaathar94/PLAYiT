package com.example.playit.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playit.R
import com.example.playit.databinding.FragmentTabBinding

class TabFragment : Fragment() {
    private var binding:FragmentTabBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentTabBinding.inflate(LayoutInflater.from(requireContext()),container,false)



        return binding?.root
    }

}