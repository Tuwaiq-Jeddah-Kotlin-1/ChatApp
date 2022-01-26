package com.myprojects.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.mainBottomNav)
        val navController = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView)?.findNavController()

        if (navController != null){
            bottomNav.setupWithNavController(navController)
        }


        var user = User()
        Firebase.firestore.collection("users")
            .document("nwhdk6pG3UgXTlQMy8GHpWOYa9t2")
            .get().addOnCompleteListener {
                if (it.isSuccessful){
                    user = it.result?.toObject(User::class.java)!!
                    Firebase.firestore.collection("friends")
                        .document("tyq8x73HB0MBvutTDCpiv0dyQET2")
                        .collection("friendList")
                        .document(user.id)
                        .set(user)
                }
            }


    }

    fun setCurrentUser() = CoroutineScope(Dispatchers.IO).launch{
        Log.d("TAG", "${FirebaseAuth.getInstance().uid}")
        FirebaseAuth.getInstance().uid?.let {
            Firebase.firestore.collection("users")
                .document(it)
                .get().addOnCompleteListener {
                    if (it.isSuccessful) currentUser = it.result?.toObject(User::class.java)!!
                }
        }
    }

    fun getCurrentUser(): User{
        return currentUser
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_profile -> {
                true
            }
            R.id.action_settings -> {
                true
            }
            R.id.action_logout -> {
                true
            }
            else -> false
        }
    }
}