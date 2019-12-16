package org.wit.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.activity_map_lists.*
import kotlinx.android.synthetic.main.activity_placemark.*
import kotlinx.android.synthetic.main.activity_placemark_list.*
import kotlinx.android.synthetic.main.activity_placemark_list.toolbar
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.intentFor
import org.wit.placemark.R
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.Location
import org.wit.placemark.models.PlacemarkModel
import java.util.ArrayList

class MapLists : AppCompatActivity(),PlacemarkMapListener{

    lateinit var app: MainApp
    var location = Location(52.245696, -7.139102, 15f)
    val LOCATION_REQUEST = 2
    private lateinit var database : FirebaseDatabase
    private lateinit var databaseRef : DatabaseReference
    lateinit var placemarks: ArrayList<PlacemarkModel>
    var placemark = PlacemarkModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_lists)
        app = application as MainApp
        setSupportActionBar(toolbar)
        toolbar.title = "Maps"
        val layoutManager = LinearLayoutManager(this)
        rv_maps.layoutManager = layoutManager

        placemarks = ArrayList()
        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("Placemark")
        getPlacemarkListFromFirebase()
    }

    override fun onClick(lat : String,lng : String) {
        if (lat.isEmpty() && lng.isEmpty()){
            Toast.makeText(this,"Location is not set", Toast.LENGTH_SHORT).show()
        }
        else{
            location.lat = java.lang.Double.parseDouble(lat)
            location.lng = java.lang.Double.parseDouble(lng)
            startActivityForResult(intentFor<MapActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

    }


    private fun getPlacemarkListFromFirebase(){

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (dataSnapshot in snapshot.getChildren()) {


                    val  placemarkModel =  dataSnapshot.getValue(placemark::class.java)
                    if(placemarkModel!=null){
                        placemarks.add(placemarkModel)
                    }}

                setAdapter()

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(FragmentActivity.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun setAdapter(){
        rv_maps.adapter = PlacemarkMapAdapter(placemarks,this)
    }
}
