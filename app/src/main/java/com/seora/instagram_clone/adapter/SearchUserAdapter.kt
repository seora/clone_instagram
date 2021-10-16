package com.seora.instagram_clone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.seora.instagram_clone.Model.User
import com.seora.instagram_clone.R
import com.squareup.picasso.Picasso

class SearchUserAdapter(private var mContext : Context,
                        private var mUser : List<User>,
                        private var isFragment : Boolean = false) : RecyclerView.Adapter<SearchUserAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false)
        return SearchUserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchUserAdapter.ViewHolder, position: Int) {
        val user = mUser[position]

        holder.userNameTextView.text = user.getUsername()
        holder.userbioTextView.text = user.getBio()
        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(holder.userimgView)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }


    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
        var userNameTextView : TextView = itemView.findViewById(R.id.search_userID)
        var userbioTextView : TextView = itemView.findViewById(R.id.search_userdesc)
        var userimgView : ImageView = itemView.findViewById(R.id.search_img_user)
    }

}