package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags

data class GetPropertyTypeData(
    val `data`: ArrayList<GetPropertyTypeDataClass>
)
data class GetPropertyTypeDataClass(
    val id:String,
    val propertyTypeId:String,
    val propertyType:String
)