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
    fun saveSignUpFlagValue(sign:Boolean){
        editor.putBoolean("sign",sign)
        editor.apply()
    }

    fun getLoginFlagValue(): Boolean {
        return sharedPreferences.getBoolean("login", false)
    }
    fun getSignUpFlagValue(): Boolean {
        return sharedPreferences.getBoolean("sign", false)
    }


    fun saveSingleFlagValue(singleProperty:Boolean){
        editor.putBoolean("single",singleProperty)
        editor.apply()
    }

    fun getSingleFlagValue(): Boolean {
        return sharedPreferences.getBoolean("single", false)
    }




}