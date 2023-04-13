package com.example.nekoshigoto

import JobAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView


class Home : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    var newPosition = 0;
    var startingPosition = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //loadFragment(HomeFragment())
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.Container) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNav.setupWithNavController(navController)
        /*bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    newPosition = 1
                    navController.navigate(R.id.)
                    true
                }
                R.id.activity -> {
                    newPosition = 2
                    loadFragment(ActivityFragment())
                    true
                }
                R.id.consultation -> {
                    newPosition = 3
                    loadFragment(ConsulationFragment())
                    true
                }
                R.id.saved -> {
                    newPosition = 4
                    loadFragment(SavedFragment())
                    true
                }
                else ->{
                    newPosition = 5
                    loadFragment(ProfileFragment())
                    true
                }
            }
        }*/




    }

    private fun loadFragment(fragment: Fragment): Boolean {
        if(fragment != null) {
            if(startingPosition == newPosition){
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.Container, fragment).commit();
            }
            if(startingPosition > newPosition) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit )
                    .replace(R.id.Container, fragment).commit();

            }
            if(startingPosition < newPosition) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit)
                    .replace(R.id.Container, fragment).commit();

            }
            startingPosition = newPosition;
            return true;
        }

        return false;
    }


}