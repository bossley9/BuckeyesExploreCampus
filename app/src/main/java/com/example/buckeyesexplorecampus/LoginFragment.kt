package com.example.buckeyesexplorecampus

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
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

            AlertDialog.Builder(context)
                .setMessage("redirecting to signup...")
                .setPositiveButton("ok") { _, _ ->
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.fragmentContainer, signupFragment)
                        ?.addToBackStack(null)
                        ?.commit()
                }
                .setNegativeButton("not what I intended") { _, _ ->
                    Toast.makeText(activity, "Sorry. We'll make sure it doesn't happen again.", Toast.LENGTH_SHORT).show()
                }
                .show()

        }

        return view
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(activity, "login fragment has been paused.", Toast.LENGTH_SHORT).show()
    }

}
