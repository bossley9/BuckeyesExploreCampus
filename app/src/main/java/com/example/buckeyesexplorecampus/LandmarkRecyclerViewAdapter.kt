package com.example.buckeyesexplorecampus

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.buckeyesexplorecampus.LandmarkMenuFragment.OnListFragmentInteractionListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_landmark.view.*
import kotlin.coroutines.coroutineContext
import kotlin.math.acos

/**
 * [RecyclerView.Adapter] lays out the display for a [Landmark] in the main menu.
 */
class LandmarkRecyclerViewAdapter(
    private val mValues: List<Landmark>,
    private val mListener: OnListFragmentInteractionListener?
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

        val imgUri : Uri = item.imgUrl.toUri()
        holder.mImagePreview.setImageURI(imgUri)
        
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content
        val mImagePreview: ImageView = mView.locationPreview

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
