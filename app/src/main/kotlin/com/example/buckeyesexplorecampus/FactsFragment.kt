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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_facts, container, false)

        // landmark id
        val landmarkId = arguments!!.getString("landmarkId") as String

        val landmarkTitleText : TextView = view.findViewById(R.id.landmarkTitleText)
        val landmarkFactText : TextView = view.findViewById(R.id.landmarkFactText)
        val providedLandmarkImage : ImageView = view.findViewById(R.id.providedLandmarkImage)
        val userLandmarkImage : ImageView = view.findViewById(R.id.userLandmarkImage)

        val factsBackButton : Button = view.findViewById(R.id.factsBackButton)
        factsBackButton.setOnClickListener { fragmentManager?.popBackStack() }

        val store = Store.instance()

        // get landmark and set data

        val landmark: Landmark? = store.landmarks.find { it.id == landmarkId }

        providedLandmarkImage.setImageBitmap(
            Store.decodeBitmap(landmark?.imgBase64)
        )

        landmarkTitleText.text = landmark?.name
        landmarkFactText.text = landmark?.fact

        // get user and set data

        val successfulLandmarks: HashMap<String, String>? = store.user?.successfulLandmarks

        if (successfulLandmarks != null) {
            val userImageBase64 = successfulLandmarks[landmarkId]
            if (userImageBase64 != null) {
                userLandmarkImage.setImageBitmap(Store.decodeBitmap(userImageBase64))
            }
        }

        return view
    }

}
