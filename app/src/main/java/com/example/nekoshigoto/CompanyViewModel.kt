package com.example.nekoshigoto

import androidx.lifecycle.ViewModel

class CompanyViewModel : ViewModel(){
    private var company = Company("","","","","","","","","")

    fun getCompany():Company{
        return this.company
    }

    fun setCompany(company: Company) {
        this.company = company
    }
}