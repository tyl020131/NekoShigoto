package com.example.nekoshigoto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class CompanyHome : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    var newPosition = 0;
    var startingPosition = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_home)

        loadFragment(VacancyFragment())
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
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
        }




    }

    private fun loadFragment(fragment: Fragment): Boolean {
        if(fragment != null) {
            if(startingPosition == newPosition){
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment).commit();
            }
            if(startingPosition > newPosition) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit )
                    .replace(R.id.container, fragment).commit();

            }
            if(startingPosition < newPosition) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit)
                    .replace(R.id.container, fragment).commit();

            }
            startingPosition = newPosition;
            return true;
        }

        return false;
    }
}