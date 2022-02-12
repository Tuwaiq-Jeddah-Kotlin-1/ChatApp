package com.myprojects.chatapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.models.User
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import kotlin.math.abs
import kotlin.reflect.KClass

object Utils {

    const val DAY_LENGTH_MILLIS = 86400000L

    fun <T : Any> mapToObject(map: Map<String, Any>, clazz: KClass<T>) : T {
        //Get default constructor
        val constructor = clazz.constructors.first()

        //Map constructor parameters to map values
        val args = constructor
            .parameters
            .map { it to map.get(it.name) }
            .toMap()

        //return object from constructor call
        return constructor.callBy(args)
    }

    suspend fun getCurrentUser(): User {
        val currentUserId = FirebaseAuth.getInstance().uid
        val userDocRef = currentUserId?.let { Firebase.firestore.collection("users").document(it) }
        lateinit var result: User

        userDocRef?.get()?.addOnCompleteListener {
            if (it.isSuccessful){
                result = it.result?.toObject(User::class.java)!!
            }
        }?.await()
        return result
    }

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