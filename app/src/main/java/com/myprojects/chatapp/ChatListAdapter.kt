package com.myprojects.chatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.chatapp.models.ChatList

class ChatListAdapter(val chatList: ArrayList<ChatList>) : RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {
    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chatPicture = itemView.findViewById<ImageView>(R.id.chatPictureIV)
        val chatTitle = itemView.findViewById<TextView>(R.id.chatTitleTV)
        val chatLastMessage = itemView.findViewById<TextView>(R.id.chatLastMessageTV)
        val chatLastActivityTime = itemView.findViewById<TextView>(R.id.chatLastActivityTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_list_item,parent,false)

        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.chatTitle.text = chatList[position].title
        holder.chatLastMessage.text = chatList[position].lastMessage
        holder.chatLastActivityTime.text = chatList[position].lastActivityTime
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}