package com.myprojects.chatapp.models

import android.net.Uri
import java.util.*

data class User(val id: Int,
                val userName: String,
                val profilePicture: Uri,
                val friends: List<User>? = null,
                val memberSince: Date)
