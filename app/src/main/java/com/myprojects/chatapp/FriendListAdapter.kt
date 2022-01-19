package com.myprojects.chatapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.myprojects.chatapp.models.User

class FriendListAdapter(val friendList: ArrayList<User>) : RecyclerView.Adapter<FriendListAdapter.FriendViewHolder>() {
    private val currentUid = FirebaseAuth.getInstance().uid
    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val profilePic = itemView.findViewById<ImageView>(R.id.friendProfilePicIV)
        val friendUserName = itemView.findViewById<TextView>(R.id.friendUserNameTV)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(view: View?) {
            val argument = bundleOf("fromId" to currentUid,
                "toId" to friendList[position].id)
            Navigation.findNavController(view!!).navigate(R.id.chatFragment,argument)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_list_item,parent,false)

        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.friendUserName.text = friendList[position].userName
    }

    override fun getItemCount(): Int {
        return friendList.size
    }
}