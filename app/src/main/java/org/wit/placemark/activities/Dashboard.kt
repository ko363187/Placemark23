package org.wit.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.placemark.R
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.UserModel
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import java.nio.file.Files.delete




class Dashboard : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
//Dashborad ativities

    lateinit var app: MainApp
    var user = UserModel()
    lateinit var firebaseUser:FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.wit.placemark.R.layout.activity_dashboard)

        setSupportActionBar(dashboardToolbar)

        val toggle = ActionBarDrawerToggle(this, drawer, dashboardToolbar,
            org.wit.placemark.R.string.navigation_drawer_open,
            org.wit.placemark.R.string.navigation_drawer_close
        )

        app = application as MainApp

        user = app.users.getLoggedIn()

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser()!!

        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationview.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            org.wit.placemark.R.id.up -> Toast.makeText(this, "Up", Toast.LENGTH_SHORT).show()
            org.wit.placemark.R.id.add -> startActivityForResult<PlacemarkActivity>(200)
            org.wit.placemark.R.id.cancel -> Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            //org.wit.placemark.R.id.delete -> delete()
            org.wit.placemark.R.id.map -> startActivityForResult<MapLists>(200)
            org.wit.placemark.R.id.favourite -> startActivityForResult<Favourite>(200)
            org.wit.placemark.R.id.setting -> startActivityForResult<Setting>(200)
            org.wit.placemark.R.id.list -> startActivityForResult<PlacemarkListActivity>(200)
            org.wit.placemark.R.id.logout ->  logout()
        }
        drawer.closeDrawer(GravityCompat.START)
        return  true
    }

    private fun logout(){
        app.users.logOut()
        startActivity(intentFor<LoginActivity>())
    }

    private fun delete(){
        //app.users.delete(user)
        firebaseUser.delete()
            .addOnCompleteListener(OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                   // Log.d(FragmentActivity.TAG, "User account deleted.")
                    startActivity(intentFor<LoginActivity>())
                }
            })

    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}
