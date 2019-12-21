// PlacemarkListActivity


package org.wit.placemark.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_placemark_list.*
import kotlinx.android.synthetic.main.card_placemark.view.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.placemark.R
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.PlacemarkModel
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.startActivity
import java.util.*
import kotlin.collections.ArrayList


class PlacemarkListActivity : AppCompatActivity(), PlacemarkListener,SharePlacemark {

    lateinit var app: MainApp
    private lateinit var database : FirebaseDatabase
    private lateinit var databaseRef : DatabaseReference
    var placemark = PlacemarkModel()
    lateinit var placemarks: ArrayList<PlacemarkModel>
    lateinit var placemarkAdapter:PlacemarkAdapter
    lateinit var placemarksFilter : ArrayList<PlacemarkModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.wit.placemark.R.layout.activity_placemark_list)
        app = application as MainApp
        toolbar.title = title
        setSupportActionBar(toolbar)

        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("Placemark")
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        //recyclerView.adapter = PlacemarkAdapter(app.placemarks.findAll(), this)
        getPlacemarkListFromFirebase()
        placemarks = ArrayList()
        placemarksFilter = ArrayList()
        search_placemark.imeOptions = EditorInfo.IME_ACTION_SEND


        // search view to search the placemark
        search_placemark.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //placemarkAdapter.filter.filter(newText)
                setFilter(newText)
                return true
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(org.wit.placemark.R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            org.wit.placemark.R.id.item_add -> startActivityForResult<PlacemarkActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPlacemarkClick(placemark: PlacemarkModel) {
        startActivityForResult(intentFor<PlacemarkActivity>().putExtra("placemark_edit", placemark), 0)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    // it call all the data which is there in firebase database with reference "Placemark"
    private fun getPlacemarkListFromFirebase(){

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                placemarks.clear()
                for (dataSnapshot in snapshot.getChildren()) {
                    dataSnapshot.getValue(placemark::class.java)?.let { placemarks.add(it) }
                    dataSnapshot.getValue(placemark::class.java)?.let { placemarksFilter.add(it) }
                }
                setAdapter()

                //Log.d(FragmentActivity.TAG, "Value is: " + value!!)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(FragmentActivity.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun setAdapter(){
        placemarkAdapter = PlacemarkAdapter(placemarks, this,this)
        recyclerView.adapter = placemarkAdapter

    }



    private fun setFilter(text: String) {
        placemarks.clear()
        for (item in placemarksFilter){
            if (item.title.contains(text)){
                placemarks.add(item)
            }
        }

        placemarkAdapter.notifyDataSetChanged()
    }

    override fun onShareClick(placemark: PlacemarkModel) {
        val email = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","",null))
        email.putExtra(Intent.EXTRA_EMAIL,"")
        email.putExtra(Intent.EXTRA_SUBJECT, "Placemark")
        email.putExtra(Intent.EXTRA_TEXT, placemark)
        startActivity(Intent.createChooser(email, "Send result"))
    }
}