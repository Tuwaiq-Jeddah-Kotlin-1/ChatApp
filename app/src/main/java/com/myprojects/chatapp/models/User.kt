package com.myprojects.chatapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class User(val id: String = "",
                val userName: String = "",
                val userEmail: String = "",
                val profilePicture: String? = "",
                val chatRooms: MutableMap<String, Any>? = null,
                val memberSince: String = "")
