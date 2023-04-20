package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nekoshigoto.databinding.FragmentAdminProfileBinding

class AdminProfileFragment : Fragment() {

    private lateinit var binding : FragmentAdminProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

}