package rconnect.retvens.technologies.dashboard.configuration.others.holiday

data class AddHolidayDataClass(
    val userId : String,
    val propertyId : String,
    val shortCode : String,
    val holidayName : String,
    val startDate : String,
    val endDate : String
)

data class UpdateSeasonDataClass(
    val shortCode : String,
    val identityType : String,
)

data class GetHotelDataClass(
    val `data` : ArrayList<GetHotelData>,
    val message : String
)
data class GetHotelData(
    val propertyId : String,
    val holidayId : String,
    val shortCode : String,
    val holidayName : String,
    val startDate : String,
    val endDate : String,
    val createdOn : String,
    val createdBy : String,
    val modifiedBy : String,
    val modifiedOn : String
)
