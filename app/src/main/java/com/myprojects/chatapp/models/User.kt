package com.myprojects.chatapp.models

import android.net.Uri
import java.util.*

data class User(val id: String,
                val userName: String,
                val userEmail: String,
                val profilePicture: String? = null,
                val friends: List<User>? = null,
                val memberSince: Date)
