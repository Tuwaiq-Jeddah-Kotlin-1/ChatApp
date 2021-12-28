package com.myprojects.chatapp

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.myprojects.chatapp.models.Message
import org.w3c.dom.Text

class MessageAdapter(val mList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_SENT = 1
    private val ITEM_RECEIVED = 2

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.sentTV)

    }

    class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivedMessage = itemView.findViewById<TextView>(R.id.receivedTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_SENT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.sent_message, parent, false)
            return SentViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.received_message, parent, false)
            return ReceivedViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = mList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessage.text = currentMessage.message
        }
        if (holder.javaClass == ReceivedViewHolder::class.java) {
            val viewHolder = holder as ReceivedViewHolder
            viewHolder.receivedMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (mList[position].senderId.equals(FirebaseAuth.getInstance().currentUser?.uid)) {
            return ITEM_SENT
        } else {
            return ITEM_RECEIVED
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}