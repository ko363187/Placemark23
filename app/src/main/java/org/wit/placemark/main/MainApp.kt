package org.wit.placemark.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.placemark.models.PlacemarkMemStore
import org.wit.placemark.models.UserJson
import org.wit.placemark.models.UserStore

class MainApp : Application(), AnkoLogger {

  val placemarks = PlacemarkMemStore()
  lateinit var users: UserStore

  override fun onCreate() {
    super.onCreate()
    users = UserJson(applicationContext)
    info("Placemark started")
  }
}