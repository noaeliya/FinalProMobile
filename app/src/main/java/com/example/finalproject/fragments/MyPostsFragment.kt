package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.ViewModel
import com.example.finalproject.adapters.MyPostsAdapter
import com.example.finalproject.databinding.FragmentMyPostsBinding
import com.example.finalproject.entities.MyPost
import com.example.finalproject.entities.Post
import com.google.firebase.auth.FirebaseAuth

class MyPostsFragment : Fragment() {

    private val viewModel: ViewModel by viewModels()
    private lateinit var binding: FragmentMyPostsBinding
    private val posts = mutableListOf<MyPost>()
    private lateinit var postsAdapter: MyPostsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MyPostsFragment", "onCreateView called")

        // אינתחול ה-binding
        binding = FragmentMyPostsBinding.inflate(inflater, container, false)

        binding.recyclerMyPosts.layoutManager = LinearLayoutManager(context)

        // הגדרת האדאפטר
        postsAdapter = MyPostsAdapter(posts) { post ->
            Log.d("MyPostsFragment", "Edit button clicked for post: ${post.id}")
            navigateToEditPost(post)
        }
        binding.recyclerMyPosts.adapter = postsAdapter

        // קבלת ה-UserId של המשתמש הנוכחי
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            Log.d("MyPostsFragment", "User ID: $userId")
        } else {
            Log.e("MyPostsFragment", "User not logged in")
        }

        // קריאה ל-ViewModel להוריד את הפוסטים של המשתמש הנוכחי
        viewModel.fetchUserPosts(userId ?: return binding.root)

        // עדכון הפוסטים של המשתמש הנוכחי
        viewModel.postsLiveData.observe(viewLifecycleOwner, Observer { posts ->
            Log.d("MyPostsFragment", "Received ${posts.size} posts from ViewModel")
            this.posts.clear()  // מנקה את הרשימה הישנה
            this.posts.addAll(posts)  // מוסיף את הפוסטים החדשים
            postsAdapter.notifyDataSetChanged()  // עדכון ה-Adapter
            Log.d("MyPostsFragment", "RecyclerView updated with ${posts.size} posts")
        })

        // טיפול בשגיאות אם ישנן
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            Log.e("MyPostsFragment", "Error occurred: $error")
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }


    private fun navigateToEditPost(post: MyPost) {
        // ניווט לעורך פוסט
        Log.d("MyPostsFragment", "Navigating to edit post: ${post.id}")
    }
}
