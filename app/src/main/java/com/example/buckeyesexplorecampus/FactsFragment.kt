package com.example.buckeyesexplorecampus

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException

/**
 * A [Fragment] subclass which displays information for a given item.
 */
class FactsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_facts, container, false)

        val providedLandmarkImage : ImageView = view.findViewById(R.id.providedLandmarkImage)
        val userLandmarkImage : ImageView = view.findViewById(R.id.userLandmarkImage)
        val landmarkFactText : TextView = view.findViewById(R.id.landmarkFactText)
        val landmarkTitleText : TextView = view.findViewById(R.id.landmarkTitleText)
        val successMessageText : TextView = view.findViewById(R.id.successMessageText)
        //TODO: Set up back button capability
        val factsBackButton : Button = view.findViewById(R.id.factsBackButton)


        val db = FirebaseFirestore.getInstance()
        //TODO: get landmark name without hardcoding
        val landmarkDocRef = db.collection("landmarks").document("CFmjmO6nf36JL5zVTpOZ")

        landmarkDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                    val picture : String = document.get("imgBase64") as String
                    val fact : String = document.get("fact") as String
                    val name : String = document.get("name") as String

                    val imageBitMap : Bitmap? = decodeFromFirebaseBase64(picture)

                    providedLandmarkImage.setImageBitmap(imageBitMap)
                    landmarkFactText.text = fact
                    landmarkTitleText.text = name
                    successMessageText.text = "Congratulations! You've successfully taken a photo of " + name + "! Here's a fun fact about this landmark:"

                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if(currentUserUid != null) {
            val userDocRef = db.collection("users").document(currentUserUid)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                        val successfulLandmarkArray: Map<String, String> =
                            document.get("successfulLandmarks") as Map<String, String>
                        val picture = successfulLandmarkArray["CFmjmO6nf36JL5zVTpOZ"]

                        val imageBitMap: Bitmap? = decodeFromFirebaseBase64(picture)

                        userLandmarkImage.setImageBitmap(imageBitMap)

                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }

        }
        return view
    }

    @Throws(IOException::class)
    fun decodeFromFirebaseBase64(image: String?): Bitmap? {
        val decodedByteArray =
            Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }
}
