package com.example.buckeyesexplorecampus

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var fusedLocationClient: FusedLocationProviderClient

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location == null) {
                    Toast.makeText(applicationContext, "birthday party", Toast.LENGTH_LONG).show()
                    requestNewLocationData()
                } else {
                    Toast.makeText(applicationContext, "birthday party", Toast.LENGTH_LONG).show()
                    val arguments = Bundle()
                    arguments.putDouble("longitude", location.longitude )
                    arguments.putDouble("longitude", location.latitude)
                    arguments.putString("hello", "hello")

                    if (isSignedIn()) {
                        // connect signed in user to data in Firestore
                        findOrCreateUserObj()

                        // create main screen fragment
                        val lmFrag = LandmarkMenuFragment()
                        lmFrag.arguments = arguments
                        supportFragmentManager
                            .beginTransaction()
                            .add(R.id.fragmentContainer, lmFrag)
                            .commit()
                    } else {
                        // sign in first
                        createSignInIntent()
                    }
                }
            }

    }

    private fun isSignedIn() : Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return user != null
    }

    // TODO method unecessary?
    private fun findOrCreateUserObj() {
        val db = FirebaseFirestore.getInstance()
        val users = db.collection("users")

        val user = FirebaseAuth.getInstance().currentUser

        // create user obj if it does not exist
//        val data = hashMapOf("successfulLandmarks" to hashMapOf<String, String>())
//        users.document(user?.uid as String).set(data, SetOptions.merge())
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
                .setLogo(R.drawable.logo)
                .setTheme(R.style.AppTheme)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
//                val user = FirebaseAuth.getInstance().currentUser
//                Toast.makeText(this, "Welcome " + user!!.displayName + "!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "" + response!!.error, Toast.LENGTH_LONG).show()
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
        // delete user
        val db = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

        db.collection("users")
            .document(user?.uid as String)
            .delete()
            .addOnCompleteListener {

                // delete Firebase auth
                AuthUI.getInstance()
                    .delete(this)
                    .addOnCompleteListener {
                        createSignInIntent()
                    }
            }
    }

    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var location: Location = locationResult.lastLocation
            val arguments = Bundle()
            arguments.putDouble("longitude", location.longitude )
            arguments.putDouble("longitude", location.latitude)
            arguments.putString("hello", "hello")

            if (isSignedIn()) {
                // connect signed in user to data in Firestore
                findOrCreateUserObj()

                // create main screen fragment
                val lmFrag = LandmarkMenuFragment()
                lmFrag.arguments = arguments
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer, lmFrag)
                    .commit()
            } else {
                // sign in first
                createSignInIntent()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }

}
