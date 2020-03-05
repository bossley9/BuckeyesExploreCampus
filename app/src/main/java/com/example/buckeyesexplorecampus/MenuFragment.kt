package com.example.buckeyesexplorecampus

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A [Fragment] subclass which displays all images and instructions.
 */
class MenuFragment : Fragment() {

    private lateinit var landmarks: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init data
        landmarks = ArrayList()
        landmarks.add("hello")
        landmarks.add("world")
        landmarks.add("test")
        landmarks.add("123")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        // recycler view

        val recycler = view.findViewById(R.id.recyclerView) as RecyclerView
        recycler.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // span count
            val spanCount = 2
            layoutManager = GridLayoutManager(activity, spanCount)

//            adapter = Adapter()
        }


        // logout

        val logoutSubmit = view.findViewById(R.id.logoutSubmit) as Button
        logoutSubmit.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("are you sure you would like to logout?")
                .setPositiveButton("logout") { _, _ ->
                    val loginFragment = LoginFragment()
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.fragmentContainer, loginFragment)
                        ?.commit()
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
                    val loginFragment = LoginFragment()
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.fragmentContainer, loginFragment)
                        ?.commit()
                }
                .setNegativeButton("cancel", null)
                .show()
        }

        return view
    }

}

//class LandmarkAdapter(private val data: Array<String>) : RecyclerView.Adapter<>() {

//}
