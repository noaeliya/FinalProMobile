package com.example.finalproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.finalproject.ViewModel
import com.example.finalproject.adapters.MyPostsAdapter
import com.example.finalproject.databinding.FragmentMyPostsBinding
import com.example.finalproject.entities.MyPost
import com.google.firebase.auth.FirebaseAuth

/**

 */
class MyPostsFragment : Fragment() {
    private val viewModel: ViewModel by viewModels()
    private lateinit var binding: FragmentMyPostsBinding
    private val Myposts = mutableListOf<MyPost>()
    private lateinit var adapter: MyPostsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel = ViewModelProvider(this, ViewModel(repository)).get(ViewModel::class.java)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

//        viewModel.loadUserPosts(userId)
//
//        viewModel.userPosts.observe(viewLifecycleOwner) { posts ->
//            adapter = MyPostsAdapter(posts) { postToEdit ->
//                showEditDialog(postToEdit)
//            }
//            binding.recyclerMyPosts.adapter = adapter
//        }


        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            // פה תציגי טוסט או דיאלוג במקרה של שגיאה
        }
    }

    fun showEditDialog(postToEdit: MyPost) {}


}