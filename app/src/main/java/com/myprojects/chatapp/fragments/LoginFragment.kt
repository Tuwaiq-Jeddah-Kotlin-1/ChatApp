package com.myprojects.chatapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import com.myprojects.chatapp.R


class LoginFragment : Fragment() {

    private lateinit var signupTextView: TextView
    private lateinit var signInButton: Button

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

        signInButton = view.findViewById(R.id.signInBtn)
        signInButton.setOnClickListener {  }

        return view
    }

}