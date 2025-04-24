package com.example.finalproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.databinding.ItemBookBinding
import com.example.finalproject.entities.BookItem
import com.squareup.picasso.Picasso

class BooksAdapter(
    private var bookList: List<BookItem>,
    private val onItemClick: (BookItem) -> Unit
) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    inner class BookViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BookItem) {
            binding.bookTitle.text = book.volumeInfo?.title ?: "ללא כותרת"
            binding.bookDescription.text = book.volumeInfo?.description ?: "אין תיאור זמין"

            val thumbnailUrl = book.volumeInfo?.imageLinks?.thumbnail?.replace("http://", "https://")
            if (!thumbnailUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .fit()
                    .centerCrop()
                    .into(binding.bookImage)
            }
            else {
                binding.bookImage.setImageResource(R.drawable.placeholder_image) // תמונת ברירת מחדל אם אין תמונה
            }

            binding.root.setOnClickListener {
                onItemClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun getItemCount(): Int = bookList.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    fun updateList(newList: List<BookItem>) {
        bookList = newList
        notifyDataSetChanged()
    }
}
