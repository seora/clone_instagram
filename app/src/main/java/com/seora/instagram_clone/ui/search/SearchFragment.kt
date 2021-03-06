package com.seora.instagram_clone.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.seora.instagram_clone.Model.User
import com.seora.instagram_clone.R
import com.seora.instagram_clone.adapter.SearchUserAdapter
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var searchUserAdapter : SearchUserAdapter? = null
    private var mUser : MutableList<User>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_search)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)

        mUser = ArrayList()
        searchUserAdapter = context?.let{ SearchUserAdapter(it, mUser as ArrayList<User>, true) }
        recyclerView?.adapter = searchUserAdapter

        view.search_edit_txt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (view.search_edit_txt.text.toString() == ""){
                }
                else{
                    recyclerView?.visibility = View.VISIBLE
                    retrieveUsers()
                    searchUser(s.toString().toLowerCase())
                }
            }
        })

        return view
    }

    private fun searchUser(input: String) {
        val query = FirebaseDatabase.getInstance().getReference()
            .child("Users")
            .orderByChild("fullname")
            .startAt(input).endAt(input + "\uf8ff")

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                mUser?.clear()

                for (snapshot in datasnapshot.children){
                    val user = snapshot.getValue(User::class.java)
                    if (user != null){
                        mUser?.add(user)
                    }
                }
                searchUserAdapter?.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    private fun retrieveUsers(){
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users")
        usersRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                if (view?.search_edit_txt?.text.toString() == ""){
                    mUser?.clear()

                    for (snapshot in datasnapshot.children){
                        val user = snapshot.getValue(User::class.java)
                        if (user != null){
                            mUser?.add(user)
                        }
                    }
                    searchUserAdapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}