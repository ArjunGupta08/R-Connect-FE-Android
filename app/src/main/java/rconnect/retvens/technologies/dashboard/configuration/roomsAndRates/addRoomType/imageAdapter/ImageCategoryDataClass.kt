package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter

import android.net.Uri

data class ImageCategoryDataClass(
    val imageType: String,
    val imageList: ArrayList<Uri>
) {
    constructor(imageType: String) : this(imageType, ArrayList())
}