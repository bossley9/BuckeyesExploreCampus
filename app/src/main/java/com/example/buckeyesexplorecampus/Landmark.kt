package com.example.buckeyesexplorecampus

class Landmark(private val id: String,
               val name: String,
               val fact: String,
               val latitude: Double,
               val longitude: Double,
               val imgUrl: String) {
    override fun toString(): String = id
}
