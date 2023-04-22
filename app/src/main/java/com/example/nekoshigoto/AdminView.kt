package com.example.nekoshigoto

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.example.nekoshigoto.databinding.ActivityAdminViewBinding

class AdminView : AppCompatActivity() {
    private lateinit var binding : ActivityAdminViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.userViewBtn.setOnClickListener {
            findNavController(R.id.container).navigate(R.id.action_adminViewUserFragment2_self)
        }

        binding.companyViewBtn.setOnClickListener {
            findNavController(R.id.container).navigate(R.id.action_adminViewUserFragment2_to_adminViewCompanyFragment2)
            binding.companyViewBtn.setTextColor(ContextCompat.getColor(this, R.color.color2))
            binding.companyViewBtn.isClickable = false

            binding.userViewBtn.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.userViewBtn.isClickable = true

            binding.userViewBtn.setOnClickListener {
                findNavController(R.id.container).navigate(R.id.action_adminViewCompanyFragment2_to_adminViewUserFragment2)
                binding.userViewBtn.setTextColor(ContextCompat.getColor(this, R.color.color2))
                binding.userViewBtn.isClickable = false

                binding.companyViewBtn.setTextColor(ContextCompat.getColor(this, R.color.white))
                binding.companyViewBtn.isClickable = true
            }
        }


    }

}