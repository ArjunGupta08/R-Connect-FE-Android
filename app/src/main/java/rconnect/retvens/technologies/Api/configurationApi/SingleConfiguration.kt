package rconnect.retvens.technologies.Api.configurationApi


import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import rconnect.retvens.technologies.dashboard.configuration.properties.FetchPropertyData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.CreateRoomData
import rconnect.retvens.technologies.onboarding.ResponseData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface SingleConfiguration {

    /*---------------------------------------Add Room-----------------------------------------*/
    @POST("createRoom")
    fun createRoomApi(
        @Body createRoomData: CreateRoomData
    ): Call<ResponseData>

}