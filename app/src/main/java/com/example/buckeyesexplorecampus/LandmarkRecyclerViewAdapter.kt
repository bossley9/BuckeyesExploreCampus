package com.example.buckeyesexplorecampus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buckeyesexplorecampus.LandmarkMenuFragment.OnListFragmentInteractionListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_landmark.view.*


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
        holder.mContentView.text = item.fact

        holder.mImagePreview.setOnClickListener {
            val parent : LandmarkMenuFragment = mParentFragment as LandmarkMenuFragment
            parent.openCamera()
        }

        // Reference to item's image file in Cloud Storage
        val storageReference: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(item.imgUrl)

        // Load the image using Glide
        if (mParentFragment != null) {
            Glide.with(mParentFragment) //Context used is the parent fragment's, this is the lifecycle the image will follow
                .load(storageReference)
                .into(holder.mImagePreview)
        }


        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
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
