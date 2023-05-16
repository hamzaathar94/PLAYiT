package com.example.playit.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.playit.R
import com.example.playit.databinding.FragmentPermissionBinding

class PermissionFragment : Fragment() {
private var binding:FragmentPermissionBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPermissionBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        val name=arguments?.getString("name")
        Toast.makeText(requireContext(), "${name}", Toast.LENGTH_SHORT).show()
        binding?.btnpermission?.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        return binding?.root
    }

}