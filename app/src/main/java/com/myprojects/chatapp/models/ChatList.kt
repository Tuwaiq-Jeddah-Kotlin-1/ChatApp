package com.myprojects.chatapp.models

data class ChatList(val roomId: String = "",
                    val receiverId: String = "",
                    val receiverUserName: String = "",
                    val lastMessage: String = "",
                    val lastActivityTime: String = "")
