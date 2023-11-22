package rconnect.retvens.technologies.utils

import android.content.Context
import android.content.SharedPreferences

class UserSessionManager(context: Context) {
    private val prefName = "UserSession"
    private val userIdKey = "userId"
    private val tokenKey = "token"
    private val saveUsername = "saveUsername"

    private val roomTypeIdKey = "roomTypeIdKey"
    private val propertyIdKey = "propertyId"

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveUserData(userId: String, token: String) {
        editor.putString(userIdKey, userId)
        editor.putString(tokenKey, token)
        editor.apply()
    }

    fun savePropertyId(propertyId: String) {
        editor.putString(propertyIdKey, propertyId)
        editor.apply()
    }

    fun saveRoomTypeId(roomTypeId: String) {
        editor.putString(roomTypeIdKey, roomTypeId)
        editor.apply()
    }

    fun saveUsername(username: String) {
        editor.putString(roomTypeIdKey, saveUsername)
        editor.apply()
    }

    fun getUsername() : String? {
        return sharedPreferences.getString(saveUsername, null)
    }

    fun getPropertyId(): String? {
        return sharedPreferences.getString(propertyIdKey, null)
    }

    fun getRoomTypeId(): String? {
        return sharedPreferences.getString(roomTypeIdKey, null)
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