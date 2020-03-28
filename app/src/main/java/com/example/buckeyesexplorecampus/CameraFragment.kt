package com.example.buckeyesexplorecampus

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.io.IOException


/**
 * A [Fragment] subclass which displays the camera.
 */
class CameraFragment : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        val takePictureButton: Button? = view.findViewById(R.id.takePictureButton)
        val displayPictureButton: Button? = view.findViewById(R.id.displayPictureButton)
        val displayImageView : ImageView? = view.findViewById(R.id.displayImage)


        takePictureButton?.setOnClickListener{ _ ->

            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                activity?.packageManager?.let {
                    takePictureIntent.resolveActivity(it)?.also {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }

        displayPictureButton?.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("users").document("testUser")

            if (displayImageView != null) {
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                            val picture : String = document.get("picture") as String

                            val imageBitMap : Bitmap? = decodeFromFirebaseBase64(picture)

                            displayImageView.setImageBitmap(imageBitMap)

                        } else {
                            Log.d(ContentValues.TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "get failed with ", exception)
                    }
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras!!["data"] as Bitmap?
            // mImageLabel.setImageBitmap(imageBitmap)
            if (imageBitmap != null) {
                encodeBitmapAndSaveToFirebase(imageBitmap)
            }
        }
    }

    fun encodeBitmapAndSaveToFirebase(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val imageEncoded: String = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)

        val data = hashMapOf(
            "picture" to imageEncoded
        )
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document("testUser").set(data)
            .addOnSuccessListener { _ ->
                Log.d("Firebase Add", "Picture written with ID: Test User")
            }
            .addOnFailureListener { e ->
                Log.d("Firebase Add", "Error adding picture", e)
            }
    }

    @Throws(IOException::class)
    fun decodeFromFirebaseBase64(image: String?): Bitmap? {
        val decodedByteArray =
            Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }
}
