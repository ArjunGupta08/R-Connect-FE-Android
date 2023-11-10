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
