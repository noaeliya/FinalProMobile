package com.example.finalproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.R
import com.example.finalproject.viewModel

class SignUpFragment : Fragment() {

    private lateinit var _viewModel: viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // התחברות ל־ViewModel
        _viewModel = ViewModelProvider(this).get(viewModel::class.java)

        // כאן דוגמה איך תוכלי להאזין לסטטוס ההרשמה
        _viewModel.authResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                // נניח שתרצי לעבור למסך אחר או להראות טוסט
                Toast.makeText(requireContext(), "נרשמת בהצלחה!", Toast.LENGTH_SHORT).show()
            }
        }

        // כאן דוגמה ללחיצה על כפתור – תעדכני לפי ה־binding שלך
        // נניח שיש לך כפתור עם ID בשם btnSignUp
        view.findViewById<Button>(R.id.signUpBtn).setOnClickListener {
            val email = view.findViewById<EditText>(R.id.emailSignUp).text.toString()
            val password = view.findViewById<EditText>(R.id.passwordSignUp).text.toString()
            _viewModel.register(email, password)
        }
    }
}
