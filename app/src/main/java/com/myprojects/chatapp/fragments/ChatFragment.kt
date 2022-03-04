package com.myprojects.chatapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.myprojects.chatapp.adapters.MessageAdapter
import com.myprojects.chatapp.R
import com.myprojects.chatapp.viewmodels.SharedViewModel
import com.myprojects.chatapp.models.ChatList
import com.myprojects.chatapp.models.Message
import com.myprojects.chatapp.models.User
import com.myprojects.chatapp.utils.Utils
import com.myprojects.chatapp.viewmodels.ChatViewModel
import kotlin.collections.ArrayList


class ChatFragment : Fragment() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var senderId: String
    private lateinit var receiverId: String
    private lateinit var roomId: String
    private lateinit var senderReceiver: ArrayList<User>
    private lateinit var senderRooms: MutableMap<String, Any>
    private lateinit var receiverRooms: MutableMap<String, Any>
    private val rootRef = FirebaseFirestore.getInstance()
    private val chatsCollectionRef = rootRef.collection("chats")
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var chatVM: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = getSenderAndReceiver()[1].userName

        Utils.hideBottomNav(this)

        chatVM = ViewModelProvider(this).get(ChatViewModel::class.java)


        initFields()

        setRoomId()

        chatVM.setChatMessages(roomId)

        messageBox = view.findViewById(R.id.sendMessageET)
        sendButton = view.findViewById(R.id.sendIB)
        messagesRecyclerView = view.findViewById(R.id.messageRV)
        messagesRecyclerView.layoutManager = LinearLayoutManager(context)

        getChatMessages()

        sendButton.setOnClickListener {
            if (senderRooms == null) {
                senderRooms = mutableMapOf()
            }

            val messageText = messageBox.text.toString()
            val message = Message(messageText, senderId, System.currentTimeMillis())

            val senderChatList = ChatList(
                roomId,
                senderReceiver[1].id,
                senderReceiver[1].userName,
                messageText,
                System.currentTimeMillis().toString()
            )
            val receiverChatList = ChatList(
                roomId,
                senderReceiver[0].id,
                senderReceiver[0].userName,
                messageText,
                System.currentTimeMillis().toString()
            )

            senderRooms?.set(roomId, true)
            senderReceiver[0]?.chatRooms = senderRooms
            senderReceiver[0]?.let {
                rootRef.collection("users").document(senderId).set(it, SetOptions.merge())
            }
            senderReceiver[1]?.let {
                rootRef.collection("friends").document(senderId)
                    .collection("friendList").document(receiverId).set(it, SetOptions.merge())
            }

            rootRef.collection("chats").document(senderId)
                .collection("userChatRooms").document(roomId).set(senderChatList)

            if (receiverRooms == null) {
                receiverRooms = mutableMapOf()
            }
            receiverRooms?.set(roomId, true)
            senderReceiver[1]?.chatRooms = receiverRooms
            senderReceiver[1]?.let {
                rootRef.collection("users").document(receiverId).set(it, SetOptions.merge())
            }
            senderReceiver[0]?.let {
                rootRef.collection("friends").document(receiverId)
                    .collection("friendList").document(senderId).set(it, SetOptions.merge())
            }
                rootRef.collection("chats").document(receiverId)
                    .collection("userChatRooms").document(roomId).set(receiverChatList)



            rootRef.collection("messages")
                .document(roomId).collection("roomMessages")
                .document(System.currentTimeMillis().toString()).set(message)

            messageBox.text.clear()
            it.hideKeyboard()
        }


        return view
    }

    private fun initFields() {
        senderReceiver = getSenderAndReceiver()

        senderId = senderReceiver[0].id
        receiverId = senderReceiver[1].id

        roomId = arguments?.get("roomId") as String

        senderRooms = senderReceiver[0].chatRooms!!
        receiverRooms = senderReceiver[1].chatRooms!!
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun getChatMessages() {
        chatVM.getChatMessages().observe(viewLifecycleOwner,{messageList ->
            messagesRecyclerView.adapter = MessageAdapter(messageList)
        })
    }



    private fun getSenderAndReceiver(): ArrayList<User> {
        val senderReceiverList = ArrayList<User>()
        senderReceiverList.add(sharedViewModel.getCurrentUser().value!!)
        senderReceiverList.add(requireArguments().get("toUser") as User)

        return senderReceiverList
    }


    fun setRoomId(){
        if (roomId == "noRoomId") {
            roomId = chatsCollectionRef.document().id
            for ((key, _) in senderRooms) {
                if (receiverRooms.contains(key)) {
                    roomId = key
                }
            }
        }
    }

}