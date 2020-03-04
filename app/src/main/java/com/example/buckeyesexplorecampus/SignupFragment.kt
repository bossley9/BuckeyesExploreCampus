package com.example.buckeyesexplorecampus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

/**
 * A [Fragment] subclass which handles user creation.
 */
class SignupFragment : Fragment() {

    var error = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        val signupSubmit = view.findViewById(R.id.signupSubmit) as Button
        signupSubmit.setOnClickListener {

            if (areFieldsValid()) {
                val menuFragment = MenuFragment()

                fragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragmentContainer, menuFragment)
                    ?.commit()

            } else {
                Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
            }

        }

        val loginRedirect = view.findViewById(R.id.loginRedirect) as Button
        loginRedirect.setOnClickListener {
            val loginFragment = LoginFragment()

            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragmentContainer, loginFragment)
                ?.commit()
        }

        return view
    }

    /**
     * validate all signup fields with basic checks for creating a new user
     */
    private fun areFieldsValid(): Boolean {
        var isValid = false

        val usernameField = view?.findViewById(R.id.username) as EditText
        val username = usernameField.text.toString()

        val emailField = view?.findViewById(R.id.email) as EditText
        val email = emailField.text.toString()

        val emailField2 = view?.findViewById(R.id.email2) as EditText
        val email2 = emailField2.text.toString()

        val passwordField = view?.findViewById(R.id.password) as EditText
        val password = passwordField.text.toString()

        val passwordField2 = view?.findViewById(R.id.password) as EditText
        val password2 = passwordField2.text.toString()

        // all fields non-null

        if (username.isNullOrEmpty() ||
            email.isNullOrEmpty() ||
            email2.isNullOrEmpty() ||
            password.isNullOrEmpty() ||
            password2.isNullOrEmpty()) {
            error = "at least one field is empty"
            return isValid
        }

        // email is valid

        if (!email.contains("@")) {
            error = "email is invalid"
            return isValid
        }

        // emails match

        if (email != email2) {
            error = "emails do not match"
            return isValid
        }

        // passwords match

        if (password != password2) {
            error = "passwords do not match"
            return isValid
        }

        // TODO username is not taken

        isValid = true
        return isValid
    }
}
