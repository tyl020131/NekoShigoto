package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.nekoshigoto.databinding.FragmentAdminViewCompanyBinding

class AdminViewCompanyFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentAdminViewCompanyBinding>(inflater,
            R.layout.fragment_admin_view_company,container,false)
        return binding.root
    }
}