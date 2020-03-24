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
import com.example.buckeyesexplorecampus.dummy.DummyContent
import com.example.buckeyesexplorecampus.dummy.DummyContent.DummyItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import android.util.Log
import android.widget.Toast

/**
 * The main screen fragment.
 */
class LandmarkMenuFragment : Fragment() {
    private var columnCount = 3
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var rv: RecyclerView
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_landmark_menu, container, false)

        // recycler view

        rv = view.findViewById(R.id.recyclerView) as RecyclerView
        rv.setHasFixedSize(true)
        rv.layoutManager = GridLayoutManager(context, columnCount)
        rv.adapter = LandmarkRecyclerViewAdapter(DummyContent.ITEMS, listener)

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
        fun onListFragmentInteraction(item: DummyItem?)
    }
}
