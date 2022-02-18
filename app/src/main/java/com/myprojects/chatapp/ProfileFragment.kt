package com.myprojects.chatapp

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.models.User


class ProfileFragment : Fragment() {

    private lateinit var profilePicture: ImageView
    private lateinit var userEmail: TextView
    private lateinit var username: EditText
    private lateinit var saveButton: Button
    private lateinit var logout: TextView
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)

        currentUser = (requireActivity() as MainActivity).getCurrentUser()

        profilePicture = view.findViewById(R.id.profilePictureIV)
        userEmail = view.findViewById(R.id.emailTV)
        username = view.findViewById(R.id.usernameET)
        saveButton = view.findViewById(R.id.profileSaveBtn)
        logout = view.findViewById(R.id.logoutTV)

        populateViews()

        saveButton.setOnClickListener {
            Firebase.firestore.collection("users").document(currentUser.id).update("userName", username.text.toString())
            (requireActivity() as MainActivity).setCurrentUser()
        }

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Navigation.findNavController(view).navigate(R.id.loginFragment)
            val sharedPref = requireContext().getSharedPreferences("stayLoggedIn", Context.MODE_PRIVATE)
            sharedPref.edit().putBoolean("loggedIn", false).apply()
        }

        return view
    }

    private fun populateViews() {
        userEmail.text = currentUser.userEmail
        username.text = Editable.Factory.getInstance().newEditable(currentUser.userName)
    }

}