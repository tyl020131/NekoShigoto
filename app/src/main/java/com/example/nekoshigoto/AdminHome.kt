package com.example.nekoshigoto

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.nekoshigoto.databinding.ActivityAdminHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminHome : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        val navController = this.findNavController(R.id.Container)
        bottomNav.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this,navController)
    }
}