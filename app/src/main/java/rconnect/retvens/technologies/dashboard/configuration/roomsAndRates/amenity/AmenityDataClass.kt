package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity

data class AmenityDataClass (
    val `data` : ArrayList<GetAmenityData>
)

data class GetAmenityData (
    val getAmenity : String,
    val amenityName : String,
    val amenityType : String,
    val amenityIconLink : String
)

        /* Get Amenity Type */
data class GetAmenityType (
    val `data` : ArrayList<GetAmenityTypeData>
)
data class GetAmenityTypeData (
    val amenityTypeId : String,
    val amenityType : String
)

        /* Get Amenity Icon */
data class GetAmenityIcon (
    val `data` : ArrayList<GetAmenityIconData>
)
data class GetAmenityIconData (
    val amenityIconId : String,
    val amenityIconLink : String,
    val amenityIconTags : ArrayList<String>,
)
