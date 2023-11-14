package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

data class AddBedTypeData(
    val bedTypeId : String
)

data class GetBedTypeData(
    val `data` : ArrayList<GetBedTypeDataClass>
)

data class GetBedTypeDataClass(
    val bedTypeId : String,
    val bedType : String
)
