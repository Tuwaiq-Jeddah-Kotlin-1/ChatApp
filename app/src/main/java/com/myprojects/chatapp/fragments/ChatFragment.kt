package com.myprojects.chatapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.myprojects.chatapp.MainActivity
import com.myprojects.chatapp.MessageAdapter
import com.myprojects.chatapp.R
import com.myprojects.chatapp.models.Message
import com.myprojects.chatapp.models.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.collections.ArrayList
import kotlin.math.log


class ChatFragment : Fragment() {

    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    private lateinit var messages: ArrayList<Message>
    private lateinit var senderId: String
    private lateinit var receiverId: String
    private lateinit var roomId: String
    private lateinit var senderReceiver: ArrayList<User>
    private val rootRef = FirebaseFirestore.getInstance()
    private val chatsCollectionRef = rootRef.collection("chats")
    private val messagesCollectionRef = rootRef.collection("messages")
    private val userCollectionRef = rootRef.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)


        senderReceiver = getSenderAndReceiver()

        senderId = senderReceiver[0].id
        receiverId = senderReceiver[1].id

        roomId = arguments?.get("roomId") as String

        messages = ArrayList()

        val m1 = Message("Hello world",senderId,null)
        val m2 = Message("dont hello me",receiverId,null)

        val mList = arrayListOf(m1,m2)



        var senderRooms = senderReceiver[0]?.chatRooms
        var receiverRooms = senderReceiver[1]?.chatRooms

        if (roomId == "noRoomId"){
            roomId = chatsCollectionRef.document().id
            if(senderRooms != null){
                for ((key, _) in senderRooms){
                    if (receiverRooms != null){
                        if (receiverRooms.contains(key)){
                            roomId = key
                        }
                    }
                }
            }
        }



        messageBox = view.findViewById(R.id.sendMessageET)
        sendButton = view.findViewById(R.id.sendIB)
        messagesRecyclerView = view.findViewById(R.id.messageRV)
        messagesRecyclerView.layoutManager = LinearLayoutManager(context)

        //messagesRecyclerView.adapter = MessageAdapter(mList)

            GlobalScope.launch {

        }
        updateChat()

        sendButton.setOnClickListener {
            if (senderRooms == null){
                senderRooms = mutableMapOf()
            }
            senderRooms?.set(roomId, true)
            senderReceiver[0]?.chatRooms = senderRooms
            senderReceiver[0]?.let { rootRef.collection("users").document(senderId).set(it, SetOptions.merge()) }
            senderReceiver[1]?.let {
                rootRef.collection("friends").document(senderId)
                    .collection("friendList").document(receiverId).set(it, SetOptions.merge())
            }
            senderReceiver[1]?.let {
                rootRef.collection("chats").document(senderId)
                    .collection("userChatRooms").document(roomId).set(it, SetOptions.merge()) }

            if (receiverRooms == null){
                receiverRooms = mutableMapOf()
            }
            receiverRooms?.set(roomId, true)
            senderReceiver[1]?.chatRooms = receiverRooms
            senderReceiver[1]?.let { rootRef.collection("users").document(receiverId).set(it, SetOptions.merge()) }
            senderReceiver[0]?.let {
                rootRef.collection("friends").document(receiverId)
                    .collection("friendList").document(senderId).set(it, SetOptions.merge())
            }
            senderReceiver[0]?.let {
                rootRef.collection("chats").document(receiverId)
                    .collection("userChatRooms").document(roomId).set(it, SetOptions.merge()) }

            val messageText = messageBox.text.toString()
            val message = Message(messageText, senderId)
            rootRef.collection("messages")
                .document(roomId).collection("roomMessages").document(System.currentTimeMillis().toString()).set(message)
            GlobalScope.launch {

                //updateChat()
            }
            messageBox.text.clear()
        }


        return view
    }


    private fun getSender(): User {
        return arguments?.get("fromUser") as User
    }

    private fun getReceiver(): User {
        return arguments?.get("toUser") as User
    }

    private fun updateChat(){
        messagesCollectionRef.document(roomId).collection("roomMessages").addSnapshotListener { snapshots, e ->
            if (snapshots != null) {
                messages.clear()
                for (snap in snapshots){

                    messages.add(snap.toObject(Message::class.java))
                }
                Log.d("5555555555555555555", "${messages.size}")

                messagesRecyclerView.adapter = MessageAdapter(messages)
            }
        }
    }


//    private suspend fun getMessageList() {
//        messagesCollectionRef.document(roomId).collection("roomMessages").get().addOnCompleteListener { snapshot ->
//            if (snapshot.isSuccessful) {
//                for (snap in snapshot.result!!){
//                    val message = snap.toObject(Message::class.java)
//                    messages.add(message)
//                }
//                messagesRecyclerView.adapter = MessageAdapter(messages)
//            }
//        }.await()
//    }

    private fun getSenderAndReceiver(): ArrayList<User>{
        val senderReceiverList = ArrayList<User>()
        senderReceiverList.add((activity as MainActivity).getCurrentUser())//userCollectionRef.document(getSenderId()).get().result?.toObject(User::class.java)
        senderReceiverList.add(requireArguments().get("toUser") as User)//userCollectionRef.document(getReceiverId()).get().result?.toObject(User::class.java)

        return senderReceiverList
    }


}