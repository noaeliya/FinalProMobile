package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.finalproject.R
import com.example.finalproject.ViewModel
import com.example.finalproject.adapters.BooksAdapter
import com.example.finalproject.databinding.FragmentBooksListBinding
import com.example.finalproject.fragments.BooksListFragmentDirections


class BooksListFragment : Fragment() {

    private lateinit var viewModel: ViewModel
    private lateinit var adapter: BooksAdapter
    private lateinit var binding: FragmentBooksListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBooksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        adapter = BooksAdapter(emptyList()) { bookItem ->
            val action = BooksListFragmentDirections.actionBooksListFragmentToBookFragment(bookItem.volumeInfo)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter

        viewModel.books.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

    }
}
