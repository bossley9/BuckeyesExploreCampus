package com.example.buckeyesexplorecampus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

/**
 * A [Fragment] subclass which handles user creation.
 */
class SignupFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        val signupSubmit = view.findViewById(R.id.signupSubmit) as Button
        signupSubmit.setOnClickListener {
            Toast.makeText(activity, "submitted!", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
