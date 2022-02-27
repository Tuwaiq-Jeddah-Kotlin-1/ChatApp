package com.myprojects.chatapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.models.ChatList

class ChatListViewModel : ViewModel() {
    private val chatList = MutableLiveData<ArrayList<ChatList>>()

    init {
        chatList.value = ArrayList()
    }

    fun getChatList(): LiveData<ArrayList<ChatList>> {
        val uid = FirebaseAuth.getInstance().uid
        Firebase.firestore
            .collection("chats")
            .document(uid!!)
            .collection("userChatRooms")
            .get()
            .addOnCompleteListener { snapshot ->
                if (snapshot.isSuccessful) {
                    for (snap in snapshot.result!!) {
                        chatList.value?.clear()
                        chatList.value?.add(snap.toObject(ChatList::class.java))
                        chatList.postValue(chatList.value)
                    }
                }
            }
        return chatList
    }
}