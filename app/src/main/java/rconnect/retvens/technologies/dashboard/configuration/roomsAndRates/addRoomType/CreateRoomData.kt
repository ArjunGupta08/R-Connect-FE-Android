package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.GetAmenityData

data class CreateRoomData(
    val userId : String,
    val propertyId : String,
    val baseAdult : String,
    val baseChild : String,
    val shortCode : String,
    val roomDescription : String,
    val roomTypeName : String,
    val maxAdult : String,
    val maxChild : String,
    val maxOccupancy : String,
    val numberOfRooms : Int,
    val noOfBeds : String,
    val bedTypeIds : String,
    val amenityIds : String,
    val deviceType : String,
)

data class UpdateRoomData(
    val userId: String,
    val propertyId: String,
    val baseRate: String,
    val extraAdultRate: String,
    val extraChildRate: String,
    val minimumRate: String,
    val maximumRate: String,
)

data class GetRoomData(
    val `data` : ArrayList<GetRoomDataClass>
)

data class GetRoomDataClass(
    val userId: String,
    val roomTypeId: String,
    val propertyId: String,
    val shortCode : String,
    val dateUTC : String,
    val dateLocal : String,
    val roomTypeName : String,
    val baseAdult : String,
    val maxAdult : String,
    val extraAdultRate : String,
    val baseChild : String,
    val maxChild : String,
    val extraChildRate : String,
    val maxOccupancy : String,
    val baseRate: String,
    val numberOfRooms : Int,
    val noOfBeds : String,
    val bedType : Int,
    val amenities : Int,
)

data class FetchRoomData(
    val `data` : ArrayList<FetchRoomDataClass>
)

data class FetchRoomDataClass(
    val shortCode : String,
    val roomDescription: String,
    val roomTypeName : String,
    val numberOfRooms : Int,
    val noOfBeds : String,
    val bedType : ArrayList<BedTypeData>,
    val baseAdult : String,
    val maxAdult : String,
    val extraAdultRate : String,
    val baseChild : String,
    val maxChild : String,
    val extraChildRate : String,
    val maxOccupancy : String,
    val baseRate: String,
    val minimumRate: String,
    val maximumRate : String,
    val amenities : ArrayList<GetAmenity>,
    val roomTypeId: String,
    val roomImages : ArrayList<RoomImages>
)

data class RoomImages(
    val images : String
)

data class GetAmenity(
    val amenityName: String,
    val amenityIconLink: String
)

data class BedTypeData(
    val bedTypeId : String,
    val bedType : String,
)