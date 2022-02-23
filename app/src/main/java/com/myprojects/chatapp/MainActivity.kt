package com.myprojects.chatapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.mainBottomNav)
        val navController = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView)?.findNavController()

        if (navController != null){
            bottomNav.setupWithNavController(navController)
        }

        sharedPref = getSharedPreferences("stayLoggedIn", Context.MODE_PRIVATE)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_profile -> {
                supportFragmentManager
                    .findFragmentById(R.id.fragmentContainerView)
                    ?.findNavController()
                    ?.navigate(R.id.profileFragment)
                true
            }
            R.id.action_settings -> {
                supportFragmentManager
                    .findFragmentById(R.id.fragmentContainerView)
                    ?.findNavController()
                    ?.navigate(R.id.settingsFragment)
                true
            }
            R.id.action_logout -> {
                sharedPref.edit().putBoolean("loggedIn",false).apply()
                FirebaseAuth.getInstance().signOut()
                supportFragmentManager
                    .findFragmentById(R.id.fragmentContainerView)
                    ?.findNavController()
                    ?.navigate(R.id.loginFragment)
                true
            }
            else -> false
        }
    }
}