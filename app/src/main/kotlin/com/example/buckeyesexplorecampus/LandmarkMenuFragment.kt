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
import android.app.FragmentTransaction

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

        // updates
        retrieveLandmarks()

        rv.setHasFixedSize(true)
        rv.setItemViewCacheSize(20)
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
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.replace(R.id.fragmentContainer, cameraFragment)
            ?.addToBackStack(null)
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
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.replace(R.id.fragmentContainer, factsFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun retrieveLandmarks() {
        val landmarks = Store.instance().landmarks
        rv.adapter = LandmarkRecyclerViewAdapter(landmarks, listener, this)
    }

    override fun onResume() {
        super.onResume()
        retrieveLandmarks()
    }
}

