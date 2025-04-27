package com.example.finalproject.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.finalproject.ViewModel
import com.example.finalproject.databinding.FragmentAddingPostBinding
import com.example.finalproject.entities.UploadState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.example.finalproject.R


class AddingPostFragment : Fragment() {

    private lateinit var binding: FragmentAddingPostBinding
    private var imageUri: Uri? = null
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddingPostBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnSelectImage.setOnClickListener {
            openGallery()
        }

        binding.btnUpload.setOnClickListener {
            uploadPost()
        }

        observeViewModel()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data
            binding.imagePreview.setImageURI(imageUri)
        }
    }

    private fun uploadPost() {
        val description = binding.etDescription.text.toString().trim()

        if (description.isEmpty()) {
            Toast.makeText(requireContext(), "כתבי תיאור לפוסט", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri == null) {
            Toast.makeText(requireContext(), "יש להוסיף תמונה לפוסט", Toast.LENGTH_SHORT).show()
            return
        }

        // כאן מעבירים ל-ViewModel
        viewModel.uploadPost(description, imageUri!!)
    }

    private fun observeViewModel() {
        viewModel.uploadState.observe(viewLifecycleOwner) { state: UploadState ->
            when (state) {
                is UploadState.Success -> {
                    Toast.makeText(requireContext(), "פוסט הועלה בהצלחה", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_AddingPostFragment_to_HomePageFragment)
                }
                is UploadState.Error -> {
                    Toast.makeText(requireContext(), "שגיאה: ${state.message}", Toast.LENGTH_SHORT).show()
                }
                is UploadState.Loading -> {
                }
                else -> {}
            }
        }
    }
}
