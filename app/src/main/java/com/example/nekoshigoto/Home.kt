package com.example.nekoshigoto

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.NumberFormat
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.util.*


class Home : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    var newPosition = 0;
    var startingPosition = 0;
    private lateinit var viewModel: JobSeekerViewModel
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        var sh : SharedPreferences = this.getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)
        var email = sh.getString("userid","").toString()
        viewModel = ViewModelProvider(this).get(JobSeekerViewModel::class.java)

        db.collection("Job Seeker").document(email).get()
            .addOnSuccessListener {
                val user = it.toObject<JobSeeker>()  //convert the doc into object
                if(user != null) {
                    viewModel.setJobSeeker(user)
                    val navController = this.findNavController(R.id.Container)
                    bottomNav.setupWithNavController(navController)
                    val appBarConfiguration = AppBarConfiguration(navController.graph)
                    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
                    supportActionBar?.setTitle("My Home")
//                    NavigationUI.setupActionBarWithNavController(this,navController)

                }

            }
            .addOnFailureListener{
                Toast.makeText(this, "Something went wrong. Please sign in again.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Logout::class.java)
                startActivity(intent)
            }

        db.collection("Qualification").document(email).get()
            .addOnSuccessListener {
                val qualification = it.toObject<Qualification>()  //convert the doc into object
                if(qualification != null)
                    viewModel.setQualification(qualification)
            }
            .addOnFailureListener{
                Toast.makeText(this, "Something went wrong. Please sign in again.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Logout::class.java)
                startActivity(intent)
            }
    }

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