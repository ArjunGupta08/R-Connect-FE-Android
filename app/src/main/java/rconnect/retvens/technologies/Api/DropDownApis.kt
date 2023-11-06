package rconnect.retvens.technologies.Api

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.GetPropertyTypeData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.GetPropertyTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.GetPropertyTypeRatingData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface DropDownApis {

    @GET("getPropertyType")
    fun getPropertyType(): Call<GetPropertyTypeData>

    @GET("getRating")
    fun getPropertyTypeRating(): Call<GetPropertyTypeRatingData>

}