package com.example.finalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [authFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class authFragment : Fragment() {
    private var forSignUpBtn: TextView? = null
    private var signInBtn: Button? = null
    private var _viewModel: ViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forSignUpBtn = view.findViewById(R.id.forSignUpBtn)
        signInBtn = view.findViewById(R.id.signInBtn)

        val email = view.findViewById<EditText>(R.id.signInEmail).text.trim()
        val password = view.findViewById<EditText>(R.id.signInPassword).text.trim()

        signInBtn?.setOnClickListener {
            _viewModel?.login(email,password)
            Toast.makeText(requireContext(), " התחברת !", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_authFragment_to_HomePageFragment)
        }

        forSignUpBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_signUpFragment)
        }
    }


}