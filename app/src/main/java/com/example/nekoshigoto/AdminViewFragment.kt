package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.example.nekoshigoto.databinding.FragmentAdminViewBinding

class AdminViewFragment : Fragment() {
    private lateinit var binding : FragmentAdminViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAdminViewBinding.inflate(inflater, container, false)

        binding.userViewBtn.setOnClickListener {
            val navHostFragment = childFragmentManager.findFragmentById(R.id.container) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.action_adminViewUserFragment2_self)
        }

        binding.companyViewBtn.setOnClickListener {
            val navHostFragment = childFragmentManager.findFragmentById(R.id.container) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.action_adminViewUserFragment2_to_adminViewCompanyFragment2)
            binding.companyViewBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.color2))
            binding.companyViewBtn.isClickable = false

            binding.userViewBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.userViewBtn.isClickable = true

            binding.userViewBtn.setOnClickListener {
                val navHostFragment = childFragmentManager.findFragmentById(R.id.container) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.action_adminViewCompanyFragment2_to_adminViewUserFragment2)
                binding.userViewBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.color2))
                binding.userViewBtn.isClickable = false

                binding.companyViewBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.companyViewBtn.isClickable = true
            }
        }

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        val activity = activity as Home
        activity?.showBottomNav()
    }
}