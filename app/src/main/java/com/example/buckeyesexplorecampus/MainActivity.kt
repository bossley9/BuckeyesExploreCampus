package com.example.buckeyesexplorecampus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth


class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO if not online

        if (isSignedIn()) {
            // create main screen fragment
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, LandmarkMenuFragment())
                .commit()
        } else {
            // sign in first
            createSignInIntent()
        }

    }

    private fun isSignedIn() : Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return user != null
    }

    private fun createSignInIntent() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode === Activity.RESULT_OK) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, "Welcome " + user!!.displayName + "!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "" + response!!.error, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                createSignInIntent()
            }
    }

    fun deleteAccount() {
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
                createSignInIntent()
            }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }

}
