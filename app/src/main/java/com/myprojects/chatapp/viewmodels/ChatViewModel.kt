package com.myprojects.chatapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.models.Message

class ChatViewModel : ViewModel() {
    private val messages = MutableLiveData<ArrayList<Message>>()

    init {
        messages.value = ArrayList()
    }

    fun setChatMessages(roomId: String) {
        Firebase.firestore
            .collection("messages")
            .document(roomId)
            .collection("roomMessages")
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    messages.value?.clear()
                    for (snap in snapshot) {
                        messages.value?.add(snap.toObject(Message::class.java))
                        messages.postValue(messages.value)
                    }
                }
            }
    }

    fun getChatMessages(): LiveData<ArrayList<Message>> {
        return messages
    }
}