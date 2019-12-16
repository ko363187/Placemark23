package org.wit.placemark.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_placemark.view.*
import org.wit.placemark.helpers.readImageFromPath
import org.wit.placemark.models.PlacemarkModel
import java.util.ArrayList


interface PlacemarkListener {
  fun onPlacemarkClick(placemark: PlacemarkModel)
}

interface SharePlacemark{
  fun onShareClick(placemark: PlacemarkModel)
}

//private var placemarksFilter : List<PlacemarkModel>? = null

class PlacemarkAdapter constructor(private var placemarks: ArrayList<PlacemarkModel>,
                                   private val listener: PlacemarkListener,private val sharePlacemark: SharePlacemark) : RecyclerView.Adapter<PlacemarkAdapter.MainHolder>() {


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
    return MainHolder(
      LayoutInflater.from(parent?.context).inflate(
        org.wit.placemark.R.layout.card_placemark,
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: MainHolder, position: Int) {
    val placemark = placemarks[holder.adapterPosition]
    holder.bind(placemark, listener,sharePlacemark)
  }

  override fun getItemCount(): Int = placemarks.size

  class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(placemark: PlacemarkModel, listener: PlacemarkListener,sharePlacemark: SharePlacemark) {
      itemView.placemarkTitle.text = placemark.title
      itemView.description.text = placemark.description
      itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, placemark.image))
      itemView.setOnClickListener {
        listener.onPlacemarkClick(placemark)
      }
      itemView.share_placemark.setOnClickListener {sharePlacemark.onShareClick(placemark) }
    }

  }



}