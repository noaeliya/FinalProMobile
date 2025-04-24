package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.R
import com.example.finalproject.ViewModel
import com.example.finalproject.databinding.FragmentHomePageBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.finalproject.adapters.PostAdapter
import com.example.finalproject.entities.Post


/**

 */
class HomePageFragment : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var firestore: FirebaseFirestore
    private val posts = mutableListOf<Post>()
    private lateinit var adapter: PostAdapter
    private val viewModel: ViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firestore = FirebaseFirestore.getInstance()
        adapter = PostAdapter(posts)

        binding.recyclerPosts.adapter = adapter

        binding.signOutBtn.setOnClickListener {
            viewModel.signOut()
            findNavController().navigate(
                R.id.authFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(findNavController().graph.startDestinationId, true)
                    .build()
            )
        }

        loadPosts()
    }

    private fun loadPosts() {
        firestore.collection("book_posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                posts.clear()
                for (doc in result) {
                    val post = doc.toObject(Post::class.java)
                    posts.add(post)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("HomeFragment", "שגיאה בטעינת הפוסטים: ${e.message}", e)
                Toast.makeText(requireContext(), "שגיאה בטעינה", Toast.LENGTH_SHORT).show()
            }
    }
}