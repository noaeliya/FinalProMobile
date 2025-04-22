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
import com.example.finalproject.databinding.FragmentAddingPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AddingPostFragment : Fragment() {

    private lateinit var binding: FragmentAddingPostBinding
    private var imageUri: Uri? = null
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddingPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        firestore = FirebaseFirestore.getInstance()
        binding.btnSelectImage.setOnClickListener {
            openGallery()
        }

        binding.btnUpload.setOnClickListener {
            uploadPost()
        }
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
        Log.d("UploadPost", "מתחיל להעלות פוסט")

        if (description.isEmpty()) {
            context?.let {
                Toast.makeText(it, "כתבי תיאור לפוסט", Toast.LENGTH_SHORT).show()
            }
            Log.d("UploadPost", "העלאה הופסקה - אין תיאור")
            return
        }

        if (imageUri == null) {
            context?.let {
                Toast.makeText(it, "יש להוסיף תמונה לפוסט", Toast.LENGTH_SHORT).show()
            }
            Log.d("UploadPost", "העלאה הופסקה - אין תמונה")
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "עלייך להתחבר כדי לפרסם פוסט", Toast.LENGTH_SHORT).show()
            return
        }

        val post = hashMapOf(
            "description" to description,
            "timestamp" to FieldValue.serverTimestamp(),
            "userId" to userId
        )

        Log.d("UploadPost", "יש תמונה, מנסה להמיר ל-base64")
        val base64Image = encodeImageToBase64(imageUri!!)
        if (base64Image.isNotEmpty()) {
            post["imageBase64"] = base64Image

            val imageSizeInBytes = base64Image.length * 3 / 4
            val imageSizeInKB = imageSizeInBytes / 1024
            val imageSizeInMB = imageSizeInKB / 1024.0

            Log.d("UploadPost", "גודל תמונה (bytes): $imageSizeInBytes")
            Log.d("UploadPost", "גודל תמונה (KB): $imageSizeInKB")
            Log.d("UploadPost", "גודל תמונה (MB): $imageSizeInMB")

            Log.d("UploadPost", "תמונה הומרה ונוספה לפוסט")
        } else {
            Log.e("UploadPost", "המרת התמונה נכשלה")
            Toast.makeText(requireContext(), "המרת התמונה נכשלה", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("book_posts")
            .add(post)
            .addOnSuccessListener {
                Log.d("UploadPost", "הפוסט פורסם בהצלחה")
                context?.let {
                    Toast.makeText(it, "הפוסט פורסם", Toast.LENGTH_SHORT).show()
                }
                binding.etDescription.text.clear()
                binding.imagePreview.setImageDrawable(null)
                imageUri = null
            }
            .addOnFailureListener { e ->
                Log.e("UploadPost", "שגיאה בפרסום הפוסט: ${e.message}", e)
                context?.let {
                    Toast.makeText(it, "שגיאה בפרסום הפוסט", Toast.LENGTH_SHORT).show()
                }
            }
    }




    private fun encodeImageToBase64(uri: Uri): String {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            ""
        }
    }
}
