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
import com.google.firebase.auth.FirebaseAuth
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

    // opens the facts fragment
    fun openFacts(landmarkId: String) {
        val factsFragment = FactsFragment()
        val args = Bundle()
        args.putString("landmarkId", landmarkId)
        factsFragment.arguments = args

        fragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragmentContainer, factsFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun retrieveLandmarks() {
        Toast.makeText(activity, "Retrieving landmarks...", Toast.LENGTH_SHORT).show()
        val list : ArrayList<Landmark> = ArrayList()

        var userId = FirebaseAuth.getInstance().currentUser?.uid as String

        // TEMP set store user
        var store = Store.instance()
        store.setUser(userId)
        Toast.makeText(activity, "user set in store.", Toast.LENGTH_SHORT).show()

        // TODO use promises
        db.collection("landmarks")
            .get()
            .addOnSuccessListener { landmarks ->

                db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { user ->

                        val data = user.get("successfulLandmarks") as HashMap<*, *>?;
                        val successfulLandmarks = HashMap<String, String>();

                        if (data != null) {
                            for ((k, v) in data) {
                                successfulLandmarks[k as String] = v as String
                            }
                        }

                        for (doc in landmarks) {
                            val name: String? = doc.get("name") as String?
                            val fact: String? = doc.get("fact") as String?
                            val geopoint: GeoPoint? = doc.get("location") as GeoPoint?
                            val lat: Double? = geopoint?.latitude
                            val long: Double? = geopoint?.longitude
                            val imgBase64: String? = doc.get("imgBase64") as String?

                            if (name != null &&
                                fact != null &&
                                lat != null &&
                                long != null &&
                                imgBase64 != null) {

                                // if already completed, mark as completed
                                val isCompleted = successfulLandmarks.containsKey(doc.id)

                                val item = Landmark(doc.id, name, fact, lat, long, imgBase64, isCompleted)
                                list.add(item)
                            }
                        }

                        rv.adapter = LandmarkRecyclerViewAdapter(list, listener, this)
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Get failed with exception", exception)
                    }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Get failed with exception", exception)
            }
    }
}

