package com.myprojects.chatapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.models.User

class FriendListViewModel : ViewModel() {
    private val friendList = MutableLiveData<ArrayList<User>>()

    init {
        friendList.value = ArrayList()
        setFriendList()
    }

    fun setFriendList(){
        val uid = FirebaseAuth.getInstance().uid!!
        Firebase.firestore
            .collection("friends")
            .document(uid)
            .collection("friendList")
            .get()
            .addOnCompleteListener { snapshot ->
                if (snapshot.isSuccessful){
                    for (snap in snapshot.result!!){
                        friendList.value?.add(snap.toObject(User::class.java))
                        friendList.postValue(friendList.value)
                    }
                }
            }
    }

    fun getFriendList(): LiveData<ArrayList<User>>{
        return friendList
    }
}