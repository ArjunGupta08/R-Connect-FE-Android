package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.GetAmenityData

data class GetPropertyTypeData(
    val `data`: ArrayList<GetPropertyTypeDataClass>
)
data class GetPropertyTypeDataClass(
    val id:String,
    val propertyTypeId:String,
    val propertyType:String
)

data class GetPropertyData(
    val data: GetPropertyDataClass
)
data class GetPropertyDataClass(
    val propertyId: String,
    val createdOn: String,
    val country: String,
    val propertyAddress1 : String,
    val propertyAddress2 : String,
    val postCode: String,
    val city: String,
    val state: String,
    val propertyName: String,
    val amenities: ArrayList<GetAmenityData>,
    val hotelLogo: String,
    val propertyEmail: String,
    val propertyType: String,
    val propertyDescription: String,
    val rating: String,
    val websiteUrl: String,
    val phone: String,
    val reservationPhone: String,
    val latitude: String,
    val longitude: String,
    val propertyRating:String
)