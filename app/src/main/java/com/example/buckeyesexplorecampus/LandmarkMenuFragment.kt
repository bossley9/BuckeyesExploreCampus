package com.example.buckeyesexplorecampus

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buckeyesexplorecampus.dummy.DummyContent
import com.example.buckeyesexplorecampus.dummy.DummyContent.DummyItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import android.util.Log
import android.widget.Toast

/**
 * The main screen fragment.
 */
class LandmarkMenuFragment : Fragment() {
    private var columnCount = 3
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var rv: RecyclerView
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_landmark_menu, container, false)

        // recycler view

        rv = view.findViewById(R.id.recyclerView) as RecyclerView
        rv.setHasFixedSize(true)
        rv.layoutManager = GridLayoutManager(context, columnCount)
        rv.adapter = LandmarkRecyclerViewAdapter(DummyContent.ITEMS, listener)

        // crud buttons

        val createButton: Button? = view.findViewById(R.id.createButton)
        val retrieveButton: Button? = view.findViewById(R.id.retrieveButton)
        val updateButton: Button? = view.findViewById(R.id.updateButton)
        val deleteButton: Button? = view.findViewById(R.id.deleteButton)


        createButton?.setOnClickListener { _ ->
            val data = hashMapOf(
                "username" to "Test User",
                "password" to "Test Password"
            )

            db.collection("users").document("Test User").set(data)
                .addOnSuccessListener { _ ->
                    Log.d("Firebase Add", "DocumentSnapshot written with ID: Test User")
                }
                .addOnFailureListener { e ->
                    Log.d("Firebase Add", "Error adding document", e)
                }

        }


        retrieveButton?.setOnClickListener { _ ->
            val docRef = db.collection("users").document("Test User")

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        val docUsername : String = document.get("username") as String
                        Toast.makeText(activity, "username: " + docUsername, Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }

        updateButton?.setOnClickListener { _ ->
            val data = hashMapOf("username" to "Updated Test Username")

            db.collection("users").document("Test User")
                .set(data, SetOptions.merge())
        }

        deleteButton?.setOnClickListener { _ ->
            db.collection("users").document("Test User")
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        }

        // logout
        val logoutSubmit = view.findViewById(R.id.logoutSubmit) as Button
        logoutSubmit.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("are you sure you would like to logout?")
                .setPositiveButton("logout") { _, _ ->
                    val act: MainActivity? = activity as? MainActivity
                    act?.signOut()
                }
                .setNegativeButton("cancel", null)
                .show()
        }

        // delete account
        val deleteSubmit = view.findViewById(R.id.deleteSubmit) as Button
        deleteSubmit.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("are you sure you would like to delete your account? (warning: this cannot be undone!)")
                .setPositiveButton("delete my account") { _, _ ->
                    val act: MainActivity? = activity as? MainActivity
                    act?.deleteAccount()
                }
                .setNegativeButton("cancel", null)
                .show()
        }

        return view

    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: DummyItem?)
    }
}
