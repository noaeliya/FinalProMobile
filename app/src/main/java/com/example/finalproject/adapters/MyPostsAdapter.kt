package com.example.finalproject.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.R
import com.example.finalproject.entities.MyPost

class MyPostsAdapter(
    private var posts: MutableList<MyPost>,
    private val onEditClick: (MyPost) -> Unit
) : RecyclerView.Adapter<MyPostsAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentTextView: TextView = itemView.findViewById(R.id.textDescriptionMyPosts)
        val postImageView: ImageView = itemView.findViewById(R.id.imageMyPost)
        val editButton: Button = itemView.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        Log.d("MyPostsAdapter", "onCreateViewHolder called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.contentTextView.text = post.description

        Glide.with(holder.itemView.context)
            .load(post.imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.postImageView)
        Log.d("MyPostsAdapter", "Post image URL: ${post.imageUrl}")


        holder.editButton.setOnClickListener {
            onEditClick(post)
        }
    }

    override fun getItemCount(): Int = posts.size

}
