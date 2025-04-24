package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalproject.entities.VolumeInfo
import com.squareup.picasso.Picasso
import com.example.finalproject.R
import com.example.finalproject.databinding.FragmentBookBinding

class BookFragment : Fragment() {
    private lateinit var binding: FragmentBookBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val book: VolumeInfo = requireArguments().let {
            BookFragmentArgs.fromBundle(it).book
        }
        binding.title.text = book.title
        binding.description.text = book.description
        binding.author.text = book.authors?.joinToString(", ") ?: "מחבר לא ידוע"

        val imageUrl = book.imageLinks?.thumbnail?.replace("http://", "https://")
        Log.d("BookFragment", "Thumbnail URL: $imageUrl")

        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .fit()
                .centerCrop()
                .into(binding.image)
        } else {
            binding.image.setImageResource(R.drawable.placeholder_image)
        }
    }
}
