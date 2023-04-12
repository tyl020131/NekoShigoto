package com.example.nekoshigoto

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.nekoshigoto.databinding.FragmentProfileBinding
import com.example.nekoshigoto.databinding.FragmentQualificationBinding


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
            activity
                ?.supportFragmentManager
                ?.beginTransaction()?.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit)
                ?.replace(R.id.container, QualificationFragment())
                ?.commit()}

        return binding.root

    }


}