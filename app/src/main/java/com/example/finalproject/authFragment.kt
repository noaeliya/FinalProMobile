package com.example.finalproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
    private var registerBtn: TextView? = null
    private val viewModel: ViewModel by viewModels()


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
        val emailField = view.findViewById<EditText>(R.id.signInEmail)
        val passwordField = view.findViewById<EditText>(R.id.signInPassword)

        signInBtn?.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            viewModel.login(email, password)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collectLatest { state ->
                when (state) {
                    is LoginState.Loading -> {
//                        progress.visibility = View.VISIBLE
                    }
                    is LoginState.Success -> {
//                        progress.visibility = View.GONE
                        Toast.makeText(requireContext(), "התחברות הצליחה", Toast.LENGTH_SHORT).show()

                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        if (uid != null) {
                            FirebaseFirestore.getInstance().collection("users").document(uid)
                                .get()
                                .addOnSuccessListener { document ->

                                    findNavController().navigate(R.id.HomePageFragment)
//                                    (activity as? MainActivity)?.let { mainActivity ->
////                                        mainActivity.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.selectedItemId = R.id.homeFragment
//                                    }

                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(requireContext(), "שגיאה בשליפת נתוני המשתמש", Toast.LENGTH_SHORT).show()
                                    Log.e("Login", "Error fetching user data", exception)
                                }
                        }
                    }
                    is LoginState.Error -> {
//                        progress.visibility = View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }
                    else -> {
//                        progress.visibility = View.GONE
                    }
                }
            }
        }

        forSignUpBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_signUpFragment)
        }
    }


}