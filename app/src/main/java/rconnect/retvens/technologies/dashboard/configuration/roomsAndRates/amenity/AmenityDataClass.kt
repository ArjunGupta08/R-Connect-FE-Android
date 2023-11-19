package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity

data class PostAmenityData (
    val userId : String,
    val shortCode : String,
    val amenityName : String,
    val propertyId : String,
    val amenityType : String,
    val amenityIcon : String,
    val amenityIconLink : String
)

data class PatchAmenityData (
    val amenityName : String,
    val amenityType : String,
    val amenityIcon : String,
    val amenityIconLink : String
)

data class AmenityDataClass (
    val `data` : ArrayList<GetAmenityData>
)
data class GetAmenityData (
    val amenityId : String,
    val amenityName : String,
    val amenityType : String,
    val amenityIconLink : String
)

        /* Fetch Amenities Type */
data class FetchAmenities (
    val `data` : ArrayList<FetchAmenitiesData>
)
data class FetchAmenitiesData (
    val shortCode : String,
    val amenityId : String,
    val amenityName : String,
    val amenityType : String,
    val amenityIcon : String,
    val amenityIconId : String,
    val amenityIconLink : String,
    val createdOn : String,
    val createdBy : String,
    val modifiedBy : String,
    val modifiedOn : String,
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
