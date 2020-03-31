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
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.GeoPoint

/**
 * The main screen fragment.
 */
class LandmarkMenuFragment : Fragment() {
    private var columnCount = 3
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var rv: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_landmark_menu, container, false)

        // recycler view
        rv = view.findViewById(R.id.recyclerView) as RecyclerView

        rv.setHasFixedSize(true)
        rv.layoutManager = GridLayoutManager(context, columnCount)

        // retrieve landmarks
        retrieveLandmarks()

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
        fun onListFragmentInteraction(item: Landmark?)
    }

    private fun retrieveLandmarks() {
        val list : ArrayList<Landmark> = ArrayList()
        Toast.makeText(activity, "retrieving landmarks...", Toast.LENGTH_SHORT).show()

        db.collection("landmarks")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val name: String? = doc.get("name") as String?
                    val fact: String? = doc.get("fact") as String?
                    val geopoint: GeoPoint? = doc.get("location") as GeoPoint?
                    val lat: Double? = geopoint?.latitude
                    val long: Double? = geopoint?.longitude
                    val imgUrl: String? = doc.get("imgUrl") as String?

                    if (name != null &&
                        fact != null &&
                        lat != null &&
                        long != null &&
                        imgUrl != null) {

                      val item = Landmark(doc.id, name, fact, lat, long, imgUrl)
                      list.add(item)
                    }
                }
                rv.adapter = LandmarkRecyclerViewAdapter(list, listener, this)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    fun openCamera() {
        fragmentManager
            ?.beginTransaction()
            ?.add(R.id.fragmentContainer, CameraFragment())
            ?.addToBackStack("camera")
            ?.commit()
    }

    override fun onResume() {
        super.onResume()
        retrieveLandmarks()
    }
}
