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
        setChatList()
    }

    fun setChatList() {
        val uid = FirebaseAuth.getInstance().uid
        Firebase.firestore
            .collection("chats")
            .document(uid!!)
            .collection("userChatRooms")
            .addSnapshotListener { snapshot, error ->
                if (snapshot != null) {
                    chatList.value?.clear()
                    for (snap in snapshot) {
                        chatList.value?.add(snap.toObject(ChatList::class.java))
                        chatList.postValue(chatList.value)
                    }
                }
            }
    }

    fun getChatList(): LiveData<ArrayList<ChatList>> {
        return chatList
    }
}