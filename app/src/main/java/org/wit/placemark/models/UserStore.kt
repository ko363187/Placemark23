package org.wit.placemark.models

interface UserStore {
  fun findAll(): List<UserModel>
  fun create(user: UserModel)
  fun delete(user: UserModel)
  fun update(user: UserModel)
  fun indexOf(user: UserModel): Int
  fun size(): Int
  fun login(user: UserModel): Boolean
  fun signup(user: UserModel): Boolean
  fun getLoggedIn() : UserModel
  fun logOut()
/*
  fun createPlacemark(placemark: PlacemarkModel)
  fun visited() : Int
  fun updatePlacemark(placemark: PlacemarkModel)
  fun deletePlacemark(placemark: PlacemarkModel)
  fun sizePlacemarks() : Int
  fun indexOfPlacemarks(placemark: PlacemarkModel): Int
  fun findAllPlacemarks(): MutableList<PlacemarkModel>
*/
}