package com.example.buckeyesexplorecampus

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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

        val signupRedirect = view.findViewById(R.id.signupRedirect) as Button
        signupRedirect.setOnClickListener {
            val signupFragment = SignupFragment()
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragmentContainer, signupFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        val loginSubmit = view.findViewById(R.id.loginSubmit) as Button
        loginSubmit.setOnClickListener {
            // TODO add login validation
            val menuFragment = MenuFragment()
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragmentContainer, menuFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        return view
    }

//    override fun onPause() {
//        super.onPause()
//        Toast.makeText(activity, "login fragment has been paused.", Toast.LENGTH_SHORT).show()
//    }

}
