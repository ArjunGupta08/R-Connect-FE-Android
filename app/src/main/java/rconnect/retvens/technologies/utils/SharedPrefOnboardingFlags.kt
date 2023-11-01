package rconnect.retvens.technologies.utils

import android.content.Context

class SharedPrefOnboardingFlags (context:Context){


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

    fun saveSignUpFlagValue(login:Boolean){
        editor.putBoolean("signUp",login)
        editor.apply()
    }
    fun getSignUpFlagValue(): Boolean {
        return sharedPreferences.getBoolean("signUp", false)
    }

    fun saveSecondChainFlagValue(login:Boolean){
        editor.putBoolean("SecondChain",login)
        editor.apply()
    }
    fun getSecondChainFlagValue(): Boolean {
        return sharedPreferences.getBoolean("SecondChain", false)
    }

    fun saveFirstFlagValue(login:Boolean){
        editor.putBoolean("First",login)
        editor.apply()
    }
    fun getFirstFlagValue(): Boolean {
        return sharedPreferences.getBoolean("First", false)
    }

    fun saveSecondFlagValue(login:Boolean){
        editor.putBoolean("Second",login)
        editor.apply()
    }
    fun getSecondFlagValue(): Boolean {
        return sharedPreferences.getBoolean("Second", false)
    }


}