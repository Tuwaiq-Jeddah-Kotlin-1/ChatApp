package com.myprojects.chatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.chatapp.models.User

class FriendListAdapter(val friendList: ArrayList<User>) : RecyclerView.Adapter<FriendListAdapter.FriendViewHolder>() {
    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePic = itemView.findViewById<ImageView>(R.id.friendProfilePicIV)
        val friendUserName = itemView.findViewById<TextView>(R.id.friendUserNameTV)
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