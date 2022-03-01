package com.myprojects.chatapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.myprojects.chatapp.adapters.ChatListAdapter
import com.myprojects.chatapp.R
import com.myprojects.chatapp.viewmodels.ChatListViewModel



class ChatListFragment : Fragment() {


    private lateinit var bottomNav: BottomNavigationView
    private lateinit var chatListRV: RecyclerView
    private lateinit var viewModel: ChatListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.chats_fragment)

        viewModel = ViewModelProvider(this).get(ChatListViewModel::class.java)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_chat, container, false)
        bottomNav = requireActivity().findViewById(R.id.mainBottomNav)
        bottomNav.visibility = View.VISIBLE

        chatListRV = view.findViewById(R.id.chatListRV)
        chatListRV.layoutManager = LinearLayoutManager(context)

        loadChatList()

        return view
    }

    private fun loadChatList() {
        viewModel.getChatList().observe(viewLifecycleOwner, { list ->
            chatListRV.adapter = ChatListAdapter(list)
        })
    }
}