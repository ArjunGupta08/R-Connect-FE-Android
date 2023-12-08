package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter

data class ImagesDataResponse(
    val message: String,
    val data: ImageData,
    val statuscode: Int
)

data class ImageData(
    val imageId: String,
    val image: String,
    val imageTag: String
)