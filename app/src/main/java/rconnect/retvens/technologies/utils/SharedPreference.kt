package rconnect.retvens.technologies.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreference (context:Context){


    // SharedPreferences instance
    private val sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

    // Editor to make changes
    private val editor = sharedPreferences.edit()

    fun saveFlagValue(login:Boolean){
        editor.putBoolean("login",login)
        editor.apply()
    }

    fun getFlagValue(): Boolean {
        return sharedPreferences.getBoolean("login", false)
    }




}