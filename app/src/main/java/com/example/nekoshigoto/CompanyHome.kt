package com.example.nekoshigoto

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class CompanyHome : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    private lateinit var viewModel: CompanyViewModel
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_home)
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        val navController = this.findNavController(R.id.Container)
        bottomNav.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        supportActionBar?.setTitle("My Company")
        var sh : SharedPreferences = this.getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)
        var email = sh.getString("userid","").toString()
        viewModel = ViewModelProvider(this).get(CompanyViewModel::class.java)

        db.collection("Company").document(email).get()
            .addOnSuccessListener {
                val user = it.toObject<Company>()  //convert the doc into object
                if(user != null)
                    viewModel.setCompany(user)
            }
            .addOnFailureListener{
                Toast.makeText(this, "Something went wrong. Please sign in again.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Logout::class.java)
                startActivity(intent)
            }
        /*bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    newPosition = 1
                    loadFragment(VacancyFragment())
                    true
                }
                R.id.explore -> {
                    newPosition = 2
                    loadFragment(ViewUserFragment())
                    true
                }
               else -> {
                    newPosition = 3
                    loadFragment(ConsulationFragment())
                    true
                }
            }
        }*/


    }

//    private fun setSession(){
//        val sharedPreferences: SharedPreferences = getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)
//
//        val myEdit: SharedPreferences.Editor = sharedPreferences.edit()
//
//        myEdit.putString("CompanyName", "Ikun Studio")
//        myEdit.commit()
//
//
//    }
    fun hideBottomNav(){
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.visibility = View.GONE
    }
    fun showBottomNav(){
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.visibility = View.VISIBLE
    }
    fun chgTitle(title:String){
        supportActionBar?.setTitle(title)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.Container)
        return navController.navigateUp()
    }

}