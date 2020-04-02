package com.example.buckeyesexplorecampus

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
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
            val act: MainActivity? = activity as? MainActivity
            act?.signOut()
        }

        // delete account
        val deleteSubmit = view.findViewById(R.id.deleteSubmit) as Button
        deleteSubmit.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage(R.string.delete_account_confirmation)
                .setPositiveButton(R.string.delete_account) { _, _ ->
                    val act: MainActivity? = activity as? MainActivity
                    act?.deleteAccount()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }

        return view
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Landmark?)
    }

    private fun retrieveLandmarks() {
        Toast.makeText(activity, "Retrieving landmarks...", Toast.LENGTH_SHORT).show()
        val list : ArrayList<Landmark> = ArrayList()

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
                Log.d(TAG, "Get failed with exception", exception)
            }
    }

    fun openCamera(landmarkId: String) {
        val cameraFragment = CameraFragment()

        val args = Bundle()
        args.putString("landmarkId", landmarkId)
        cameraFragment.arguments = args

        fragmentManager
            ?.beginTransaction()
            ?.add(R.id.fragmentContainer, cameraFragment)
//            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onResume() {
        super.onResume()
        retrieveLandmarks()
    }
}
