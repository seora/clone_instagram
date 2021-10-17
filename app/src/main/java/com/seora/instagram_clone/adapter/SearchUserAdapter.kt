package com.seora.instagram_clone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.seora.instagram_clone.Model.User
import com.seora.instagram_clone.R
import com.squareup.picasso.Picasso

class SearchUserAdapter(private var mContext : Context,
                        private var mUser : List<User>,
                        private var isFragment : Boolean = false) : RecyclerView.Adapter<SearchUserAdapter.ViewHolder>(){

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false)
        return SearchUserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchUserAdapter.ViewHolder, position: Int) {
        val user = mUser[position]

        holder.userNameTextView.text = user.getFullname()
        holder.userbioTextView.text = user.getBio()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(holder.userimgView)

       checkFollowingStatus(user.getUId(), holder.followButton)


        holder.followButton.setOnClickListener {
            if(holder.followButton.text.toString() == "Follow"){

                firebaseUser?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(user.getUId())
                            .setValue(true).addOnCompleteListener { task ->
                                if(task.isSuccessful){

                                    firebaseUser?.uid.let { it1 ->
                                        FirebaseDatabase.getInstance().reference
                                                .child("Follow").child(user.getUId())
                                                .child("Followers").child(it1.toString())
                                                .setValue(true).addOnCompleteListener { task ->
                                                    if(task.isSuccessful){

                                                    }
                                                }
                                    }
                                }
                            }
                }


            }else{
                firebaseUser?.uid.let { it1 ->
                    FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(user.getUId())
                            .removeValue().addOnCompleteListener { task ->
                                if(task.isSuccessful){

                                    firebaseUser?.uid.let { it1 ->
                                        FirebaseDatabase.getInstance().reference
                                                .child("Follow").child(user.getUId())
                                                .child("Followers").child(it1.toString())
                                                .removeValue().addOnCompleteListener { task ->
                                                    if(task.isSuccessful){

                                                    }
                                                }
                                    }
                                }
                            }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mUser.size
    }


    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
        var userNameTextView : TextView = itemView.findViewById(R.id.search_username)
        var userbioTextView : TextView = itemView.findViewById(R.id.search_userdesc)
        var userimgView : ImageView = itemView.findViewById(R.id.search_img_user)
        var followButton : Button = itemView.findViewById(R.id.btn_follow)
    }

    private fun checkFollowingStatus(uId: String, followButton: Button) {
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                    .child("Follow").child(it1.toString())
                    .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                if(datasnapshot.child(uId).exists()){
                    followButton.text = "Following"
                }
                else{
                    followButton.text = "Follow"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

}