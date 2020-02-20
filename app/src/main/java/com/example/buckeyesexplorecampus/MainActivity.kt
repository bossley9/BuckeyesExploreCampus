package com.example.buckeyesexplorecampus

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginFragment = LoginFragment()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentContainer, loginFragment)
            .commit()
    }


}
