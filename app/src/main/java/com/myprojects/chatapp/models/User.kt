package com.myprojects.chatapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable


data class User(val id: String = "",
                val userName: String = "",
                val userEmail: String = "",
                val profilePicture: String? = "",
                var chatRooms: MutableMap<String, Any>? = null,
                val memberSince: String = ""): Serializable
