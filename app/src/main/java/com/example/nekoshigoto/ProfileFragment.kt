package com.example.nekoshigoto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.nekoshigoto.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.saveButton.visibility = View.GONE

        binding.qualificationButton.setOnClickListener {

            //it.findNavController().navigate(R.id.action_profileFragment_to_qualificationFragment)
            val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.Container, QualificationFragment())
            fragmentTransaction?.commit()
        }

        return binding.root

    }

    override fun onResume() {
        super.onResume()
        val activity = activity as Home
        activity?.hideUpButton()
    }


}