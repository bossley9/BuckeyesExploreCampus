package com.example.buckeyesexplorecampus

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
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
import android.R.attr.data
import androidx.core.app.ActivityCompat.startActivityForResult
import android.provider.MediaStore
import android.os.Environment.getExternalStorageDirectory
import android.R.attr.bitmap
import android.R.attr.data

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

     fun startCameraIntent() {
/*
         val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//         intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
         startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
*/

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            activity?.packageManager?.let {
                takePictureIntent.resolveActivity(it)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

     }

    // TODO implement image comparison
    private fun isValidImage(img: Bitmap): Boolean {
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
        } else {
            fragmentManager?.popBackStack()
        }
    }

    val callback = {
        val factsFragment = FactsFragment()

        val args = Bundle()
        args.putString("landmarkId", landmarkId)
        factsFragment.arguments = args

        fragmentManager
            ?.popBackStack()

        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragmentContainer, factsFragment)
            ?.addToBackStack("")
            ?.commit()
    }

    private fun addSuccessfulLandmark(bitmap: Bitmap) {
        val store = Store.instance()
        val user = store.user

        if (user != null) {
            user.addSuccessfulLandmark(landmarkId, bitmap, callback)
        }
    }


}
