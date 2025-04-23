package com.example.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.finalproject.R
//import com.example.booksactivity.BooksListActivity
//import com.example.booksactivity.R
//import com.example.booksactivity.databinding.FragmentMainBinding
//import com.example.booksactivity.entities.BookResponse
//import com.example.booksactivity.http.NetworkManager
import com.example.finalproject.databinding.FragmentSearchBookAPIBinding
import com.example.finalproject.ViewModel

//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response

class SearchBookAPIFragment : Fragment() {

    private var _binding: FragmentSearchBookAPIBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBookAPIBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.setOnClickListener {
            val query = binding.searchQuery.text.toString()
            if (query.isNotBlank()) {
                viewModel.search(query)
                findNavController().navigate(R.id.action_SearchBookAPIFragment_to_booksListFragment)
            }
        }
        }
    }

//    private fun searchBooks(query: String) {
//        NetworkManager.getInstance().getApi()
//            .searchBooks(query, 20, NetworkManager.API_KEY)
//            .enqueue(object : Callback<BookResponse> {
//                override fun onResponse(
//                    call: Call<BookResponse>,
//                    response: Response<BookResponse>
//                ) {
//                    binding.progressBar.visibility = View.GONE
//                    if (response.isSuccessful && response.body() != null && !response.body()!!.items.isNullOrEmpty()) {
//                        val intent = Intent(requireContext(), BooksListActivity::class.java)
//                        intent.putExtra(BooksListActivity.BOOKS_KEY, response.body())
//                        startActivity(intent)
//                    } else {
//                        if (response.body()?.items?.isEmpty() == true) {
//                            showError(getString(R.string.no_books_found))
//                        } else {
//                            showError(response.message())
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<BookResponse>, t: Throwable) {
//                    binding.progressBar.visibility = View.GONE
//                    showError(t.message ?: getString(R.string.error))
//                }
//            })
//    }
//
//    private fun showError(message: String) {
//        AlertDialog.Builder(requireContext())
//            .setTitle(R.string.error)
//            .setMessage(message)
//            .show()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

