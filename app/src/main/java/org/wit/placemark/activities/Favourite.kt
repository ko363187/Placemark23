//Favourite CLASS

package org.wit.placemark.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.activity_placemark_list.*
import kotlinx.android.synthetic.main.activity_placemark_list.toolbar
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.intentFor
import org.wit.placemark.R
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.PlacemarkModel
import java.util.ArrayList

class Favourite : AppCompatActivity(),PlacemarkListener,SharePlacemark {

    private lateinit var database : FirebaseDatabase
    private lateinit var databaseRef : DatabaseReference
    lateinit var favourites: ArrayList<PlacemarkModel>
    var placemark = PlacemarkModel()

    lateinit var app: MainApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        app = application as MainApp
        setSupportActionBar(toolbar)
        toolbar.title = "Favourite"

        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("Placemark")
        val layoutManager = LinearLayoutManager(this)
        rv_favourite.layoutManager = layoutManager

        favourites = ArrayList()
        getFavouriteListFromFirebase()
    }

    private fun getFavouriteListFromFirebase(){
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                favourites.clear()
                for (dataSnapshot in snapshot.getChildren()) {
                    val  placemarkModel =  dataSnapshot.getValue(placemark::class.java)
                    if(placemarkModel!=null){
                        if (placemarkModel.favourite){
                            favourites.add(placemarkModel)
                        }
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
        rv_favourite.adapter = PlacemarkAdapter(favourites,this,this)
    }

    override fun onPlacemarkClick(placemark: PlacemarkModel) {
        startActivityForResult(intentFor<PlacemarkActivity>().putExtra("placemark_edit", placemark), 0)
    }

    override fun onShareClick(placemark: PlacemarkModel) {
        val email = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","",null))
        email.putExtra(Intent.EXTRA_EMAIL,"")
        email.putExtra(Intent.EXTRA_SUBJECT, "Placemark")
        email.putExtra(Intent.EXTRA_TEXT, placemark)
        startActivity(Intent.createChooser(email, "Send result"))
    }
}
