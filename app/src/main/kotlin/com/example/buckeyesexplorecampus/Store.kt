package com.example.buckeyesexplorecampus

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.FirebaseFirestore
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import android.util.Base64
import java.io.IOException
import android.graphics.BitmapFactory
import com.google.firebase.firestore.SetOptions

//
// user class
//

class User(
    val id: String,
    val successfulLandmarks: HashMap<String, String>
) {

    fun getLocation(): GeoPoint {
        return GeoPoint(4.0, 4.0);
        // TODO return location of user
    }

    fun addSuccessfulLandmark(landmarkId: String, bitmap: Bitmap, callback: () -> Int?) {
        // encode image
        val imgEncoded: String = Store.encodeBitmap(bitmap)



        // create map pair from landmark id to encoded image
        val data = hashMapOf(
            "successfulLandmarks" to hashMapOf(
                landmarkId to imgEncoded
            )
        )

        // push to Firebase
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(id)
            .set(data, SetOptions.merge())
            .addOnSuccessListener { _ ->

                // write changes to store
                val store = Store.instance()
                val user = store.user

                if (user != null) {
                    user.successfulLandmarks.put(landmarkId, imgEncoded)

                    val landmark = store.landmarks.find { it.id == landmarkId }
                    if (landmark != null) {
                        var index = store.landmarks.indexOf(landmark)
                        store.landmarks[index].hasBeenCompleted = true

                        callback()

                    }

                }

            }
            .addOnFailureListener { e ->
                Log.d("Firebase Add", "Error adding picture", e)
            }
    }

    override fun toString(): String = id
}

//
// store class
//

class Store {
    private val db = FirebaseFirestore.getInstance()

    companion object {

        // instance

        private var instance: Store? = null
        fun instance(): Store {
            if (instance == null) instance = Store()
            return instance as Store
        }

        // given a bitmap, encode and convert to a string
        fun encodeBitmap(bitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
        }

        // given a string?, decode and convert to a bitmap?
        @Throws(IOException::class)
        fun decodeBitmap(image: String?): Bitmap? {
            val decodedByteArray =
                Base64.decode(image, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
        }

    }

    // set main store data

    fun setDataWithCallback(userId: String?, callback: () -> Int) {
        setUser(userId, callback)
    }

    // user instance

    var user: User? = null

    private fun setUser(id: String?, callback: () -> Int) {
        if (id != null) {
            db.collection("users")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val data = document.get("successfulLandmarks") as HashMap<*, *>?;
                        val successfulLandmarks = HashMap<String, String>();

                        if (data != null)
                            for ((k, v) in data)
                                successfulLandmarks[k as String] = v as String

                        // set user
                        user = User(id, successfulLandmarks)

                        retrieveLandmarks(callback)

                    } else {
                        Log.d(ContentValues.TAG, "No such user document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
    }

    // landmarks instance

    val landmarks: ArrayList<Landmark> = ArrayList()

    private fun retrieveLandmarks(callback: () -> Int) {
        db.collection("landmarks")
            .get()
            .addOnSuccessListener { documents ->

                for (doc in documents) {
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
                        var isCompleted = false
                        if (user != null) {
                            isCompleted = (user as User)
                                .successfulLandmarks
                                .containsKey(doc.id)
                        }

                        val item = Landmark(doc.id, name, fact, lat, long, imgBase64, isCompleted)
                        landmarks.add(item)
                    }
                }

                callback()
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Get failed with exception", exception)
            }

    }

}
