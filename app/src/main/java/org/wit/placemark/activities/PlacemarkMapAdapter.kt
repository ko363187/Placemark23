package org.wit.placemark.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_placemark.view.*
import org.wit.placemark.R
import org.wit.placemark.helpers.readImageFromPath
import org.wit.placemark.models.Location
import org.wit.placemark.models.PlacemarkModel


interface PlacemarkMapListener{
    fun onClick(latitude : String,longitude : String)
}

class PlacemarkMapAdapter constructor(private var placemarks: List<PlacemarkModel>,private val listener: PlacemarkMapListener): RecyclerView.Adapter<PlacemarkMapAdapter.MainHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_placemark,parent,false))
    }

    override fun getItemCount(): Int = placemarks.size


    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = placemarks[holder.adapterPosition]
        holder.bind(placemark,listener)
    }

    class MainHolder constructor(itemView : View): RecyclerView.ViewHolder(itemView){
        fun bind(placemark: PlacemarkModel,listener: PlacemarkMapListener) {
            itemView.placemarkTitle.text = placemark.title
            itemView.description.text = placemark.description
            itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, placemark.image))
            itemView.setOnClickListener { listener.onClick(placemark.lat,placemark.lng) }
        }
    }
}

