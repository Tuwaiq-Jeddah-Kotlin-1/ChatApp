package com.myprojects.chatapp.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.R
import com.myprojects.chatapp.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class SignupFragment : Fragment() {

    private lateinit var signinTextView: TextView
    private lateinit var signUpButton: Button
    private lateinit var usernameET: EditText
    private lateinit var emailET: TextInputEditText
    private lateinit var passwordET: TextInputEditText
    private val userCollectionRef = Firebase.firestore.collection("users")
    private lateinit var bottomNav: BottomNavigationView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        bottomNav = requireActivity().findViewById(R.id.mainBottomNav)
        bottomNav.visibility = View.GONE

        signinTextView = view.findViewById(R.id.signInTV)
        signinTextView.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_loginFragment)
        }

        signUpButton = view.findViewById(R.id.signUpBtn)
        usernameET = view.findViewById(R.id.usernameET)
        emailET = view.findViewById(R.id.emailET)
        passwordET = view.findViewById(R.id.passwordET)

        signUpButton.setOnClickListener {
            when {
                TextUtils.isEmpty(emailET.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        context,
                        "Please Enter Email",
                        Toast.LENGTH_LONG
                    ).show()
                }

                TextUtils.isEmpty(passwordET.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        context,
                        "Please Enter Password",
                        Toast.LENGTH_LONG
                    ).show()

                }
                else -> {
                    //val userID: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    val userName: String = usernameET.text.toString().trim { it <= ' ' }
                    val email: String = emailET.text.toString().trim { it <= ' ' }
                    val password: String = passwordET.text.toString().trim { it <= ' ' }
                    //val birthday: String = birthdayET.text.toString().trim { it <= ' ' }
                    val memberSince = Date(2021,5,2).toString()


                    // create an instance and create a register with email and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            // if the registration is successfully done
                            if (task.isSuccessful) {
                                //firebase register user
                                val userID: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
                                val user = User(userID, userName, email, null, null,memberSince)
                                addUser(user)

                                Toast.makeText(
                                    context,
                                    "You were registered successfully",
                                    Toast.LENGTH_LONG
                                ).show()

                                findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                            } else {
                                // if the registration is not successful then show error massage
                                Toast.makeText(
                                    context,
                                    task.exception?.localizedMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }
        }
        return view
    }

    fun addUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
        try {
            userCollectionRef.document(user.id).set(user).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Successfully saved data", Toast.LENGTH_LONG)
                    .show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

}