package com.example.finalproject.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.finalproject.R
import com.example.finalproject.ViewModel
import com.example.finalproject.databinding.FragmentProfileBinding
import android.widget.Toast
import com.squareup.picasso.Picasso
import java.io.File

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ViewModel by viewModels()

    private var selectedImageUri: Uri? = null

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            binding.imageProfile.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserProfileImage() // <-- הוספתי קריאה לטעינת התמונה

        binding.imageProfile.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.savebutton.setOnClickListener {
            val name = binding.editTextNameProfile.text.toString().trim()
            if (name.isNotEmpty()) {
                viewModel.updateProfile(name, selectedImageUri)
            } else {
                Toast.makeText(requireContext(), "נא להזין שם", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.updateSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "הפרופיל עודכן בהצלחה", Toast.LENGTH_SHORT).show()
                loadUserProfileImage() // נטען מחדש את התמונה אחרי עדכון
            } else {
                Toast.makeText(requireContext(), "שגיאה בעדכון הפרופיל", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUserProfileImage() {
        viewModel.getUserProfile { userProfile ->
            userProfile?.profileImageLocalPath?.let { localPath ->
                val file = File(localPath)
                if (file.exists()) {
                    Picasso.get()
                        .load(file)
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_image)
                        .into(binding.imageProfile)
                } else {
                    binding.imageProfile.setImageResource(R.drawable.placeholder_image) // fallback אם אין קובץ
                }
            }
        }
    }
}
