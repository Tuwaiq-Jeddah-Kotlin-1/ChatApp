package com.myprojects.chatapp.utils


import java.text.SimpleDateFormat
import kotlin.reflect.KClass

object Utils {

    const val DAY_LENGTH_MILLIS = 86400000L

    fun formatTimeDate(timeStamp: Long): String{
        val currentTime = System.currentTimeMillis()
        val compareFormatted = SimpleDateFormat("D")

        if (compareFormatted.format(currentTime).equals(compareFormatted.format(timeStamp))){
            return SimpleDateFormat("h:mm a").format(timeStamp)
        } else{
            return SimpleDateFormat("h:mm a '-' MMM d yyyy").format(timeStamp)
        }
    }

}