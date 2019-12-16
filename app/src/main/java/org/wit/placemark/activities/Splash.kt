package org.wit.placemark.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import org.wit.placemark.R

class Splash : AppCompatActivity() {

    private var handler: Handler? = null
    private val TIME: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initialize()
        startTimer()
    }


    private fun initialize() {
        handler = Handler()
    }

    private fun startTimer() {
        handler?.postDelayed(
            {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
                finishAffinity()
            }, TIME
        )
    }

}
