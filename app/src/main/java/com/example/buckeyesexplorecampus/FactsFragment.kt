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
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException

/**
 * A [Fragment] subclass which displays information for a given item.
 */
class FactsFragment : Fragment() {
    private lateinit var landmarkId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_facts, container, false)

        // landmark id
        landmarkId = arguments!!.getString("landmarkId") as String

        val landmarkTitleText : TextView = view.findViewById(R.id.landmarkTitleText)
        val landmarkFactText : TextView = view.findViewById(R.id.landmarkFactText)
        val providedLandmarkImage : ImageView = view.findViewById(R.id.providedLandmarkImage)
        val userLandmarkImage : ImageView = view.findViewById(R.id.userLandmarkImage)

        val factsBackButton : Button = view.findViewById(R.id.factsBackButton)
        factsBackButton.setOnClickListener { fragmentManager?.popBackStack() }

        val db = FirebaseFirestore.getInstance()
        val landmarkDocRef = db.collection("landmarks").document(landmarkId)

        landmarkDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val picture = document.get("imgBase64") as String?
                    val name = document.get("name") as String?
                    val fact = document.get("fact") as String?

                    if (picture != null &&
                        name != null &&
                        fact != null) {

                        val imageBitMap : Bitmap? = decodeFromFirebaseBase64(picture)
                        providedLandmarkImage.setImageBitmap(imageBitMap)

                        landmarkTitleText.text = name
                        landmarkFactText.text = fact
                     }

                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if(userId != null) {
            val userDocRef = db.collection("users").document(userId)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val data = document.get("successfulLandmarks") as HashMap<*, *>?;
                        val successfulLandmarks = HashMap<String, String>();

                        if (data != null) {
                            for ((k, v) in data) {
                                successfulLandmarks[k as String] = v as String
                            }
                        }

                        val userImageBase64 = successfulLandmarks[landmarkId]
                        if (userImageBase64 != null) {
                            Toast.makeText(activity, "opening facts for landmark " + landmarkId, Toast.LENGTH_LONG).show()
                            val imageBitMap: Bitmap? = decodeFromFirebaseBase64(userImageBase64)
                            userLandmarkImage.setImageBitmap(imageBitMap)
                        }

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
