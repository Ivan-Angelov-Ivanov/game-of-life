package com.example.gameoflife

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameoflife.roomdb.User

class UserAdapter( val callback: (User) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var users:List<User> = listOf()
    private var selectedPosition = -1

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.text_item)!!
        fun bind(userViewModel: User) {
            name.text = userViewModel.email
        }
    }

    fun setUsers(users: List<User> ) {
        this.users = users
        notifyDataSetChanged()
    }

    /**
     * Creates new views with R.layout.item_view as its template
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)

        return UserViewHolder(layout)
    }

    /**
     * Replaces the content of an existing view with new data
     */

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = users[position]
        holder.bind(item)
        holder.name.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        }

        if(selectedPosition == position) {
            holder.name.setBackgroundResource(R.color.blue_500)
            callback(item)
        } else {
            holder.name.setBackgroundResource(R.color.blue_700)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}