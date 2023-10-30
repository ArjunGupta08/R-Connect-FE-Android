package rconnect.retvens.technologies.utils

import android.content.Context
import android.content.SharedPreferences

class UserSessionManager(context: Context) {
    private val prefName = "UserSession"
    private val userIdKey = "userId"
    private val tokenKey = "token"

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveUserData(userId: String, token: String) {
        editor.putString(userIdKey, userId)
        editor.putString(tokenKey, token)
        editor.apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(userIdKey, null)
    }

    fun getToken(): String? {
        return sharedPreferences.getString(tokenKey, null)
    }

    fun clearUserData() {
        editor.remove(userIdKey)
        editor.remove(tokenKey)
        editor.apply()
    }
}