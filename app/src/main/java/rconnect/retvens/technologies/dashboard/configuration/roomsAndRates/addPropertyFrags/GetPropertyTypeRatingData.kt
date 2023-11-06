package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags

data class GetPropertyTypeRatingData(
    val `data`: ArrayList<GetPropertyTypeRatingDataClass>
)
data class GetPropertyTypeRatingDataClass(
    val id:String,
    val propertyRatingId:String,
    val propertyRating:String
)