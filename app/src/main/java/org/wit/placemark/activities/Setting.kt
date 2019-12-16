package org.wit.placemark.activities

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_setting.*
import org.wit.placemark.R
import org.wit.placemark.main.MainApp
import com.google.firebase.internal.FirebaseAppHelper.getUid
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import org.wit.placemark.helpers.readImage
import org.wit.placemark.helpers.showImagePicker
import org.wit.placemark.models.PlacemarkModel
import java.util.ArrayList


class Setting : AppCompatActivity(),View.OnClickListener {
//setting

    lateinit var app: MainApp
    val IMAGE_REQUEST = 1
    lateinit var firebaseUser: FirebaseUser
    private lateinit var database : FirebaseDatabase
    private lateinit var databaseRef : DatabaseReference
    lateinit var placemarks: ArrayList<PlacemarkModel>
    var placemark = PlacemarkModel()
    var visited: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.wit.placemark.R.layout.activity_setting)

        app = application as MainApp
        setSupportActionBar(toolbar)

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser()!!
        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("Placemark")
        placemarks = ArrayList()
        getPlacemarkListFromFirebase()
        if (firebaseUser != null){
            val name = firebaseUser.getDisplayName()
            val email = firebaseUser.getEmail()
            val photoUrl = firebaseUser.getPhotoUrl()


            // set the current logged in user from firebaseUser
            username.setText("Email : " + email)

            if (photoUrl != null){
                profileImage.setImageURI(photoUrl)
            }

        }

        logout.setOnClickListener(this)
        deleteProfile.setOnClickListener(this)
        chooseProfileImage.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
       if (p0 == logout){
           logout()
       }
        else if (p0 == deleteProfile){
           delete()
       }
        else if (p0 == chooseProfileImage){
           showImagePicker(this,IMAGE_REQUEST)
       }
    }
    private fun logout(){
       // app.users.logOut()
        startActivity(intentFor<LoginActivity>())
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
                        if (placemarkModel.visited){
                        visited++
                    }
                }}

                //set the number of placemarks and set the placmarks visited
                no_placemarks.setText("Number of placemarks : " + placemarks.size)
                placemark_visited.setText("Placemark Visited : $visited" )

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Log.w(FragmentActivity.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun delete(){
        //app.users.delete(user)
        firebaseUser.delete()
            .addOnCompleteListener(OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    // Log.d(FragmentActivity.TAG, "User account deleted.")
                    startActivity(intentFor<LoginActivity>())
                }
                else{
                    Toast.makeText(this, "Some error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST){
            if (data != null){
                profileImage.setImageBitmap(readImage(this,resultCode,data))
                data.data?.path
                changeProfilePicture(data.data)
                var i = 9
            }
        }
    }


    private fun changeProfilePicture(data: Uri?){

         val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(data)
            .build()

        firebaseUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Profile picture saved successfully", Toast.LENGTH_SHORT).show()
                }
            }




    }
}
