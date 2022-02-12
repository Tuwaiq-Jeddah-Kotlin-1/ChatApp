package com.myprojects.chatapp.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(val message: String = "",
                   val senderId: String = "",
                   val sentAt: Long = 0)
