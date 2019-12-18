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



}