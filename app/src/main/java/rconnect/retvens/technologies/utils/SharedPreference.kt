package rconnect.retvens.technologies.utils

import android.content.Context

class SharedPreference (context:Context){


    // SharedPreferences instance
    private val sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

    // Editor to make changes
    private val editor = sharedPreferences.edit()

    fun saveLoginFlagValue(login:Boolean){
        editor.putBoolean("login",login)
        editor.apply()
    }

    fun getLoginFlagValue(): Boolean {
        return sharedPreferences.getBoolean("login", false)
    }




}