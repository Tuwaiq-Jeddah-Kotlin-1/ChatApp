package com.myprojects.chatapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.myprojects.chatapp.adapters.FriendListAdapter
import com.myprojects.chatapp.R
import com.myprojects.chatapp.models.User
import com.myprojects.chatapp.utils.Utils
import com.myprojects.chatapp.viewmodels.FriendListViewModel


class FriendListFragment : Fragment() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var friendsRecyclerView: RecyclerView
    private lateinit var friendListVM: FriendListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.friends_fragment)

        bottomNav = requireActivity().findViewById(R.id.mainBottomNav)
        bottomNav.visibility = View.VISIBLE

        friendListVM = ViewModelProvider(this).get(FriendListViewModel::class.java)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friend_list, container, false)

        friendsRecyclerView = view.findViewById(R.id.friendListRV)

        getFriendList()
        friendsRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    fun getFriendList(){
        friendListVM.getFriendList().observe(viewLifecycleOwner, { list ->
            friendsRecyclerView.adapter = FriendListAdapter(list)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.friend_list_menu,menu)
        val TAG = "searchView"
        val searchIcon: MenuItem = menu!!.findItem(R.id.app_bar_search)
        val searchView = searchIcon.actionView as SearchView
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d(TAG,"QueryTextSubmit: $query")
                    showSearchResult(query)
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    return false
                }
            })
        }.setOnCloseListener {
            getFriendList()
            true
        }
        super.onCreateOptionsMenu(menu,inflater)
    }

    private fun showSearchResult(query: String?) {
        val searchResult = ArrayList<User>()
        Firebase.firestore.collection("users")
            .whereGreaterThanOrEqualTo("userEmail", query.toString())
            .whereLessThan("userEmail", query.toString() + "z")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    searchResult.add(document.toObject(User::class.java))
                }
                friendsRecyclerView.adapter = FriendListAdapter(searchResult)
            }
    }
}