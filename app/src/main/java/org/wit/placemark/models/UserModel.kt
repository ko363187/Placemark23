package org.wit.placemark.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel (
    var id: Long = 0,

    var email: String = "",
    var password: String = ""

) : Parcelable