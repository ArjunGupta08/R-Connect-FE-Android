package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

data class RoomDetailPrices(
    var baseChildCount:Int = 0,
    var baseAdultCount:Int = 0,
    var pricePerExtraAdult:Int = 0,
    var pricePerExtraChild:Int = 0,
    var totalCharges:Int = 0,
    var totalPriceForExtraChild:Int = 0,
    var  totalPriceForExtraAdult:Int = 0,
) {
}