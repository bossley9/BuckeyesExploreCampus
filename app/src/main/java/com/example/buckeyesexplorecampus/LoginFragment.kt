package com.example.buckeyesexplorecampus

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

/**
 * A [Fragment] subclass which handles user login and authentication.
 */
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val buttonSignup = view.findViewById(R.id.buttonSignup) as Button
        buttonSignup.setOnClickListener {
            val signupFragment = SignupFragment()

            AlertDialog.Builder(context)
                .setMessage("redirecting to signup...")
                .setPositiveButton("ok", null)
                .setNegativeButton("not what I intended", null)
                .show()

            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragmentContainer, signupFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        return view
    }

}
