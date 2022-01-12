package com.myprojects.chatapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class User(val id: String = "",
                val userName: String = "",
                val userEmail: String = "",
                val profilePicture: String? = "",
                val memberSince: String = "") : Parcelable{
                    //constructor(): this("","","","",null,"")
                }
