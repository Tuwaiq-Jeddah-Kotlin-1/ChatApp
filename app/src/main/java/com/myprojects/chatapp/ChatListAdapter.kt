package com.myprojects.chatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.models.ChatList
import com.myprojects.chatapp.models.User
import com.myprojects.chatapp.utils.Utils

class ChatListAdapter(val chatList: ArrayList<ChatList>) :
    RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val currentUid = FirebaseAuth.getInstance().uid

        lateinit var toUser: User

        init {
            itemView.setOnClickListener(this)
        }

        val chatPicture = itemView.findViewById<ImageView>(R.id.chatPictureIV)
        val chatReceiver = itemView.findViewById<TextView>(R.id.chatReceiverTV)
        val chatLastMessage = itemView.findViewById<TextView>(R.id.chatLastMessageTV)
        val chatLastActivityTime = itemView.findViewById<TextView>(R.id.chatLastActivityTime)

        override fun onClick(view: View?) {
            val toUserId = chatList[position].receiverId
            Firebase.firestore.collection("users")
                .document(toUserId)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        toUser = it.result?.toObject(User::class.java)!!
                    }
                    val arguments = bundleOf(
                        "fromUser" to currentUid,
                        "toUser" to toUser,
                        "roomId" to "noRoomId"
                    )
                    Navigation.findNavController(view!!).navigate(R.id.chatFragment, arguments)
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_list_item, parent, false)

        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.chatReceiver.text = chatList[position].receiverUserName
        holder.chatLastMessage.text = chatList[position].lastMessage
        holder.chatLastActivityTime.text =
            Utils.formatTimeDate(chatList[position].lastActivityTime.toLong())
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}