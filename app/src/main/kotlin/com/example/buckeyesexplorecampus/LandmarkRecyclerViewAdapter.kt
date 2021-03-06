package com.example.buckeyesexplorecampus

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buckeyesexplorecampus.LandmarkMenuFragment.OnListFragmentInteractionListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_landmark.view.*
import java.io.IOException


/**
 * [RecyclerView.Adapter] lays out the display for a [Landmark] in the main menu.
 */
class LandmarkRecyclerViewAdapter(
    private val mValues: List<Landmark>,
    private val mListener: OnListFragmentInteractionListener?,
    private val mParentFragment: Fragment?
) : RecyclerView.Adapter<LandmarkRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Landmark
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_landmark, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.name

        if (item.hasBeenCompleted) {
            holder.mContentView.text = "completed!"
        } else {
            holder.mContentView.text = "this loc is __ mi away"
        }

        holder.mImagePreview.setOnClickListener {
            val parent : LandmarkMenuFragment = mParentFragment as LandmarkMenuFragment

            if (item.hasBeenCompleted) {
                parent.openFacts(item.id)
            } else {
                parent.openCamera(item.id)
            }
        }


        val picture : String = item.imgBase64
        val imageBitMap : Bitmap? = decodeFromFirebaseBase64(picture)
        holder.mImagePreview.setImageBitmap(imageBitMap)


        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    //TODO: This function is resused code from Facts Fragment
    @Throws(IOException::class)
    fun decodeFromFirebaseBase64(image: String?): Bitmap? {
        val decodedByteArray =
            Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.locationName
        val mContentView: TextView = mView.content
        val mImagePreview: ImageView = mView.locationPreview

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
