package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter

data class ImageCategoryDataClass(
    val imageId:String,
    val imageTag: String,
    val imageList: ArrayList<String>
) {
    constructor(imageId: String,imageTag: String) : this(imageId,imageTag, ArrayList())
}