package com.example.buckeyesexplorecampus

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


/**
 * A [Fragment] subclass which displays all images and instructions.
 */
class MenuFragment : Fragment() {

    val db = FirebaseFirestore.getInstance()

    val mUpdateButton: Button? = activity?.findViewById(R.id.updateButton)
    val mDeleteButton: Button? = activity?.findViewById(R.id.deleteButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_menu, container, false)

        val createButton: Button? = myView.findViewById(R.id.createButton)
        val retrieveButton: Button? = myView.findViewById(R.id.retrieveButton)
        val updateButton: Button? = myView.findViewById(R.id.updateButton)
        val deleteButton: Button? = myView.findViewById(R.id.deleteButton)


        createButton?.setOnClickListener { view ->
            val data = hashMapOf(
                "username" to "Test User",
                "password" to "Test Password"
            )

            db.collection("users").document("Test User").set(data)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firebase Add", "DocumentSnapshot written with ID: Test User")
                }
                .addOnFailureListener { e ->
                    Log.d("Firebase Add", "Error adding document", e)
                }

        }


        retrieveButton?.setOnClickListener { view ->
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

        updateButton?.setOnClickListener { view ->
            val data = hashMapOf("username" to "Updated Test Username")

            db.collection("users").document("Test User")
                .set(data, SetOptions.merge())
        }

        deleteButton?.setOnClickListener { view ->
            db.collection("users").document("Test User")
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        }

        return myView
    }

}
