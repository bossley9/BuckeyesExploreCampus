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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.io.ByteArrayOutputStream
import java.io.IOException


/**
 * A [Fragment] subclass which displays the camera.
 */
class CameraFragment : Fragment() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    private lateinit var landmarkId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        // get landmark id
        landmarkId = arguments!!.getString("landmarkId") as String

        // open camera
        startCameraIntent()

        return view
    }

    private fun startCameraIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            activity?.packageManager?.let {
                takePictureIntent.resolveActivity(it)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun isValidImage(img: Bitmap): Boolean {
        // TODO implement image comparison
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras!!["data"] as Bitmap?
            if (imageBitmap != null) {

                if (isValidImage(imageBitmap)) {
                    addSuccessfulLandmark(imageBitmap)
                } else {
                    Toast.makeText(activity, "Images do not match. Try again!", Toast.LENGTH_LONG).show()
                    // close camera fragment
                    fragmentManager?.popBackStack()
                }

            }
        }
    }

    private fun addSuccessfulLandmark(bitmap: Bitmap) {
        // encode image
        val imgEncoded: String = encodeBitmap(bitmap)

        // get user
        val user = FirebaseAuth.getInstance().currentUser

        // create map pair from landmark id to encoded image
        val data = hashMapOf(
            "successfulLandmarks" to hashMapOf(
                landmarkId to imgEncoded
            )
        )

        // push to Firebase
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(user?.uid as String)
            .set(data, SetOptions.merge())
            .addOnSuccessListener { _ ->
                val factsFragment = FactsFragment()

                fragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragmentContainer, factsFragment)
                    ?.commit()
            }
            .addOnFailureListener { e ->
                Log.d("Firebase Add", "Error adding picture", e)
            }
    }

    // given a bitmap, encode and convert to a string
    private fun encodeBitmap(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
    }

    // given a string?, decode and convert to a bitmap?
    @Throws(IOException::class)
    fun decodeBitmap(image: String?): Bitmap? {
        val decodedByteArray =
            Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }
}
