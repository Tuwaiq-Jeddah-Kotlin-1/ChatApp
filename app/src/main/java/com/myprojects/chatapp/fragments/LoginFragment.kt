package com.myprojects.chatapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.MainActivity
import com.myprojects.chatapp.R


class LoginFragment : Fragment() {

    private lateinit var signupTextView: TextView
    private lateinit var signInButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        signupTextView = view.findViewById(R.id.signupTV)
        signupTextView.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment)
        }

        emailEditText = view.findViewById(R.id.emailET)
        passwordEditText = view.findViewById(R.id.passwordET)

        signInButton = view.findViewById(R.id.signInBtn)
        signInButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (!(email.isNullOrEmpty() && password.isNullOrEmpty())){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                       if (task.isSuccessful){
                           (activity as MainActivity).setCurrentUser()
                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_chatsFragment)
                       }else{
                           Toast.makeText(context, "Wrong email or password", Toast.LENGTH_SHORT).show()
                       }
                    }
            }else{
                Toast.makeText(context,"Please enter email and password",Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}