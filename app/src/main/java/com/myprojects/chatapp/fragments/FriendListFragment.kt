package com.myprojects.chatapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.myprojects.chatapp.R


class FriendListFragment : Fragment() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomNav = requireActivity().findViewById(R.id.mainBottomNav)
        bottomNav.visibility = View.VISIBLE
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_friend_list, container, false)
        return view
    }

}