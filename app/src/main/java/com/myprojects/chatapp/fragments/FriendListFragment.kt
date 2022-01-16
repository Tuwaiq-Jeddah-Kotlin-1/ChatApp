package com.myprojects.chatapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.FriendListAdapter
import com.myprojects.chatapp.R
import com.myprojects.chatapp.models.User
import com.myprojects.chatapp.utils.Utils
import kotlinx.coroutines.tasks.await
import kotlin.math.log


class FriendListFragment : Fragment() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var friendsRecyclerView: RecyclerView
    private lateinit var adapter: FriendListAdapter
    private val currentUserId = FirebaseAuth.getInstance().uid
    private val userDocRef =
        currentUserId?.let { Firebase.firestore.collection("users").document(it) }
    private val userFriendsColRef =
        currentUserId?.let {
            Firebase.firestore.collection("friends").document(it).collection("friendList")
        }

    interface Callback {
        fun onCallback(value: ArrayList<User>)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomNav = requireActivity().findViewById(R.id.mainBottomNav)
        bottomNav.visibility = View.VISIBLE


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friend_list, container, false)

        friendsRecyclerView = view.findViewById(R.id.friendListRV)

        getFriendList(object : Callback {
            override fun onCallback(value: ArrayList<User>) {
                Log.d("777777777777777", value.toString())
                friendsRecyclerView.adapter = FriendListAdapter(value)
            }

        })
        friendsRecyclerView.layoutManager = LinearLayoutManager(context)


        return view
    }

    private fun getFriendList(callback: Callback) {

        var friendList = ArrayList<User>()
        userFriendsColRef?.get()?.addOnCompleteListener { snapshot ->
            if (snapshot.isSuccessful) {

                for (snap in snapshot.result!!) {
                    val user = snap.toObject(User::class.java)
                    friendList.add(user)
                }

                callback.onCallback(friendList)
            }
        }
    }

    private fun mapToList(map: ArrayList<Map<String, Any>>): ArrayList<User> {
        val list = ArrayList<User>()
        for (i in 0 until map.size) {
            list.add(Utils.mapToObject(map[i], User::class))
        }
        return list
    }

}