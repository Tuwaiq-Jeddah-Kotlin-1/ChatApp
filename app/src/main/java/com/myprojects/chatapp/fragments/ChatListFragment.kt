package com.myprojects.chatapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.ChatListAdapter
import com.myprojects.chatapp.MainActivity
import com.myprojects.chatapp.R
import com.myprojects.chatapp.models.ChatList


class ChatListFragment : Fragment() {


    private lateinit var bottomNav: BottomNavigationView
    private lateinit var chatListRV: RecyclerView
    private lateinit var chatList: ArrayList<ChatList>
    private val uid = FirebaseAuth.getInstance().uid
    private val chatsCollectionRef = Firebase.firestore.collection("chats")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.chats_fragment)
        // Inflate the layout for this fragment
        chatList = ArrayList()

        val view = inflater.inflate(R.layout.fragment_list_chat, container, false)
        bottomNav = requireActivity().findViewById(R.id.mainBottomNav)
        bottomNav.visibility = View.VISIBLE

        chatListRV = view.findViewById(R.id.chatListRV)
        chatListRV.layoutManager = LinearLayoutManager(context)

        loadChatList()

        return view
    }

    private fun loadChatList(){
        chatsCollectionRef.document(uid!!)
            .collection("userChatRooms")
            .get()
            .addOnCompleteListener { snapshot ->
                if (snapshot.isSuccessful){
                    for (snap in snapshot.result!!){
                        chatList.add(snap.toObject(ChatList::class.java))
                    }
                    Log.d("00000000000000000000", "$chatList")
                    chatListRV.adapter = ChatListAdapter(chatList)
                }
            }
    }
}