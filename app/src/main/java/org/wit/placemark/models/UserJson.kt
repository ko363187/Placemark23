package org.wit.placemark.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.placemark.helpers.exists
import org.wit.placemark.helpers.read
import org.wit.placemark.helpers.write
import java.util.*
/*
val USER_JSON_FILE = "users.json"
val gsonUserBuilder = GsonBuilder().setPrettyPrinting().create()
val listUserType = object : TypeToken<ArrayList<UserModel>>() {}.type
var loggedIn = UserModel()

 */
val USER_JSON_FILE = "users.json"
val gsonUserBuilder = GsonBuilder().setPrettyPrinting().create()
val listUserType = object : TypeToken<java.util.ArrayList<UserModel>>() {}.type
var loggedIn = UserModel()


fun generateRandomId(): Long {
    return Random().nextLong()
}

class UserJson: UserStore,AnkoLogger {

    var users = mutableListOf<UserModel>()
    val context: Context


     constructor (context: Context) {
        this.context = context
        if (exists(context, USER_JSON_FILE)) {
            deserialize()
        }
    }




    override fun findAll(): List<UserModel> {
      return users
    }

    override fun create(user: UserModel) {
        user.id = generateRandomId()
        users.add(user)
        serialize()
    }

    override fun delete(user: UserModel) {
        if (user != null) {
            users.remove(user)
        }
    }

    override fun update(user: UserModel) {
        var foundUser: UserModel? = users.find { q -> q.id == user.id }
        if (foundUser != null) {
            foundUser.email = user.email
            foundUser.password = user.password
            logAll()
            serialize()
        }
    }

    override fun indexOf(user: UserModel): Int {
        return users.indexOf(user)
    }

    override fun size(): Int {
        return users.size
    }

    override fun login(user: UserModel): Boolean {
        var foundUser: UserModel? = users.find { u -> u.email == user.email && u.password == user.password }
        if (foundUser != null) {
            loggedIn = foundUser
            logAll()
            return false // login success
        }
        return true // login failed
    }

    override fun signup(user: UserModel): Boolean {
        var foundUser: UserModel? = users.find { u -> u.email == user.email }
        if (foundUser != null) {
            return true // sign up failed
        }
        return false //signup success
    }

    fun logAll() {
        users.forEach { info("${it}") }
    }
    override fun getLoggedIn(): UserModel {
       return loggedIn
    }

    override fun logOut() {
        loggedIn = UserModel()
    }


    // put all users into file
    private fun serialize() {
        val jsonString = gsonUserBuilder.toJson(users, listUserType)
        write(context, USER_JSON_FILE, jsonString)
    }

    // get all users from file
    private fun deserialize() {
        val jsonString = read(context, USER_JSON_FILE)
        users = Gson().fromJson(jsonString, listUserType)
    }



}