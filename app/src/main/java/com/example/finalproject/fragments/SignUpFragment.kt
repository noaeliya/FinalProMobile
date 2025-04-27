package com.example.finalproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.finalproject.R
import com.example.finalproject.ViewModel

class SignUpFragment : Fragment() {

    private lateinit var _viewModel: ViewModel
    private lateinit var registerBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        registerBtn = view.findViewById(R.id.signUpBtn)

        _viewModel.authResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "נרשמת בהצלחה!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signUpFragment_to_HomePageFragment)
            }
        }


        registerBtn.setOnClickListener {
            Log.d("SignUp", "Button clicked")
            val email = view.findViewById<EditText>(R.id.emailSignUp).text.toString()
            val password = view.findViewById<EditText>(R.id.passwordSignUp).text.toString()
            _viewModel.register(email, password)
        }
    }
}
