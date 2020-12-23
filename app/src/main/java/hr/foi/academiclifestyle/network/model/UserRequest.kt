package hr.foi.academiclifestyle.network.model

import android.graphics.Bitmap
import android.graphics.Picture
import android.graphics.drawable.Drawable

data  class UserRequest(
        val name: String,
        val surname: String,
        val program: Int,
        val year: Int,
        val profile_picture: Int
)