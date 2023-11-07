package rconnect.retvens.technologies.Api

import rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory.InventoryDataClass
import rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory.ResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RatesAndInventoryInterface {

    @GET("getInventory")
    fun getInventory(
        @Query("userId") userId: String,
        @Query("propertyId") propertyId: String,
        @Query("checkInDate") checkInDate: String,
        @Query("checkOutDate") checkOutDate: String
    ): Call<ResponseData>

}