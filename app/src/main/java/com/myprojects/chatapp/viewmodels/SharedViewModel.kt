package com.myprojects.chatapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.models.User

class SharedViewModel : ViewModel() {
    private val currentUser = MutableLiveData<User>()

    fun setCurrentUser(){
        FirebaseAuth.getInstance().uid?.let {
            Firebase.firestore
                .collection("users")
                .document(it)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful) currentUser.value = it.result?.toObject(User::class.java)
                }
        }
    }

    fun getCurrentUser(): LiveData<User>{
        return currentUser
    }
}