package com.example.nekoshigoto

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class Home : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    var newPosition = 0;
    var startingPosition = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        loadFragment(HomeFragment())
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        val selectedItem = intent.getIntExtra("selectedItem", 0)
        bottomNav.menu.findItem(selectedItem)?.isChecked = true

        if(selectedItem != 0)
        {
            when(selectedItem) {
                R.id.home -> {
                    newPosition = 1
                    loadFragment(HomeFragment())
                                    }
                R.id.activity -> {
                    newPosition = 2
                    loadFragment(ActivityFragment())
                                    }
                R.id.consultation -> {
                    newPosition = 3
                    loadFragment(ConsulationFragment())
                                    }
                R.id.saved -> {
                    newPosition = 4
                    loadFragment(SavedFragment())
                                    }
                else ->{
                    newPosition = 5
                    loadFragment(ProfileFragment())
                }
            }

        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    newPosition = 1
                    loadFragment(HomeFragment())
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
        }




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

//    override fun onBackPressed() {
//
//        val fm: FragmentManager = supportFragmentManager
//        if (fm.backStackEntryCount > 0) {
//            Log.i("MainActivity", "popping backstack")
//            fm.popBackStack()
//        } else {
//            Log.i("MainActivity", "nothing on backstack, calling super")
//            super.onBackPressed()
//        }
//    }

    override fun onBackPressed() {

        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 1) {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            fragmentManager.popBackStackImmediate()
        } else {
            Toast.makeText(this, fragmentManager.backStackEntryCount.toString(), Toast.LENGTH_SHORT).show()
            super.onBackPressed()
        }
    }

    fun showUpButton() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun hideUpButton() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }


}