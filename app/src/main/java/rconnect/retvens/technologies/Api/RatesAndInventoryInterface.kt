package rconnect.retvens.technologies.Api

import rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory.InventoryDataClass
import rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory.RatesDataClass
import rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory.ResponseData
import rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory.UpdateBulkInventory
import rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory.UpdateSingleInventory
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
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

    @GET("getRate")
    fun getRates(
        @Query("roomTypeId") roomTypeId: String,
        @Query("startDate") checkInDate: String,
        @Query("endDate") checkOutDate: String,
        @Query("userId") userId: String
    ): Call<RatesDataClass>

    @PATCH("updateInventory")
    fun updateInventory(
        @Body updateInventory : UpdateSingleInventory
    ):Call<rconnect.retvens.technologies.onboarding.ResponseData>

    @PATCH("updateInventory")
    fun updateBulkInventory(
        @Body updateInventory : UpdateBulkInventory
    ):Call<rconnect.retvens.technologies.onboarding.ResponseData>
}