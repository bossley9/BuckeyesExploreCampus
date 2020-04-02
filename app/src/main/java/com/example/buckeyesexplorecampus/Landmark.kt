package com.example.buckeyesexplorecampus

class Landmark(val id: String,
               val name: String,
               val fact: String,
               val latitude: Double,
               val longitude: Double,
               val imgUrl: String,
               val isCompleted: Boolean) {
    override fun toString(): String = id
}
