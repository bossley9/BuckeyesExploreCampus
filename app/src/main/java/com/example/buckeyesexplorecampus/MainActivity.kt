package com.example.buckeyesexplorecampus

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val menuFragment = LoginFragment()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer, menuFragment)
            .commit()
    }

//    override fun onResume() {
//        super.onResume()
//        Toast.makeText(this, "re-execute any async tasks if needed", Toast.LENGTH_SHORT).show()
//    }
}
