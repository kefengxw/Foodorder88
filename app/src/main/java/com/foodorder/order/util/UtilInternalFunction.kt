package com.foodorder.order.util

import android.content.SharedPreferences

object UtilInternalFunction {
    fun saveUserDataForNextOpen(sharedPre: SharedPreferences, username: String, password: String) {

        val editor = sharedPre.edit()

        editor.putString("username", username)
        editor.putString("password", password)
        editor.commit()
    }
}