package com.myprojects.chatapp.models

import android.net.Uri
import java.util.*

data class User(val id: String,
                val userName: String,
                val profilePicture: Uri? = null,
                val friends: List<User>? = null,
                val memberSince: Date)
